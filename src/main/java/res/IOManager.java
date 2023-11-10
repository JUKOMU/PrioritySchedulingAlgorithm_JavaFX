package res;

import pcb.PCB;
import process.Process;

import static process.ProcessManager.endIOSignal;


/**
 * 管理阻塞进程，
 */
public class IOManager {
    private static IOResource resource;
    private static Process IOProcess; // 当前IO进程
    private static long IOENDTime; // 当前进程结束IO时间
    private static PCB blockedPCBHead; // 阻塞队列头指针
    private static PCB blockedPCBBack; // 阻塞队列尾指针
    private static int numOfBlockedProcesses; // 阻塞进程数

    static {
        resource = new IOResource();
        blockedPCBHead = null;
        blockedPCBBack = null;
        numOfBlockedProcesses = 0;
    }

    public IOManager() {
        resource = new IOResource();
        blockedPCBHead = null;
        blockedPCBBack = null;
        numOfBlockedProcesses = 0;
        // 进入IO等待循环
        run();
    }

    public static boolean request(Process process) {
        return resource.request();
    }

    public static void addBlockedPCB(PCB PCB) {
        numOfBlockedProcesses++;
        if (blockedPCBHead == null) {
            blockedPCBHead = PCB;
            blockedPCBBack = PCB;
            blockedPCBHead.setBack_PCB(blockedPCBBack);
            blockedPCBBack.setFront_PCB(blockedPCBHead);
            return;
        }
        blockedPCBBack.setBack_PCB(PCB);
        blockedPCBHead.setFront_PCB(PCB);
        PCB.setFront_PCB(blockedPCBBack);
        PCB.setBack_PCB(blockedPCBHead);
        blockedPCBBack = PCB;
    }

    private static void delBlockedPCB(PCB PCB) {
        if (PCB.getFront_PCB() == PCB.getBack_PCB()) {
            blockedPCBHead = null;
            blockedPCBBack = null;
            return;
        }
        PCB front = PCB.getFront_PCB();
        PCB back = PCB.getBack_PCB();
        front.setBack_PCB(back);
        back.setFront_PCB(front);
        if (PCB == blockedPCBHead) {
            blockedPCBHead = back;
        }
    }

    // 顺序处理IO调用
    private static void run() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // IO空闲
                        if (IOProcess == null && blockedPCBHead != null) {
                            IOProcess = blockedPCBHead.getProcess();
                            delBlockedPCB(blockedPCBHead);
                            long presetIOTime = IOProcess.getPresetIOTime();
                            long IOTime = 0;
                            long t = System.currentTimeMillis();
                            while (IOTime < presetIOTime) {
                                long currentTime = System.currentTimeMillis();
                                IOTime = currentTime - t;
                            }
                            IOENDTime = System.currentTimeMillis();
                            endIOSignal();
                            IOProcess = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    static void IOCompleteSignal() {
        run();
    }
}

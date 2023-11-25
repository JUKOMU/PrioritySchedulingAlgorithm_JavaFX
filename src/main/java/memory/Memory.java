package memory;

import pcb.PCB;
import process.Process;
import queue.ReadyQueue;

import java.util.ArrayList;
import java.util.List;

public class Memory {
    private ReadyQueue readyQueue = new ReadyQueue();
    ;
    private List<Process> loadedProcess = new ArrayList<>();

    private Integer[] memorySituation = new Integer[64];


    public Memory() {
        for (int i = 0; i < memorySituation.length; i++) {
            memorySituation[i] = 0;
        }
        Process OS = new Process("OS", 1000, 100, 5);
        OS.setStatus(0);
        PCB pcb = new PCB(OS, 0);
        OS.setPCB(pcb);
        for (int i = 0; i < 5; i++) {
            memorySituation[i] = 1;
        }
        loadedProcess.add(OS);
    }

    public List<Process> getReadyQueue() {
        return readyQueue.getReadyQueue();
    }

    public List<Process> getLoadingQueue() {
        return loadedProcess;
    }

    public Integer[] getMemorySituation() {
        return memorySituation;
    }

    /**
     * 添加进程到就绪队列，并分配内存
     *
     * @param process
     * @param mode    0->最先适应算法，1->最优适应算法
     * @return
     */
    public boolean addReadyProcess(Process process, int mode) {
        int requiredMemory = process.getMemory(); // 获取进程的内存占用大小

        if (mode == 0) {
            // 最先适应算法
            int memoryBegin = allocateFirstFit(requiredMemory);
            // 刷新内存状态
            if (memoryBegin != -1) {
                for (int i = memoryBegin; i < memoryBegin + requiredMemory; i++) {
                    memorySituation[i] = 1;
                }
                // 设置PCB，记录内存占用情况
                PCB pcb = new PCB(process, memoryBegin);
                process.setPCB(pcb);
                // 将进程加入就绪队列
                if (readyQueue.addReadyProcess(process)) {
                    loadedProcess.add(process);
                    String str = process.getName();
                    System.out.println("Memory:进程 " + str + "加入就绪队列");
                    return true;
                }
            }
            return false;
        } else if (mode == 1) {
            // 最优适应算法
            int memoryBegin = allocateBestFit(requiredMemory);
            // 刷新内存状态
            if (memoryBegin != -1) {
                for (int i = memoryBegin; i < memoryBegin + requiredMemory; i++) {
                    memorySituation[i] = 1;
                }
                // 设置PCB，记录内存占用情况
                PCB pcb = new PCB(process, memoryBegin);
                process.setPCB(pcb);
                // 将进程加入就绪队列
                if (readyQueue.addReadyProcess(process)) {
                    loadedProcess.add(process);
                    String str = process.getName();
                    System.out.println("Memory:进程 " + str + "加入就绪队列");
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * 返回最小满足进程内存的区间开始下标，否则返回-1
     *
     * @param requiredMemory
     * @return
     */
    private int allocateBestFit(int requiredMemory) {
        int bestFitStartIndex = -1;
        int bestFitSize = Integer.MAX_VALUE;
        int currentBlockSize = 0;

        for (int i = 6; i < memorySituation.length; i++) {
            if (memorySituation[i] == 0) {
                currentBlockSize++;
            } else {
                if (currentBlockSize >= requiredMemory && currentBlockSize < bestFitSize) {
                    bestFitSize = currentBlockSize;
                    bestFitStartIndex = i - currentBlockSize;
                }
                currentBlockSize = 0;
            }
        }

        // 检查最后的区间
        if (currentBlockSize >= requiredMemory && currentBlockSize < bestFitSize) {
            bestFitStartIndex = memorySituation.length - currentBlockSize;
        }

        return bestFitStartIndex;
    }

    /**
     * 返回最先找到的内存块的开始下标，否则返回-1
     *
     * @param requiredMemory
     * @return
     */
    private int allocateFirstFit(int requiredMemory) {
        int currentBlockSize = 0;
        //System.out.println(Arrays.toString(memorySituation));

        for (int i = 6; i < memorySituation.length; i++) {
            if (memorySituation[i] == 0) {
                currentBlockSize++;
            } else {
                currentBlockSize = 0;
            }
            if (currentBlockSize >= requiredMemory) {
                return i - currentBlockSize;
            }
        }
        // 没有足够大的空闲块
        return -1;
    }

    /**
     * 让进程退出就绪队列
     * 进程挂起或加入CPU
     *
     * @param process
     */
    public void removeReadyProcess(Process process) {
        String str = process.getName();
        if (readyQueue.deleteProcess(process)){
            System.out.println("Memory:进程" + str + "退出就绪队列");
        }
    }

    public void removeRunningProcess(Process process) {
        String str = process.getName();
        try {
            removeReadyProcess(process);
            loadedProcess.remove(process);
            System.out.println("Memory:进程" + str + "退出内存");
            freeMemory(process);
        } catch (Exception e) {

        }
    }

    public void freeMemory(Process process) {
        String name = process.getName();
        int memoryUsage = process.getMemory();
        int memoryBegin = process.getPCB().getMemoryStart();
        for (int i = memoryBegin; i < memoryBegin + memoryUsage; i++) {
            memorySituation[i] = 0;
        }
        System.out.println("Memory:进程" + name + "内存已释放," + memoryUsage + "KB");
    }

    /**
     * 发生抢占时调用
     */
    public void reAddReadyProcess(Process process) {
        readyQueue.addReadyProcess(process);
    }

}

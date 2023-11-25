package queue;

import process.Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 就绪队列
 * 存储就绪进程
 */
public class ReadyQueue {
    private final List<Process> readyQueue;

    public ReadyQueue() {
        readyQueue = new ArrayList<>();
    }

    /**
     * 只对列表进行操作，仅由主线程调用，同时判断剩余主存是否支持
     */
    public boolean addReadyProcess(Process process) {
        try {
            if (readyQueue.contains(process)){
                return false;
            }
            readyQueue.add(process);
            process.setStatus(0);
            sortReadyQueue();
            System.out.println("ReadyQueue:添加就绪队列成功");
            return true;
        } catch (Exception e) {
            System.out.println("ReadyQueue:添加就绪队列失败");
        }
        return false;
    }

    public void sortReadyQueue() {
        Collections.sort(readyQueue);
    }

    public List<Process> getReadyQueue() {
        return readyQueue;
    }

    public boolean deleteProcess(Process process) {
        try {
            readyQueue.remove(process);
            sortReadyQueue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package queue;

import process.Process;

import java.util.ArrayList;
import java.util.List;

public class SuspendQueue {
    private List<Process> suspendQueue;

    public SuspendQueue() {
        suspendQueue = new ArrayList<Process>();
    }

    public boolean addSuspendProcess(Process process) {
        try {
            if (suspendQueue.contains(process)) {
                return false;
            }
            suspendQueue.add(process);
            System.out.println("SuspendQueue:添加挂起队列成功");
            return true;
        } catch (Exception e) {
            System.out.println("SuspendQueue:添加挂起队列成功");
        }
        return false;
    }

    public List<Process> getSuspendQueue() {
        return suspendQueue;
    }

    public void removeSuspendProcess(Process process) {
        try {
            suspendQueue.remove(process);
        } catch (Exception e) {

        }
    }
}

package queue;

import process.Process;

import java.util.ArrayList;
import java.util.List;

public class CompletionQueue {
    private List<Process> completionQueue;

    public CompletionQueue() {
        completionQueue = new ArrayList<Process>();
    }

    public boolean addCompletionProcess(Process completionProcess) {
        try {
            if (completionQueue.contains(completionProcess)) {
                System.out.println("CompletionQueue:已存在");
                return false;
            }
            completionQueue.add(completionProcess);
            System.out.println("CompletionQueue:添加完成队列成功");
            return true;
        } catch (Exception e) {
            System.out.println("CompletionQueue:添加完成队列失败");
        }
        return false;
    }

    public List<Process> getCompletionQueue() {
        return completionQueue;
    }
}

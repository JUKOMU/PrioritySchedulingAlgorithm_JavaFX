package queue;

import lombok.Getter;
import process.Process;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CompletionQueue {
    private final List<Process> completionQueue;

    public CompletionQueue() {
        completionQueue = new ArrayList<>();
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

}

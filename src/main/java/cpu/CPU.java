package cpu;

import process.ProcessManager;

public class CPU {
    private final ProcessManager manager;
    public CPU() {
        manager = new ProcessManager();
        while (true) {
            manager.priorityScheduling(); // 执行一次调度
        }
    }
}

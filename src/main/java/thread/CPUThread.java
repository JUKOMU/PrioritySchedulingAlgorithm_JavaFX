package thread;

import cpu.CPU;

public class CPUThread{
    public CPUThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CPU cpu = new CPU();
            }
        });
        thread.start();
    }
}

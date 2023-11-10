package pcb;

import process.Process;

public class PCB implements Comparable{
    private  int id;
    private  double space;
    private long runTime;
    private long IOTime;
    private PCB front_PCB;
    private PCB back_PCB;
    private Process process;
    private int priority;

    public PCB() {}

    public PCB(int id, Process process) {
        this.id = id;
        this.space = 1000.0;
        this.runTime = 0;
        this.IOTime = 0;
        this.process = process;
        this.priority = process.getPresetPriority();
    }

    public PCB(int id,Process process, double space) {
        this.id = id;
        this.space = space;
        this.runTime = 0;
        this.IOTime = 0;
        this.process = process;
        this.priority = process.getPresetPriority();
    }

    public PCB getFront_PCB() {
        return front_PCB;
    }

    public void setFront_PCB(PCB front_PCB) {
        this.front_PCB = front_PCB;
    }

    public PCB getBack_PCB() {
        return back_PCB;
    }

    public void setBack_PCB(PCB back_PCB) {
        this.back_PCB = back_PCB;
    }

    public Process getProcess() {
        return process;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public long getIOTime() {
        return IOTime;
    }

    public void setIOTime(long IOTime) {
        this.IOTime = IOTime;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof PCB)
        {
            // 使用成员变量 value 进行比较
            if (this.priority < ((PCB) o).priority) {
                return -1;
            } else if (this.priority > ((PCB) o).priority) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public int getPriority() {
        return priority;
    }
}

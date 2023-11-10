package process;


import pcb.PCB;

import static process.ProcessManager.*;

/**
 * 进程类
 * id，name，优先级，实时优先级，模拟运行时间，状态
 * 运行时间：CPU时间 + IO时间
 */
public class Process {
    static final int READY = 0;
    static final int RUNNING = 1;
    static final int BLOCKED = 2;
    static final int ENDED = 3;
    static final int MAX_PRIORITY = 10;
    static final int MIN_PRIORITY = 0;
    static final int NOT_IO = 0;
    static final int IO = 1;
    private int id;
    private  String name;
    private  int presetPriority;
    private int realTimePriority;
    private  long presetRuntime;
    private  int IOOperation;
    private  long presetIOTime;
    private int status;
    private PCB PCB;
    private ProcessThread t;

    public Process() {
    }

    void initialize() {
    }
    Process(String name, int presetPriority, long presetRuntime) {
        this.name = name;
        this.presetPriority = presetPriority;
        this.realTimePriority = presetPriority;
        this.presetRuntime = presetRuntime;
        this.status = READY;
        this.IOOperation = NOT_IO;
        this.presetIOTime = 0;
    }

    Process(String name, int presetPriority, long presetRuntime, long presetIOTime) {
        this.name = name;
        this.presetPriority = presetPriority;
        this.realTimePriority = presetPriority;
        this.presetRuntime = presetRuntime;
        this.status = READY;
        this.IOOperation = IO;
        this.presetIOTime = presetIOTime;
    }

    /**
     * 进程运行
     * 记录开始时间
     * 包含一个线程更新进程状态
     */
    void run() {
        this.t = new ProcessThread(this);
        t.start();
    }
    void end() throws InterruptedException {
        this.PCB.setRunTime(presetRuntime);
        this.PCB.setIOTime(presetIOTime);
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                endRunningSignal();
            }
        });
        t.start();

    }
    void interrupt() {
        saveScene();
        this.status = READY;
        t.interrupted = true;
        t.interrupt();
        long runTime = t.runTime;
        PCB.setRunTime(runTime);
    }
    void saveScene() {
        this.status = READY;
    }
    void setBlocked() {
        interrupt();
        this.status = BLOCKED;
        blockSignal(this);
    }
    void signal() {

    }
    void wakeUp() {

    }

    int getId() {
        return id;
    }
    void setId(int id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    public int getPresetPriority() {
        return presetPriority;
    }

    int getRealTimePriority() {
        return realTimePriority;
    }

    void setRealTimePriority(int realTimePriority) {
        this.realTimePriority = realTimePriority;
    }

    long getPresetRuntime() {
        return presetRuntime;
    }

    int getStatus() {
        return status;
    }

    pcb.PCB getPCB() {
        return PCB;
    }

    void setPCB(pcb.PCB PCB) {
        this.PCB = PCB;
    }

    int getIO() {
        return IOOperation;
    }

    public long getPresetIOTime() {
        return presetIOTime;
    }


    public void init() {
        this.t = null;
    }
}

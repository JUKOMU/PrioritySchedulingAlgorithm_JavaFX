package process;

import app.MainApp;
import javafx.scene.control.ProgressBar;

public class ProcessThread extends Thread {
    Process myProcess;
    long presetRunTime;
    long startTime;
    long interruptTime;
    long endTime;
    long runTime;
    int IO;
    long IOStartTime;
    long IOTime;

    boolean interrupted = false;

    ProcessThread() {
    }

    ProcessThread(Process myProcess) {
        this.presetRunTime = myProcess.getPresetRuntime();
        this.runTime = 0;
        this.IO = myProcess.getIO();
        this.IOTime = IO>0?myProcess.getPresetIOTime():0;
        this.myProcess = myProcess;
        this.IOStartTime = IO>0?(long)(Math.random()*presetRunTime):0;

        this.startTime = 0;
        this.endTime = 0;
        this.interruptTime = 0;
    }

    public ProcessThread(Runnable target) {
        super(target);
    }

    public ProcessThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public ProcessThread(String name) {
        super(name);
    }

    public ProcessThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public ProcessThread(Runnable target, String name) {
        super(target, name);
    }

    public ProcessThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public ProcessThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    public ProcessThread(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
        super(group, target, name, stackSize, inheritThreadLocals);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * 进程运行循环
     * 包括判断运行是否结束，是否进入IO
     * 2023.10.30 16：47 暂时完成本方法的编写
     * 2023.11.03 00：13 添加进度条事件
     */
    @Override
    public void run() {
        this.interrupted = false;
        this.runTime = myProcess.getPCB().getRunTime();
        // 判断是否为首次运行，首次运行则刷新开始时间，否则保持
        if (this.startTime == 0) {
            this.startTime = System.currentTimeMillis();
        }
        this.interruptTime = System.currentTimeMillis();
        // 从中断处计时，首次运行中断时间与开始时间相同
        long temptRunTime = this.runTime;

        // 进度条
        ProgressBar progressBar = MainApp.get_2().getListViewTop().getProgressBar();

        while (!this.isInterrupted()) {
            long currentTime = System.currentTimeMillis();
            this.runTime = temptRunTime + currentTime - this.interruptTime;

            progressBar.setProgress((double) runTime /presetRunTime);
            // 到达运行时间，结束进程
            if (this.runTime >= this.presetRunTime) {
                this.endTime = System.currentTimeMillis();
                try {
                    this.myProcess.end();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public boolean isInterrupted() {
        return super.isInterrupted();
    }


    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return super.getContextClassLoader();
    }

    @Override
    public void setContextClassLoader(ClassLoader cl) {
        super.setContextClassLoader(cl);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return super.getUncaughtExceptionHandler();
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


}

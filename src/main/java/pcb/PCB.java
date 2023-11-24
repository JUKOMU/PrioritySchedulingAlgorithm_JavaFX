package pcb;

import process.Process;

public class PCB {
    private int id;
    private Process process;
    private Integer memory;
    private Integer memoryStart;

    public PCB() {}

    public PCB(Process process, Integer memoryStart) {
        try {
            this.id = process.getId();
        } catch (Exception e) {
            this.id = -1;
        }
        this.process = process;
        this.memory = process.getMemory();
        this.memoryStart = memoryStart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getMemoryStart() {
        return memoryStart;
    }

    public void setMemoryStart(Integer memoryStart) {
        this.memoryStart = memoryStart;
    }
}

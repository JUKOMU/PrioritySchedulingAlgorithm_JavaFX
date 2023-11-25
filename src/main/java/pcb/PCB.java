package pcb;

import lombok.Getter;
import process.Process;

@Getter
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

    public void setId(int id) {
        this.id = id;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public void setMemoryStart(Integer memoryStart) {
        this.memoryStart = memoryStart;
    }
}

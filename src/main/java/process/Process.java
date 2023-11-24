package process;


import pcb.PCB;

import java.util.Objects;

/**
 * 进程类
 * id，name，优先级，实时优先级，模拟运行时间，状态
 * 运行时间：CPU时间 + IO时间
 */
public class Process implements Comparable<Process> {
    static final Integer BACKED = -1;
    static final Integer READY = 0;
    static final Integer RUNNING = 1;
    static final Integer ENDED = 2;
    static final Integer MAX_PRIORITY = 1000;
    static final Integer MIN_PRIORITY = 0;
    private Integer id;
    private String name;
    private Integer priority;
    private Integer runtime;
    private Integer status;
    private Integer memory;
    private PCB PCB; // 内存信息在PCB中

    public Process() {
    }

    public Process(String name, Integer priority, Integer runtime, Integer memory) {
        this.name = name;
        this.priority = priority;
        this.runtime = runtime;
        this.status = -1;
        this.memory = memory;
        this.PCB = new PCB();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public pcb.PCB getPCB() {
        return PCB;
    }

    public void setPCB(pcb.PCB PCB) {
        this.PCB = PCB;
    }


    public void runtimeChangBy(Integer t) {
        this.runtime += t;
    }

    public void priorityChangBy(Integer t) {
        this.priority += t;
    }

    /**
     * 按降序排序
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Process o) {
        return o.getPriority() - this.getPriority();
    }
}
package queue;

import dao.impl.ProcessDaoImpl;
import lombok.Getter;
import process.Process;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BackQueue{
    private final ProcessDaoImpl dao;
    private List<Process> backQueue;

    /**
     * 仅由_1调用
     */
    public BackQueue() {
        dao = new ProcessDaoImpl();
        backQueue = new ArrayList<>();
    }
    public boolean addBackProcess(Process process) {
        try {
            if (dao.insertProcess(process)) {
                System.out.println("BackQueue:添加后备队列成功");
                return true;
            }
        } catch (Exception e) {
            System.out.println("BackQueue:添加后备队列失败");
        }
        return false;
    }

    public List<Process> getBackQueue() {
        backQueue = dao.selectProcesses();
        return backQueue;
    }

    public void clearAll() {
        dao.deleteAllRecords();
    }

    public void removeBackProcess(Process process) {
        dao.deleteProcess(process);
    }
}

package dao;

import process.Process;

import java.util.List;

public interface ProcessDao {
    /**
     * 查询全部后备进程
     */
    List<Process> selectProcesses();

    /**
     * 添加后备进程
     */
    boolean insertProcess(Process process);

    /**
     * 删除所有元素
     */
    boolean deleteAllRecords();

    boolean deleteProcess(Process process);
}

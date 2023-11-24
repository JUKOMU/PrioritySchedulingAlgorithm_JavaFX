package dao.impl;

import dao.ProcessDao;
import process.Process;
import utils.DataSourceUtil;

import java.util.List;

public class ProcessDaoImpl extends DataSourceUtil implements ProcessDao {
    @Override
    public List<Process> selectProcesses() {
        String sql = "SELECT * FROM backupque";
        return executeQueryList(sql, Process.class);
    }

    @Override
    public boolean insertProcess(Process process) {
        // 构建 SQL 语句
        String sql = "INSERT INTO backupque (name, priority, runtime, status, memory) VALUES (?, ?, ?, ?, ?)";

        // 执行 SQL 语句
        int rowsAffected = executeInsert(sql, process.getName(), process.getPriority(), process.getRuntime(), process.getStatus(), process.getMemory());

        // 判断是否插入成功
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteAllRecords() {
        // 构建 SQL 语句
        String sql = "DELETE FROM backupque";

        // 执行 SQL 语句
        int rowsAffected = executeUpdate(sql);

        // 判断是否删除成功
        return rowsAffected > 0;
    }
    @Override
    public boolean deleteProcess(Process process) {
        // 构建 SQL 语句
        String sql = "DELETE FROM backupque WHERE id = ?";

        // 执行 SQL 语句
        int rowsAffected = executeUpdate(sql, process.getId());

        return rowsAffected > 0;
    }
}

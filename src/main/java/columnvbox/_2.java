package columnvbox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import memory.Memory;
import process.Process;

/**
 * 就绪队列和CPU
 */
public class _2 extends VBox {
    // 创建表格
    TableView<Process> tableView = new TableView<>();
    TableColumn<Process, String> nameColumn = new TableColumn<>("Name");
    TableColumn<Process, Integer> priorityColumn = new TableColumn<>("Priority");
    TableColumn<Process, Integer> runtimeColumn = new TableColumn<>("Runtime");
    TableColumn<Process, Integer> memoryColumn = new TableColumn<>("MemoryUsage");

    ObservableList<Process> processList;

    private Memory memory;

    public Process runningProcess; // 运行的进程

    public boolean isRunningNotEmpty = false; // 运行进程是否为空

    Label label2 = new Label("CPU");
    Label label3 = new Label("");// CPU状态
    Label label4 = new Label("");// 运行的进程信息
    /**
     * 设置列表字段，绑定属性
     */
    public _2(Memory memory) {
        this.memory = memory;
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        runtimeColumn.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        memoryColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));
        // 将列添加到表格
        tableView.getColumns().addAll(nameColumn, priorityColumn, runtimeColumn, memoryColumn);
        // 创建 Process 对象的列表
        ObservableList<Process> processList = FXCollections.observableArrayList();
        // 添加按钮和表格到 VBox
        Label label1 = new Label("就绪队列");
        label1.setFont(new Font(25));
        label2.setFont(new Font(25));
        label3.setFont(new Font(20));
        label4.setFont(new Font(18));

        this.getChildren().addAll(label1, tableView, label2, label3, label4);
        refreshTableView();
    }
    public void refreshTableView() {
        processList = FXCollections.observableArrayList(memory.getReadyQueue());
        Platform.runLater(() -> tableView.setItems(processList));
        showRunningProcess();
    }

    public void showRunningProcess() {
        String message;
        try {
            message = "PID:" + runningProcess.getName() + "  优先级:" + runningProcess.getPriority() + "  时间:" + runningProcess.getRuntime();
        } catch (Exception e) {
            message = "";
        }
        String finalMessage = message;
        Platform.runLater(() -> label4.setText(finalMessage));
    }

    public void setInfo(String message) {
        Platform.runLater(() -> label3.setText(message));
    }
}

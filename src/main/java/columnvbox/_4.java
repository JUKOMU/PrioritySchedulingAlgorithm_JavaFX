package columnvbox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import process.Process;
import queue.CompletionQueue;
import queue.SuspendQueue;

/**
 * 挂起队列和完成队列
 */
public class _4 extends VBox {
    TableView<Process> tableView1 = new TableView<>();
    TableColumn<Process, String> nameColumn1 = new TableColumn<>("Name");
    TableColumn<Process, Integer> priorityColumn1 = new TableColumn<>("Priority");
    ObservableList<Process> processList1;
    TableView<Process> tableView2 = new TableView<>();
    TableColumn<Process, String> nameColumn2 = new TableColumn<>("Name");
    TableColumn<Process, Integer> priorityColumn2 = new TableColumn<>("Priority");
    TableColumn<Process, Integer> runtimeColumn2 = new TableColumn<>("Runtime");
    TableColumn<Process, Integer> memoryColumn2 = new TableColumn<>("MemoryUsage");
    ObservableList<Process> processList2;

    public CompletionQueue completionQueue;
    public SuspendQueue suspendQueue;

    public _4() {
        // 设置列与实体类属性的关联
        nameColumn1.setCellValueFactory(new PropertyValueFactory<>("name"));
        priorityColumn1.setCellValueFactory(new PropertyValueFactory<>("priority"));
        nameColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        priorityColumn2.setCellValueFactory(new PropertyValueFactory<>("priority"));
        runtimeColumn2.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        memoryColumn2.setCellValueFactory(new PropertyValueFactory<>("memory"));

        completionQueue = new CompletionQueue();
        suspendQueue = new SuspendQueue();

        // 将列添加到表格
        tableView1.getColumns().addAll(nameColumn1, priorityColumn1);
        tableView2.getColumns().addAll(nameColumn2, priorityColumn2, runtimeColumn2, memoryColumn2);
        // 创建 Process 对象的列表
        processList1 = FXCollections.observableArrayList();
        processList2 = FXCollections.observableArrayList();

        Label label1 = new Label("完成队列");
        Label label2 = new Label("挂起队列");
        label1.setFont(new Font(25));
        label1.setPadding(new Insets(10, 10, 10, 10));
        label2.setFont(new Font(25));
        label2.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(label1, tableView1, label2, tableView2);
        refreshTableView1();
        refreshTableView2();
    }

    public void refreshTableView1() {
        processList1 = FXCollections.observableArrayList(completionQueue.getCompletionQueue());
        Platform.runLater(() -> tableView1.setItems(processList1));
    }
    public void refreshTableView2() {
        processList2 = FXCollections.observableArrayList(suspendQueue.getSuspendQueue());
        Platform.runLater(() -> tableView2.setItems(processList2));
    }

}

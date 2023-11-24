package columnvbox;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import memory.Memory;
import process.Process;

import java.util.List;


/**
 * 内存
 */
public class _3 extends VBox {
    private TableView<ObservableList<String>> tableView = new TableView<>();
    // 创建表格列
    TableColumn<ObservableList<String>, String> rowColumn = new TableColumn<>("Row");
    TableColumn<ObservableList<String>, String> pidColumn = new TableColumn<>("PID");
    TableColumn<ObservableList<String>, String> statusColumn = new TableColumn<>("Status");
    TableColumn<ObservableList<String>, String> usageColumn = new TableColumn<>("占用内存");
    ObservableList<ObservableList<String>> data;

    private Memory memory;
    public _3(Memory memory) {
        this.memory = memory;
        // 将列添加到表格中
        tableView.getColumns().addAll(rowColumn, pidColumn, statusColumn, usageColumn);

        // 初始化数据
        data = FXCollections.observableArrayList();
        for (int i = 0; i < 64; i++) {
            ObservableList<String> row = FXCollections.observableArrayList(
                    String.valueOf(i + 1), "", "", "");
            data.add(row);
        }

        // 将数据设置到表格中
        tableView.setItems(data);
        VBox.setVgrow(tableView, javafx.scene.layout.Priority.ALWAYS);

        // 设置每列的单元格工厂
        rowColumn.setCellValueFactory(param -> Bindings.stringValueAt(param.getValue(), 0));
        pidColumn.setCellValueFactory(param -> Bindings.stringValueAt(param.getValue(), 1));
        statusColumn.setCellValueFactory(param -> Bindings.stringValueAt(param.getValue(), 2));
        usageColumn.setCellValueFactory(param -> Bindings.stringValueAt(param.getValue(), 3));
        // 设置状态列的单元格工厂
        statusColumn.setCellFactory(column -> new TableCell<ObservableList<String>, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle(""); // 清空样式
                } else {
                    setText(item);

                    // 根据状态值设置颜色样式
                    if (item.equals("运行")) {
                        setStyle("-fx-background-color: rgba(255,83,83,0.82); -fx-text-fill: #000000;");
                    } else if (item.equals("占用")) {
                        setStyle("-fx-background-color: rgba(255,255,115,0.81); -fx-text-fill: black;");
                    } else {
                        setStyle("-fx-background-color: rgba(133,255,133,0.71); -fx-text-fill: black;"); // 清空样式
                    }
                }
            }
        });
        Label label = new Label("内存");
        label.setFont(new Font(25));
        this.getChildren().addAll(label, tableView);
        refreshTableView();
    }

    private String getStatusString(int statusValue) {
        // 根据实际需要进行映射
        if (statusValue == 0) {
            return "占用";
        } else if (statusValue == 1) {
            return "运行";
        } else {
            return "空闲";
        }
    }
    // 修改指定行的PID和状态
    public void updateRowData(int rowIndex, String newPid, String newStatus, String usage) {
        ObservableList<String> row = tableView.getItems().get(rowIndex);
        row.set(1, newPid); // 更新PID
        row.set(2, newStatus); // 更新状态
        row.set(3, usage); //
    }

    public void refreshTableView() {
        try {
            for (int i = 0; i < 64; i++) {
                ObservableList<String> row = FXCollections.observableArrayList(
                        String.valueOf(i + 1), "", "", "");
                data.set(i, row);
            }
            List<Process> loadingQueue = memory.getLoadingQueue();
            for (Process p : loadingQueue) {
                String name = p.getName();
                //System.out.println(name);
                String status = getStatusString(p.getStatus());
                int memoryUsage = p.getMemory();
                int memoryBegin = p.getPCB().getMemoryStart();
                for (int i = memoryBegin; i < memoryBegin + memoryUsage; i++) {
                    if (i == memoryBegin){
                        updateRowData(i, name, status,""+memoryUsage);
                    } else {
                        updateRowData(i, "", status, "");
                    }
                }
            }
        } catch (Exception e) {}
    }
}

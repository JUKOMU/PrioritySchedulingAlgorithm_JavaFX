package columnvbox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import process.Process;
import queue.BackQueue;

import java.util.Random;

import static app.MainApp.setScheduling;

/**
 * 后备队列和按钮
 */
public class _1 extends VBox {
    GridPane gridPane = new GridPane();
    // 创建按钮
    Button startButton = new Button("开始");
    Button addJobButton = new Button("添加作业");
    Button randomAddButton = new Button("随机添加10个作业");
    Button refreshBackQueueButton = new Button("刷新后备队列");
    Button pauseButton = new Button("停止");
    public Button suspendButton = new Button("挂起");
    public Button unsuspendButton = new Button("解挂");
    Button clearBackQueueButton = new Button("清空后备队列");

    // 创建表格
    TableView<Process> tableView = new TableView<>();
    TableColumn<Process, String> nameColumn = new TableColumn<>("Name");
    TableColumn<Process, Integer> priorityColumn = new TableColumn<>("Priority");
    TableColumn<Process, Integer> runtimeColumn = new TableColumn<>("Runtime");
    TableColumn<Process, Integer> memoryColumn = new TableColumn<>("MemoryUsage");

    ObservableList<Process> processList;

    public BackQueue backgroundQueue;

    /**
     * 设置列表字段，绑定属性
     */
    public _1() {
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(startButton,0,0);
        gridPane.add(pauseButton,1,0);
        gridPane.add(addJobButton,0,1);
        gridPane.add(refreshBackQueueButton,1,1);
        gridPane.add(randomAddButton,0,2);
        gridPane.add(suspendButton,1,2);
        gridPane.add(unsuspendButton,0,3);
        gridPane.add(clearBackQueueButton,1,3);
        gridPane.setAlignment(Pos.CENTER); // 设置GridPane中内容居中
        // 设置按钮之间的水平和垂直间距
        gridPane.setHgap(70); // 设置水平间距为10像素
        gridPane.setVgap(10); // 设置垂直间距为10像素
        // 设置列约束，使列中的元素居中
        for (int i = 0; i < 2; i++) {
            gridPane.getColumnConstraints().add(i, new javafx.scene.layout.ColumnConstraints());
            gridPane.getColumnConstraints().get(i).setHalignment(javafx.geometry.HPos.CENTER);
        }
        for (Node i :gridPane.getChildren()) {
            i.setStyle("-fx-min-width: 100px; -fx-min-height: 40px; -fx-pref-width: 120px; -fx-pref-height: 40px; -fx-max-width: 140px; -fx-max-height: 40px; -fx-background-color: #6db2ff; -fx-border-radius: 10px; -fx-background-radius: 10px; ");
            // 设置按钮在点击时的样式
            i.setOnMousePressed(e -> i.setStyle("-fx-background-color: #ffffff;-fx-min-width: 100px; -fx-min-height: 40px; -fx-pref-width: 120px; -fx-pref-height: 40px; -fx-max-width: 140px; -fx-max-height: 40px; -fx-border-radius: 10px; -fx-background-radius: 10px;"));
            // 设置按钮在释放鼠标时的样式
            i.setOnMouseReleased(e -> i.setStyle("-fx-min-width: 100px; -fx-min-height: 40px; -fx-pref-width: 120px; -fx-pref-height: 40px; -fx-max-width: 140px; -fx-max-height: 40px; -fx-background-color: #6db2ff; -fx-border-radius: 10px; -fx-background-radius: 10px;"));

        }

        // 设置列与实体类属性的关联
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        runtimeColumn.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        memoryColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));



        backgroundQueue = new BackQueue();
        // 将列添加到表格
        tableView.getColumns().addAll(nameColumn, priorityColumn, runtimeColumn, memoryColumn);
        // 设置列的自适应宽度策略
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, javafx.scene.layout.Priority.ALWAYS);
        // 创建 Process 对象的列表
        processList = FXCollections.observableArrayList();
        // 添加按钮和表格到 VBox
        Label label1 = new Label("后备队列");
        label1.setFont(new Font(25));
        label1.setPadding(new Insets(10, 10, 10, 10));
        this.getChildren().addAll(gridPane, label1, tableView);
        startButton.setOnAction(event -> setOnStartButton());
        addJobButton.setOnAction(event -> setAddJobButton());
        randomAddButton.setOnAction(event -> setRandomAddButton());
        refreshBackQueueButton.setOnAction(event -> setRefreshBackQueueButton());
        pauseButton.setOnAction(event -> setPauseButton());
        clearBackQueueButton.setOnAction(event -> setClearBackQueueButton());
        refreshTableView();
    }

    /**
     * 查询数据库获取后备队列，放入列表，提交刷新指令到主线程
     */
    public void refreshTableView() {
        processList = FXCollections.observableArrayList(backgroundQueue.getBackQueue());
        Platform.runLater(() -> tableView.setItems(processList));
    }

    public void setOnStartButton() {
        setScheduling(true);
    }
    public void setAddJobButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("添加作业");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Process Information:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField processNameField = new TextField();
        TextField priorityField = new TextField();
        TextField runTimeField = new TextField();
        TextField memoryUsageField = new TextField();

        grid.add(new Label("进程名:"), 0, 0);
        grid.add(processNameField, 1, 0);
        grid.add(new Label("优先级:"), 0, 1);
        grid.add(priorityField, 1, 1);
        grid.add(new Label("要求运行时间:"), 0, 2);
        grid.add(runTimeField, 1, 2);
        grid.add(new Label("内存占用:"), 0, 3);
        grid.add(memoryUsageField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // 等待用户点击OK或取消
        dialog.showAndWait().ifPresent(result -> {
            // 进行合法性检查
            if (processNameField.getText().isEmpty() ||
                    priorityField.getText().isEmpty() ||
                    runTimeField.getText().isEmpty() ||
                    memoryUsageField.getText().isEmpty()) {
                // 提示用户输入不能为空
                return;
            }

            try {
                // 将用户输入的文本转换为相应的数据类型
                String processName = processNameField.getText();
                int priority = Integer.parseInt(priorityField.getText());
                int runTime = Integer.parseInt(runTimeField.getText());
                int memoryUsage = Integer.parseInt(memoryUsageField.getText());

                // 在这里可以进行进一步的逻辑处理，例如保存到数据结构中
                System.out.println("_1:进行存储");
                Process process = new Process(processName, priority, runTime, memoryUsage);
                backgroundQueue.addBackProcess(process);
                refreshTableView();
            } catch (NumberFormatException e) {
                // 处理转换异常，提示用户输入的格式不正确
                System.out.println("_1:Invalid input format. Please enter valid numbers.");
            }
        });
    }
    
    public void setRandomAddButton() {
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            String processName = "P" + String.format("%04d",(random.nextInt(9998) + 1)); // 生成1到9999之间的随机数
            int priority = random.nextInt(999) + 1;
            int runTime = random.nextInt(8) + 2;
            int memoryUsage = random.nextInt(19) + 1;
            System.out.println("_1:进行存储");
            Process process = new Process(processName, priority, runTime, memoryUsage);
            backgroundQueue.addBackProcess(process);
            refreshTableView();
        }
    }

    public void setRefreshBackQueueButton() {
        refreshTableView();
    }

    /**
     * 暂停调度
     */
    public void setPauseButton() {
        setScheduling(false);
    }

    /**
     * 清除全部后备队列
     */
    public void setClearBackQueueButton() {
        backgroundQueue.clearAll();
        refreshTableView();
    }

}

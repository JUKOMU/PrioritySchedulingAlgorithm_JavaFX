package app;

import columnvbox.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import process.ProcessManager;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static process.ProcessManager.*;


public class MainApp extends Application {
    /**
     * 进程管理
     */
    public static ProcessManager processManager;

    private static _1 _1;
    private static _2 _2;
    private static _3 _3;
    private static _4 _4;

    // 演示模式标志
    private static boolean demonstrationMode;

    // 演示线程
    private static Thread demonstrationThread;

    // 演示模式的标志时间
    private static long time;

    // 间隔时间

    private static long intervalTime;

    private static Thread thread;

    public static void main(String[] args) {
        demonstrationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!demonstrationThread.isInterrupted()) {
                    try {
                        // 随机创建运行时间为500ms~8000ms的进程，优先级为1~10
                        long current = System.currentTimeMillis();
                        if (current - time >= intervalTime) {
                            time = current;
                            intervalTime = (long) ((2000.0 * Math.random() + 1) + 2000);
                            String name = (time % 10000) + "";
                            int priority = (int) (10.0 * Math.random()) + 1;
                            long runtime = (long) ((4500.0 * Math.random() + 1) + 500);
                            processManager.createProcess(name, priority, runtime, 0, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (MainApp.isDemonstrationMode()) {
                    try {
                        if (!get_1().getListItems().isEmpty()) {
                            ListViewItem listViewItem = _1.getListItems().get(0);
                            _1.removeListViewItem(listViewItem);
                            _1.refresh();
                            delBacklogPCB(listViewItem.getPCB());
                            updateReadyQueue(listViewItem);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        intervalTime = (long) ((2000.0 * Math.random() + 1) + 8000);
        time = System.currentTimeMillis();
        demonstrationMode = false;
        processManager = new ProcessManager();
        launch(args);
    }

    /**
     * 进行一次调度
     */
    public static void schedulingOnce() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                processManager.priorityScheduling();
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                thread.start();
            }
        });
    }


    /**
     * 添加作业
     */
    public static void addScheduledTask() {
        TaskInputDialog dialog = new TaskInputDialog();
        dialog.show();
    }

    /**
     * 演示模式
     * 自动创建进程并运行
     */

    public static void demonstrationMode() {
        demonstrationMode = !demonstrationMode;
        if (!demonstrationMode) {
            _1.getButtons().get(1).setDisable(false);
            _1.getButtons().get(0).setText("演示");
            demonstrationThread.interrupt();
            return;
        }
        _1.getButtons().get(1).setDisable(true);
        _1.getButtons().get(0).setText("退出演示模式");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                demonstrationThread.start();
            }
        });
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                thread.start();
            }
        });
    }

    public static boolean isDemonstrationMode() {
        return demonstrationMode;
    }

    /**
     * 创建一个带标签的VBox
     *
     * @param label
     * @return
     */

    public static VBox createListView(String label) {
        VBox section = new VBox(10);
        section.setPadding(new Insets(0, 15, 0, 15));
        Label queueLabel = new Label(label);
        queueLabel.setFont(new Font(20));
        section.getChildren().addAll(queueLabel);
        return section;
    }

    public static columnvbox._1 get_1() {
        return _1;
    }

    public static columnvbox._2 get_2() {
        return _2;
    }

    public static columnvbox._3 get_3() {
        return _3;
    }

    public static columnvbox._4 get_4() {
        return _4;
    }

    public static void endRunningSignal() {
        Platform.runLater(() -> {
            ListViewItem item = MainApp.get_2().getListViewTop();
            _2 _2 = MainApp.get_2();
            _2.removeListViewTopItem(item);
            _2.refreshTop();
            try {
                updateCompletionQueue(item);
            } finally {
                ProcessManager.runningProcess = null;
                try {
                    MainApp.schedulingOnce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("CPU调度");

        HBox root = new HBox(10); // 创建一个水平布局容器，列之间有10像素的间距

        // 创建4个列
        CustomColumn column1 = new CustomColumn("");
        CustomColumn column2 = new CustomColumn("");
        CustomColumn column3 = new CustomColumn("");
        CustomColumn column4 = new CustomColumn("");

        // 为每列设置背景色
        column1.setStyle("-fx-background-color: #ffffff;"); // 列1背景颜色为橙色
        column2.setStyle("-fx-background-color: #ffffff;"); // 列2背景颜色为绿色
        column3.setStyle("-fx-background-color: #ffffff;"); // 列3背景颜色为蓝色
        column4.setStyle("-fx-background-color: #ffffff;"); // 列4背景颜色为粉色

        // 将列的宽度与窗口宽度绑定
        DoubleBinding columnWidthBinding = Bindings.createDoubleBinding(() -> primaryStage.getWidth() / 4, primaryStage.widthProperty());


        // 添加第1列内容
        _1 = new _1();
        _1.prefWidthProperty().bind(columnWidthBinding);
        column1.getChildren().add(_1);

        // 添加第2列内容
        _2 = new _2();
        _2.prefWidthProperty().bind(columnWidthBinding);
        column2.getChildren().add(_2);

        // 添加第3列内容
        _3 = new _3();
        _3.prefWidthProperty().bind(columnWidthBinding);
        column3.getChildren().add(_3);

        // 添加第4列内容
        _4 = new _4();
        _4.prefWidthProperty().bind(columnWidthBinding);
        column4.getChildren().add(_4);

        root.getChildren().addAll(column1, column2, column3, column4);

        column1.prefWidthProperty().bind(columnWidthBinding);
        column2.prefWidthProperty().bind(columnWidthBinding);
        column3.prefWidthProperty().bind(columnWidthBinding);
        column4.prefWidthProperty().bind(columnWidthBinding);

        root.setPadding(new Insets(5, 10, 0, 10));
        root.setStyle("-fx-background-color: #d0d0d0;");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    static class CustomColumn extends Pane {
        public CustomColumn(String label) {
            setMaxHeight(Double.MAX_VALUE); // 列的高度可扩展到最大高度

            Label columnLabel = new Label(label);
            getChildren().add(columnLabel);
        }
    }

    static class TaskInputDialog extends Stage {
        private final TextField nameField = new TextField();
        private final TextField priorityField = new TextField();
        private final TextField runtimeField = new TextField();
        private final ComboBox<String> ioComboBox = new ComboBox<>();
        private final TextField ioTimeField = new TextField();

        public TaskInputDialog() {
            initModality(Modality.APPLICATION_MODAL);
            setTitle("输入任务信息");

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            Label nameLabel = new Label("名称:");
            Label priorityLabel = new Label("优先级:");
            Label runtimeLabel = new Label("运行时间:");
            Label ioLabel = new Label("是否 I/O:");
            Label ioTimeLabel = new Label("I/O 时间:");

            ioComboBox.getItems().addAll("是", "否");
            ioComboBox.setValue("是");

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameField, 1, 0);
            gridPane.add(priorityLabel, 0, 1);
            gridPane.add(priorityField, 1, 1);
            gridPane.add(runtimeLabel, 0, 2);
            gridPane.add(runtimeField, 1, 2);
            //gridPane.add(ioLabel, 0, 3);
            // gridPane.add(ioComboBox, 1, 3);
//            gridPane.add(ioTimeLabel, 0, 4);
//            gridPane.add(ioTimeField, 1, 4);

            Button submitButton = getSubmitButton();

            // 添加监听器以根据下拉选择框的值启用或禁用IOTime输入框
            ioComboBox.setOnAction(event -> {
                ioTimeField.setDisable("否".equals(ioComboBox.getValue()));
            });
            gridPane.setPadding(new Insets(0, 1, 1, 5));

            gridPane.add(submitButton, 0, 5, 2, 1);

            Scene scene = new Scene(gridPane, 300, 200);
            setScene(scene);
        }

        private Button getSubmitButton() {
            Button submitButton = new Button("确定");
            submitButton.setOnAction(event -> {
                // 处理用户的输入
                String name = nameField.getText();
                String priority = priorityField.getText();
                String runtime = runtimeField.getText();
                String io = ioComboBox.getValue();
                String ioTime;

                // 将 "是" 映射为 1，"否" 映射为 0
                io = "0";
                ioTime = "0";

                // 创建进程，添加到后备队列
                processManager.createProcess(name, parseInt(priority), parseLong(runtime), 0, 0);

                close();
            });
            return submitButton;
        }

        @Override
        public String toString() {
            return "TaskInputDialog{" +
                    "nameField=" + nameField.getText() +
                    ", priorityField=" + priorityField.getText() +
                    ", runtimeField=" + runtimeField.getText() +
                    ", ioComboBox=" + ioComboBox.getValue() +
                    ", ioTimeField=" + ioTimeField.getText() +
                    '}';
        }
    }
}

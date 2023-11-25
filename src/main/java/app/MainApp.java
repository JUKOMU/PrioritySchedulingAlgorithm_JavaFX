package app;

import columnvbox._1;
import columnvbox._2;
import columnvbox._3;
import columnvbox._4;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import memory.Memory;
import process.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainApp extends Application {
    private static _1 _1;
    private static _2 _2;
    private static _3 _3;
    private static _4 _4;

    private Memory memory;
    private static boolean scheduling = false; // 调度开始的标志

    private static int allocateFit = 1; // 设置适应算法，0最先适应算法，1最优适应算法

    private boolean ReadyToRunning = false; // 是否可以让进程进入CPU

    private Timer timer = new Timer();



    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.memory = new Memory();

        primaryStage.setTitle("CPU调度");

        HBox root = new HBox(10); // 创建一个水平布局容器，列之间有10像素的间距

        // 将列的宽度与窗口宽度绑定
        DoubleBinding columnWidthBinding = Bindings.createDoubleBinding(() -> primaryStage.getWidth() / 4, primaryStage.widthProperty());
        // 添加第1列内容
        _1 = new _1();
        _1.prefWidthProperty().bind(columnWidthBinding);

        // 添加第2列内容
        _2 = new _2(memory);
        _2.prefWidthProperty().bind(columnWidthBinding);

        // 添加第3列内容
        _3 = new _3(memory);
        _3.prefWidthProperty().bind(columnWidthBinding);
        VBox.setVgrow(_3, javafx.scene.layout.Priority.ALWAYS);

        // 添加第4列内容
        _4 = new _4();
        _4.prefWidthProperty().bind(columnWidthBinding);

        // 创建4个列
        _1 column1 = _1;
        _2 column2 = _2;
        _3 column3 = _3;
        _4 column4 = _4;

        // 为每列设置背景色
        column1.setStyle("-fx-background-color: #ffffff;");
        column2.setStyle("-fx-background-color: #ffffff;");
        column3.setStyle("-fx-background-color: #ffffff;");
        column4.setStyle("-fx-background-color: #ffffff;");

        root.getChildren().addAll(column1, column2, column3, column4);

        root.setPadding(new Insets(5, 10, 0, 10));
        root.setStyle("-fx-background-color: #d0d0d0;");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        _1.suspendButton.setOnAction(event -> setSuspendButton());
        _1.unsuspendButton.setOnAction(event -> setUnsuspendButton());

        // 设置定时任务
        // 调度主逻辑
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (scheduling){
                    /**
                     * CPU调度逻辑
                     */
                    if (_2.isRunningNotEmpty) {
                        // CPU有进程占用
                        Process p = _2.runningProcess;
                        if (p.getRuntime() > 0) {
                            // 进程时间未结束
                            p.runtimeChangBy(-1);
                            ReadyToRunning = false;
                            _2.showRunningProcess();
                        } else {
                            // 进程时间结束
                            //进程退出内存
                            memory.removeRunningProcess(p);
                            // 进程进入完成队列
                            _4.completionQueue.addCompletionProcess(p);
                            // 设置进程状态
                            p.setPCB(null);
                            p.setRuntime(0);
                            p.setStatus(2);
                            // 将CPU置为空
                            _2.runningProcess = null;
                            _2.isRunningNotEmpty = false;
                            _2.showRunningProcess();

                            // 允许进程进入CPU
                            ReadyToRunning = true;
                        }
                    } else {
                        ReadyToRunning = true;
                    }
                    /**
                     * 就绪队列
                     */
                    if (ReadyToRunning) {
                        try {// CPU为空，进程进入CPU运行
                            // 获取就绪队列头进程
                            Process p = memory.getReadyQueue().get(0);
                            // 进程退出就绪队列
                            memory.removeReadyProcess(p);
                            // 进程进入CPU运行
                            _2.runningProcess = p;
                            _2.isRunningNotEmpty = true;
                            // 设置进程状态
                            p.setStatus(1);
                        } catch (Exception e) {}
                    }
                    /**
                     * 后备队列
                     */
                    try {
                        List<Process> backQueue = _1.backgroundQueue.getBackQueue();
                        Process p = backQueue.get(0);
                        if (memory.addReadyProcess(p, allocateFit)) {
                            // 后备进程可以进入就绪队列，进程退出后备队列
                            _1.backgroundQueue.removeBackProcess(p);
                            p.setStatus(0);
                        }
                    } catch (Exception e) {

                    } finally {
                        _1.refreshTableView();
                        _2.refreshTableView();
                        _3.refreshTableView();
                        _4.refreshTableView1();
                        _4.refreshTableView2();
                    }
                } else {
                    _2.setInfo("停止");
                    _3.refreshTableView();
                }
            }
        }, 1000, 1100);
    }

    private void runProcess() {
        try {
            if (_2.runningProcess == null) {
                List<Process> readyQueue = memory.getReadyQueue();

                _2.runningProcess = readyQueue.get(0);
                _2.runningProcess.setStatus(1);
            }
        } catch (Exception e) {

        }
    }

    private void CPU() {
        try {
            Process runningProcess = _2.runningProcess;
            runningProcess.runtimeChangBy(-1);
            _2.showRunningProcess();
            if (runningProcess.getRuntime() <= 0) {
                memory.removeRunningProcess(runningProcess);
                _4.completionQueue.addCompletionProcess(runningProcess);
                _2.runningProcess = null;
            }
        } catch (Exception e) {

        }
    }

    private void loadProcess() {
        try {
            Process process = _1.backgroundQueue.getBackQueue().get(0);
            if (memory.addReadyProcess(process, allocateFit)) {
                _1.backgroundQueue.removeBackProcess(process);
            }
        } catch (Exception e) {

        }
    }

    public static void setScheduling(boolean i) {
        scheduling = i;
    }
    public static void setAllocateFit(int i) {
        allocateFit = i;
    }

    /**
     * 挂起进程
     */
    public void setSuspendButton() {
        openSuspendProcessWindow(memory.getLoadingQueue());
    }

    private void openSuspendProcessWindow(List<Process> loadedProcess) {
        // Create a new stage for the process window
        Stage processStage = new Stage();
        processStage.initModality(Modality.APPLICATION_MODAL);
        processStage.setTitle("挂起进程");

        // TableView setup
        TableView<Process> tableView = new TableView<>();
        TableColumn<Process, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Process, Integer> priorityColumn = new TableColumn<>("Priority");
        TableColumn<Process, Integer> runtimeColumn = new TableColumn<>("Runtime");
        TableColumn<Process, Integer> memoryColumn = new TableColumn<>("Memory");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        runtimeColumn.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        memoryColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));

        ObservableList<Process> processList = FXCollections.observableArrayList(new ArrayList<>(loadedProcess));
        for (Process p : processList) {
            if (p.getName().equals("OS")) {
                processList.remove(p);
                break;
            }
        }


        tableView.getColumns().addAll(nameColumn, priorityColumn, runtimeColumn, memoryColumn);
        tableView.setItems(processList);

        // Confirm button setup
        Button confirmButton = new Button("确定");
        confirmButton.setOnAction(e -> {
            Process selectedProcess = tableView.getSelectionModel().getSelectedItem();
            if (selectedProcess != null) {
                System.out.println("Selected Process: " + selectedProcess.getName());
                // Do something with the selected process
                if (_2.runningProcess == selectedProcess) {
                    // 当前运行进程被挂起
                    // 进程时间结束
                    // 进程退出内存
                    memory.removeRunningProcess(selectedProcess);
                    // 进程进入挂起队列
                    _4.suspendQueue.addSuspendProcess(selectedProcess);
                    // 设置进程状态
                    selectedProcess.setPCB(null);
                    selectedProcess.setStatus(-1);
                    // 将CPU置为空
                    _2.runningProcess = null;
                    _2.isRunningNotEmpty = false;
                    _2.showRunningProcess();

                    // 允许进程进入CPU
                    ReadyToRunning = true;
                } else {
                    // 就绪进程被挂起
                    // 进程退出就绪队列
                    memory.removeReadyProcess(selectedProcess);
                    memory.removeRunningProcess(selectedProcess);
                    // 进程进入挂起队列
                    _4.suspendQueue.addSuspendProcess(selectedProcess);
                    // 设置进程状态
                    selectedProcess.setPCB(null);
                    selectedProcess.setStatus(-1);
                }
                _2.setInfo("进程被挂起");
                _2.refreshTableView();
                _3.refreshTableView();
                _4.refreshTableView2();;
            } else {
                System.out.println("No process selected");
            }
            processStage.close(); // Close the process window after confirmation
        });

        // Cancel button setup
        Button cancelButton = new Button("取消");
        cancelButton.setOnAction(event -> {
            processStage.close();
        });
        HBox btn = new HBox(20);
        btn.getChildren().addAll(confirmButton,cancelButton);
        // Layout setup
        VBox processLayout = new VBox(10, tableView, btn);
        processLayout.setPadding(new Insets(10));

        Scene processScene = new Scene(processLayout, 400, 300);
        processStage.setScene(processScene);

        // Show the process window
        processStage.showAndWait();
    }

    /**
     * 解挂进程
     */
    public void setUnsuspendButton() {
        openUnSuspendProcessWindow(_4.suspendQueue.getSuspendQueue());
    }

    private void openUnSuspendProcessWindow(List<Process> suspendProcesses) {
        // Create a new stage for the process window
        Stage processStage = new Stage();
        processStage.initModality(Modality.APPLICATION_MODAL);
        processStage.setTitle("解挂进程");

        // TableView setup
        TableView<Process> tableView = new TableView<>();
        TableColumn<Process, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Process, Integer> priorityColumn = new TableColumn<>("Priority");
        TableColumn<Process, Integer> runtimeColumn = new TableColumn<>("Runtime");
        TableColumn<Process, Integer> memoryColumn = new TableColumn<>("Memory");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        runtimeColumn.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        memoryColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));

        ObservableList<Process> processList = FXCollections.observableArrayList(new ArrayList<>(suspendProcesses));
        for (Process p : processList) {
            if (p.getName().equals("OS")) {
                processList.remove(p);
                break;
            }
        }

        tableView.getColumns().addAll(nameColumn, priorityColumn, runtimeColumn, memoryColumn);
        tableView.setItems(processList);

        // Confirm button setup
        Button confirmButton = new Button("确定");
        confirmButton.setOnAction(e -> {
            Process selectedProcess = tableView.getSelectionModel().getSelectedItem();
            if (selectedProcess != null) {
                System.out.println("Selected Process: " + selectedProcess.getName());
                // Do something with the selected process
                // 进程退出挂起队列
                _4.suspendQueue.removeSuspendProcess(selectedProcess);
                String processName = selectedProcess.getName();
                int priority = selectedProcess.getPriority();
                int runTime = selectedProcess.getRuntime();
                int memoryUsage = selectedProcess.getMemory();
                Process process = new Process(processName, priority, runTime, memoryUsage);
                _1.backgroundQueue.addBackProcess(process);
                _1.refreshTableView();
                _4.refreshTableView2();
            } else {
                System.out.println("No process selected");
            }
            processStage.close(); // Close the process window after confirmation
        });

        // Cancel button setup
        Button cancelButton = new Button("取消");
        cancelButton.setOnAction(event -> {
            processStage.close();
        });

        HBox btn = new HBox(20);
        btn.getChildren().addAll(confirmButton,cancelButton);
        // Layout setup
        VBox processLayout = new VBox(10, tableView, btn);
        processLayout.setPadding(new Insets(10));

        Scene processScene = new Scene(processLayout, 400, 300);
        processStage.setScene(processScene);

        // Show the process window
        processStage.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        // 关闭程序，关闭计时器
        super.stop();
        timer.cancel();
    }
}

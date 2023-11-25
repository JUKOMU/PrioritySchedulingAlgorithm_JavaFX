package columnvbox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import memory.Memory;
import process.Process;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 内存占用折线图
     */
    private static final int MAX_DATA_POINTS = 10;
    private static int xSeriesData = 0;
    private static XYChart.Series<Number, Number> series;

    ObservableList<Process> processList;

    private Memory memory;

    public Process runningProcess; // 运行的进程

    public boolean isRunningNotEmpty = false; // 运行进程是否为空

    Label label1 = new Label("就绪队列");
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
        label1.setFont(new Font(25));
        label1.setPadding(new Insets(10, 10, 10, 10));
        label2.setFont(new Font(25));
        label2.setPadding(new Insets(5, 10, 0, 10));
        label3.setFont(new Font(20));
        label3.setPadding(new Insets(10, 10, 10, 13));
        label4.setFont(new Font(18));
        label4.setPadding(new Insets(2, 2, 2, 14));

        /**
         * 内存占用折线图
         */
        NumberAxis xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        NumberAxis yAxis = new NumberAxis(0, 100, 10); // 设置y轴最大值为100
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);  // 不显示x轴的数标
        xAxis.setTickMarkVisible(false);    // 不显示x轴的刻度线
        yAxis.setSide(Side.RIGHT); // 将 Y 轴显示在右边

        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, null, "%"));

        series = new XYChart.Series<>();
        series.setName("Memory Usage");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setHorizontalGridLinesVisible(true);

        lineChart.getData().add(series);


        this.getChildren().addAll(label1, tableView, label2, label3, label4, lineChart);
        this.setSpacing(10);
        refreshTableView();
    }
    public void refreshTableView() {
        int countReadyProcess = memory.getReadyQueue().size();
        processList = FXCollections.observableArrayList(memory.getReadyQueue());
        Platform.runLater(() -> {
            tableView.setItems(processList);
            label1.setText("就绪队列 ("+ countReadyProcess +"个进程)");
        });
        showRunningProcess();
    }

    public void showRunningProcess() {
        String message;
        try {
            message = runningProcess.getName() + "  优先级:" + runningProcess.getPriority() + "  时间:" + runningProcess.getRuntime();
        } catch (Exception e) {
            message = "";
        }
        String finalMessage = message;
        Platform.runLater(() -> {
            if (finalMessage.equals("")) {
                setInfo("空闲");
            } else {
                setInfo("调度中  正在运行:");
            }
            label4.setText(finalMessage);
        });
    }

    public void setInfo(String message) {
        Platform.runLater(() -> label3.setText(message));
    }
    public static void updateData(double newData) {
        Platform.runLater(() -> {
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(xSeriesData++, newData);
            if (series.getData().size() > MAX_DATA_POINTS) {
                dataPoint = new XYChart.Data<>(MAX_DATA_POINTS, newData);
                List<XYChart.Data<Number, Number>> points = new ArrayList<>();
                for (int i = 1; i < series.getData().size(); i++) {
                    XYChart.Data<Number, Number> numberNumberData = series.getData().get(i);
                    numberNumberData.setXValue(numberNumberData.getXValue().doubleValue()-1);
                    points.add(numberNumberData);
                }
                points.add(dataPoint);
                series.getData().clear();
                for (int i = 0; i < points.size(); i++) {
                    series.getData().add(points.get(i));
                }
            } else {
                series.getData().add(dataPoint);
            }
            // 设置绘制的点的形状为自定义节点
            dataPoint.nodeProperty().setValue(createDataNode());

        });
    }

    private static Node createDataNode() {
        Circle circle = new Circle(1); // 设置圆形的半径
        circle.setFill(Color.RED); // 设置圆形的填充颜色
        return circle;
    }
}

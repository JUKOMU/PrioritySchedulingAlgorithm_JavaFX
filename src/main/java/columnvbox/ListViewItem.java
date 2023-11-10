package columnvbox;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pcb.PCB;

public class ListViewItem extends VBox implements Comparable<ListViewItem> {
    private Button button;
    private Label label;
    private ProgressBar progressBar;
    private PCB PCB;

    public ListViewItem() {
    }

    public ListViewItem(int id, String name, int priority, long runTime, long IOTime) {
        this.PCB = null;
        this.label = new Label("P"+id+" "+name+" &优先级:"+priority+" &Time:"+runTime);
        this.button = new Button("");
        this.progressBar = new ProgressBar(0);

        HBox box = new HBox(35);
        box.getChildren().addAll(this.label,this.button);
        DoubleBinding columnWidthBinding = Bindings.createDoubleBinding(() -> this.getWidth() / 2, this.widthProperty());
        this.label.prefWidthProperty().bind(columnWidthBinding);
        this.setSpacing(2);
        this.getChildren().addAll(box,this.progressBar);

        this.setPrefWidth(Double.MAX_VALUE);
        box.prefWidthProperty().bind(this.prefWidthProperty());
        DoubleBinding columnWidthBinding2 = Bindings.createDoubleBinding(() -> this.getWidth() * 0.8, this.widthProperty());

        this.progressBar.prefWidthProperty().bind(columnWidthBinding2);
        this.setPadding(new Insets(2,10,1,1));
    }

    public Button getButton() {
        return button;
    }

    public pcb.PCB getPCB() {
        return PCB;
    }

    public void setPCB(pcb.PCB PCB) {
        this.PCB = PCB;
    }

    @Override
    public int compareTo(ListViewItem o) {
        return o.getPCB().getPriority() - this.getPCB().getPriority();
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void init() {
        this.progressBar.setProgress(0.0);
        this.PCB.setRunTime(0);
        this.PCB.getProcess().init();
    }
}

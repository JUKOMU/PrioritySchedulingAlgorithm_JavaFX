package columnvbox;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static app.MainApp.createListView;

public class _2 extends VBox {
    private final ListView<VBox> listViewTop;
    private final ArrayList<ListViewItem> listViewTopItems;
    private final ListView<VBox> listViewBottom = null;
    private ArrayList<ListViewItem> listViewBottomItems;
    public _2() {
        // 创建上半部分
        VBox topSection = createListView("CPU");
        // 创建列表
        listViewTop = new ListView<>();
        listViewTop.setPrefHeight(800);
        listViewTopItems = new ArrayList<>();
        listViewTop.setPadding(new Insets(1,10,1,1));
        topSection.getChildren().addAll(listViewTop);

        // 布局整体结构
        this.setPadding(new Insets(5,0,0,0));
        this.getChildren().addAll(topSection);
    }
    public void addListViewTopItem(ListViewItem item) {
        listViewTopItems.add(item);
        item.prefWidthProperty().bind(this.prefWidthProperty());
        refreshTop();
        System.out.println("add");
    }
    public void removeListViewTopItem(ListViewItem item) {
        listViewTopItems.remove(item);
    }
    public void removeListViewBottomItem(ListViewItem item) {
        listViewBottomItems.remove(item);
    }
    public void refreshTop() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                listViewTop.getItems().clear();
                listViewTop.getItems().addAll(listViewTopItems);
            }
        });
    }
    public void addListViewBottomItem(ListViewItem item) {
        listViewBottomItems.add(item);
        item.prefWidthProperty().bind(this.prefWidthProperty());
        refreshBottom();
    }

    public void refreshBottom() {
        listViewBottom.getItems().clear();
        listViewBottom.getItems().addAll(listViewBottomItems);
    }

    public ListViewItem getListViewTop() {
        return listViewTopItems.get(0);
    }

    public ArrayList<ListViewItem> getListViewTopItems() {
        return listViewTopItems;
    }

    public ListViewItem getListViewBottom() {
        return listViewBottomItems.get(0);
    }

    public ArrayList<ListViewItem> getListViewBottomItems() {
        return listViewBottomItems;
    }
}

package columnvbox;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import pcb.PCB;

import java.util.ArrayList;
import java.util.Collections;

import static app.MainApp.createListView;

public class _3 extends VBox {
    private final ListView<VBox> listViewTop;
    private final ArrayList<ListViewItem> listViewTopItems;
    private final ListView<VBox> listViewBottom;
    private final ArrayList<ListViewItem> listViewBottomItems;
    public _3() {
        // 创建上半部分
        VBox topSection = createListView("就绪队列");
        // 创建列表
        listViewTop = new ListView<>();
        listViewTop.setPrefHeight(400);
        listViewTopItems = new ArrayList<>();
        listViewTop.setPadding(new Insets(1,10,1,1));
        topSection.getChildren().addAll(listViewTop);

        // 创建下半部分
        VBox bottomSection = createListView("挂起队列");
        // 创建列表
        listViewBottom = new ListView<>();
        listViewBottom.setPrefHeight(400);
        listViewBottomItems = new ArrayList<>();
        listViewBottom.setPadding(new Insets(1,10,1,1));
        bottomSection.getChildren().addAll(listViewBottom);

        // 布局整体结构
        this.setPadding(new Insets(5,0,0,0));
        this.getChildren().addAll(topSection,bottomSection);
    }

    public void addListViewTopItem(ListViewItem item) {
        listViewTopItems.add(item);
        item.prefWidthProperty().bind(this.prefWidthProperty());
        Collections.sort(listViewTopItems);
        refreshTop();
    }
    public void removeListViewTopItem(ListViewItem item) {
        listViewTopItems.remove(item);
    }
    public void removeListViewBottomItem(ListViewItem item) {
        listViewBottomItems.remove(item);
    }
    public void refreshTop() {
        listViewTop.getItems().clear();
        listViewTop.getItems().addAll(listViewTopItems);
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

    public ListViewItem removeFirstListViewTopItem() {
        ListViewItem item = listViewTopItems.get(0);
        listViewTopItems.remove(0);
        refreshTop();
        return item;
    }
}

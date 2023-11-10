package columnvbox;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static app.MainApp.createListView;

public class _4 extends VBox {
    private final ListView<VBox> listView;
    private final ArrayList<ListViewItem> listItems;
    public _4() {
        VBox section = createListView("完成进程");

        // 创建列表
        listView = new ListView<>();
        listView.setPrefHeight(800);
        listItems = new ArrayList<>();
        listView.setPadding(new Insets(1,10,1,1));
        section.getChildren().addAll(listView);
        // 布局整体结构
        this.setPadding(new Insets(5,0,0,0));
        this.getChildren().add(section);
    }

    public void removeListViewItem(ListViewItem item) {
        listItems.remove(item);
    }


    public void refresh() {
        listView.getItems().clear();
        listView.getItems().addAll(listItems);
    }

    public void addListViewItem(ListViewItem item) {
        listItems.add(item);
        item.getButton().setText("还原");
        item.prefWidthProperty().bind(this.prefWidthProperty());
        refresh();
    }
}

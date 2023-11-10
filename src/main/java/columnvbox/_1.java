package columnvbox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static app.MainApp.*;

public class _1 extends VBox {
    private final ListView<VBox> listView;
    private final ArrayList<ListViewItem> listItems;

    private final ArrayList<Button> buttons;
    public _1() {
        // 创建上半部分（按钮）
        VBox topSection = new VBox(15);
        Button button1 = new Button("演示");
        Button button5 = new Button("添加作业");
        button1.setOnAction(event -> {demonstrationMode();});
        button1.setAlignment(Pos.CENTER);
        button5.setOnAction(event -> {addScheduledTask();});
        button5.setAlignment(Pos.CENTER);

        buttons = new ArrayList<Button>();
        buttons.add(button1);
        buttons.add(button5);
        HBox row3 = new HBox(20, button5,button1);

        // 设置每行水平居中对齐);
        row3.setAlignment(Pos.CENTER);

        topSection.setPadding(new Insets(0,5,0,0));


        topSection.getChildren().addAll(row3);

        VBox bottomSection = createListView("后备队列");

        // 创建列表
        listView = new ListView<>();
        listView.setPrefHeight(700);
        listItems = new ArrayList<>();
        listView.setPadding(new Insets(1,10,1,1));
        bottomSection.getChildren().addAll(listView);
        // 布局整体结构
        this.setPadding(new Insets(5,0,0,0));
        this.getChildren().addAll(topSection, bottomSection);
    }
    public void addListViewItem(ListViewItem item) {
        listItems.add(item);
        item.prefWidthProperty().bind(this.prefWidthProperty());
        refresh();
    }
    public void refresh() {
        listView.getItems().clear();
        listView.getItems().addAll(listItems);
    }
    public void removeListViewItem(ListViewItem item) {
        listItems.remove(item);
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public ArrayList<ListViewItem> getListItems() {
        return listItems;
    }
}

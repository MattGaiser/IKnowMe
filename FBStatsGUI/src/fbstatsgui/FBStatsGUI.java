/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fbstatsgui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author Matth
 */
public class FBStatsGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Facebook Data Analysis");
        Group root = new Group();
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuView = new Menu("View");
        //menuBar.getMenus().addAll(menuFile, menuEdit, menuView);        
        TabPane tabPane = new TabPane();
        tabPane.tabMinWidthProperty().set(145);
        tabPane.tabMinHeightProperty().set(40);
        BorderPane borderPane = new BorderPane();
        String[] tabNames = {"Home", "Messages", "Friends", "Comments", "Analytics"};
        for (int i = 0; i < 5; i++) {
            Tab tab = new Tab();
            tab.setText(tabNames[i]);
            HBox hbox = new HBox();
            hbox.getChildren().add(new Label("Tab" + i));
            if (tabNames[i].contentEquals("Friends")) {
                TilePane tile = new TilePane();
                tile.setHgap(8);
                tile.setPrefColumns(4);
                for (int j = 0; j < 8; j++) {
                    tile.getChildren().add(friendProfile());
                }
                hbox.getChildren().add(tile);
            }
            hbox.setAlignment(Pos.CENTER);
            tab.setContent(hbox);
            tabPane.getTabs().add(tab);
        }
        // bind to take available space

        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
        root.getChildren().addAll(menuBar);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static HBox friendProfile() {
        HBox hbox = new HBox();
        hbox.getChildren().add(new Label("Statistics"));
        hbox.getChildren().add(new Label("Name: Ellen Anders"));

        return hbox; 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

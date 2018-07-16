package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("文件导入到数据库");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.getIcons().add(new Image(//窗口左上角图标
                Main.class.getResourceAsStream("/img/ico.jpg")));
        primaryStage.show();
        System.out.println("当前程序路径 "+System.getProperty("user.dir").toString());
    }


    public static void main(String[] args) {
        launch(args);
    }
}

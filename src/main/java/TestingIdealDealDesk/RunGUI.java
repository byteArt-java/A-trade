package TestingIdealDealDesk;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.util.Arrays;

public class RunGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/GUI.fxml"));

        primaryStage.setTitle("USD Testing Ideal Deal");
        Toolkit t = Toolkit.getDefaultToolkit();//размеры окна рабочего полностью
        Dimension dimension = t.getScreenSize();
        primaryStage.setScene(new Scene(root, 961, 703));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent event) {
                System.out.println(Arrays.toString(AnalyzeNoRandom.checkNoRandom));
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

}

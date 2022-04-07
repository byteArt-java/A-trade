import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;

public class RunGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("A-Trade");
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension dimension = t.getScreenSize();
        primaryStage.setScene(new Scene(root, dimension.getWidth() - 20, dimension.getHeight() - 20));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    private void initAreaChart(){

    }
}

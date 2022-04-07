import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML AreaChart<Number,Number> areaChart;

    @FXML TextArea lastOperation, textAreaNasdaq, textAreaNYSE, textAreaJPX, textAreaLSE, textAreaMOEX;
    @FXML TextField bootAddress;
    @FXML Button bootButton, buttonShowAllDeals;
    @FXML RadioButton radioDDEServer, radioReport;
    @FXML Label labelAverageYields, labelMemory;
    @FXML ToggleGroup radioGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberAxis xAxis = new NumberAxis(0,12,1);
        xAxis.setLabel("месяцы");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("тыс р");
        areaChart = new AreaChart<Number, Number>(xAxis,yAxis);
        areaChart.setTitle("Доходность");
        areaChart.setLegendSide(Side.LEFT);

        XYChart.Series<Number,Number> series2021 = new XYChart.Series<Number,Number>();
        series2021.setName("2021");
//        series2021.getData().add(new XYChart.Data<>(1,72716));
//        series2021.getData().add(new XYChart.Data<>(2,3951));
//        series2021.getData().add(new XYChart.Data<>(3,-19786));
//        series2021.getData().add(new XYChart.Data<>(4,138389));
//        series2021.getData().add(new XYChart.Data<>(5,89652));
//        series2021.getData().add(new XYChart.Data<>(6,131679));
//        series2021.getData().add(new XYChart.Data<>(7,259514));
//        series2021.getData().add(new XYChart.Data<>(8,-46744));
//        series2021.getData().add(new XYChart.Data<>(9,-130169));
//        series2021.getData().add(new XYChart.Data<>(10,130865));
//        series2021.getData().add(new XYChart.Data<>(11,239441));
//        series2021.getData().add(new XYChart.Data<>(12,224297));
        areaChart.getData().add(series2021);
    }

    @FXML private void bootDeals(){
        RadioButton radioButton = new RadioButton();
    }

    @FXML private void showAllDeals(){
        String pathAllDeals = "";
    }
}

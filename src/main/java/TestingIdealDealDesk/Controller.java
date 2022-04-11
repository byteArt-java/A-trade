package TestingIdealDealDesk;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final String yesShortDownPathPrefix = "YesShortDown/";
    private final String yesLongDownPathPrefix = "YesLongDown/";
    private final String yesShortUpPathPrefix = "YesShortUp/";
    private final String yesLongUpPathPrefix = "YesLongUp/";
    private final String noPathPrefix = "No/";

    private int count = 1;
    private int countTrue = 0;
    private int countFalse = 0;
    private final List<ImageAdapter> imagesShort = new ArrayList<>();
    private final List<ImageAdapter> imagesLong = new ArrayList<>();
    private int randomPhoto = 0;

    @FXML ImageView imageTesting;
    @FXML TextArea outputResult;
    @FXML TextArea textTrue;
    @FXML TextArea textFalse;
    @FXML RadioButton trueUp;
    @FXML RadioButton radioFalse;
    @FXML RadioButton trueDown;
    @FXML Button sendTesting;
    @FXML Button next;

    ToggleGroup toggleGroup = new ToggleGroup();

    public Controller() throws InterruptedException {
        randomPhoto = (int) (Math.random() * imagesShort.size());
        addImages(imagesShort,imagesLong);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radioFalse.setSelected(true);

        imageTesting.setImage(imagesShort.get(randomPhoto).image);
        trueDown.setToggleGroup(toggleGroup);
        trueUp.setToggleGroup(toggleGroup);
        radioFalse.setToggleGroup(toggleGroup);
    }

    public void sendCheckResult(ActionEvent actionEvent) {
        if (toggleGroup.getSelectedToggle().toString().contains("trueDown")
                && imagesShort.get(randomPhoto).pathImage.equals("YesShortDown/")){
            outputResult.appendText(countTrue + ". Верно\n");
            textTrue.setText(++countTrue + ".Верно");
            imageTesting.setImage(imagesLong.get(randomPhoto).image);
        }else if (toggleGroup.getSelectedToggle().toString().contains("trueUp") &&
                imagesShort.get(randomPhoto).pathImage.equals("YesShortUp/")){
            outputResult.appendText(count + ". Верно\n");
            textTrue.setText(++countTrue + ".Верно");
            imageTesting.setImage(imagesLong.get(randomPhoto).image);
        }else if (toggleGroup.getSelectedToggle().toString().contains("radioFalse") &&
                imagesShort.get(randomPhoto).pathImage.equals("YesShortDown/") ||
                toggleGroup.getSelectedToggle().toString().contains("radioFalse") &&
                        imagesShort.get(randomPhoto).pathImage.equals("YesShortUp/")){
            outputResult.appendText(count + ". Неверно\n");
            textFalse.setText(++countFalse + ".Не верно");
            imageTesting.setImage(imagesLong.get(randomPhoto).image);
        }else if (toggleGroup.getSelectedToggle().toString().contains("radioFalse") &&
                imagesShort.get(randomPhoto).pathImage.equals("No/")){
            outputResult.appendText(count + ". Верно\n");
            textTrue.setText(++countTrue + ".Верно");
        }else {
            outputResult.appendText(count + ". Не верно\n");
            textFalse.setText(++countFalse + ".Не верно");
        }
    }

    public void nextImage(){
        randomPhoto = (int) (Math.random() * imagesShort.size());
        imageTesting.setImage(imagesShort.get(randomPhoto).image);
        count++;
        System.out.println(imagesShort.get(randomPhoto).pathImage + ". Count: " + count + ". Random Number = " +
                randomPhoto);
    }

    private void addImages(List<ImageAdapter> imagesShort,List<ImageAdapter> imagesLong){

        for (int i = 0; i < 23; i++) {
            imagesShort.add(new ImageAdapter(new Image(yesShortDownPathPrefix + (i + 1) + ".jpg"), yesShortDownPathPrefix));
        }
        for (int i = 0; i < 22; i++) {
            imagesShort.add(new ImageAdapter(new Image(yesShortUpPathPrefix + (i + 1) + ".jpg"), yesShortUpPathPrefix));
        }
        for (int i = 0; i < 23; i++) {
            imagesLong.add(new ImageAdapter(new Image(yesLongDownPathPrefix + (i + 1) + ".jpg"), yesLongDownPathPrefix));
        }
        for (int i = 0; i < 22; i++) {
            imagesLong.add(new ImageAdapter(new Image(yesLongUpPathPrefix + (i + 1) + ".jpg"), yesLongUpPathPrefix));
        }
        for (int i = 0; i < 18; i++) {
            imagesShort.add(new ImageAdapter(new Image(noPathPrefix + (i + 1) + ".jpg"),noPathPrefix));
        }
    }
}

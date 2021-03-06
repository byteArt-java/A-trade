package TestingIdealDealDesk;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static TestingIdealDealDesk.AnalyzeNoRandom.checkNoRandom;

public class Controller implements Initializable {
    private final String yesShortDownPathPrefix = "YesShortDown/";
    private final String yesLongDownPathPrefix = "YesLongDown/";
    private final String yesShortUpPathPrefix = "YesShortUp/";
    private final String yesLongUpPathPrefix = "YesLongUp/";
    private final String noPathPrefix = "No/";

    private int countNextImage = 0;

    private int countTrue = 0;
    private int countFalse = 0;
    private final List<ImageAdapter> imagesShort = new ArrayList<>();
    private final List<ImageAdapter> imagesLong = new ArrayList<>();
    private int randomPhoto = 0;
    private int sizeFolderNo;
    private int sizeFolderYesDown;
    private int sizeFolderYesUp;

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

        sizeFolderNo = Objects.requireNonNull(new File("C:\\Users\\CFA\\IdeaProjects" +
                "\\A-trade\\src\\main\\resources\\No").listFiles()).length;

        sizeFolderYesDown = Objects.requireNonNull(new File("C:\\Users\\CFA\\IdeaProjects\\A-trade" +
                "\\src\\main\\resources\\YesLongDown").listFiles()).length;

        sizeFolderYesUp = Objects.requireNonNull(new File("C:\\Users\\CFA\\IdeaProjects\\A-trade" +
                "\\src\\main\\resources\\YesLongUp").listFiles()).length;

        addImages(imagesShort,imagesLong);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radioFalse.setSelected(true);

        checkNoRandom = new int[imagesShort.size()];
        checkNoRandom[randomPhoto] = 1;

        imageTesting.setImage(imagesShort.get(randomPhoto).image);
        trueDown.setToggleGroup(toggleGroup);
        trueUp.setToggleGroup(toggleGroup);
        radioFalse.setToggleGroup(toggleGroup);
    }

    public void sendCheckResult(ActionEvent actionEvent) throws InterruptedException {
        if (countNextImage == (countTrue + countFalse)){
            if (toggleGroup.getSelectedToggle().toString().contains("trueDown")
                    && imagesShort.get(randomPhoto).pathImage.equals("YesShortDown/")){
                textTrue.setText(++countTrue + ".Верно");
                imageTesting.setImage(imagesLong.get(randomPhoto).image);
            }else if (toggleGroup.getSelectedToggle().toString().contains("trueUp") &&
                    imagesShort.get(randomPhoto).pathImage.equals("YesShortUp/")){
                textTrue.setText(++countTrue + ".Верно");
                imageTesting.setImage(imagesLong.get(randomPhoto).image);
            }else if (toggleGroup.getSelectedToggle().toString().contains("radioFalse") &&
                    imagesShort.get(randomPhoto).pathImage.equals("YesShortDown/") ||
                    toggleGroup.getSelectedToggle().toString().contains("radioFalse") &&
                            imagesShort.get(randomPhoto).pathImage.equals("YesShortUp/")){
                textFalse.setText(++countFalse + ".Не верно");
                if (countFalse >= 2){
                    outputResult.setText("Вы не сдали тест");
                }
                imageTesting.setImage(imagesLong.get(randomPhoto).image);
            }else if (toggleGroup.getSelectedToggle().toString().contains("radioFalse") &&
                    imagesShort.get(randomPhoto).pathImage.equals("No/")){
                textTrue.setText(++countTrue + ".Верно");
            }else {
                textFalse.setText(++countFalse + ".Нет входа");
                if (countFalse >= 2){
                    outputResult.setText("Вы не сдали тест");
                }
            }
        }
    }

    public void nextImage(ActionEvent actionEvent) throws IOException {
        countNextImage++;

        if (countNextImage == (countFalse + countTrue)){//тут проверяется,что кнопка next была нажата, но не была
            //нажата sendTesting, тогда изобр остается прежним и программа просит проверку результата
            randomPhoto = (int) (Math.random() * imagesShort.size());
            //тут проверяется, если числоРандом уже было оно меняется на другое, кот еще не было
            while (checkNoRandom[randomPhoto] != 0){
                if (AnalyzeNoRandom.size == checkNoRandom.length - 1){
                    outputResult.appendText("All Image Showed\n");
                    break;
                }
                randomPhoto = (int) (Math.random() * imagesShort.size());
            }
            AnalyzeNoRandom.size++;
        }else if (countNextImage > (countFalse + countTrue)){
            countNextImage = (countFalse + countTrue);
        }


        checkNoRandom[randomPhoto] = 1;
        imageTesting.setImage(imagesShort.get(randomPhoto).image);
        System.out.println(imagesShort.get(randomPhoto).pathImage + ". Count: " + countNextImage + ". Random Number = " +
                randomPhoto);
    }

    private void addImages(List<ImageAdapter> imagesShort,List<ImageAdapter> imagesLong){
        for (int i = 0; i < sizeFolderYesDown; i++) {
            imagesShort.add(new ImageAdapter(new Image(yesShortDownPathPrefix + (i + 1) + ".jpg"), yesShortDownPathPrefix));
        }
        for (int i = 0; i < sizeFolderYesUp; i++) {
            imagesShort.add(new ImageAdapter(new Image(yesShortUpPathPrefix + (i + 1) + ".jpg"), yesShortUpPathPrefix));
        }
        for (int i = 0; i < sizeFolderYesDown; i++) {
            imagesLong.add(new ImageAdapter(new Image(yesLongDownPathPrefix + (i + 1) + ".jpg"), yesLongDownPathPrefix));
        }
        for (int i = 0; i < sizeFolderYesUp; i++) {
            imagesLong.add(new ImageAdapter(new Image(yesLongUpPathPrefix + (i + 1) + ".jpg"), yesLongUpPathPrefix));
        }
        for (int i = 0; i < sizeFolderNo; i++) {
            imagesShort.add(new ImageAdapter(new Image(noPathPrefix + (i + 1) + ".jpg"),noPathPrefix));
        }
    }
}

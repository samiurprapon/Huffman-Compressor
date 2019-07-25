package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.HuffmanCompressing;
import models.HuffmanDecompressing;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    public Button mEncode;

    @FXML
    public Button mDecode;

    @FXML
    public Button mOpen;

    @FXML
    public Label mCurrent;

    @FXML
    public Label mPrevious;

    @FXML
    public Label mName;

    @FXML
    public ImageView mLoading;

    private File openedFile;
    private File anotherFile;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage = new Stage();
        mLoading.setVisible(false);
        mDecode.setOnAction(event -> decoding());
        mEncode.setOnAction(event -> encoding());
        mOpen.setOnAction(event -> openFileSystem());

    }


    private void openFileSystem() {
        FileChooser fileChooser = new FileChooser();
        openedFile = fileChooser.showOpenDialog(null);

        if (openedFile != null) {
            mName.setText(openedFile.getName());
            mPrevious.setText(openedFile.length() + " bytes");
        }

    }

    private void decoding() {

        Thread timer = new Thread(() -> {
            try {
                if (openedFile != null) {
                    mLoading.setVisible(true);
                    String pathAddress = openedFile.getPath();
                    HuffmanDecompressing.initialize(openedFile.getPath());

                    pathAddress = pathAddress.substring(0, pathAddress.length() - 5);
                    anotherFile = new File(pathAddress);
                }
            } finally {
                mLoading.setVisible(false);

                mCurrent.setText(anotherFile.length() + " Bytes");
            }
        });

        timer.start();
    }

    private void encoding() {

        Thread timer = new Thread(() -> {
            try {
                if (openedFile != null) {
                    mLoading.setVisible(true);
                    HuffmanCompressing.initialize(openedFile.getPath());
                    mPrevious.setText(openedFile.length() + " Bytes");

                    anotherFile = new File(openedFile.getPath() + ".hufz");
                }
            } finally {
                mLoading.setVisible(false);
                mCurrent.setText(anotherFile.length() + " Bytes");
            }
        });

        timer.start();

    }

}

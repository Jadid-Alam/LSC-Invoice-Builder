package org.example.lscinvoicebuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;
import java.awt.Desktop;

public class MainController {

    private DirectoryChooser directoryChooser;
    private Storage storage;
    public Storage getStorage() {return storage;}
    public void setStorage(Storage storage) {this.storage = storage;}
    private Stage stage;
    public Stage getStage() {return stage;}
    public void setStage(Stage stage) {this.stage = stage;}
    private SecondController secondController;
    public SecondController getSecondController() {return secondController;}
    public void setSecondController(SecondController secondController) {this.secondController = secondController;}
    private Scene scene2;
    public Scene getScene2() {return scene2;}
    public void setScene2(Scene scene2) {this.scene2 = scene2;}
    private String fileLocation;

    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Label fileOutputLabel;
    @FXML
    private Button fileOutputButton;
    @FXML
    private Label startDateValidation;
    @FXML
    private Label endDateValidation;
    @FXML
    private VBox paymentInformation;
    @FXML
    private Button generateInvoiceButton;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    protected void handleAddCustomer() {
        stage.setScene(this.scene2);
        stage.show();

        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        customerComboBox.setValue("");
    }

    @FXML
    public void initialize() {
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    String formattedDate = newValue.format(formatter);
                } catch (DateTimeParseException e) {
                    startDatePicker.setValue(oldValue);
                }
            }
        });

        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    String formattedDate = newValue.format(formatter);
                } catch (DateTimeParseException e) {
                    endDatePicker.setValue(oldValue);
                }
            }
        });

        customerComboBox.valueProperty().addListener((obserable, oldValue, newValue) -> {
            String customer = customerComboBox.getValue();
            int id = storage.findID(customer);
            if (id != -1) {
                storage.setId(id);
                storage.load(id);
                refreshPaymentInformation();
            }

        });


        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        fileLocation = determineDownloadsPath();
        directoryChooser.setInitialDirectory(new File(fileLocation));
        fileOutputLabel.setText("Save Invoice to: " + fileLocation);
    }


    public void refreshDropdown() {
        customerComboBox.getItems().clear();
        try {
            this.storage.refreshDropdownOptions();
            for (String option : storage.getDropdownOptions()) {
                customerComboBox.getItems().add(option);
            }

        } catch (NullPointerException e) {
            System.out.println("No customers found");
        }
    }


    public void refreshPaymentInformation() {
        paymentInformation.getChildren().clear();
        for (int i = 0; i < storage.getNoOfChildren(); i++) {
            Label l = new Label();
            l.getStyleClass().add("paymentLabel");
            l.setId("weeklyFeeLabel"+i);
            l.setText(storage.getStudentName(i) + "'s Fees/Week:");
            TextField weeklyFee = new TextField();
            weeklyFee.getStyleClass().add("paymentTextField");
            weeklyFee.setId("weeklyFee"+i);
            weeklyFee.setPromptText("(Required)");

            Label l2 = new Label();
            l2.getStyleClass().add("paymentLabel");
            l2.setId("hoursPerWeekLabel"+i);
            l2.setText(storage.getStudentName(i) + "'s Hours/Week:");
            TextField hoursPerWeek = new TextField();
            hoursPerWeek.getStyleClass().add("paymentTextField");
            hoursPerWeek.setId("hoursPerWeek"+i);
            hoursPerWeek.setPromptText("(Optional)");

            HBox hBox = new HBox();
            hBox.getStyleClass().add("paymentInformationHBox");
            hBox.getChildren().add(l);
            hBox.getChildren().add(weeklyFee);

            HBox hBox1 = new HBox();
            hBox1.getStyleClass().add("paymentInformationHBox");
            hBox1.getChildren().add(l2);
            hBox1.getChildren().add(hoursPerWeek);

            paymentInformation.getChildren().add(hBox);
            paymentInformation.getChildren().add(hBox1);

            weeklyFee.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("^\\$?\\d*(\\.\\d{0,2})?$")) {
                    weeklyFee.setText(oldValue);
                }
            });

            hoursPerWeek.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("^\\$?\\d*(\\.\\d{0,2})?$")) {
                    hoursPerWeek.setText(oldValue);
                }
            });
        }
    }

    public void clearPaymentInformation() {
        paymentInformation.getChildren().clear();
    }


    @FXML
    private void handleBrowse() {
        Stage stage = (Stage) fileOutputLabel.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            fileOutputLabel.setText("Save Invoice to: " + selectedDirectory.getAbsolutePath());
            storage.setPdfLocation(selectedDirectory.getAbsolutePath());
        } else {
            storage.setPdfLocation(determineDownloadsPath());
            fileOutputLabel.setText("Save Invoice to: " + determineDownloadsPath());
        }
    }

    private String determineDownloadsPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (os.contains("win")) {
            return userHome + "\\Downloads";
        } else if (os.contains("mac")) {
            return userHome + "/Downloads";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return userHome + "/Downloads";
        } else {
            return userHome;
        }
    }

    private void determineFileName() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String startD = startDatePicker.getValue().format(formatter1);
        String endD = endDatePicker.getValue().format(formatter1);

        if (os.contains("win")) {
            storage.setPdfName(storage.getPdfLocation()+"\\"+storage.getLName()+" "+startD+" to "+endD+".pdf");
        } else if (os.contains("mac")) {
            storage.setPdfName(storage.getPdfLocation()+"/"+storage.getLName()+" "+startD+" to "+endD+".pdf");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            storage.setPdfName(storage.getPdfLocation()+"/"+storage.getLName()+" "+startD+" to "+endD+".pdf");
        }
    }

    @FXML
    private void handleGenerateInvoice() {

        if (customerComboBox.getValue() == null || startDatePicker.getValue() == null || endDatePicker.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter all required information");
            alert.showAndWait();
            return;
        }


        String customer = customerComboBox.getValue();
        if (storage.findID(customer) != -1)
        {
            storage.setStartDate(startDatePicker.getValue().format(formatter));
            storage.setEndDate(endDatePicker.getValue().format(formatter));
            storage.setId(storage.findID(customer));
            storage.load(storage.findID(customer));
            determineFileName();

            boolean isHrThere = false;
            for (int i = 0; i < storage.getNoOfChildren(); i++) {
                TextField weeklyFee = (TextField) paymentInformation.lookup("#weeklyFee" + i);
                TextField hoursPerWeek = (TextField) paymentInformation.lookup("#hoursPerWeek" + i);

                if (weeklyFee.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Please enter all required information");
                    alert.showAndWait();
                    return;
                }

                if (weeklyFee.getText() != null) {
                    storage.setStudentFPW(Double.parseDouble(weeklyFee.getText()), i);
                }

                if (hoursPerWeek != null && !hoursPerWeek.getText().isEmpty()) {
                    storage.setStudentHPW(Double.parseDouble(hoursPerWeek.getText()), i);
                    storage.setStudentPPW(storage.getStudentFPW(i) / storage.getStudentHPW(i), i);
                    isHrThere = true;
                }
            }
            storage.setIsHoursThere(isHrThere);

            PdfBuilder pdfBuilder = new PdfBuilder(storage);
            pdfBuilder.createPdf();

            File folder = new File(fileLocation);
            if (folder.exists() && folder.isDirectory()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.open(folder);
                        System.out.println("Folder opened: " + fileLocation);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Failed to open folder: " + fileLocation);
                    }
                } else {
                    System.out.println("Desktop API is not supported on this platform.");
                }
            } else {
                System.out.println("The specified folder does not exist or is not a directory.");
            }

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer not found");
            alert.setContentText("Please select a customer from the dropdown list (or add a new one if the dropdown is empty)");
            alert.showAndWait();


        }
    }

}
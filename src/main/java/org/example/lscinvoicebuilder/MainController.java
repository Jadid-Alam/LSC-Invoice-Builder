package org.example.lscinvoicebuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

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

    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Label fileOutputLabel;
    @FXML
    private Label startDateValidation;
    @FXML
    private Label endDateValidation;
    @FXML
    private VBox paymentInformation;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    protected void handleAddCustomer() {
        stage.setScene(this.scene2);
        stage.show();
    }

    @FXML
    public void initialize() {
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    String formattedDate = newValue.format(formatter);
                    startDateValidation.setText("");
                } catch (DateTimeParseException e) {
                    startDatePicker.setValue(oldValue);
                    startDateValidation.setText("Invalid date format. Please select a valid date.");
                }
            } else {
                startDateValidation.setText("No date selected!");
            }
        });

        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    endDateValidation.setText("");
                } catch (DateTimeParseException e) {
                    endDatePicker.setValue(oldValue);
                    endDateValidation.setText("Invalid date format. Please select a valid date.");
                }
            } else {
                endDateValidation.setText("No date selected!");
            }
        });

        customerComboBox.valueProperty().addListener((obserable, oldValue, newValue) -> {
            System.out.println("Customer changed1");
            String customer = customerComboBox.getValue();
            System.out.println(customer);
            int id = storage.findID(customer);
            if (id != -1) {
                storage.setId(id);
                storage.load(id);
                refreshPaymentInformation();
                System.out.println("Customer changed2");
            }

        });


        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        directoryChooser.setInitialDirectory(new File(determineDownloadsPath()));
        fileOutputLabel.setText("Save Invoice to: " + determineDownloadsPath());
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
            l.setId("weeklyFeeLabel"+i);
            l.setText("Student " + (i + 1) + " Weekly Fee:");
            TextField weeklyFee = new TextField();
            weeklyFee.setId("weeklyFee"+i);
            weeklyFee.setPromptText("Please enter the weekly fee for student " + (i + 1) + "(Required)");
            Label l2 = new Label();
            l2.setId("hoursPerWeekLabel"+i);
            l2.setText("Student " + (i + 1) + " Hours Per Week:");
            TextField hoursPerWeek = new TextField();
            hoursPerWeek.setId("hoursPerWeek"+i);
            hoursPerWeek.setPromptText("Please enter the hours per week for student " + (i + 1) + "(Optional)");

            paymentInformation.getChildren().add(l);
            paymentInformation.getChildren().add(weeklyFee);
            paymentInformation.getChildren().add(l2);
            paymentInformation.getChildren().add(hoursPerWeek);

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


    @FXML
    private void handleBrowse() {
        Stage stage = (Stage) fileOutputLabel.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            fileOutputLabel.setText("Save Invoice to: " + selectedDirectory.getAbsolutePath());
            storage.setPdfLocation(selectedDirectory.getAbsolutePath());
        } else {
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

            System.out.println(storage.getRegistry());
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
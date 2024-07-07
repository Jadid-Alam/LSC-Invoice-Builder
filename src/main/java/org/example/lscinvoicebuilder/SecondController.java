package org.example.lscinvoicebuilder;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;


public class SecondController {
    private Stage stage;
    public Stage getStage() {return stage;}
    public void setStage(Stage stage) {this.stage = stage;}
    private MainController mainController;
    public MainController getMainController() {return mainController;}
    public void setMainController(MainController mainController) {this.mainController = mainController;}
    private Scene scene1;
    public Scene getScene1() {return scene1;}
    public void setScene1(Scene scene1) {this.scene1 = scene1;}
    private Storage storage;
    public Storage getStorage() {return storage;}
    public void setStorage(Storage storage) {this.storage = storage;}

    @FXML
    private ComboBox registryComboBox;
    @FXML
    private VBox studentDetailsContainer;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField address1TextField;
    @FXML
    private TextField address2TextField;
    @FXML
    private TextField address3TextField;
    @FXML
    private TextField postcodeTextField;
    @FXML
    private Spinner<Integer> noOfStudentsSpinner;
    private String[] studentNames;
    private String[] studentDOBs;

    @FXML
    private void handleBack() {
        stage.setScene(this.scene1);
        stage.show();
    }

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1);
        noOfStudentsSpinner.setValueFactory(valueFactory);
    }

    @FXML
    public void handleAddStudent() {
        refreshStudentInputs();
    }

    @FXML
    public void refreshStudentInputs() {
        studentDetailsContainer.getChildren().clear();
        for (int i = 0; i < noOfStudentsSpinner.getValue(); i++) {
            Label label = new Label();
            label.setId("studentLabel" + i);
            label.setText("Student " + (i+1) + ":");
            TextField textField = new TextField();
            textField.setId("studentName" + i);
            textField.setPromptText("Please enter the student's name (Required)");
            Label label2 = new Label();
            label2.setId("studentDOBLabel" + i);
            label2.setText("Student " + (i+1) + "'s Date of Birth:");
            DatePicker datePicker = new DatePicker();
            datePicker.setId("studentDOB" + i);

            studentDetailsContainer.getChildren().add(label);
            studentDetailsContainer.getChildren().add(textField);
            studentDetailsContainer.getChildren().add(label2);
            studentDetailsContainer.getChildren().add(datePicker);
        }
    }

    @FXML
    public void handleAddCustomer() {

        storage.setRegistry(registryComboBox.getValue().toString());
        storage.setTitle(titleTextField.getText());
        storage.setFName(firstNameTextField.getText());
        storage.setLName(lastNameTextField.getText());
        storage.setAddress1(address1TextField.getText());
        storage.setAddress2(address2TextField.getText());
        storage.setAddress3(address3TextField.getText());
        storage.setPostcode(postcodeTextField.getText());
        storage.setNoOfChildren(noOfStudentsSpinner.getValue());
        storage.setStudentNames(new String[storage.getNoOfChildren()]);
        storage.setStudentDOBs(new String[storage.getNoOfChildren()]);

        for (int i = 0; i < noOfStudentsSpinner.getValue(); i++) {
            TextField studentName = (TextField) studentDetailsContainer.lookup("#studentName" + i);
            DatePicker studentDOB = (DatePicker) studentDetailsContainer.lookup("#studentDOB" + i);
            storage.setStudentName(studentName.getText(), i );
            storage.setStudentDOB(studentDOB.getValue().toString(), i );
        }

        storage.store();
        storage.refreshDropdownOptions();
        mainController.refreshDropdown();
        handleBack();
    }

}

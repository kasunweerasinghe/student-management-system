package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import view.tdm.StudentTM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentFormController {
    public AnchorPane StudentManageFormContext;
    public JFXTextField txtStudentID;
    public JFXTextField txtStudentName;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextField txtAddress;
    public JFXTextField txtNIc;
    public JFXButton btnAddNewStudent;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public TableView tblStudent;

    public void initialize(){

    }

    public void btnAddNewStudentOnAction(ActionEvent actionEvent) {
        txtStudentID.setDisable(false);
        txtStudentName.setDisable(false);
        txtEmail.setDisable(false);
        txtContact.setDisable(false);
        txtAddress.setDisable(false);
        txtNIc.setDisable(false);

        txtStudentID.clear();
        txtStudentID.setText(generateNewId());

        txtStudentName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNIc.clear();
        txtStudentName.requestFocus();

        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblStudent.getSelectionModel().clearSelection();
    }

   

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
    }
    private void initUI() {
        txtStudentID.clear();
        txtStudentName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNIc.clear();

        txtStudentID.setDisable(true);
        txtStudentName.setDisable(true);
        txtEmail.setDisable(true);
        txtContact.setDisable(true);
        txtAddress.setDisable(true);
        txtNIc.setDisable(true);

        txtStudentID.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    private String generateNewId() {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            ResultSet rst = connection.createStatement().executeQuery("SELECT studentId FROM Student ORDER BY studentId DESC LIMIT 1;");
            if (rst.next()) {
                String id = rst.getString("id");
                int newCustomerId = Integer.parseInt(id.replace("S-", "")) + 1;
                return String.format("S-%03d", newCustomerId);
            } else {
                return "C00-001";
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new id " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (tblStudent.getItems().isEmpty()) {
            return "C00-001";
        } else {
            String id = getLastCustomerId();
            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C00-%03d", newCustomerId);
        }

    }
    private String getLastCustomerId() {
        List<StudentTM> tempStudentsList = new ArrayList<>(tblStudent.getItems());
        Collections.sort(tempStudentsList);
        return tempStudentsList.get(tempStudentsList.size() - 1).getId();
    }

}

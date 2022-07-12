package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import view.tdm.StudentTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    public TableView<StudentTM> tblStudent;

    public void initialize(){
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("nic"));

        initUI();

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtStudentID.setText(newValue.getId());
                txtStudentName.setText(newValue.getName());
                txtEmail.setText(newValue.getEmail());
                txtContact.setText(newValue.getContact());
                txtAddress.setText(newValue.getAddress());
                txtNIc.setText(newValue.getNic());

                txtStudentID.setDisable(false);
                txtStudentName.setDisable(false);
                txtEmail.setDisable(false);
                txtContact.setDisable(false);
                txtAddress.setDisable(false);
                txtNIc.setDisable(false);
                
                
            }
        });

        txtNIc.setOnAction(event -> btnSave.fire());
        loadAllCustomers();

    }

    private void loadAllCustomers() {
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
        //Delete Customer
        String id = tblStudent.getSelectionModel().getSelectedItem().getId();
        try {
            if (!existStudent(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM Student WHERE studentId=?");
            pstm.setString(1, id);
            pstm.executeUpdate();

            tblStudent.getItems().remove(tblStudent.getSelectionModel().getSelectedItem());
            tblStudent.getSelectionModel().clearSelection();
            initUI();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    boolean existStudent(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT studentId FROM Student WHERE studentId=?");
        pstm.setString(1, id);
        return pstm.executeQuery().next();
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
            String id = getLastStudentId();
            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C00-%03d", newCustomerId);
        }

    }
    private String getLastStudentId() {
        List<StudentTM> tempStudentsList = new ArrayList<>(tblStudent.getItems());
        Collections.sort(tempStudentsList);
        return tempStudentsList.get(tempStudentsList.size() - 1).getId();
    }

}

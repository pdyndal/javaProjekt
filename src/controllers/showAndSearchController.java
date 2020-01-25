package controllers;

import entity.Employee;
import entity.Job;
import entity.ShowEmployeeData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.DatabaseOperations;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class showAndSearchController implements Initializable {

  @FXML
  private ComboBox<String> tableComboBox;
  @FXML
  private TextField toFindTextField;

  @FXML
  private TableView<ShowEmployeeData> databaseTable;
  @SuppressWarnings("FieldCanBeLocal")
  @FXML
  private TableColumn<ShowEmployeeData, Integer> ID;
  @SuppressWarnings("FieldCanBeLocal")
  @FXML
  private TableColumn<ShowEmployeeData, String> name;
  @SuppressWarnings("FieldCanBeLocal")
  @FXML
  private TableColumn<ShowEmployeeData, String> surName;
  @SuppressWarnings("FieldCanBeLocal")
  @FXML
  private TableColumn<ShowEmployeeData, String> position;
  @FXML
  private TableColumn<ShowEmployeeData, Integer> miscData; // TODO: add button showing more information

  final DatabaseOperations DbOps = new DatabaseOperations();

  @FXML
  void searchForSomeone(ActionEvent event) {
    //TODO: zabezpieczenie
    String value = tableComboBox.getValue();
    String textToFind = toFindTextField.getText();

    createAlertForEmptyString(value);

    if (!toFindTextField.getText().isEmpty()) {

      switch (value) {
        case "ID": {
          int id = Integer.parseInt(textToFind);
          List emp = DbOps.findEmployeesByID(id);
          this.createAlertForSearch(emp);
          break;
        }
        case "Imię": {
          List emp = DbOps.findEmployeesByName(textToFind);
          this.createAlertForSearch(emp);
          break;
        }
        case "Nazwisko": {
          List emp = DbOps.findEmployeesBySurName(textToFind);
          this.createAlertForSearch(emp);
          break;
        }
        default: {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Błędne wprowadzenie");
          alert.setHeaderText("Puste pole tekstowe");
          alert.setContentText("Proszę wprowadzić odpowiednia wartość.");

          alert.showAndWait();
        }
      }
    }
  }

  void createAlertForSearch(List emp) {
    if (emp.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Wynik wyszukiwania");
      alert.setHeaderText("Nie znaleziono.");
      alert.setContentText("Nie znaleziono pracownika o podanych danych.");

      alert.showAndWait();
      return;
    }
    String output = null;

    Iterator iterator = emp.listIterator();

    while (iterator.hasNext()) {
      Employee employee = (Employee) iterator.next();
      //noinspection StringConcatenationInLoop
      output += employee.toString() + "\n";
    }

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Wynik wyszukania");
    alert.setHeaderText("Znaleziono pracownika.");
    alert.setContentText(output);

    alert.showAndWait();
  }

  void createAlertForEmptyString(String str) {
    if (!str.isEmpty()) {
      return;
    }
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Błędne wprowadzenie");
    alert.setHeaderText("Niepoprawna opcja");
    alert.setContentText("Proszę wybrać odpowiednią wartość z listy rozwijanej.");

    alert.showAndWait();
  }//TODO: make it work, doesn't show anything

  void updateTableContent() {
    //TODO: pobranie z DbOps danych, dodanie ich do tabeli
    List employees = DbOps.getAllEmployees();
    List jobs = DbOps.getAllJobs();

    System.out.println(employees);
    System.out.println(jobs);

    databaseTable.getColumns().clear();

    ID = new TableColumn("ID");
    ID.setCellValueFactory(new PropertyValueFactory<>("id"));

    name = new TableColumn("Imię");
    name.setCellValueFactory(new PropertyValueFactory<>("firstName"));

    surName = new TableColumn("Nazwisko");
    surName.setCellValueFactory(new PropertyValueFactory<>("surName"));

    position = new TableColumn("Pozycja");
    position.setCellValueFactory(new PropertyValueFactory<>("name"));

    //TODO: add button inside column for more info

    databaseTable.getColumns().addAll(ID, name, surName, position);

    Iterator iterator = employees.iterator();

    while ( iterator.hasNext() ){
      ShowEmployeeData data = new ShowEmployeeData();
      Employee e = (Employee) iterator.next();

      data.setId( e.getId() );
      data.setFirstName( e.getFirstName() );
      data.setSurName( e.getSurName() );
      Iterator iteratorJob = jobs.iterator();

      while ( iteratorJob.hasNext() ){
        Job j = (Job) iteratorJob.next();
        if ( e.getIdJob() == j.getId() ) data.setName(j.getName());
      }

      databaseTable.getItems().add(data);
      //TODO: add to columns
    }

  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    tableComboBox.getItems().setAll("ID", "Imię", "Nazwisko");
    updateTableContent();
  }
}

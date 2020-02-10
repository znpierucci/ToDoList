/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 *
 * @author zpierucci
 */
public class FXMLDocumentController implements Initializable {
    
    
    //instance field
    @FXML
    private TextField task;
    
    @FXML
    private ListView list;
    
    @FXML
    private ListView completed;
    
    @FXML
    private AnchorPane root;
    
    List lists;
    
    //methods
    private void buildList() {
        list.getItems().add("Feed Dog");
        list.getItems().add("Wash Dishes");
        list.getItems().add("Take Out Trash");
        list.getItems().add("Homework");
        list.getItems().add("Workout");
    }
    
    private void addItem() {
        
        String currentTask = task.getText().trim();
        List itemsList = list.getItems();
        boolean exists = itemsList.contains(currentTask);
        List doneList = completed.getItems();
        boolean exists2 = doneList.contains(currentTask);

        
        if (exists == false && exists2 == false && !currentTask.isEmpty()){
            System.out.println("Task Added!");
            list.getItems().add(currentTask);
            task.setText("");
        } else {
            System.out.println("Invalid Item!");
            displayExistsAlert();
        }
        
    }
    
    
    private void deleteItem() {
        
        int selected = -1;
        selected = list.getSelectionModel().getSelectedIndex();
        if(selected != -1) {
            list.getItems().remove(selected);
            System.out.println("Task Deleted!");
        } else {
            displayDeleteAlert();
        }
        
    }
    
    private void editItem() {
        
        String currentTask = task.getText().trim();
        int selected = list.getSelectionModel().getSelectedIndex();
        List itemsList = list.getItems();
        
        boolean exists = itemsList.contains(currentTask);

        
        if (exists == false && !currentTask.isEmpty()){
            System.out.println("Task Edited!");
            itemsList.set(selected, currentTask);
            task.setText("");
        } else {
            System.out.println("Invalid Item!");
            displayExistsAlert();
        }

    }
    
    public void clearList() {
        list.getItems().clear();
        completed.getItems().clear();
        System.out.println("Tasks Cleared!");
    }
    
    public void completeItem() {
        
        int selected = list.getSelectionModel().getSelectedIndex();
        List itemsList = list.getItems();
        List done = completed.getItems();
        
        if(selected != -1) {
            
            Object move = itemsList.get(selected);
            
            done.add(move);
            
            list.getItems().remove(selected);
            
        } else {
            displayDeleteAlert();
        }
        
    }
    
    public void removeItem() {
        int selected = -1;
        selected = completed.getSelectionModel().getSelectedIndex();
        if(selected != -1) {
            completed.getItems().remove(selected);
            System.out.println("Task Deleted!");
        } else {
            displayDeleteAlert();
        }
    }
    
    public void incompleteItem() {
        int selected = completed.getSelectionModel().getSelectedIndex();
        List itemsList = list.getItems();
        List done = completed.getItems();
        
        if(selected != -1) {
            
            Object move = done.get(selected);
            
            itemsList.add(move);
            
            completed.getItems().remove(selected);
            
        } else {
            displayDeleteAlert();
        }
    }
    
    public String toJsonString() {
        JSONObject obj = new JSONObject();
        
        List entries = list.getItems();
        List done = completed.getItems();
        
        if(entries != null) obj.put("Entries", entries);
        if(entries != null) obj.put("Completed", done);
        
        return obj.toJSONString();
    }
    
    public void initFromJsonString(String jsonString) {
        
        if (jsonString == null || jsonString == "") return;
        
        JSONObject jsonObj;

        try {
            jsonObj = (JSONObject) JSONValue.parse(jsonString);
        } catch(Exception ex) {
            return;
        }
       
        if(jsonObj == null){
            return;
        }
        
        list.getItems().clear();
        completed.getItems().clear();
        
        Object entries = jsonObj.getOrDefault("Entries", null);
        Object done = jsonObj.getOrDefault("Completed", null);
        JSONArray array = (JSONArray) entries;
        JSONArray array2 = (JSONArray) done;
        
        int length = array.size();
        String current = "";
        
        for(int i = 0; i < length; i++) {
            current = (String) array.get(i);
            list.getItems().add(current);
        }
        
        int length2 = array2.size();
        String current2 = "";
        
        for(int i = 0; i < length2; i++) {
            current2 = (String) array2.get(i);
            completed.getItems().add(current2);
        }

    }
    
    
    //FXML actions
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildList();
    }
    
    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        addItem();
    }
    
    @FXML
    private void handleEditButtonAction(ActionEvent event) {
        editItem();
    }
    
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        deleteItem();
    }
    
    @FXML
    private void handleClearButtonAction(ActionEvent event) {
        clearList();
    }
    
    @FXML
    private void handleCompleteButtonAction(ActionEvent event) {
        completeItem();
    }
    
    @FXML
    private void handleRemoveButtonAction(ActionEvent event) {
        removeItem();
    }
    
    @FXML
    private void handleIncompleteButtonAction(ActionEvent event) {
        incompleteItem();
    }
    
    @FXML
    public void handleOpen(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        
        Stage stage = (Stage) root.getScene().getWindow();
        
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        
        File file = fileChooser.showOpenDialog(stage);
        
        if(file != null)
        {
            try{
                FileReader fileReader = new FileReader(file.getPath());
                BufferedReader bufferReader = new BufferedReader(fileReader);
                
                String json = "";
                String line = null;
                
                while((line = bufferReader.readLine()) != null){
                    json += line;
                }
                
                bufferReader.close();
                fileReader.close();
                
                initFromJsonString(json);
                
            } catch(IOException ioex) {
                String message = "Exception occurred while opening " + file.getPath();
                displayExceptionAlert(message, ioex);
            } 
            
        }
    }
    
    @FXML
    public void handleSave(ActionEvent event)
    {
        if (list == null) {
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) root.getScene().getWindow();
        
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        File file = fileChooser.showSaveDialog(stage);
        
        FileWriter writer = null;
        
        if(file != null)
        {
            try 
            {
                writer = new FileWriter(file);
                
                String jsonString = toJsonString();
                try (PrintWriter out = new PrintWriter(file.getPath())) {
                    out.print(jsonString);
                }
                
            } catch(IOException ioex) {
                String message = "Exception Occurred while saving to " + file.getPath();
                displayExceptionAlert(message, ioex);
            } catch (Exception ex) {
                displayExceptionAlert(ex);
            }
            finally
            {
                if(writer != null)
                {
                    try
                    {
                        writer.close();
                    }
                    catch (IOException ioex)
                    {
                        displayExceptionAlert(ioex);
                    }
                    catch (Exception ex) 
                    {
                        displayExceptionAlert(ex);
                    }
                }
            }
        }
    }
    
    @FXML
    public void handleAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("To Do List");
        alert.setContentText("This application supports importing and exporting of .txt files written in JSON.");
        
        TextArea textArea = new TextArea("To find out the correct JSON formatting that this program supports, add a few items to each list, export the file, and open your .txt file to see!");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
            
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
    }
    
    
    //exception alerts
    private void displayExceptionAlert(Exception ex)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception!");
        alert.setHeaderText(ex.getClass().getCanonicalName());
        alert.setContentText(ex.getMessage());
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();
        
        Label label = new Label("The exception stacktrace was: ");
        
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(true);
        textArea.setWrapText(true);
        
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        
        GridPane expContent = new GridPane();
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        
        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
    }
    
    private void displayExistsAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Entry");
        alert.setContentText("Enter an item that has not been entered already.");
        
        TextArea textArea = new TextArea("The text is case sensitive.");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
            
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
    }
    
    private void displayDeleteAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Action could not be performed");
        alert.setContentText("No Task Selected");
        
        TextArea textArea = new TextArea("Click on a task to select it. Then you can: \n\nClick Delete Task to remove it from the To Do section.\nClick Complete Task to move it to the COMPLETED section.\nClick Remove to remove it from the COMPLETED section.\nClick Incomplete to move it back to the To Do section.");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
            
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
    }
    
    private void displayExceptionAlert(String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception!");
        alert.setContentText(message);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
    
}

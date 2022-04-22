package queuemanagement.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller{
    @FXML
    private Label timeLabel, avgServiceTime, avgWaitingTime, peakHour;
    @FXML
    private TextField timeLimitTF, nrClientsTF, nrQueuesTF, minArrivalTimeTF, maxArrivalTimeTF, minServiceTimeTF, maxServiceTimeTF, outputFileTF;
    @FXML
    private ChoiceBox<String> strategyChoiceBox;
    @FXML
    private TextArea resultTextArea;

    private SimulationManager simulationManager = new SimulationManager();
    private SelectionPolicy selectionPolicy;

    public void startSimulation(){
        if(strategyChoiceBox.getValue().equals("Shortest Time"))
            selectionPolicy = SelectionPolicy.SHORTEST_TIME;
        else selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
        simulationManager = new SimulationManager(Integer.parseInt(timeLimitTF.getText()),
                Integer.parseInt(minArrivalTimeTF.getText()), Integer.parseInt(maxArrivalTimeTF.getText()),
                Integer.parseInt(minServiceTimeTF.getText()), Integer.parseInt(maxServiceTimeTF.getText()),
                Integer.parseInt(nrClientsTF.getText()), Integer.parseInt(nrQueuesTF.getText()), selectionPolicy);
        simulationManager.setController(this);
        simulationManager.setOutputFile(outputFileTF.getText());
        Thread thread = new Thread(simulationManager);
        thread.start();
    }

    public void setAlert(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Queue management system");
        alert.setHeaderText("Simulation finished!");
        alert.setContentText(s);
        alert.show();
    }

    public void stopSimulation(){
        simulationManager.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Queue management system");
        alert.setHeaderText("Simulation stopped!");
        alert.show();
    }

    public void setFinalLabels(String s1, String s2, String s3){
        avgServiceTime.setText(s1);
        avgWaitingTime.setText(s2);
        peakHour.setText(s3);
    }

    public void setResultTextArea(String s){
        resultTextArea.setText(s);
    }

    public void setTimeLabel(String s){
        timeLabel.setText(s);
    }

}
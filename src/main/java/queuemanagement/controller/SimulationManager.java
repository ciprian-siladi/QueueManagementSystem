package queuemanagement.controller;

import javafx.application.Platform;
import queuemanagement.model.Client;
import queuemanagement.model.Queue;

import static queuemanagement.controller.FileService.write;

import java.util.*;

public class SimulationManager implements Runnable{

    private int timeLimit = 20;
    private int currentTime;
    private int maxArrivalTime = 15;
    private int minArrivalTime = 2;
    private int maxServiceTime = 6;
    private int minServiceTime = 3;
    private int numberOfClients = 10;
    private int numberOfQueues = 2;
    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private final Scheduler scheduler;
    private final ArrayList<Client> generatedClients = new ArrayList<>();
    private float averageWaitingTime = 0;
    private float averageServiceTime = 0;
    private int peakHour = 0;
    public boolean stop = false;

    private StringBuilder construct = new StringBuilder();

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setOutputFile(String file){
        FileService.setFile(file);
    }

    public SimulationManager() {
        scheduler = new Scheduler(numberOfQueues, selectionPolicy);
        scheduler.changeStrategy(selectionPolicy);
        generateRandomClients();
    }

    public SimulationManager(int timeLimit,
                             int minArrivalTime, int maxArrivalTime,
                             int minServiceTime, int maxServiceTime,
                             int numberOfClients, int numberOfQueues,
                             SelectionPolicy selectionPolicy){
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.numberOfClients = numberOfClients;
        this.numberOfQueues = numberOfQueues;
        scheduler = new Scheduler(numberOfQueues, selectionPolicy);
        generateRandomClients();
    }

    /**
     * Function that generates the random clients
     */
    private void generateRandomClients(){
        Random random = new Random();
        for(int i = 0; i < numberOfClients; i++){
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime) + minServiceTime;
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime) + minArrivalTime;
            Client c = new Client(i + 1, arrivalTime, serviceTime);
            averageServiceTime += c.getServiceTime();
            generatedClients.add(c);
        }
        Collections.sort(generatedClients, Comparator.comparing(Client::getArrivalTime));
        averageServiceTime /= (double)generatedClients.size();
    }

    /**
     * Function to stop all queues
     */
    public void stop(){
        stop = true;
        scheduler.stopThreads();
    }

    @Override
    public void run() {
        try {
            currentTime = 1;
            int clientsWaiting, clientsFinished;
            peakHour = 0;
            int peakClients = 0;

            while (currentTime <= timeLimit && !stop) {

                Iterator<Client> clientIterator = generatedClients.listIterator();
                while (clientIterator.hasNext()) {
                    Client client = clientIterator.next();
                    if (client.getArrivalTime() == currentTime) {
                        clientIterator.remove();
                        scheduler.dispatchClient(client);
                    }
                }

                scheduler.notifyThreads();
                Thread.sleep(1000);

                System.out.println("TIME " + currentTime);
                construct.append("TIME " + currentTime + "\n");

                clientsWaiting = 0;
                clientsFinished = 0;
                if(generatedClients.size() > 0) {
                    System.out.println("Waiting clients: " + generatedClients);
                    construct.append("Waiting clients " + generatedClients + "\n");
                }
                else {
                    System.out.println("There are no clients waiting.");
                    construct.append("There are no clients waiting.\n");
                }

                for (Queue q : scheduler.getQueues()) {
                    clientsWaiting += Math.max(q.getQueueSize() - 1, 0);
                    clientsFinished += q.getQueueSize() > 0 ? 1: 0;
                    averageWaitingTime += clientsWaiting;
                    System.out.println(q);
                    construct.append(q + "\n");
                }
                if(clientsWaiting + clientsFinished > peakClients){
                    peakClients = clientsWaiting + clientsFinished;
                    peakHour = currentTime;
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run(){
                        controller.setTimeLabel("TIME: " + (currentTime - 1));
                        controller.setResultTextArea(construct.toString());
                        construct = new StringBuilder();
                    }
                });
                write(construct.toString() + "\n");
                currentTime++;
                System.out.println();
            }
            averageWaitingTime /= (float)numberOfClients;
            scheduler.stopThreads();

            System.out.println("Average service time: " + averageServiceTime);
            System.out.println("Average waiting time: " + averageWaitingTime);
            System.out.println("Peak hour: " + peakHour);
            System.out.println("Program finished!");

            write("Average service time: " + averageServiceTime + "\nAverage waiting time: " + averageWaitingTime + "\nPeak hour: " + peakHour + "\nProgram finished!");

            Platform.runLater(new Runnable() {
                @Override
                public void run(){
                    controller.setFinalLabels("Average service time: " + averageServiceTime, "Average waiting time: " + averageWaitingTime, "Peak hour: " + peakHour);
                    controller.setAlert("Average service time: " + averageServiceTime + "\nAverage waiting time: " + averageWaitingTime + "\nPeak hour: " + peakHour);
                }
            });


            FileService.closeFile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

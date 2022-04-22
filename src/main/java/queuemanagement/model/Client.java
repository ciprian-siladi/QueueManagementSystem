package queuemanagement.model;

public class Client {
    private final int id;
    private final int arrivalTime;
    private int serviceTime;

    /**
     * Constructor for a Client
     */
    public Client(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime(){
        if(this.serviceTime > 0)
            this.serviceTime --;
    }

    /**
     * Function for converting the details about the client into a string
     * @return the client
     */
    @Override
    public String toString(){
        return "("+this.id+","+this.arrivalTime+","+this.serviceTime+")id";
    }

}
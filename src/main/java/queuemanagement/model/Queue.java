package queuemanagement.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable{
    private final int id;
    private BlockingQueue<Client> clients;
    private final AtomicInteger waitingPeriod = new AtomicInteger(0);
    private AtomicBoolean stop = new AtomicBoolean(false);

    /**
     * Constructor
     */
    public Queue(int id){
        this.id = id;
        clients = new LinkedBlockingDeque<>();
    }

    /**
     * Function for adding a new client to the queue
     * @param client the client to be added
     */
    public void addClient(Client client){
        if(client != null){
            clients.add(client);
            waitingPeriod.addAndGet(client.getServiceTime());
        }
    }

    public int getQueueSize() {
        return clients.size();
    }

    public int getWaitingPeriod() {return waitingPeriod.get();}

    public void setStop(boolean stop){
        this.stop.set(stop);
    }

    /**
     * Function for running the thread
     */
    @Override
    public void run() {
        Client currentClient = null;
        try{
            synchronized (this){
                wait();
            }

            while(!stop.get()){
                if(currentClient == null){
                    currentClient = clients.peek();
                }
                else if(currentClient.getServiceTime() == 1){
                    clients.remove(currentClient);
                    currentClient = clients.peek();
                    waitingPeriod.decrementAndGet();
                }
                else {
                    currentClient.decrementServiceTime();
                    waitingPeriod.decrementAndGet();
                }
                synchronized (this){
                    wait();
                }
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * Function for displaying the elements in the queue
     */
    public void displayQueue(){
        if(!clients.isEmpty()){
            for(Client c : clients){
                if(c.getServiceTime() != 0){
                    System.out.println(c.toString());
                }
            }
        }
        System.out.println();
    }


    /**
     * Function for converting the queue to a string
     * @return  the queue
     */
    @Override
    public String toString(){
        if(clients.isEmpty()) return "Queue " + id + ": closed";
        else return "Queue " + id + ", waiting period = " + waitingPeriod + ": " + clients;
    }
}

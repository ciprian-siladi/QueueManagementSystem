package queuemanagement.controller;

import queuemanagement.model.Client;
import queuemanagement.model.Queue;

import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Queue> queues = new ArrayList<>();
    private Strategy strategy;
    private int maxNoQueues;

    /**
     * Constructor
     * @param nr the maximum number of queues
     */
    public Scheduler(int nr, SelectionPolicy selectionPolicy){
        changeStrategy(selectionPolicy);
        this.maxNoQueues = nr;
        for(int i = 0; i < maxNoQueues; i ++){
            queues.add(new Queue(i + 1));
            new Thread(queues.get(i)).start();
        }
    }

    /**
     * Function to change the strategy for adding the clients in a queue
     * @param policy the policy on which to change (SHORTEST_TIME / SHORTEST_QUEUE)
     */
    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        else
            strategy = new ConcreteStrategyTime();
    }

    /**
     * Function for dispatching a client to a queue
     * @param c the client to be dispatched
     */
    public void dispatchClient(Client c){
        strategy.addClient(queues, c);
    }

    public void notifyThreads(){
        for(Queue queue : queues){
            synchronized (queue){
                queue.notify();
            }
        }
    }

    public void stopThreads(){
        for(Queue queue : queues){
            queue.setStop(true);
        }
        notifyThreads();
    }

    public ArrayList<Queue> getQueues(){
        return queues;
    }

}

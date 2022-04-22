package queuemanagement.controller;

import queuemanagement.model.Client;
import queuemanagement.model.Queue;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    /**
     * Function for adding a client from the list
     * @param queueList list of the queues
     * @param client client to be added
     */
    @Override
    public void addClient(List<Queue> queueList, Client client) {
        Queue bestQueue = queueList.get(0);

        for(Queue queue : queueList.subList(1, queueList.size())){
            if(queue.getWaitingPeriod() < bestQueue.getWaitingPeriod())
                bestQueue = queue;
        }

        bestQueue.addClient(client);
    }
}

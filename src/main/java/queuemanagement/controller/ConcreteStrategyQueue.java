package queuemanagement.controller;

import queuemanagement.model.Client;
import queuemanagement.model.Queue;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    /**
     * Function for adding a client from the list
     * @param queueList list of queues
     * @param client client to be added
     */
    @Override
    public void addClient(List<Queue> queueList, Client client) {
        Queue bestQueue = queueList.get(0);

        for(Queue queue : queueList.subList(1, queueList.size())){
            if(queue.getQueueSize() < bestQueue.getQueueSize())
                bestQueue = queue;
        }

        bestQueue.addClient(client);
    }
}

package queuemanagement.controller;


import queuemanagement.model.Client;
import queuemanagement.model.Queue;

import java.util.List;

public interface Strategy {
    /**
     * Function for adding a client
     * @param queues list of the queues
     * @param client client to be added(
     */
    void addClient(List<Queue> queues, Client client);
}

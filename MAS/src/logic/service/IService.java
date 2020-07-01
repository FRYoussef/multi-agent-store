package logic.service;

import jade.lang.acl.ACLMessage;

public interface IService {
    /**
     * Call this method before destroy the service. It is used to save the service state.
     */
    void takeDown();

    /**
     * Take actions for that msg
     * @param msg
     */
    void handleACLMsg(ACLMessage msg);
}

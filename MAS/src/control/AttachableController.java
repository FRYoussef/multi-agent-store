package control;

import jade.lang.acl.ACLMessage;

public interface AttachableController {

    /**
     * All controllers should use this method to update their views before show them.
     */
    void viewSetup();

    /**
     * Should call its service to handle the msg
     * @param msg
     */
    void handleACLMsg(ACLMessage msg);

    /**
     * Use it before destroy the controller.
     */
    void takeDown();
}

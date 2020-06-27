package logic.agents.recommenderAgent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class RecommenderAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("RecommenderAgent(" + getAID().getName() + ") is running");

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("RecommenderAgent");
        sd.setName(getName());
        sd.setOwnership("UCM");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            addBehaviour(new RecommenderBehaviour(this));
        } catch (FIPAException e) {
            doDelete();
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        super.takeDown();
    }
}

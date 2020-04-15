package agents.chatbotAgent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ChatbotAgent extends Agent {
    @Override
    protected void setup() {
        // Registro en el DF
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("ChatbotAgent");
        sd.setName(getName());
        sd.setOwnership("UCM");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this,dfd);
            addBehaviour(new ChatbotBehaviour(this));
        } catch (FIPAException e) {
            doDelete();
        }
    }

    @Override
    protected void takeDown() {
        try { // se deregistra del DF
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        super.takeDown();
    }
}

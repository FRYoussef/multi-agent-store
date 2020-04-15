package agents.guiAgent;

import agents.guiAgent.control.MainController;
import jade.core.AID;
import agents.chatbotAgent.ChatbotBehaviour;
import agents.guiAgent.control.GuiLauncher;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Iterator;

public class GuiAgent extends jade.gui.GuiAgent {
    public static final int CMD_EXIT = 0;
    public static final int CMD_SEND = 1;

    @Override
    protected void setup(){
        System.out.println("GuiAgent(" + getAID().getName() + ") is running");

        //DF register
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("GuiAgent");
        sd.setName(getName());
        sd.setOwnership("UCM");
        dfd.setName(getAID());
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            addBehaviour(new GuiBehaviour(this));
        } catch (FIPAException e) {
            doDelete();
        }

        GuiLauncher.instance().setup(this);
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
        // Close runtime when gui agent ends
        Runtime rt = Runtime.instance();
        rt.shutDown();
        System.exit(0);
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        int cmd = guiEvent.getType();
        if (cmd == CMD_EXIT) {
            doDelete(); // calls takeDown()
        }
        else if (cmd == CMD_SEND) {
            sendToChatbot((String) guiEvent.getParameter(0));
        }
    }

    public void sendToChatbot(String input) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setContent(input);
        ArrayList<AID> agents =  searchAgents();
        for ( AID agent: agents ) {
            msg.addReceiver( agent );
        }
        send(msg);
    }

    private ArrayList<AID> searchAgents() {
        ArrayList<AID> agents = new ArrayList<AID>();

        try {
            DFAgentDescription templateAD = new DFAgentDescription();
            ServiceDescription templateSD = new ServiceDescription();
            templateSD.setType("ChatbotAgent");
            templateAD.addServices(templateSD);

            DFAgentDescription[] results = DFService.search(this, templateAD);

            for (int i = 0; i < results.length; i++) {
                DFAgentDescription dfd = results[i];
                AID provider = dfd.getName();
                Iterator it = dfd.getAllServices();
                while (it.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) it.next();
                    if (sd.getType().equals("ChatbotAgent")) {
                        agents.add(provider);
                    }
                }
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        return agents;
    }
}


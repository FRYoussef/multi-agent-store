package logic.agents.guiAgent;

import jade.core.AID;
import control.GuiLauncher;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import logic.agents.chatbotAgent.ChatbotAgent;
import logic.agents.recommenderAgent.RecommenderAgent;

import java.util.ArrayList;
import java.util.Iterator;

public class GuiAgent extends jade.gui.GuiAgent {
    public static final int CMD_EXIT = 0;
    public static final int CMD_SEND_CHATBOT = 1;
    public static final int CMD_SEND_RECOMMENDER = 2;
    public static final String NAME = "GuiAgent";

    @Override
    protected void setup(){
        System.out.println(NAME + "(" + getAID().getName() + ") is running");

        //DF register
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(NAME);
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

        new Thread(() -> GuiLauncher.instance().setup(this)).start();
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
        else if (cmd == CMD_SEND_CHATBOT)
            sendTo((String) guiEvent.getParameter(0), ChatbotAgent.NAME);
        else if(cmd == CMD_SEND_RECOMMENDER)
            sendTo((String) guiEvent.getParameter(0), RecommenderAgent.NAME);
    }

    public void sendTo(String input, String to) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setContent(input);
        ArrayList<AID> agents =  searchAgents(to);
        for ( AID agent: agents ) {
            msg.addReceiver( agent );
        }
        send(msg);
    }

    private ArrayList<AID> searchAgents(String name) {
        ArrayList<AID> agents = new ArrayList<>();

        try {
            DFAgentDescription templateAD = new DFAgentDescription();
            ServiceDescription templateSD = new ServiceDescription();
            templateSD.setType(name);
            templateAD.addServices(templateSD);

            DFAgentDescription[] results = DFService.search(this, templateAD);

            for (int i = 0; i < results.length; i++) {
                DFAgentDescription dfd = results[i];
                AID provider = dfd.getName();
                Iterator it = dfd.getAllServices();

                while (it.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) it.next();
                    if (sd.getType().equals(name))
                        agents.add(provider);
                }
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        return agents;
    }
}


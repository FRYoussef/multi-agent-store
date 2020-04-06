package agents.guiAgent;

import agents.guiAgent.control.Gui;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiEvent;
import javafx.application.Application;

public class GuiAgent extends jade.gui.GuiAgent {
    protected static final int CMD_EXIT = 0;
    protected static final int CMD_SEND = 1;

    @Override
    protected void setup(){
        System.out.println("GuiAgent (" + getAID().getName() + ") is running");

        //DF register
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("GuiAgent");
        sd.setName(getName());
        sd.setOwnership("UCM");
        dfd.setName(getAID());
        dfd.addServices(sd);

        try {
            DFService.register(this,dfd);
            //addBehaviour(new PingBehaviour(this, gui));
        } catch (FIPAException e) {
            doDelete();
        }

        new Thread(() -> Application.launch(Gui.class)).start();
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        //gui.dispose();	// ends with agent's GUI
        super.takeDown();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        int cmd = guiEvent.getType();
        if ( cmd == CMD_EXIT ) {
            doDelete(); // calls takeDown()
        }
        else if ( cmd == CMD_SEND ) {
            // TODO
        }
    }
}

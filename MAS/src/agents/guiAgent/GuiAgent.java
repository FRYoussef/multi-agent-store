package agents.guiAgent;

import agents.guiAgent.control.GuiLauncher;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiEvent;
import javafx.application.Application;

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
        } catch (FIPAException e) {
            doDelete();
        }

        GuiLauncher.addAgent(this);
        Application.launch(GuiLauncher.class);
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
            // TODO
        }
    }
}

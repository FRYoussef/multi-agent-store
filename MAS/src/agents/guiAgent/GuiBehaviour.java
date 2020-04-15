package agents.guiAgent;

import agents.guiAgent.control.MainController;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import agents.guiAgent.control.GuiLauncher;

public class GuiBehaviour extends SimpleBehaviour {
    private static final long serialVersionUID = 1L;



    public GuiBehaviour(Agent a) {
        super(a);

    }


    @Override
    public void action() {
        ACLMessage  msg = myAgent.receive();
        if(msg != null){
            String content = msg.getContent();
            GuiLauncher.instance().showMessage(content);
        }
        else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}

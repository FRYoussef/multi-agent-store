package agents.guiAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import agents.guiAgent.control.GuiLauncher;

public class GuiBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;

    public GuiBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage  msg = myAgent.receive();
        if(msg != null){
            String content = msg.getContent();
            new Thread(() -> GuiLauncher.instance().showMessage(content)).start();
        }
        block();
    }
}

package agents.chatbotAgent;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class ChatbotBehaviour extends SimpleBehaviour {

    private static final long serialVersionUID = 1L;

    public ChatbotBehaviour(Agent a) {
        super(a);
    }


    @Override
    public void action() {
        ACLMessage  msg = myAgent.receive();
        if(msg != null){
            ACLMessage reply = msg.createReply();
            reply.setContent("Hello!");
            myAgent.send(reply);
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

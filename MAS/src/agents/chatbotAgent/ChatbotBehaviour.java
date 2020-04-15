package agents.chatbotAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ChatbotBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = 1L;

    public ChatbotBehaviour(Agent a) {
        super(a);
    }


    @Override
    public void action() {
        ACLMessage  msg = myAgent.receive();
        if(msg != null){
            String content = msg.getContent();
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("Hello!");
            myAgent.send(reply);
        }
        else {
            block();
        }
    }
}

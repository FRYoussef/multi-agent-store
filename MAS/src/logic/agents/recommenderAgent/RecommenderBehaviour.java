package logic.agents.recommenderAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RecommenderBehaviour extends CyclicBehaviour {

    private static final String CONTENT_BASED_ROCOMMENDER = "";
    private static final String COLABORATIVE_FILTER_ROCOMMENDER = "";//TODO poner ruta

    public RecommenderBehaviour(Agent a){
        super(a);
    }

    @Override
    public void action() {
        //TODO
        ACLMessage msg = myAgent.receive();
        if(msg != null){
            ACLMessage response = msg.createReply();
            String content = msg.getContent();

            Process recommender = Runtime.getRuntime().exec( + content);
            recommender.waitFor();

            response.setPerformative(ACLMessage.INFORM);
            response.setContent();
            myAgent.send(response);
        }

        block();
    }
}

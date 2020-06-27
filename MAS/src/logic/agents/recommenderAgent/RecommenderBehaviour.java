package logic.agents.recommenderAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class RecommenderBehaviour extends CyclicBehaviour {
    private static final String CONTENT_BASED_ROCOMMENDER = "src/logic/agents/recommenderAgent/content-based-rocommender.py";
    private static final String COLABORATIVE_FILTER_ROCOMMENDER = "src/logic/agents/recommenderAgent/";//TODO poner ruta completa

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

            Process recommender = null;
            try {
                recommender = Runtime.getRuntime().exec("python3 " + CONTENT_BASED_ROCOMMENDER + " " + content);
                recommender.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            response.setPerformative(ACLMessage.INFORM);
            response.setContent();
            myAgent.send(response);
        }

        block();
    }
}

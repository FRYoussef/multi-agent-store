package logic.agents.recommenderAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Scanner;

public class RecommenderBehaviour extends CyclicBehaviour {
    private static final String CONTENT_BASED_ROCOMMENDER = "src/logic/agents/recommenderAgent/content-based-rocommender.py";
    private static final String COLABORATIVE_FILTER_ROCOMMENDER = "src/logic/agents/recommenderAgent/";//TODO poner ruta completa
    private static final String RESULT_PATH = "src/logic/agents/recommenderAgent/result.txt";
    private static final String ERROR = "error";

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
            String result = "";

            Process recommender = null;
            try {
                recommender = Runtime.getRuntime().exec("python3 " + CONTENT_BASED_ROCOMMENDER + " " + content);
                recommender.waitFor();

                // Read recommendation
                Scanner reader = new Scanner(RESULT_PATH);
                result = reader.nextLine();

                // see if theres an recommendation or an error.
                result = (result.equals(ERROR)) ? "" : result;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            response.setPerformative(ACLMessage.INFORM);
            response.setContent(result);
            myAgent.send(response);
        }

        block();
    }
}

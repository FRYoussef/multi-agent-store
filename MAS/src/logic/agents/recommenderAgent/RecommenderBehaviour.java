package logic.agents.recommenderAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class RecommenderBehaviour extends CyclicBehaviour {
    private static final String CONTENT_BASED_RECOMMENDER = "src" + File.separator + "logic" + "agents" + File.separator
            + "recommenderAgent" + File.separator + "content-based-rocommender.py";
    private static final String COLABORATIVE_FILTER_RECOMMENDER = "src" + File.separator + "logic" + "agents" + File.separator
            + "recommenderAgent" + File.separator + "collaborative-filter-recommender.py";
    private static final String RESULT_PATH = "result.txt";

    public RecommenderBehaviour(Agent a){
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null){
            ACLMessage response = msg.createReply();
            RecommenderMsg recommenderMsg = new RecommenderMsg(msg.getContent());
            String result = "";

            String recommender = "";
            if(recommenderMsg.getType() == RecommenderMsg.CONTENT_BASED_TYPE)
                recommender = CONTENT_BASED_RECOMMENDER;
            else if(recommenderMsg.getType() == RecommenderMsg.COLABORATIVE_FILTER_TYPE)
                recommender = COLABORATIVE_FILTER_RECOMMENDER;

            try {
                String call = "python3 " + recommender + " " + recommenderMsg.getMsg();
                Process request = Runtime.getRuntime().exec(call);
                request.waitFor();

                // Read recommendation
                Scanner reader = new Scanner(new File(RESULT_PATH));
                result = (reader.hasNext()) ? reader.nextLine() : "";
            } catch (IOException | InterruptedException e) {
                //e.printStackTrace();
                System.out.println(recommender + "has produced an error.");
            }

            response.setPerformative(ACLMessage.INFORM);
            response.setContent(result);
            response.setSender(getAgent().getAID());
            myAgent.send(response);
        }

        block();
    }
}

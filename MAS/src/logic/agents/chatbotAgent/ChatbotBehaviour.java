package logic.agents.chatbotAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Scanner;
import java.io.IOException;
import java.io.File;

public class ChatbotBehaviour extends CyclicBehaviour {

    private static final String CHATBOT_LOCATION = "src/logic/agents/chatbotAgent/chatbot.py";
    private static final String RESULT_PATH = "src/logic/agents/chatbotAgent/result.txt";
    private static final long serialVersionUID = 1L;
    private static int user_id;

    public ChatbotBehaviour(Agent a) {
        super(a);

        this.user_id = (int) (Math.random() * 100001);
    }


    @Override
    public void action() {
        ACLMessage  msg = myAgent.receive();
        if(msg != null){
            ACLMessage reply = msg.createReply();
            String answer = "Connection failed";
            String user_msg = msg.getContent();
            Boolean end = false;

            try {
                Process api_request = Runtime.getRuntime().exec("python3 "
                        + CHATBOT_LOCATION + " " + this.user_id + " " + user_msg);
                api_request.waitFor();
                File fresult = new File(RESULT_PATH);
                Scanner reader = new Scanner(fresult);
                end = reader.nextLine().equals("END");
                answer = reader.nextLine();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent(answer);

            if(end) {
                System.out.print("END");
                //Now, analyze the "answer"
            }
            myAgent.send(reply);
        }
        else {
            block();
        }
    }
}

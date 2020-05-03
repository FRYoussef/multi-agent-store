package agents.chatbotAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class ChatbotBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = 1L;
    private static int user_id;
    public ChatbotBehaviour(Agent a) {
        super(a);
        Random rand = new Random();
        this.user_id = rand.nextInt();
    }


    @Override
    public void action() {
        ACLMessage  msg = myAgent.receive();
        if(msg != null){
            String content = msg.getContent();
            ACLMessage reply = msg.createReply();
            String answer = "Connection failed";
            String user_msg = msg.getContent();
            Boolean end = false;
            try {
                /*Process proc = Runtime.getRuntime().exec(new String[] {"which", "python3"});
                BufferedReader input =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader error =
                new BufferedReader(new InputStreamReader(proc.getErrorStream()));

                String line = null;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                while ((line = error.readLine()) != null) {
                    System.out.println(line);
                }

            */
               /* ProcessBuilder pb = new ProcessBuilder();
                pb.command("chatbot.py", "run", "" + this.id_user, user_msg);
                Process p = pb.start();
                p.waitFor();
                ProcessHandle.Info a = p.info();*/

                Process api_request = Runtime.getRuntime().exec("python3 chatbot.py " + this.user_id + " " + user_msg);
                api_request.waitFor();

                File fresult = new File("result.txt");
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

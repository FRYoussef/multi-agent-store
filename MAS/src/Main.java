import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import logic.agents.chatbotAgent.ChatbotAgent;
import logic.agents.guiAgent.GuiAgent;
import logic.agents.recommenderAgent.RecommenderAgent;

public class Main {

    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);

        ProfileImpl profile = new ProfileImpl(false);

        AgentContainer container = rt.createMainContainer(profile);
        AgentController agentControllerGUI = null;
        AgentController agentControllerChatbot = null;
        AgentController agentControllerRecommender = null;

        try {
            agentControllerGUI = container.createNewAgent(GuiAgent.NAME,
                    "logic.agents.guiAgent.GuiAgent", null);
            agentControllerGUI.start();

            agentControllerChatbot = container.createNewAgent(ChatbotAgent.NAME,
                    "logic.agents.chatbotAgent.ChatbotAgent", null);
            agentControllerChatbot.start();

            agentControllerRecommender = container.createNewAgent(RecommenderAgent.NAME,
                    "logic.agents.recommenderAgent.RecommenderAgent", null);
            agentControllerRecommender.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}

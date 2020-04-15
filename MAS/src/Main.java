import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {

    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);

        ProfileImpl profile = new ProfileImpl(false);

        AgentContainer container = rt.createMainContainer(profile);
        AgentController agentControllerGUI = null;
        AgentController agentControllerChatbot = null;

        try {
            agentControllerGUI = container.createNewAgent("GuiAgent", "agents.guiAgent.GuiAgent", null);
            agentControllerGUI.start();
            agentControllerChatbot = container.createNewAgent("ChatbotAgent", "agents.chatbotAgent.ChatbotAgent", null);
            agentControllerChatbot.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}

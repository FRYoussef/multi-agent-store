import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.core.Runtime;
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

        try {
            agentControllerGUI = container.createNewAgent("GuiAgent", "agents.guiAgent.GuiAgent", null);
            agentControllerGUI.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}

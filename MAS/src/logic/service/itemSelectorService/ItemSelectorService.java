package logic.service;

import com.sun.javafx.geom.AreaOp;
import control.GuiLauncher;
import control.ItemController;
import control.ItemSelectorController;
import dataAccess.ClothingDao;
import dataAccess.CustomerDao;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import logic.agents.chatbotAgent.ChatbotAgent;
import logic.agents.chatbotAgent.Intents;
import logic.agents.guiAgent.GuiAgent;
import logic.agents.recommenderAgent.RecommenderAgent;
import logic.agents.recommenderAgent.RecommenderMsg;
import logic.agents.recommenderAgent.pythonArgsAdapter.ColaborativeFilterAdapter;
import logic.agents.recommenderAgent.pythonArgsAdapter.ContentBasedAdapter;
import logic.transfer.Clothing;
import logic.transfer.Customer;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ItemSelectorService implements IService{

    private static final String ERROR_RECOMMENDATION = "error";
    private Customer customer;
    private ArrayList<Clothing> clothings;
    private ArrayList<Clothing> recomendations;
    private ItemSelectorController controller;
    private GuiAgent guiAgent;
    private DFA dfa;

    public ItemSelectorService(Customer customer, ItemSelectorController controller, GuiAgent guiAgent) {
        dfa = new DFA();
        this.customer = customer;
        this.controller = controller;
        this.guiAgent = guiAgent;
        ClothingDao dao = new ClothingDao();
        this.clothings = new ArrayList<>(dao.getAll());
    }

    public int getNumberItems(){
        ArrayList<Clothing> cs = (recomendations == null) ? clothings : recomendations;
        return cs.size();
    }

    public Clothing getItem(int i){
        ArrayList<Clothing> cs = (recomendations == null) ? clothings : recomendations;

        if(i < 0 || i >= cs.size())
            return new Clothing();

        return cs.get(i);
    }

    @Override
    public void takeDown() {
        CustomerDao cusDao = new CustomerDao();
        cusDao.write(customer);
    }

    public void onClickItem(int item){
        ItemController con = new ItemController(guiAgent, getItem(item), customer);
        String uri = "../views/item.fxml";

        try {
            takeDown();
            GuiLauncher.instance().switchView(uri, con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickSend(String msg){
        States actualState = dfa.getState();
        System.out.println("onClickSend");
        switch (actualState) {
            case Q0:
                GuiEvent ge0 = new GuiEvent(this, GuiAgent.CMD_SEND_CHATBOT);
                ge0.addParameter(msg);
                guiAgent.postGuiEvent(ge0);
                break;
            case Q2:

                if(msg.compareToIgnoreCase("YES") == 0){
                    ContentBasedAdapter adapter = new ContentBasedAdapter(customer);
                    RecommenderMsg rMsg = new RecommenderMsg(RecommenderMsg.CONTENT_BASED_TYPE, adapter.getPythonArgs());
                    GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_RECOMMENDER);
                    ge.addParameter(rMsg.getRawMsg());
                    guiAgent.postGuiEvent(ge);
                    dfa.nextState(Alphabet.YES);
                }
                else if (msg.compareToIgnoreCase("NO") == 0) {
                    GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_CHATBOT);
                    ge.addParameter(Alphabet.NO.getSymbol());
                    guiAgent.postGuiEvent(ge);
                    customer.delPeferences();
                    dfa.nextState(Alphabet.NO);
                }
                else {
                    controller.showMessage("Sorry, I don't understand you");
                }
                break;
            case Q4:
                GuiEvent ge4 = new GuiEvent(this, GuiAgent.CMD_SEND_CHATBOT);
                ge4.addParameter(msg);
                guiAgent.postGuiEvent(ge4);
                break;
            case Q5:
                GuiEvent ge5 = new GuiEvent(this, GuiAgent.CMD_SEND_CHATBOT);
                if (customer.getGender() != null) {
                    ge5.addParameter(msg + " for " + customer.getGender());
                }
                else {
                    ge5.addParameter(msg + customer.getGender());
                }
                guiAgent.postGuiEvent(ge5);
                break;
            case Q6:
                break;
            case Q7:
                break;
            case Q8:
                //error
                break;
            default:
                break;
        }

        // TODO uncomment for chatbot interaction
        // notify gui agent
        /*GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_CHATBOT);
        ge.addParameter(msg);
        guiAgent.postGuiEvent(ge);*/



        // TODO comment. It's just for testing
        /*customer.addPreference("blue");
        customer.setGender("male");
        ContentBasedAdapter adapter = new ContentBasedAdapter(customer);
        RecommenderMsg rMsg = new RecommenderMsg(RecommenderMsg.CONTENT_BASED_TYPE, adapter.getPythonArgs());
        GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_RECOMMENDER);
        ge.addParameter(rMsg.getRawMsg());
        guiAgent.postGuiEvent(ge);*/


    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        if(msg.getSender().getName().contains(ChatbotAgent.NAME))
            handleChatbotMsg(msg.getContent());

        else if(msg.getSender().getName().contains(RecommenderAgent.NAME))
            handleRecommenderMsg(msg.getContent());
    }

    private void handleChatbotMsg(String msg) {
        System.out.println("handleChatbotMsg");
        States actualState = dfa.getState();
        Intents intent = Intents.DEFAULT;
        String end_state = "";
        String msgs = "";
        String[] params = null;
        if (msg.length() > 0) {
            params =  msg.split("-")[0].split(",");
            end_state = msg.split("-")[1];
            msgs = msg.split("-")[2];
            String intention = msg.split("-")[3];
            intent = Intents.parseIntent(intention);
            controller.showMessage(msgs);
        }

        switch (actualState) {
            case Q0:

                if (!intent.equals(Intents.WELCOME)) {
                    if (intent.equals(Intents.YES_RECOMMENDER)) {
                        dfa.nextState(Alphabet.YES);
                        handleChatbotMsg("");
                    } else if (intent.equals(Intents.NO_RECOMMENDER))
                        dfa.nextState(Alphabet.NO);

                }

                break;
            case Q2:
                //ask about preview preferences
                String msg_gui = "";
                ArrayList<String> preferences = customer.getPreferences();
                if(preferences.isEmpty()){

                    msg_gui = "Oh, it's seems like we don't have previous preferences of you stored";

                    GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_CHATBOT);
                    ge.addParameter(Alphabet.NO.getSymbol());
                    guiAgent.postGuiEvent(ge);

                    dfa.nextState(Alphabet.NO);

                    controller.showMessage(msg_gui);
                }
                else {
                    if (preferences.size() == 2) {
                        msg_gui = "Would you like " + preferences.get(0) + " " + preferences.get(1) + "?";
                        controller.showMessage(msg_gui);
                    }
                    if (preferences.size() == 1) {
                        msg_gui = "Would you like " + preferences.get(0) + "?";
                        controller.showMessage(msg_gui);
                    }

                }
                break;
            case Q4:
                if (intent.equals(Intents.WANTS_QUESTIONS)) {
                    dfa.nextState(Alphabet.YES);
                } else if (intent.equals(Intents.NO_WANT_QUESTIONS)) {
                    dfa.nextState(Alphabet.NO);
                    handleChatbotMsg("");
                }
                break;
            case Q5:
                if (end_state.equals("True")){
                    customer.setGender(params[0]);
                    customer.addPreference(params[1]);
                    if (params.length>2){
                        customer.addPreference(params[2]);
                    }
                    dfa.nextState(Alphabet.YES);
                    handleChatbotMsg("");
                }
                break;
            case Q6:
                //collaborative filter
                ColaborativeFilterAdapter adapter = new ColaborativeFilterAdapter(customer);
                RecommenderMsg rMsg = new RecommenderMsg(RecommenderMsg.COLABORATIVE_FILTER_TYPE, adapter.getPythonArgs());
                GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_RECOMMENDER);
                ge.addParameter(rMsg.getRawMsg());
                guiAgent.postGuiEvent(ge);
                break;
            case Q7:
                //content-based filter
                ContentBasedAdapter adapter_cb = new ContentBasedAdapter(customer);
                RecommenderMsg rMsg_cb = new RecommenderMsg(RecommenderMsg.CONTENT_BASED_TYPE, adapter_cb.getPythonArgs());
                GuiEvent ge_cb = new GuiEvent(this, GuiAgent.CMD_SEND_RECOMMENDER);
                ge_cb.addParameter(rMsg_cb.getRawMsg());
                guiAgent.postGuiEvent(ge_cb);
                break;
            case Q8:
                //error
                break;
            default:
                break;
        }


        System.out.println(dfa.getState());
    }

    private void handleRecommenderMsg(String msg){
        if(msg.equals("")) {
            controller.showMessage("I don't found recommendations for you, sorry :(");
            return;
        }
        else if(msg.equals(ERROR_RECOMMENDATION)) {
            controller.showMessage("Internal error");
            return;
        }

        // Transform string to int
        String[] chunks = msg.split(",");
        int[] ids = new int[chunks.length];
        for(int i = 0; i < chunks.length; i++){
            try{ ids[i] = Integer.parseInt(chunks[i]); } catch (Exception ignored){}
        }

        // get DB ids as Clothing objects
        ClothingDao dao = new ClothingDao();
        recomendations = dao.getFromIds(ids);

        controller.refreshItems();
        controller.showMessage("I' ve found " + recomendations.size() + " clothes for you, I hope you like them.");
    }
}

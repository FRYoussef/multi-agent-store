package logic.service.itemSelectorService;

import logic.agents.chatbotAgent.Intents;
import logic.transfer.CustomerResponse;

public class DfaItemSelectorService {
    private static final int Q0 = 0;
    private static final int Q1 = 1;
    private static final int Q2 = 2;
    private static final int Q3 = 3;
    private static final int Q4 = 4;
    private static final int Q5 = 5;
    private static final int Q6 = 6;
    private static final int Q7 = 7;

    public static final String ERROR = "Sorry, I don't understand you.";
    public static final String FINAL_RESPONSE = "Talk to me for more advises.";

    private int state;
    private ItemSelectorService service;

    public DfaItemSelectorService(ItemSelectorService service){
        this.state = Q0;
        this.service = service;
    }

    public void runDFA(String msg, boolean isChatbot){
        String chatbotMsg = parseChatbotMsg(msg);
        Intents intent = parseIntent(msg);

        switch (state){
            case Q0:
                // send "hello" to chatbot in order to launch replication
                // or user message
                String to = (msg.equals("")) ? "hello" : msg;
                service.notifyChatbotAgent(to);
                state = Q1;
                break;
            case Q1:
                // show chatbot message, offering help
                service.showMessage(chatbotMsg);
                state = Q2;
                break;
            case Q2:
                // user replication to help offering
                service.notifyChatbotAgent(msg);
                state = Q3;
                break;
            case Q3:
                // chatbot confirming customer response
                if (intent.equals(Intents.YES_RECOMMENDER)) {
                    if(service.getCustomer().hasPreferences()){
                        // has previous recommendations
                        String ask = "Would you like " + service.getCustomer().preferencesToString() + "?";
                        service.showMessage(ask);
                        state = Q4;
                    }
                    else {
                        // notify chatbot that there is no previous recommendations
                        service.notifyChatbotAgent(CustomerResponse.NO);
                        state = Q5;
                    }
                }
                else{
                    // he doesn't want recommendations, or answer error
                    state = Q0;
                    service.showMessage(chatbotMsg);
                }
                break;
            case Q4:
                // customer response of previous recommendations
                CustomerResponse response = new CustomerResponse(msg);

                if(response.isPositive()){
                    service.notifyCBRecommender(true);
                    state = Q0;
                }
                else if(response.isNegative()){
                    service.notifyChatbotAgent(CustomerResponse.NO);
                    service.getCustomer().deletePreferences();
                    state = Q5;
                }
                else
                    state = Q0;
                break;
            case Q5:
                // ask for preferences?
                service.showMessage(chatbotMsg);
                state = Q6;
                break;
            case Q6:
                // customer response to Q5
                CustomerResponse response1 = new CustomerResponse(msg);
                if(response1.isPositive()) {
                    service.notifyChatbotAgent(msg);
                    state = Q7;
                }
                else if(response1.isNegative()){
                    service.notifyCFRecommender();
                    state = Q0;
                }
                else
                    state = Q0;
                break;
            case Q7:
                if (isChatbot) { // chatbot questions
                    service.showMessage(chatbotMsg);
                    String all_params_required = msg.split("-")[1];
                    if (all_params_required.equals("True")) {
                        // chatbot response with labels for CB recommender
                        String[] params = msg.split("-")[0].split(",");
                        service.getCustomer().setGender(params[0]);
                        service.getCustomer().addPreferences(params, 1, params.length);
                        service.notifyCBRecommender(true);
                        state = Q0;
                    }
                }
                else { // user response
                    String gender = service.getCustomer().getGender();
                    if (!gender.equals(""))
                        msg += " for " + service.getCustomer().getGender();

                    service.notifyChatbotAgent(msg);
                }
                break;
            default:
                break;
        }
    }

    private String parseChatbotMsg(String msg){
        String msgs = "";
        String[] list;

        if (msg.length() > 0) {
            list = msg.split("-");
            if(list.length > 2)
                msgs = list[2];
        }

        return msgs;
    }

    private Intents parseIntent(String str){
        Intents intent = Intents.DEFAULT;
        if (str.length() > 0) {
            String[] intention = str.split("-");
            if(intention.length > 3)
                intent = Intents.parseIntent(intention[3]);
        }
        return intent;
    }
}

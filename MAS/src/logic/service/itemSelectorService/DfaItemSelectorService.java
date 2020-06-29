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

    private static final String ERROR = "Sorry, I don't understand you.";

    private int state;
    private ItemSelectorService service;

    public DfaItemSelectorService(ItemSelectorService service){
        this.state = Q0;
        this.service = service;
    }

    public void runDFA(String msg){
        String chatbotMsg = parseChatbotMsg(msg);
        Intents intent = parseIntent(msg);

        switch (state){
            case Q0:
                // send "hello" to chatbot in order to launch replication
                // or user message
                String to = (msg.equals("")) ? "hello" : msg;
                service.notifyChatbotAgent(to);
                state = Q1;
                System.out.println("Q0");
                break;
            case Q1:
                // show chatbot message, offering help
                service.showMessage(chatbotMsg);
                state = Q2;
                System.out.println("Q1");
                break;
            case Q2:
                // user replication to help offering
                service.notifyChatbotAgent(msg);
                state = Q3;
                System.out.println("Q2");
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
                System.out.println("Q3");
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
                System.out.println("Q4");
                break;
            case Q5:
                // check chatbot intent for answers
                service.showMessage(chatbotMsg);

                if(intent.equals(Intents.WANTS_QUESTIONS)){
                    // customer answers for content based recommender
                    String toCB = msg;
                    if (service.getCustomer().getGender() != null)
                        toCB = toCB + " for ";

                    service.notifyChatbotAgent(toCB + service.getCustomer().getGender());
                    state = Q6;
                }

                else if(intent.equals(Intents.NO_WANT_QUESTIONS)){
                    service.notifyCFRecommender();
                    state = Q0;
                }
                System.out.println("Q5");
                break;
            case Q6:
                // chatbot response with labels for CB recommender
                service.showMessage(chatbotMsg);
                String[] params = msg.split("-")[0].split(",");
                service.getCustomer().setGender(params[0]);
                service.getCustomer().addPreferences(params, 1, params.length-1);

                service.notifyCBRecommender(true);
                state = Q0;
                System.out.println("Q6");
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

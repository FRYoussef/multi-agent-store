package logic.agents.chatbotAgent;

public enum Intents {
    DEFAULT("Default Fallback Intent"), WELCOME("Welcome"), NO_RECOMMENDER("No recommender"), YES_RECOMMENDER("Yes recommender"), ASK_PREFERENCES("Ask preferences"), WANTS_QUESTIONS("Want questions"), NO_WANT_QUESTIONS("No want questions"), ASK_QUESTIONS("Ask questions"), PREV_PREFERENCES("Yes to prev preferences");

    final private String s;

    Intents(String s) {
        this.s = s;
    }

    public String getText() {
        return s;
    }

    static public Intents parseIntent(String msg){
        for (Intents intent : Intents.values()) {
            if (msg.equals(intent.s)) return intent;
        }
        return null;
    }
}

package logic.agents.recommenderAgent;

public class RecommenderMsg {
    public static final int CONTENT_BASED_TYPE = 0;
    public static final int COLABORATIVE_FILTER_TYPE = 1;

    private static final String CONTENT_BASED_STR = "CB:";
    private static final String COLABORATIVE_FILTER_STR = "CF:";

    private String rawMsg;
    private int type = -1;
    private String msg;

    public RecommenderMsg(String rawMsg) {
        this.rawMsg = rawMsg;
        generateMsg();
    }

    public RecommenderMsg(int type, String msg) {
        this.type = type;
        this.msg = msg;
        generateRawMsg();
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    private void generateMsg(){
        if(rawMsg.contains(CONTENT_BASED_STR)){
            type = CONTENT_BASED_TYPE;
            msg = rawMsg.replace(CONTENT_BASED_STR, "");
        }
        else if(rawMsg.contains(COLABORATIVE_FILTER_STR)){
            type = COLABORATIVE_FILTER_TYPE;
            msg = rawMsg.replace(COLABORATIVE_FILTER_STR, "");
        }
    }

    public String getRawMsg() {
        return rawMsg;
    }

    private void generateRawMsg(){
        String sort = "";
        if(type == CONTENT_BASED_TYPE)
            sort = CONTENT_BASED_STR;
        else if(type == COLABORATIVE_FILTER_TYPE)
            sort = COLABORATIVE_FILTER_STR;

        rawMsg = sort + msg;
    }
}

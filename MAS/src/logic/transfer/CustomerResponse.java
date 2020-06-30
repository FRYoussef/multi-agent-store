package logic.transfer;

import java.util.Arrays;
import java.util.HashSet;

public class CustomerResponse {
    public static final String YES = "yes";
    public static final String NO = "no";

    private static final HashSet<String> positiveAnswer = new HashSet<>(Arrays.asList(
            YES,
            "y",
            "sure",
            "of course",
            "ok",
            "okey"));

    private static final HashSet<String> negativeAnswer = new HashSet<>(Arrays.asList(
            NO,
            "n",
            "no way"));

    private final String response;

    public CustomerResponse(String response) {
        this.response = response.toLowerCase();
    }

    public boolean isPositive() {
        return positiveAnswer.contains(response);
    }

    public boolean isNegative() {
        return negativeAnswer.contains(response);
    }

    public String getResponse() {
        return response;
    }
}
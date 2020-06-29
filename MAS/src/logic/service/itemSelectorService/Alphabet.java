package logic.service.itemSelectorService;

public enum Alphabet {
    YES("Yes"), NO("No");
    final private String s;

    Alphabet(String s) {
        this.s = s;
    }

    public String getSymbol() {
        return s;
    }
}
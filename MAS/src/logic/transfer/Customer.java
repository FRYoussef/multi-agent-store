package logic.transfer;

import dataAccess.CsvHandler;

public class Customer implements ICsvObjectionable{
    private int id;
    private String password;
    private String name;
    private String gender;
    private int[] viewsId;
    private int[] purchasesId;
    private String[] preferences;

    public Customer(){
        this.id = -1;
        this.password = "";
        this.name = "None";
        this.gender = "";
        this.viewsId = new int[]{-1};
        this.purchasesId = new int[]{-1};
        this.preferences = new String[]{""};
    }

    public Customer(int id, String password, String name, String gender, int[] viewsId, int[] purchasesId, String[] preferences) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.viewsId = viewsId;
        this.purchasesId = purchasesId;
        this.preferences = preferences;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int[] getViewsId() {
        return viewsId;
    }

    public void setViewsId(int[] viewsId) {
        this.viewsId = viewsId;
    }

    public int[] getPurchasesId() {
        return purchasesId;
    }

    public void setPurchasesId(int[] purchasesId) {
        this.purchasesId = purchasesId;
    }

    public String[] getPreferences() {
        return preferences;
    }

    public void setPreferences(String[] preferences) {
        this.preferences = preferences;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String toCsvFormat(){
        StringBuilder sb = new StringBuilder(id + CsvHandler.CSV_SEPARATOR)
                .append(password).append(CsvHandler.CSV_SEPARATOR)
                .append(name).append(CsvHandler.CSV_SEPARATOR)
                .append(gender).append(CsvHandler.CSV_SEPARATOR);

        for(int id : viewsId)
            sb.append(id).append(CsvHandler.FIELD_SEPARATOR);

        sb.append(CsvHandler.CSV_SEPARATOR);
        for(int id : purchasesId)
            sb.append(id).append(CsvHandler.FIELD_SEPARATOR);

        sb.append(CsvHandler.CSV_SEPARATOR);
        for(String p : preferences)
            sb.append(p).append(CsvHandler.FIELD_SEPARATOR);

        return sb.toString();
    }
}

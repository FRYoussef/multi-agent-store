package logic.transfer;

import dataAccess.CsvHandler;

import java.util.ArrayList;
import java.util.*;

public class Customer implements ICsvObjectionable{
    private int id;
    private String password;
    private String name;
    private String gender;
    private ArrayList<Integer> viewsId;
    private ArrayList<Integer> purchasesId;
    private String[] preferences;

    public Customer(){
        this.id = -1;
        this.password = "";
        this.name = "None";
        this.gender = "";
        this.viewsId = new ArrayList<>();
        this.purchasesId = new ArrayList<>();
        this.preferences = new String[]{""};
    }

    public Customer(int id, String password, String name, String gender, int[] viewsId, int[] purchasesId, String[] preferences) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.viewsId = new ArrayList<>(viewsId.length);
        this.purchasesId = new ArrayList<>(purchasesId.length);
        this.preferences = preferences;

        for(int v : viewsId)
            this.viewsId.add(v);

        for(int p : purchasesId)
            this.purchasesId.add(p);
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public void addNewView(int id) {
        viewsId.add(id);
    }

    public void addNewPurchase(int id){
        purchasesId.add(id);
    }

    public String[] getPreferences() {
        return preferences;
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

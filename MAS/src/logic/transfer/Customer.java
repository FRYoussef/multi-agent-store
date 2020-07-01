package logic.transfer;

import dataAccess.CsvHandler;

import java.util.ArrayList;
import java.util.*;

import static java.lang.Math.abs;

public class Customer implements ICsvObjectionable, Comparable<Customer>{
    private int id;
    private String password;
    private String name;
    private String gender;
    private ArrayList<Integer> viewsId;
    private ArrayList<Integer> purchasesId;
    private ArrayList<String> preferences;

    public Customer(){
        this.id = -1;
        this.password = "";
        this.name = "None";
        this.gender = "";
        this.viewsId = new ArrayList<>();
        this.purchasesId = new ArrayList<>();
        this.preferences = new ArrayList<>();
    }

    public Customer(int id, String name, String password){
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = "";
        this.viewsId = new ArrayList<>();
        this.purchasesId = new ArrayList<>();
        this.preferences = new ArrayList<>();
    }

    public Customer(int id, String password, String name, String gender, int[] viewsId, int[] purchasesId, String[] preferences) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.viewsId = new ArrayList<>(viewsId.length);
        this.purchasesId = new ArrayList<>(purchasesId.length);
        this.preferences = transformArray(preferences);

        for(int v : viewsId)
            this.viewsId.add(v);

        for(int p : purchasesId)
            this.purchasesId.add(p);
    }

    private ArrayList<String> transformArray(String[] array){
        if(array == null)
            return new ArrayList<>();
        if(array.length > 0 && array[0].equals(""))
            return new ArrayList<>();

        return new ArrayList<>(Arrays.asList(array));
    }

    public boolean isValidPassword(String password){
        return this.password.equals(password);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
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

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public void addPreferences(String[] preferences, int start, int end){
        if(start > end)
            return;
        if(start < 0)
            return;

        for(int i = start; i < end; i++)
            this.preferences.add(preferences[i]);
    }

    public boolean hasPreferences(){
        return !preferences.isEmpty();
    }

    public void deletePreferences(){
        this.preferences.clear();
    }

    public String preferencesToString(){
        StringBuilder sb = new StringBuilder();

        for(String p : preferences)
            sb.append(p).append(" ");

        if(preferences.size() > 0)
            sb.deleteCharAt(sb.length()-1);

        return sb.toString();
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

        if(viewsId.size() > 0)
            sb.deleteCharAt(sb.length()-1);

        sb.append(CsvHandler.CSV_SEPARATOR);
        for(int id : purchasesId)
            sb.append(id).append(CsvHandler.FIELD_SEPARATOR);

        if(purchasesId.size() > 0)
            sb.deleteCharAt(sb.length()-1);

        sb.append(CsvHandler.CSV_SEPARATOR);
        for(String p : preferences)
            sb.append(p).append(CsvHandler.FIELD_SEPARATOR);

        if(preferences.size() > 0)
            sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    @Override
    public int compareTo(Customer o) {
        if(id < 0 && o.getId() < 0)
            return abs(id) - abs(o.getId());
        else if(id < 0)
            return -1;
        else if(o.getId() < 0)
            return 1;

        return id - o.getId();
    }
}

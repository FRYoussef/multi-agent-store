package dataAccess;

import model.Clothing;
import model.Customer;

import java.util.ArrayList;
import java.util.HashSet;

public class CustomerDao implements IDao<Customer>{
    private static final int POS_ID = 0;
    private static final int POS_PASSWORD = 1;
    private static final int POS_NAME = 2;
    private static final int POS_GENDER = 3;
    private static final int POS_VIEWS = 4;
    private static final int POS_PURCHASES = 5;
    private static final int POS_PREFERENCES = 6;
    private static final String CSV_URI = "../DB/CustomerDB.csv";

    @Override
    public Customer get(int id) {
        HashSet<Customer> customers = getAll();
        Customer customer = new Customer();

        for(Customer c : customers){
            if(c.getId() == id){
                customer = c;
                break;
            }
        }
        return customer;
    }

    private int[] toIntArray(String[] list){
        int[] intList = new int[list.length];
        int cont = 0;

        for(String e : list)
            intList[cont++] = Integer.parseInt(e);

        return intList;
    }

    @Override
    public HashSet<Customer> getAll() {
        HashSet<Customer> customers = new HashSet<>();
        ArrayList<ArrayList<String>> custFields = CsvHandler.readCSV(CSV_URI);

        // let's transform strings into objects
        for(ArrayList<String> al : custFields){
            int[] viewsIds = toIntArray(CsvHandler.fieldSplitter(al.get(POS_VIEWS)));
            int[] purchasesIds = toIntArray(CsvHandler.fieldSplitter(al.get(POS_PURCHASES)));

            Customer customer = new Customer(
                    Integer.parseInt(al.get(POS_ID)),
                    al.get(POS_PASSWORD),
                    al.get(POS_NAME),
                    al.get(POS_GENDER),
                    viewsIds,
                    purchasesIds,
                    CsvHandler.fieldSplitter(al.get(POS_PREFERENCES))
            );
            customers.add(customer);
        }
        return customers;
    }

    @Override
    public void write(Customer customer) {
        HashSet<Customer> customers = getAll();

        // if exists remove it in order to add it again (modification)
        customers.remove(customer);
        customers.add(customer);

        writeAll(customers);
    }

    @Override
    public void writeAll(HashSet<Customer> customers) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(getCsvHeader());

        for(Customer customer : customers)
            lines.add(customer.toCsvFormat());

        CsvHandler.writeCSV(CSV_URI, lines);
        lines = null;
    }

    @Override
    public String getCsvHeader() {
        return "Id" + CsvHandler.CSV_SEPARATOR +
                "Password" + CsvHandler.CSV_SEPARATOR +
                "Name" + CsvHandler.CSV_SEPARATOR +
                "Gender" + CsvHandler.CSV_SEPARATOR +
                "Views" + CsvHandler.CSV_SEPARATOR +
                "Purchases" + CsvHandler.CSV_SEPARATOR+
                "Preferences";
    }
}

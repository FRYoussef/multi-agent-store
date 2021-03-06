package dataAccess;

import logic.transfer.Customer;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

public class CustomerDao implements IDao<Customer>{
    private static final int POS_ID = 0;
    private static final int POS_PASSWORD = 1;
    private static final int POS_NAME = 2;
    private static final int POS_GENDER = 3;
    private static final int POS_VIEWS = 4;
    private static final int POS_PURCHASES = 5;
    private static final int POS_PREFERENCES = 6;
    private static final int N_FIELDS = 7;
    private static final String CSV_URI = ".." + File.separator + "DB" + File.separator + "CustomerDB.csv";
    private static ArrayList<Customer> alCustomers;

    @Override
    public Customer get(int id) {
        ArrayList<Customer> customers = getAll();
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
        if(list[0].equals(""))
            return new int[0];

        int[] intList = new int[list.length];
        int cont = 0;

        for(String e : list)
            try{ intList[cont++] = Integer.parseInt(e); } catch (Exception ignored){ }

        return intList;
    }

    @Override
    public ArrayList<Customer> getAll() {
        if(alCustomers != null)
            return alCustomers;

        alCustomers = new ArrayList<>();
        ArrayList<ArrayList<String>> custFields = CsvHandler.readCSV(CSV_URI, N_FIELDS);

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
            alCustomers.add(customer);
        }
        return alCustomers;
    }

    @Override
    public void write(Customer customer) {
        /*customers.remove(customer);
        customers.add(customer);*/

        writeAll(getAll());
    }

    @Override
    public void writeAll(ArrayList<Customer> customers) {
        alCustomers = customers;
        ArrayList<String> lines = new ArrayList<>();
        lines.add(getCsvHeader());

        for(Customer customer : customers)
            lines.add(customer.toCsvFormat());

        CsvHandler.writeCSV(CSV_URI, lines);
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

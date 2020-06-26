package logic.service;

import dataAccess.ClothingDao;
import dataAccess.CustomerDao;
import logic.transfer.Clothing;
import logic.transfer.Customer;

import java.util.ArrayList;

public class ItemSelectorService implements IService{

    private Customer customer;
    private ArrayList<Clothing> clothings;

    public ItemSelectorService(){
        customer = new Customer();
        clothings = new ArrayList<>();
        clothings.add(new Clothing());
    }

    public ItemSelectorService(Customer customer) {
        this.customer = customer;
        ClothingDao dao = new ClothingDao();
        this.clothings = new ArrayList<>(dao.getAll());
    }

    public int getNumberItems(){
        return clothings.size();
    }

    public Clothing getItem(int i){
        if(i < 0 || i >= clothings.size())
            return new Clothing();

        return clothings.get(i);
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void takeDown() {
        CustomerDao cusDao = new CustomerDao();
        cusDao.write(customer);
    }
}

package logic.service;

import dataAccess.ClothingDao;
import dataAccess.CustomerDao;
import logic.transfer.Clothing;
import logic.transfer.Customer;

public class ItemService implements IService{
    private Clothing clothing;
    private Customer customer;

    public ItemService() {
        this.clothing = new Clothing();
        this.customer = new Customer();
    }

    public ItemService(Clothing clothing, Customer customer) {
        this.clothing = clothing;
        this.customer = customer;
        addNewView();
    }

    public void addNewView(){
        clothing.addNewView();
        customer.addNewView(clothing.getId());
    }

    public void addNewSale(){
        clothing.addNewSale();
        customer.addNewPurchase(clothing.getId());
    }

    public Clothing getClothing() {
        return clothing;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void takeDown() {
        ClothingDao clothingDao = new ClothingDao();
        clothingDao.write(clothing);
        CustomerDao customerDao = new CustomerDao();
        customerDao.write(customer);
    }
}

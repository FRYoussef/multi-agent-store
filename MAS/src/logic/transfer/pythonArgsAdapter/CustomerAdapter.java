package logic.transfer.pythonArgsAdapter;

import logic.transfer.Customer;

public class CustomerAdapter implements IPythonArgs{

    private Customer customer;

    public CustomerAdapter(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String getPythonArgs() {
        StringBuilder sb = new StringBuilder();

        sb.append(customer.getId()).append(" ").append(customer.getGender());

        for (String p : customer.getPreferences())
            sb.append(p).append(" ");

        return sb.toString();
    }
}

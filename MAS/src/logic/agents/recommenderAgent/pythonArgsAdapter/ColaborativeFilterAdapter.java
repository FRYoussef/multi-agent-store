package logic.agents.recommenderAgent.pythonArgsAdapter;

import logic.transfer.Customer;

public class ColaborativeFilterAdapter implements IPythonArgs{
    private Customer customer;

    public ColaborativeFilterAdapter(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String getPythonArgs() {
        return "" + customer.getId();
    }
}

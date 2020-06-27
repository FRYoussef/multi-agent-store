package logic.agents.recommenderAgent.pythonArgsAdapter;

import logic.transfer.Customer;

public class ContentBasedAdapter implements IPythonArgs{

    private Customer customer;

    public ContentBasedAdapter(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String getPythonArgs() {
        StringBuilder sb = new StringBuilder();

        sb.append(customer.getGender()).append(" ");

        for (String p : customer.getPreferences())
            sb.append(p).append(" ");

        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }
}
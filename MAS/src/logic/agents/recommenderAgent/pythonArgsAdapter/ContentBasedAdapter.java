package logic.agents.recommenderAgent.pythonArgsAdapter;

import logic.transfer.Customer;

public class ContentBasedAdapter implements IPythonArgs{

    private Customer customer;
    private final boolean addGender;

    public ContentBasedAdapter(Customer customer, boolean gender) {
        this.customer = customer;
        this.addGender = gender;
    }

    @Override
    public String getPythonArgs() {
        StringBuilder sb = new StringBuilder();

        if(addGender && !customer.getGender().equals(""))
            sb.append(customer.getGender()).append(" ");

        for (String p : customer.getPreferences())
            sb.append(p).append(" ");

        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }
}
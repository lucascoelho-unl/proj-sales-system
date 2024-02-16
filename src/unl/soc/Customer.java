package unl.soc;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String uuid;
    private String firstName;
    private String lastName;
    private Address address;
    private List<String> emailList;

    public Customer(String uuid, String firstName, String lastName, Address address, List<String> emailList) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emailList = emailList;
    }
}

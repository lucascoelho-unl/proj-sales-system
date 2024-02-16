package unl.soc;

import java.util.List;

public class Manager extends Customer {

    public Manager(String uuid, String firstName, String lastName, Address address, List<String> emailList) {
        super(uuid, firstName, lastName, address, emailList);
    }
}

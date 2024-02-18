package unl.soc.person;

import unl.soc.Address;

import java.util.List;

public class Manager extends Person{
    public Manager(String uuid, String firstName, String lastName, Address address, List<String> emailList) {
        super(uuid, firstName, lastName, address, emailList);
    }
    public Manager(Person p){
        super(p.getUuid(), p.getFirstName(), p.getLastName(), p.getAddress(),p.getEmailList());
    }
}

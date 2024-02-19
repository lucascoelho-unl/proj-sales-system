package unl.soc.person;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import unl.soc.Address;

import java.util.List;

@XStreamAlias("person")
public class Person {
    @Expose
    private String uuid;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private Address address;

    @XStreamAlias("emails")
    private List<String> emailList;

    public Person(String uuid, String firstName, String lastName, Address address, List<String> emailList) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emailList = emailList;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getAddress() {
        return address;
    }

    public List<String> getEmailList() {
        return emailList;
    }

}

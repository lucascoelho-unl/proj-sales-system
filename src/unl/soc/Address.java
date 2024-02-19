package unl.soc;

import com.google.gson.annotations.Expose;
import unl.soc.person.Person;

public class Address {
    @Expose
    private String street;
    @Expose
    private String city;
    @Expose
    private String state;
    @Expose
    private int zipCode;

    public Address(String street, String city, String state, int zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
}

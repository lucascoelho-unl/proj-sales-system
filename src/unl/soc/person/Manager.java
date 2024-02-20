package unl.soc.person;

import com.google.gson.annotations.Expose;

public class Manager{
    @Expose
    Person manager;
    public Manager(Person manager) {
        this.manager = manager;
    }

}

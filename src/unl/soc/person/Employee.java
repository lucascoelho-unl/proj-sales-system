package unl.soc.person;

import java.util.Objects;

public class Employee {
    private Person employee;

    public Employee(Person employee) {
        this.employee = employee;
    }

    public Person getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "Employee: " + employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee1 = (Employee) o;
        return Objects.equals(employee, employee1.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee);
    }
}
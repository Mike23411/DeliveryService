package edu.gatech.cs6310;

public class Employee extends User {

    protected String uniqueTaxIdentifier;

    public Employee() {
        super();
    }

    public Employee(String account, String firstname, String lastname, String phone, String tax) {
        super(account, firstname, lastname, phone);
        this.uniqueTaxIdentifier = tax;
    }

}

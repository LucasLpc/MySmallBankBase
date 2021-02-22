package fr.paris8.iutmontreuil.mysmallbank.account.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Holder {

    private final String id;
    private final String lastName;
    private final String firstName;
    private final Address address;
    private final LocalDate birthDate;

    private final List<Account> accounts;


    public Holder(String id, String lastName, String firstName, Address address, LocalDate birthDate, List<Account> accounts) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.birthDate = birthDate;
        this.accounts = accounts;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Holder merge(Holder newHolder){

        String lastName= (newHolder.getLastName()!=null) ? newHolder.getLastName(): this.lastName;
        String firstName= (newHolder.getFirstName()!=null) ? newHolder.getFirstName(): this.firstName;
        Address address = this.updateAddress(newHolder.getAddress());
        LocalDate birthDate= (newHolder.getBirthDate()!=null) ? newHolder.getBirthDate(): this.birthDate;

        return new Holder(this.id,lastName,firstName,address,birthDate,accounts);
    }

    public Address updateAddress(Address newAddress){
        if (newAddress == null) return null;
        String country = (newAddress.getCountry()!=null) ? newAddress.getCountry(): this.address.getCountry();
        String city = (newAddress.getCity()!=null) ? newAddress.getCity(): this.address.getCity();
        String zipCode = (newAddress.getZipCode()!=null) ? newAddress.getZipCode(): this.address.getZipCode();
        String street = (newAddress.getStreet()!=null) ? newAddress.getStreet(): this.address.getStreet();
        Address finalAddress = new Address(street,zipCode,city,country);
        return finalAddress;
    }


}

package unl.soc;

import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvToSql {
    public static void main(String[] args) throws IOException {
        getAllAddressCsv("testOutput/address.txt");
        getAllPerson("testOutput/personAddress.txt");

        emailPerson("testOutput/email.txt");

        storePersonAddress("testOutput/store.txt");

        itemToSql("testOutput/items.txt");
    }

    public static List<String> getEmail() throws IOException {
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        List<String> emailsList = new ArrayList<>();

        for (Person person : people) {
            var emails = person.getEmailList();
            emailsList.addAll(emails);
        }
        return emailsList;
    }

    public static void getAllAddressCsv(String filePathOutput) throws IOException {
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var stores = DataProcessor.readStoreCSVtoList("data/Stores.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOutput));
        writer.write("insert into Address (street, city, state, zipCode) \nvalues");
        writer.newLine();
        for (Person person : people) {
            var address = person.getAddress();
            writer.write(String.format("('%s','%s','%s','%d'),", address.getStreet(), address.getCity(),address.getState(), address.getZipCode()));
            writer.newLine();
        }
        for (var store : stores) {
            var address = store.getAddress();
            var index = stores.indexOf(store);
            writer.write(String.format("('%s','%s','%s','%d')", address.getStreet(), address.getCity(),address.getState(), address.getZipCode()));
            if (index == stores.size() - 1){
                writer.write(";");
            }
            else{
                writer.write(",");
                writer.newLine();
            }
        }
        writer.close();
    }

    public static List<Address> getAllAddressList() throws IOException {
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var stores = DataProcessor.readStoreCSVtoList("data/Stores.csv");
        List<Address> addressList = new ArrayList<>();
        for (Person person : people) {
            var address = person.getAddress();
            addressList.add(address);

        }
        for (var store : stores) {
            var address = store.getAddress();
            addressList.add(address);
        }
        return addressList;
    }

    public static void getAllPerson(String filePath) throws IOException {
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");

        List<Address> addressList = getAllAddressList();

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("insert into Person (uuid, firstName, lastName, addressId) \nvalues");
        writer.newLine();

        for (Person person : people) {
            var personAddress = person.getAddress();
            var indexAddress = addressList.indexOf(personAddress) + 1;
            var indexListPeople = people.indexOf(person);

            writer.write(String.format("('%s', '%s', '%s', '%d')", person.getUuid(), person.getFirstName(), person.getLastName(), indexAddress));
            if (indexListPeople == people.size() -1){
                writer.write(';');
            }
            else {
                writer.write(',');
                writer.newLine();
            }

        }
        writer.close();
    }

    public static void emailPerson(String filePath) throws IOException {
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var emailList = getEmail();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("insert into Email (address, personId) \nvalues");
        writer.newLine();

        for (Person person : people){
            var personEmailList = person.getEmailList();
            for (String email : emailList){
                if (personEmailList.contains(email)){
                    var indexPerson = people.indexOf(person);
                    var index = people.indexOf(person);
                    writer.write(String.format("('%s', '%d')", email, indexPerson + 1));

                    if (index == people.size() -1){
                        writer.write(';');
                    }
                    else {
                        writer.write(',');
                        writer.newLine();
                    }
                }
            }
        }
        writer.close();
    }

    public static void storePersonAddress(String filePath) throws IOException {
        var peopleList = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var stores = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        List<Address> addressList = getAllAddressList();

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("insert into Store (storeCode, managerId, addressId) \nvalues ");
        writer.newLine();
        for (Store store : stores){
            var storeCode = store.getStoreCode();
            var addressIndex = addressList.indexOf(store.getAddress()) + 1;
            var managerIndex = peopleList.indexOf(store.getManager()) + 1;
            var index = stores.indexOf(store);
            writer.write(String.format("('%s', '%d', '%d')", storeCode, managerIndex, addressIndex));
            if (index == stores.size() - 1){
                writer.write(';');
            }
            else {
                writer.write(',');
                writer.newLine();
            }
        }

        writer.close();
    }

    public static void itemToSql(String filePath) throws IOException {
        var personMap = DataProcessor.readPersonCSVtoMap("data/Persons.csv");
        var storeMap = DataProcessor.readStoreCSVtoMap("data/Stores.csv");
        var personList = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var salesMap = DataProcessor.readSaleCSVToMap(personMap, storeMap, "data/Sales.csv");
        var itemMap = DataProcessor.readItemsCSVtoMap("data/Items.csv");
        var itemProcessor = DataProcessor.processPurchasedItemsIntoSalesMap(salesMap,itemMap,personMap,"data/SaleItems.csv");

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        assert itemProcessor != null;
        for (Sale sale : itemProcessor.values()){
            var items = sale.getItemsList();
            for (Item item : items){
                var uniqueCode = item.getUniqueCode();
                var name = item.getName();
                var basePrice = item.getBasePrice();
                writer.write("insert into Item ");
                if(item instanceof ProductLease){
                    writer.write("(uniqueCode, type, name, basePrice, startDate, endDate) values ");
                    var startDate = ((ProductLease) item).getStartDate();
                    var endDate = ((ProductLease) item).getEndDate();
                    writer.write(String.format("('%s','L','%s','%.2f', '%s','%s');", uniqueCode, name, basePrice, startDate, endDate));
                    writer.newLine();
                }
                else if(item instanceof ProductPurchase){
                    writer.write("(uniqueCode, type, name, basePrice) values ");
                    writer.write(String.format("('%s','P','%s','%.2f');", uniqueCode, name, basePrice));
                    writer.newLine();
                }
                else if(item instanceof DataPlan){
                    writer.write("(uniqueCode, type, name, basePrice,totalGb) values ");
                    var totalGB = ((DataPlan) item).getTotalGB();
                    writer.write(String.format("('%s','D','%s','%.2f','%.2f');", uniqueCode, name, basePrice, totalGB));
                    writer.newLine();
                }
                else if(item instanceof VoicePlan){
                    writer.write("(uniqueCode, type, name, basePrice, phoneNumber, totalPeriod) values ");
                    var phoneNumber = ((VoicePlan) item).getPhoneNumber();
                    var totalPeriod = ((VoicePlan) item).getTotalPeriod();
                    writer.write(String.format("('%s','V','%s','%.2f', '%s','%.2f');", uniqueCode, name, basePrice, phoneNumber, totalPeriod));
                    writer.newLine();
                }
                else if(item instanceof Service){
                    writer.write("(uniqueCode, type, name, basePrice, totalHours, employeeId) values ");
                    var totalHours = ((Service) item).getTotalHours();
                    var employee = ((Service) item).getEmployee();
                    var employeeIndex = personList.indexOf(employee) + 1;
                    writer.write(String.format("('%s','S','%s','%.2f', '%.2f','%d');", uniqueCode, name, basePrice, totalHours, employeeIndex));
                    writer.newLine();
                }
            }
        }

        writer.close();
    }
}

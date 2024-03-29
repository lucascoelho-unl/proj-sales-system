/*
 * The CsvToSql class provides methods for converting CSV data to SQL statements.
 */
package unl.soc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The CsvToSql class provides methods for converting CSV data to SQL statements.
 */
public class CsvToSql {

    /**
     * Main method that orchestrates the conversion process.
     *
     * @param args Command-line arguments (not used).
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        // Method calls for generating SQL files
        getAllAddressCsv("testOutput/address.txt");
        getAllPerson("testOutput/personAddress.txt");
        emailPerson("testOutput/email.txt");
        storePersonAddress("testOutput/store.txt");
        itemToSql("testOutput/items.txt");
        saleSql("testOutput/sales.txt");
    }

    /**
     * Retrieves a list of email addresses from the CSV file.
     *
     * @return A list of email addresses.
     * @throws IOException If an I/O error occurs.
     */
    public static List<String> getEmail() throws IOException {
        // Read CSV file containing person data
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        List<String> emailsList = new ArrayList<>();

        // Extract email addresses from person data
        for (Person person : people) {
            var emails = person.getEmailList();
            emailsList.addAll(emails);
        }
        return emailsList;
    }

    /**
     * Generates SQL statements for inserting address data into the database.
     *
     * @param filePathOutput The output file path.
     * @throws IOException If an I/O error occurs.
     */
    public static void getAllAddressCsv(String filePathOutput) throws IOException {
        // Read person and store data from CSV files
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var stores = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        // Create a writer for the output file
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOutput));

        // Write SQL statements for inserting address data
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

        // Close the writer
        writer.close();
    }

    /**
     * Retrieves a list of all addresses from the CSV files.
     *
     * @return A list of Address objects.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Address> getAllAddressList() throws IOException {
        // Read person and store data from CSV files
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var stores = DataProcessor.readStoreCSVtoList("data/Stores.csv");
        List<Address> addressList = new ArrayList<>();

        // Add addresses from person data
        for (Person person : people) {
            var address = person.getAddress();
            addressList.add(address);
        }

        // Add addresses from store data
        for (var store : stores) {
            var address = store.getAddress();
            addressList.add(address);
        }

        return addressList;
    }

    /**
     * Generates SQL statements for inserting person data into the database.
     *
     * @param filePath The output file path.
     * @throws IOException If an I/O error occurs.
     */
    public static void getAllPerson(String filePath) throws IOException {
        // Read person data from CSV file
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");

        // Get list of all addresses
        List<Address> addressList = getAllAddressList();

        // Create a writer for the output file
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("insert into Person (uuid, firstName, lastName, addressId) \nvalues");
        writer.newLine();

        // Write SQL statements for inserting person data
        for (Person person : people) {
            var personAddress = person.getAddress();
            var addressId = addressList.indexOf(personAddress) + 1;
            var indexListPeople = people.indexOf(person);

            writer.write(String.format("('%s', '%s', '%s', '%d')", person.getUuid(), person.getFirstName(), person.getLastName(), addressId));
            if (indexListPeople == people.size() -1){
                writer.write(';');
            }
            else {
                writer.write(',');
                writer.newLine();
            }
        }

        // Close the writer
        writer.close();
    }

    /**
     * Generates SQL statements for inserting email data into the database.
     *
     * @param filePath The output file path.
     * @throws IOException If an I/O error occurs.
     */
    public static void emailPerson(String filePath) throws IOException {
        // Read person data from CSV file
        var people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var emailList = getEmail();

        // Create a writer for the output file
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("insert into Email (address, personId) \nvalues");
        writer.newLine();

        // Write SQL statements for inserting email data
        for (Person person : people){
            var personEmailList = person.getEmailList();
            for (String email : emailList){
                if (personEmailList.contains(email)){
                    var personId = people.indexOf(person) + 1;
                    var index = people.indexOf(person);
                    writer.write(String.format("('%s', '%d')", email, personId));

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

        // Close the writer
        writer.close();
    }

    /**
     * Generates SQL statements for inserting store and address data into the database.
     *
     * @param filePath The output file path.
     * @throws IOException If an I/O error occurs.
     */
    public static void storePersonAddress(String filePath) throws IOException {
        // Read person and store data from CSV files
        var peopleList = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var stores = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        // Get list of all addresses
        List<Address> addressList = getAllAddressList();

        // Create a writer for the output file
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("insert into Store (storeCode, managerId, addressId) \nvalues ");
        writer.newLine();

        // Write SQL statements for inserting store and address data
        for (Store store : stores){
            var storeCode = store.getStoreCode();
            var addressId = addressList.indexOf(store.getAddress()) + 1;
            var managerId = peopleList.indexOf(store.getManager()) + 1;
            var index = stores.indexOf(store);
            writer.write(String.format("('%s', '%d', '%d')", storeCode, managerId, addressId));
            if (index == stores.size() - 1){
                writer.write(';');
            }
            else {
                writer.write(',');
                writer.newLine();
            }
        }

        // Close the writer
        writer.close();
    }

    /**
     * Generates SQL statements for inserting item data into the database.
     *
     * @param filePath The output file path.
     * @throws IOException If an I/O error occurs.
     */
    public static void itemToSql(String filePath) throws IOException {
        // Read person data from CSV file
        var personList = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var itemProcessor = DataProcessor.processPurchasedItemsIntoSalesMap("data/SaleItems.csv");

        // Create a writer for the output file
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        // Write SQL statements for inserting item data
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
                    var employeeId = personList.indexOf(employee) + 1;
                    writer.write(String.format("('%s','S','%s','%.2f', '%.2f','%d');", uniqueCode, name, basePrice, totalHours, employeeId));
                    writer.newLine();
                }
            }
        }

        // Close the writer
        writer.close();
    }

    /**
     * Generates SQL statements for inserting sale data into the database.
     *
     * @param filePath The output file path.
     * @throws IOException If an I/O error occurs.
     */
    public static void saleSql(String filePath) throws IOException {
        // Read sale, person, and store data from CSV files
        var saleList = DataProcessor.salesList("data/Sales.csv");
        var personList = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        var storeList = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        // Create a list of store codes
        List<String> storeCodeList = new ArrayList<>();
        for (Store store : storeList){
            storeCodeList.add(store.getStoreCode());
        }

        // Create a writer for the output file
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("insert into Sale (uniqueCode, saleDate, customerId, salesmanId, storeId) \nvalues");
        writer.newLine();

        // Write SQL statements for inserting sale data
        for (Sale sale : saleList){
            var uniqueCode = sale.getUniqueCode();
            var saleDate = sale.getDateTime();
            var customer = sale.getCustomer();
            var customerId = personList.indexOf(customer) + 1;
            var salesman = sale.getSalesman();
            var salesmanId = personList.indexOf(salesman) + 1;
            var storeCode = sale.getStore().getStoreCode();
            var storeId = storeCodeList.indexOf(storeCode) + 1;
            var index = saleList.indexOf(sale);
            writer.write(String.format("('%s', '%s', '%d', '%d', '%d')", uniqueCode,saleDate,customerId,salesmanId,storeId));

            if (index == saleList.size() - 1){
                writer.write(';');
            }
            else {
                writer.write(',');
                writer.newLine();
            }
        }
        writer.close();
    }
}

package com.atm.utilities;

import com.atm.bank.Customer;

import java.sql.*;
import java.util.Scanner;

import static com.atm.runner.Atm_Runner.user_1;
import static com.atm.runner.Atm_Runner.user_2;

public class DbUtils {
    /*
     *****************************************
     * Libraries / Classes
     *****************************************
     */
    ConsoleUtils console = new ConsoleUtils();
    Scanner scanner = new Scanner(System.in);
    Customer customer = new Customer();
    /*
     *****************************************
     * Private Fields / Data
     *****************************************
     */
    private static String connectionChoice = null; //user input for connection choice
    private String connectionDomainName; //user input for domain name
    private String connectionPortNumber; //user input for port number
    private String connectionDatabaseName; //user input for database name
    private String connectionUsername; //user input for username
    private String connectionPassword; //user input for password


    public static Connection connection; //connection object to connect to database

    /*
     *********
     * Getters
     *********
     */
    public String getConnectionChoice() { //get connection choice
        return connectionChoice;
    }

    public String getConnectionDomainName() { //get domain name
        return connectionDomainName;
    }

    public String getConnectionPortNumber() { //get port number
        return connectionPortNumber;
    }

    public String getConnectionDatabaseName() { //get database name
        return connectionDatabaseName;
    }

    public String getConnectionUsername() { //get username
        return connectionUsername;
    }

    public String getConnectionPassword() { //get password
        return connectionPassword;
    }

    /*
     *********
     * Setters
     *********
     */
    public void setConnectionChoice(String connectionChoice) { //set connection choice
        this.connectionChoice = connectionChoice;
    }

    public void setConnectionDomainName(String connectionDomainName) { //set domain name
        this.connectionDomainName = connectionDomainName;
    }

    public void setConnectionPortNumber(String connectionPortNumber) { //set port number
        this.connectionPortNumber = connectionPortNumber;
    }

    public void setConnectionDatabaseName(String connectionDatabaseName) { //set database name
        this.connectionDatabaseName = connectionDatabaseName;
    }

    public void setConnectionUsername(String connectionUsername) { //set username
        this.connectionUsername = connectionUsername;
    }

    public void setConnectionPassword(String connectionPassword) { //set password
        this.connectionPassword = connectionPassword;
    }

    /*
     *****************************************
     * Display intro message for database connection
     *****************************************
     */
    public void connectDatabaseMessage() throws SQLException {
        StringBuilder buildMessage = new StringBuilder();
        buildMessage
                //Introduction message
                .append("A few details are required before proceeding.")
                .append(console.greenBold).append(console.blackBackground)
                .append(" Following and successfully ")
                .append("completing the steps below ").append(console.reset)
                .append(" will\nallow you to complete the database connection process, ")
                .append("and this will also help with providing a smooth\nexperience.\n\n")

                //Note message in red
                .append(console.redBold).append(console.blackBackground)
                .append(" NOTE: ")
                .append(console.reset).append(console.redBold)
                .append(" It is ESSENTIAL that you connect a database to run this programming as smoothly as possible. ")
                .append(console.reset).append("\n\n")

                //How to connect a database message
                //create a hyperlink to GitHub profile readme file shown in console output
                .append("To connect a database successfully, please make sure to see the README file on GitHub for more information: \n")
                .append(console.blackBackground).append(" https://github.com/de-furkan/atmProject_3/#readme ").append(console.reset).append("\n");

        System.out.println(buildMessage);

        System.out.println("Have you read the README file in the GitHub link provided above? (Y/N)");
        String userResponse = scanner.nextLine().replaceAll("[^a-zA-Z]", "").toLowerCase();

        while (!userResponse.equals("y") && !userResponse.equals("n")) {
            System.out.println("Please provide a valid input. Type only Y or N.");
            userResponse = scanner.nextLine().replaceAll("[^a-zA-Z]", "").toLowerCase();
        }

        if (userResponse.equals("y")) {
            //if user has read the instructions, then proceed to the next step
            //Which will provide options for connecting to the database
            //The console output below will create a blank line for better readability
            System.out.println("\n");
            connectToDatabaseOptions();
        } else {
            //if user has not read the instructions, then display the message below
            //and call this method again to display the instructions again
            //Note: connectDatabaseMessage() is recursive
            System.out.println(
                    "-----------------------------------------------------\n" +
                            console.redBackground + console.blackBold + " Please READ the instructions below and try again... " + console.reset + "\n" +
                            "-----------------------------------------------------"
            );
            connectDatabaseMessage();
        }
    }

    /*
     *****************************************
     * Provide options to connect database
     * 1. automatically [admin use only]
     * 2. manually      [external use]
     *****************************************
     */
    public void connectToDatabaseOptions() {

        //initial connectionChoice value is null to ensure the while loop runs
        //while loop will run until user provides a valid input
        //valid inputs are 1 or 2
        String input;
        while (
                getConnectionChoice() == null ||
                        !getConnectionChoice().equals("1") && !getConnectionChoice().equals("2")
        ) {
            String chooseOptionMessage = console.blackBackground + console.purpleBold + " Below, you will be prompted to choose an option for connecting to a database. " + console.reset + "\n\n" +
                    "Note: This is a one time setup to ensure the program runs smoothly.\n" +
                    "Please select option 2 if you do not own this project.\n";

            System.out.println(chooseOptionMessage);

            System.out.println(
                    console.blackBackground + console.whiteBold +
                            " Please choose one of the options below by typing " + console.redBold + " 1 " + console.reset + console.blackBackground + "or" + console.greenBold + " 2 " + console.reset + "\n\n" +
                            "[" + console.redBold + "1" + console.reset + "] Automatic setup - " + console.redBold + "Admin" + console.reset + "\n" +
                            "[" + console.greenBold + "2" + console.reset + "] Manual setup - " + console.greenBold + "External" + console.reset
            );
            input = scanner.nextLine();

            if (input.startsWith("-")) {
                System.out.println(console.redBrightBackground + console.blackBold + " Invalid input, negative numbers are not allowed. Please try again. " + console.reset);
                continue;
            }

            //set the connectionChoice value to the user input
            setConnectionChoice(input.replaceAll("[^1-2]", ""));

            //the if-else clause below is used to display the option chosen by the user in colour-coded format
            if (getConnectionChoice().length() == 1 && getConnectionChoice().equals("1")) {
                System.out.println(console.greenBackground + console.blackBold + " You have chosen option: " + getConnectionChoice() + " " + console.reset + "\n");
                connectToDatabase();
                return;
            } else if (getConnectionChoice().length() == 1 && getConnectionChoice().equals("2")) {
                System.out.println(console.greenBackground + console.blackBold + "You have chosen option: " + getConnectionChoice() + " " + console.reset + "\n");
                setDataForDatabaseConnectionChoiceTwo();
            } else {
                System.out.println(console.redBrightBackground + console.blackBold + " Invalid input. Please try again. " + console.reset);
                setConnectionChoice(null);
            }
        }
    }

    /*
     *****************************************
     * Option 1 connection - Admin
     *****************************************
     */

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/atmProject3",
                    "admin",
                    "root123"
            );
            //Show success message if connection is a success
            System.out.println(
                    "Successfully connected to database:" + console.reset + " " +
                            console.blackBackground + console.greenBold + " " + connection.getCatalog() + " " + console.reset
            );
        } catch (SQLException e) {
            //throw an exception if connection fails
            e.printStackTrace();
        }
    }

    /*
     *****************************************
     * set data for connection option 2
     *****************************************
     */
    public void setDataForDatabaseConnectionChoiceTwo() {
        try {
            System.out.println(
                    "Below you will be prompted to provide valid information to connect to a postgreSQL based database.\n" +
                            "Please double check that the provided details are correct...\n\n"
            );

            //user must provide a valid domain name
            System.out.println("Please provide the domain name of the server you wish to connect to:");
            setConnectionDomainName(scanner.nextLine());

            //user must provide a valid port number
            System.out.println("Please provide the correct port number of this server:");
            setConnectionPortNumber(scanner.nextLine().replaceAll("[^0-9]", ""));

            //user must provide a valid database name
            System.out.println("Please provide the database name to connect to within this server:");
            setConnectionDatabaseName(scanner.nextLine());

            //user must provide a valid username
            System.out.println("Please provide the correct username of this server");
            setConnectionUsername(scanner.nextLine());

            //user must provide a valid password
            System.out.println("Please provide the correct password of this server");
            setConnectionPassword(scanner.nextLine());

            //connect to database using the provided information
            connectToDatabase(
                    getConnectionDomainName(),
                    getConnectionPortNumber(),
                    getConnectionDatabaseName(),
                    getConnectionUsername(),
                    getConnectionPassword()
            );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection == null) {
                System.out.println("Could not connect to database. Please try Again.");
                setDataForDatabaseConnectionChoiceTwo();
            } else {
                System.out.println("Thank-you. The provided data has been accepted.");
            }
        }
    }

    /*
     *****************************************
     * Option 2 connection - External
     *****************************************
     */
    public void connectToDatabase(String domainName, String portNumber, String databaseName, String userName, String password) {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + getConnectionDomainName() + ":" + getConnectionPortNumber() + "/" + getConnectionDatabaseName(),
                    getConnectionUsername(),
                    getConnectionPassword()
            );
            //Show success message if connection is a success
            System.out.println(
                    "Successfully connected to database:" + console.reset + " " +
                     console.blackBackground + console.greenBold + " " + connection.getCatalog() + " " + console.reset
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Create registered_users table
    public void createRegisteredUsersTable() {
        try {
            //Stored existing table name in a variable
            String checkTable = "SELECT to_regclass('public.registered_users')";
            ResultSet rs = connection.createStatement().executeQuery(checkTable);

            //Check if table exists
            if (rs.next() && rs.getString(1) != null) {
                System.out.println(console.redBrightBackground + console.blackBold + " Table already exists. " + console.reset);
            } else {
                //Create table if it does not exist
                StringBuilder createTable = new StringBuilder();
                createTable
                            .append("CREATE TABLE IF NOT EXISTS registered_users").append("(").append("\n")
                            .append("id SERIAL PRIMARY KEY,").append("\n")
                            .append("first_name VARCHAR(150) NOT NULL,").append("\n")
                            .append("last_name VARCHAR(150) NOT NULL,").append("\n")
                            .append("date_of_birth DATE NOT NULL,").append("\n")
                            .append("gender VARCHAR(6) NOT NULL,").append("\n")
                            .append("country_of_birth VARCHAR(50) NOT NULL,").append("\n")
                            .append("address VARCHAR(150) NOT NULL,").append("\n")
                            .append("ssn VARCHAR(11) NOT NULL,").append("\n")
                            .append("phone VARCHAR(15) NOT NULL,").append("\n")
                            .append("username VARCHAR(50) NOT NULL,").append("\n")
                            .append("password VARCHAR(50) NOT NULL,").append("\n")
                            .append("current_amount DECIMAL(10,2) NOT NULL,").append("\n")
                            .append("is_active BOOLEAN NOT NULL,").append("\n")
                            .append("pin INTEGER NOT NULL,").append("\n")
                            .append("card_number VARCHAR(19) NOT NULL,").append("\n")
                            .append("sort_code VARCHAR(8) NOT NULL,").append("\n")
                            .append("account_number VARCHAR(8) NOT NULL,").append("\n")
                            .append("created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP").append("\n")
                            .append(");"
                            );
                //executeUpdate method is used to execute the SQL script
                connection.createStatement().executeUpdate(createTable.toString());

                //Show success message if table is created successfully
                //Also reminds users to double-check their database to ensure the table is visible
                System.out.println(console.greenBrightBackground + console.blackBold + " Table created successfully. " + console.reset);
                System.out.println(console.purpleBold + " » Please check the table is visible in your database... " + console.reset + "\n");

                //Close ResultSet and Statement objects
                rs.close();
                connection.createStatement().close();
            }

            } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     *****************************************
     * Get saved option for connection
     *****************************************
     */
    public void getSavedOption() {
        //"jdbc:postgresql://domain:port/databaseName",
        if (getConnectionChoice().equals("1")) {
            connectToDatabase();
        } else{
            connectToDatabase(
                    getConnectionDomainName(),
                    getConnectionPortNumber(),
                    getConnectionDatabaseName(),
                    getConnectionUsername(),
                    getConnectionPassword()
            );
        }
    }

    public static void main(String[] args) {
        DbUtils dbUtils = new DbUtils();
        dbUtils.connectToDatabaseOptions();
        dbUtils.getSavedOption();
        dbUtils.closeConnectionToDatabase();
    }

    public void registerCustomer() {
        //first check the saved connection option
        // and connect to the database
        getSavedOption();

        //sql prepared statement to add user to database
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO registered_users ")

                .append("(").append("first_name, ")
                .append("last_name, ").append("date_of_birth, ")
                .append("gender, ").append("country_of_birth, ")
                .append("address, ").append("ssn, ")
                .append("phone, ").append("username, ")
                .append("password, ").append("current_amount, ")
                .append("is_active, ").append("pin, ")
                .append("card_number, ").append("sort_code, ")
                .append("account_number").append(") ")
                .append("VALUES ")
                .append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

        //Create prepared statement object
        //try-with-resources is used to automatically close the prepared statement after execution
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            preparedStatement.setString(1, user_1.getFirstName());
            preparedStatement.setString(2, user_1.getLastName());
            preparedStatement.setDate(3, user_1.getDateOfBirth());
            preparedStatement.setString(4, user_1.getGender());
            preparedStatement.setString(5, user_1.getCountryOfBirth());
            preparedStatement.setString(6, user_1.getAddress());
            preparedStatement.setString(7, user_1.getSocialSecurityNumber());
            preparedStatement.setString(8, user_1.getPhone());
            preparedStatement.setString(9, user_1.getUsername());
            preparedStatement.setString(10, user_1.getPassword());
            preparedStatement.setDouble(11, user_1.getCurrentAmount());
            preparedStatement.setBoolean(12, user_1.isActive());
            preparedStatement.setInt(13, user_1.getPin());
            preparedStatement.setString(14, user_1.getCardNumber());
            preparedStatement.setString(15, user_1.getSortCode());
            preparedStatement.setString(16, user_1.getAccountNumber());

            //execute the prepared statement
            preparedStatement.executeUpdate();
            //Show success message if customer is added to database
            System.out.println(
                    console.greenBrightBackground +
                    console.blackBold +
                    " Customer successfully added to database... " + console.reset
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database connection
            closeConnectionToDatabase();
        }
    }

    public void registerDummyData() {
        //first check the saved connection option
        // and connect to the database
        getSavedOption();

        //sql prepared statement to add user to database
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO registered_users ")

                .append("(").append("first_name, ")
                .append("last_name, ").append("date_of_birth, ")
                .append("gender, ").append("country_of_birth, ")
                .append("address, ").append("ssn, ")
                .append("phone, ").append("username, ")
                .append("password, ").append("current_amount, ")
                .append("is_active, ").append("pin, ")
                .append("card_number, ").append("sort_code, ")
                .append("account_number").append(") ")
                .append("VALUES ")
                .append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

        //Create prepared statement object
        //try-with-resources is used to automatically close the prepared statement after execution
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            preparedStatement.setString(1, user_2.getFirstName());
            preparedStatement.setString(2, user_2.getLastName());
            preparedStatement.setDate(3, user_2.getDateOfBirth());
            preparedStatement.setString(4, user_2.getGender());
            preparedStatement.setString(5, user_2.getCountryOfBirth());
            preparedStatement.setString(6, user_2.getAddress());
            preparedStatement.setString(7, user_2.getSocialSecurityNumber());
            preparedStatement.setString(8, user_2.getPhone());
            preparedStatement.setString(9, user_2.getUsername());
            preparedStatement.setString(10, user_2.getPassword());
            preparedStatement.setDouble(11, user_2.getCurrentAmount());
            preparedStatement.setBoolean(12, user_2.isActive());
            preparedStatement.setInt(13, user_2.getPin());
            preparedStatement.setString(14, user_2.getCardNumber());
            preparedStatement.setString(15, user_2.getSortCode());
            preparedStatement.setString(16, user_2.getAccountNumber());

            //execute the prepared statement
            preparedStatement.executeUpdate();
            //Show success message if customer is added to database
            System.out.println(
                    console.greenBrightBackground +
                            console.blackBold +
                            " Customer successfully added to database... " + console.reset
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database connection
            closeConnectionToDatabase();
        }
    }

    /*
     *****************************************
     * Close the database connection
     *****************************************
     */
    public void closeConnectionToDatabase() {
        //closes the connection to database
        try {
            connection.close();
            System.out.println("Connection to database " + console.purpleBold + console.blackBackground + " closed " + console.reset + " successfully.");
        } catch (SQLException e) {
            //throw an exception if connection fails
            e.printStackTrace();
        } catch (NullPointerException e) {
            //throw an exception if connection is null
            //and prints a message to let user know of the error
            System.out.println(console.redBold + "Cannot close the database connection because there is no connection to close..." + console.reset);
        }
    }

    /*
     *****************************************
     * Display Verification Success Message
     *****************************************
     */

    //Display the verification success message once the card is accepted
    //This method MUST be used within the authenticateCardholder() method
    public void verificationSuccessMessage(String accountNumber, int pin) {
        if (accountNumber.length() == 8 && pin != 0) {
            System.out.println("Cardholder verification and authentication successful...");
        }
    }

    /*
     *****************************************
     * Authenticate Cardholder
     *****************************************
     */
    public void authenticateCardholder(String accountNumber, int pin) {
        getSavedOption();
        //Authenticate cardholder
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM registered_users WHERE account_number = ? AND pin = ?");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setInt(2, pin);

            ResultSet rs = preparedStatement.executeQuery();
            String firstName = null;
            String lastName = null;

            while (rs.next()) {
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
            }

            System.out.println(
                    console.redBrightBackground + console.blackBold +
                    " Welcome " + firstName + " " + lastName + " " +
                    console.reset
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnectionToDatabase();
        }
    }

    /*
     *****************************************
     * Change user isActive to true
     *****************************************
     */
    public void changeIsActive(String accountNumber, int pin) {
        getSavedOption();

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE registered_users SET is_active = ? WHERE account_number = ? AND pin = ?");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, accountNumber);
            preparedStatement.setInt(3, pin);

            preparedStatement.executeUpdate();

            System.out.println(
                    console.greenBackground + console.blackBold +
                    " Changed user active from false to true " +
                    console.reset
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnectionToDatabase();
        }
    }

}
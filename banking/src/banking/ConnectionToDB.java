package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class ConnectionToDB {

    Scanner scanner = new Scanner(System.in);
    protected static String url = "jdbc:sqlite:C:\\Users\\p_nik\\IdeaProjects\\banking\\bankingDB.db";

    protected Connection connect() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT,\n"
                + "	balance INTEGER default 0\n"
                + ");";

        try (Connection con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String number, String pin) {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

       try (Connection con = this.connect();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
           pstmt.setString(1, number);
           pstmt.setString(2, pin);
           pstmt.executeUpdate();
       } catch (SQLException e) {
           System.out.println(e.getMessage());
       }
    }

    public boolean checkEntry(String number) {
        String sql = "SELECT pin FROM card WHERE number = ?";
        boolean entrySuccessful = false;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,number);

            ResultSet rs = pstmt.executeQuery();
            System.out.println("Enter your PIN:");
            String pin = scanner.next();
            if (pin.matches(rs.getString("pin"))) {
                entrySuccessful = true;
                return entrySuccessful;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Wrong card number or PIN!");
        return entrySuccessful;
    }

    public int getBalance(String number) {
        String sql = "SELECT balance FROM card WHERE number = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Balance: " + rs.getInt("balance"));
            int balance = rs.getInt("balance");
            return balance;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void doIncome(String number) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        System.out.println("Enter income: ");
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(2, number);
            int balance = scanner.nextInt();
            pstmt.setInt(1,balance);
            pstmt.executeUpdate();
            System.out.println("Income was added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doTransfer(String number) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        System.out.println("Enter card number: ");
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String transferNumber = scanner.next();
            String transferNumSplit = transferNumber.replaceAll("\\B", " ");
            int numArr[] = Arrays.stream(transferNumSplit.split(" ")).mapToInt(Integer::parseInt).toArray();
            if (luhnAlgoritmCheck(numArr)) {

                pstmt.setString(2, transferNumber);
                System.out.println("Enter how much money you want to transfer: ");
                int sumOfTransfer = scanner.nextInt();
                if (sumOfTransfer <= getBalance(number)) {
                    pstmt.setInt(1, sumOfTransfer);
                    pstmt.executeUpdate();
                    String sql2 = "UPDATE card SET balance = balance - ? WHERE number = ?";
                    try (Connection con = this.connect();
                         PreparedStatement pstmt2 = con.prepareStatement(sql2)) {
                        pstmt2.setString(2, number);
                        pstmt2.setInt(1, sumOfTransfer);
                        pstmt2.executeUpdate();
                        System.out.println("Success!");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("Not enough money!");
                }
            } else {
                System.out.println("Probably you made mistake in the card number. Please try again!");
            }
        } catch (SQLException e) {
            System.out.println("Such a card does not exist.");
        }
    }

    public void deleteAccount(String number) {
        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, number);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean luhnAlgoritmCheck(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            if (i == 0 || i % 2 == 0) {
                int bufferNumber = array[i];
                bufferNumber *= 2;
                if (bufferNumber > 9) {
                    bufferNumber -= 9;
                    sum += bufferNumber;
                } else {
                    sum += bufferNumber;
                }
            } else {
                sum += array[i];
            }
        }
        if (sum % 10 == 0) {
            return true;
        }
        return false;
    }

    public void selectAll() {
        String sql = "SELECT id, number, pin, balance FROM card";

        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" + rs.getString("number") + "\t" +
                        rs.getString("pin") + "\t" + rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

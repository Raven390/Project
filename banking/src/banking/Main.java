package banking;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<Account> data = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println();
        ConnectionToDB conn = new ConnectionToDB();
        conn.createTable();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        do{
            printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    createAccount();
                    conn.insert(data.get(data.size() - 1).getCardNumber(), data.get(data.size() - 1).getPIN());
                    break;
                case 2:
                    System.out.println("Enter your card number:");
                    String input = scanner.next();
                    if (conn.checkEntry(input)) {
                        System.out.println("You have successfully logged in!");
                        boolean exitAccountMenu = false;
                        do {
                            printAccountMenu();
                            command = scanner.nextInt();
                            switch (command) {
                                case 1:
                                    conn.getBalance(input);
                                    break;
                                case 2:
                                    conn.doIncome(input);
                                    break;
                                case 3:
                                    conn.doTransfer(input);
                                    break;
                                case 4:
                                    conn.deleteAccount(input);
                                case 5:
                                    System.out.println("You have successfully logged out!");
                                    exitAccountMenu = true;
                                    break;
                                case 0:
                                    System.out.println("Bye!");
                                    exitAccountMenu = true;
                                    exit = true;
                                    break;

                            }
                        } while (!exitAccountMenu);
                    }
                    break;
                case 0:
                    System.out.println("Bye!");
                    exit = true;
        }
        } while (!exit);

    }
    protected static void printMenu() {
        System.out.print("1. Create an account\n2. Log into account\n0. Exit\n");
    }

    protected static void printAccountMenu() {
        System.out.println("1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out\n0. Exit");
    }

    private static void createAccount() {
        data.add(new Account());
        System.out.print("Your card number:" + "\n" + data.get(data.size() - 1).getCardNumber() + "\n");

        System.out.print("Your card PIN:" + "\n" + data.get(data.size() - 1).getPIN() + "\n");
    }
}

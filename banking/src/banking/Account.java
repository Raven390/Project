package banking;
import java.util.Random;

import java.time.*;

public class Account {

    final static String MII = "400000";
    private String cardNumber;
    private String PIN;
    private long balance;
    long currentMilliseconds = System.currentTimeMillis();
    Random rand = new Random(currentMilliseconds);

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder(10);
        int sum = 8;
        for (int i = 1; i < 10; i++) {
            int number = rand.nextInt(9);
            if (i % 2 != 0) {
                int bufferNumber = number;
                bufferNumber *= 2;
                if (bufferNumber > 9) {
                    bufferNumber -= 9;
                    sum += bufferNumber;
                }
                else {
                    sum += bufferNumber;
                }
            } else {
                sum += number;
            }
            sb.append(number);
        }
        if (sum % 10 == 0) {
            sb.append(0);
        } else {
            int checkSum = 10 - sum % 10;
            sb.append(checkSum);
        }
        String generatedCardNumber = MII;
        generatedCardNumber = generatedCardNumber.concat(sb.toString());
        return generatedCardNumber;
    }

    private String generatePIN() {
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(rand.nextInt(9));
        }
        String generatedPIN = sb.toString();
        return generatedPIN;
    }

    public Account() {
        this.cardNumber = generateCardNumber();
        this.PIN = generatePIN();
        this.balance = 0;
    }

    protected String getCardNumber() {
        return cardNumber;
    }

    public String getPIN() {
        return PIN;
    }

    public long getBalance() {
        return balance;
    }
}



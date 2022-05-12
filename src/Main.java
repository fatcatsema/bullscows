import java.util.*;

// Main class
public class Main {

    // main method
    public static void main(String[] args) {

        System.out.println("Please, enter the secret code's length:");
        Scanner scan = new Scanner(System.in);
        // a variable which contains a secret code length as a String
        String inputLineOne = scan.nextLine();
        // a variable which contains a secret code length as an integer
        int inputLength;

        try {
            inputLength = Integer.parseInt(inputLineOne);
            final int maxLength = 36; // a maximum number of available symbols
            // an object to store a code created randomly by the program
            StringBuilder code = new StringBuilder();
            if (inputLength > maxLength) {
                System.out.println("Error: can't generate a secret number with a length of ".concat(inputLineOne)
                        .concat(" because there aren't enough unique symbols."));
            } else if (inputLength <= 0) {
                System.out.println("Error: can't generate a secret number with a length of ".concat(inputLineOne)
                        .concat(" because the size of a secret number cannot be less than one."));
            }
            else {
                System.out.println("Input the number of possible symbols in the code:");
                // a variable which contains a number of possible symbols available to form a code as a String
                String inputLineTwo = scan.nextLine();
                // a variable which contains a number of possible symbols available to form a code as an integer
                int inputSymbols;
                try {
                    inputSymbols = Integer.parseInt(inputLineTwo);
                    if (inputSymbols > maxLength) {
                        System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                    } else {
                        if (inputLength > inputSymbols) {
                            System.out.println("Error: it's not possible to generate a code with a length of "
                                    .concat(inputLineOne).concat(" with ").concat(inputLineTwo)
                                    .concat(" unique symbols."));
                        } else {
                            code.append(generateCode(inputLength, inputSymbols));
                            System.out.println("Okay, let's start a game!");
                            // an object to store a user's code
                            StringBuilder userNumber = new StringBuilder();
                            int turn = 0;
                            // a variable used to pass a final message if a secret code is guessed
                            boolean codeIsGuessed = true;
                            do {
                                turn++;
                                System.out.println("Turn " + turn + ":");
                                userNumber.delete(0, userNumber.length());
                                // a variable to store a user's new turn
                                String newTurn = scan.nextLine();
                                if (checkTheInput(newTurn, inputLength)) {
                                    userNumber.append(newTurn);
                                } else {
                                    codeIsGuessed = false;
                                    break;
                                }
                            } while (compareNumbers(code.toString(), userNumber.toString()) != 0);
                            if (codeIsGuessed) {
                                System.out.println("Congratulations! You guessed the secret code.");
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error: \"".concat(inputLineTwo) + "\"".concat(" isn't a valid number."));
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Error: \"".concat(inputLineOne) + "\"".concat(" isn't a valid number."));
        }
    }


    // method to generate a code randomly by the program
    static StringBuilder generateCode(int codeLength, int possibleSymbols) {

        // all available symbols
        final char[] symbols = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        // an object to store a secret code
        StringBuilder secretNumber = new StringBuilder();
        while (secretNumber.length() < codeLength) {
            // a string to get a new symbol for the code
            String randomNumber = String.valueOf(symbols[(int) (Math.random() * possibleSymbols)]);

            int i = 0;
            while (i < secretNumber.length()) {
                if (randomNumber.charAt(0) != secretNumber.charAt(i)) {
                    i++;
                } else {
                    i = 0;
                    randomNumber = String.valueOf(symbols[(int) (Math.random() * possibleSymbols)]);
                }
            }
            secretNumber.append(randomNumber);
        }

        // an object to form a string from asterisks (symbols of '*')
        StringBuilder asterisks = new StringBuilder();
        int k = 0;
        while (k < codeLength) {
            asterisks.append("*");
            k++;
        }

        // an object to form a message for a user after getting a new secret code created
        StringBuilder symbolsToPrint = new StringBuilder();
        final int numberOfDigits = 10;
        if (possibleSymbols <= numberOfDigits) {
            symbolsToPrint.append(symbols[0]).append("-").append(symbols[possibleSymbols - 1]);
        } else {
            symbolsToPrint.append(symbols[0]).append("-").append(symbols[numberOfDigits - 1]).append(", ")
                    .append(symbols[numberOfDigits]).append("-").append(symbols[possibleSymbols - 1]);
        }

        System.out.println("The secret is prepared: " + asterisks + " (" + symbolsToPrint + ").");
        return secretNumber;
    }


    // method to check if a user's input satisfies the requirements
    static boolean checkTheInput(String line, int size) {

        // all available symbols
        final char[] symbols = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        boolean correct = false;
        // an object to store a new input from a user
        StringBuilder string = new StringBuilder(line);

        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            for (char s : symbols) {
                if (string.charAt(i) == s) {
                    count++;
                    break;
                }
            }
        }

        if (string.length() > size) {
            System.out.println("Error: the number of input symbols more than the size of secret code.");
        } else if (string.length() < size) {
            System.out.println("Error: the number of input symbols less than the size of secret code.");
        } else if (count < string.length()) {
            System.out.println("Error: the input contains invalid symbols. Valid symbols are only: 0-9, a-z.");
        } else {
            correct = true;
        }
        return correct;
    }


    // method to compare a secret code and a user's input
    static int compareNumbers(String code, String userNumber) {

        // an object to store a secret code
        StringBuilder secretNum = new StringBuilder(code);
        // an object to store a user's input
        StringBuilder userNum = new StringBuilder(userNumber);
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < secretNum.length();) {
            if (secretNum.charAt(i) == userNum.charAt(i)) {
                bulls++;
                secretNum.deleteCharAt(i);
                userNum.deleteCharAt(i);
            } else {
                i++;
            }
        }

        for (int i = 0; i < secretNum.length(); i++) {
            for (int j = 0; j < userNum.length();) {
                if (secretNum.charAt(i) == userNum.charAt(j)) {
                    cows++;
                    userNum.deleteCharAt(j);
                } else {
                    j++;
                }
            }
        }

        String b = bulls > 1 ? "bulls" : "bull";
        String c = cows > 1 ? "cows" : "cow";
        String output;

        if (bulls > 0 && cows > 0) {
            output = "Grade: " + bulls + " " + b + " and " + cows + " " + c;
        } else if (bulls > 0 && cows == 0) {
            output = "Grade: " + bulls + " " + b;
        } else if (bulls == 0 && cows > 0) {
            output = "Grade: " + cows + " " + c;
        } else {
            output = "Grade: None.";
        }
        System.out.println(output);
        return secretNum.length();
    }
}

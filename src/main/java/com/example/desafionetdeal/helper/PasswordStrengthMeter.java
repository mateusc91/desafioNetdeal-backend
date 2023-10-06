package com.example.desafionetdeal.helper;

import java.util.HashSet;
import java.util.Set;

public class PasswordStrengthMeter {
    public static int calculatePasswordScore(String password) {
        int length = password.length();
        int uppercaseCount = countUppercaseLetters(password);
        int lowercaseCount = countLowercaseLetters(password);
        int numberCount = countNumbers(password);
        int symbolCount = countSymbols(password);
        int middleNumbersOrSymbols = countMiddleNumbersOrSymbols(password);
        int requirementsCount = calculateRequirementsCount(password);

        int score =
                (length * 4) +
                        ((length - uppercaseCount) * 2) +
                        ((length - lowercaseCount) * 2) +
                        (numberCount * 4) +
                        (symbolCount * 6) +
                        (middleNumbersOrSymbols * 2) +
                        (requirementsCount * 2) -
                        calculateDeductions(password);
        if(score>100){
            score = 100;
        }
        return Math.max(score, 0); // Ensure score is not negative
    }

    private static int countUppercaseLetters(String password) {
        return (int) password.chars().filter(Character::isUpperCase).count();
    }

    private static int countLowercaseLetters(String password) {
        return (int) password.chars().filter(Character::isLowerCase).count();
    }

    private static int countNumbers(String password) {
        return (int) password.chars().filter(Character::isDigit).count();
    }

    private static int countSymbols(String password) {
        String symbols = "!@#$%^&*()-_+=<>?/[]{}|";
        return (int) password.chars().filter(ch -> symbols.indexOf(ch) >= 0).count();
    }

    private static int countMiddleNumbersOrSymbols(String password) {
        String middleChars = password.substring(1, password.length() - 1);
        String symbols = "!@#$%^&*()-_+=<>?/[]{}|"; // Define the symbols you want to consider
        return (int) middleChars.chars().filter(ch -> Character.isDigit(ch) || (symbols.indexOf(ch) >= 0)).count();
    }

    private static int calculateRequirementsCount(String password) {
        int requirementsCount = 0;
        if (password.length() >= 8) {
            requirementsCount++;
        }
        if (countUppercaseLetters(password) > 0) {
            requirementsCount++;
        }
        if (countLowercaseLetters(password) > 0) {
            requirementsCount++;
        }
        if (countNumbers(password) > 0) {
            requirementsCount++;
        }
        if (countSymbols(password) > 0) {
            requirementsCount++;
        }
        return requirementsCount;
    }

    private static int calculateDeductions(String password) {
        int deductions = 0;
        deductions += lettersOnlyDeduction(password);
        deductions += numbersOnlyDeduction(password);
        deductions += repeatCharactersDeduction(password);
        deductions += consecutiveUppercaseLettersDeduction(password);
        deductions += consecutiveLowercaseLettersDeduction(password);
        deductions += consecutiveNumbersDeduction(password);
        deductions += consecutiveSymbolsDeduction(password);
        deductions += sequentialLettersDeduction(password);
        deductions += sequentialNumbersDeduction(password);
        deductions += sequentialSymbolsDeduction(password);
        return deductions;
    }

    private static int lettersOnlyDeduction(String password) {
        return password.matches("^[a-zA-Z]*$") ? password.length() : 0;
    }

    private static int numbersOnlyDeduction(String password) {
        return password.matches("^[0-9]*$") ? password.length() : 0;
    }

    private static int repeatCharactersDeduction(String password) {
        int ded = 0;
        String lowerPassword = password.toLowerCase();
        Set<Character> uniqueChars = new HashSet<>();
        for (int i = 0; i < lowerPassword.length(); i++) {
            char c = lowerPassword.charAt(i);
            if (uniqueChars.contains(c)) {
                ded++;
            } else {
                uniqueChars.add(c);
            }
        }
        return ded;
    }

    private static int consecutiveUppercaseLettersDeduction(String password) {
        int ded = 0;
        for (int i = 0; i < password.length() - 1; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            if (Character.isUpperCase(c1) && Character.isUpperCase(c2)) {
                ded += 2;
            }
        }
        return ded;
    }

    private static int consecutiveLowercaseLettersDeduction(String password) {
        int ded = 0;
        for (int i = 0; i < password.length() - 1; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            if (Character.isLowerCase(c1) && Character.isLowerCase(c2)) {
                ded += 2;
            }
        }
        return ded;
    }

    private static int consecutiveNumbersDeduction(String password) {
        int ded = 0;
        for (int i = 0; i < password.length() - 1; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            if (Character.isDigit(c1) && Character.isDigit(c2)) {
                ded += 2;
            }
        }
        return ded;
    }

    private static int consecutiveSymbolsDeduction(String password) {
        int ded = 0;
        String symbols = "!@#$%^&*()-_+=<>?/[]{}|";
        for (int i = 0; i < password.length() - 1; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            if (symbols.indexOf(c1) >= 0 && symbols.indexOf(c2) >= 0) {
                ded += 2;
            }
        }
        return ded;
    }

    private static int sequentialLettersDeduction(String password) {
        int ded = 0;
        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);
            if (isLetter(c1) && isLetter(c2) && isLetter(c3)) {
                ded += 3;
            }
        }
        return ded;
    }

    private static int sequentialNumbersDeduction(String password) {
        int ded = 0;
        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);
            if (isDigit(c1) && isDigit(c2) && isDigit(c3)) {
                ded += 3;
            }
        }
        return ded;
    }

    private static int sequentialSymbolsDeduction(String password) {
        int ded = 0;
        String symbols = "!@#$%^&*()-_+=<>?/[]{}|";
        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);
            if (symbols.indexOf(c1) >= 0 && symbols.indexOf(c2) >= 0 && symbols.indexOf(c3) >= 0) {
                ded += 3;
            }
        }
        return ded;
    }

    private static boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    private static boolean isDigit(char c) {
        return Character.isDigit(c);

    }
}

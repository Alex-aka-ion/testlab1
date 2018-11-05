package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        outResults("test.txt");

        outResults("numbers.txt");

    }

    private static void outResults(String fileName) {

        System.out.println("Got fileName: " + fileName);

        long lStartTime = System.nanoTime();

        try {
            System.out.println("Minimal natural value: " + (findMinNaturalValue(fileName)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        long lEndTime = System.nanoTime();
        long output = lEndTime - lStartTime;

        System.out.println("Elapsed time in milliseconds: " + output / 1_000_000);

        System.out.println("Memory total used:" + (String.format("%,d",Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));

        System.out.println("----");
    }

    private static int findMinNaturalValue(String fileName) throws IOException {

        File file = new File(fileName);

        char[] tmpCharArray = new char[12];
        boolean[] flags = new boolean[1_000_000];
        char ch;
        int cnt = 0;
        int i = 0;
        int checkInt;

        //Работает быстрее чем чтение через BufferedReader и FileReader
        byte [] fileBytes = Files.readAllBytes(file.toPath());

        for ( byte b: fileBytes ) {
            cnt++;
            ch = (char) b;
            //После последнего числа нет пробела
            if (ch != ' ' && cnt != fileBytes.length) {
                tmpCharArray[i++] = ch;
            } else {
                //создаем число из массива байт
                checkInt = charArrayToInt(tmpCharArray);
                //проверяем есть ли оно
                if (checkInt < 1_000_000 && checkInt > 0) {
                    flags[checkInt] = true;
                }
                //очищаем массив байт
                i = 0;
                Arrays.fill(tmpCharArray,'\u0000');
            }
        }

        for (i = 1; i <= flags.length; i++) {
            if (!flags[i]) {
                break;
            }
        }

        i++;

        return i;
    }

    private static int charArrayToInt(char []data)
    {
        int result = 0;
        int digit;

        for (char aData : data) {
            if (aData == '\u0000') {
                break;
            }
            digit = Character.getNumericValue(aData);
            result *= 10;
            result += digit;
        }
        return result;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author mohammad
 */
public class pasScanner {

    Scanner sc;
    String Buffer = "";
    int limit = 30;

    ArrayList<String> twoDel = new ArrayList<>();
    ArrayList<String> oneDel = new ArrayList<>();

    public pasScanner(String filePath) throws FileNotFoundException {
        sc = new Scanner(new File(filePath));
        twoDel.add("<=");
        twoDel.add(">=");
        twoDel.add("..");
        twoDel.add(":=");
        twoDel.add("<>");

        oneDel.add("/");
        oneDel.add("<");
        oneDel.add(">");
        oneDel.add("=");
        oneDel.add("+");
        oneDel.add("-");
        oneDel.add("*");
        oneDel.add(".");
        oneDel.add(";");
        oneDel.add(",");
        oneDel.add(":");
        oneDel.add("[");
        oneDel.add("]");
        oneDel.add("(");
        oneDel.add(")");
        oneDel.add(" ");
        oneDel.add("\n");

    }

    public String nextToken() {
        if (Buffer.length() < 30 && sc.hasNextLine()) {
            Buffer += sc.nextLine();
        }

        int min = Integer.MAX_VALUE;

        int indexC = Buffer.indexOf("{");

        while (indexC == 0) {
            Buffer = Buffer.substring(1).trim();
            if (Buffer.length() < 30 && sc.hasNextLine()) {
                Buffer += sc.nextLine();
            }
            int index2 = Buffer.indexOf("}");
            if (index2 != -1) {
                Buffer = Buffer.substring(index2 + 1).trim();
                if (Buffer.length() < 30 && sc.hasNextLine()) {
                    Buffer += sc.nextLine();
                }
            }

            indexC = Buffer.indexOf("{");
        }

        Buffer = Buffer.replaceAll("end\\.", "end .");
//        System.out.println(Buffer);
        for (String i : oneDel) {
            int index = Buffer.indexOf(i);

            if (index == 0) {
                for (String j : twoDel) {
                    if (Buffer.charAt(0) == j.charAt(0)) {
                        if (Buffer.length() > 1 && Buffer.charAt(1) == j.charAt(1)) {
                            String temp = Buffer.substring(0, 2);
                            Buffer = Buffer.substring(2).trim();
                            return temp;
                        }
                    }
                }
                if (i.equals(".")) {
                    continue;
                }
                String temp = Buffer.substring(0, 1);
                Buffer = Buffer.substring(1).trim();
                return temp;
            }

            if (i.equals(".")) {
                continue;
            }
            if (index != -1 && index < min) {
                min = index;
            }
        }
        if (min != Integer.MAX_VALUE) {
            String temp = Buffer.substring(0, min);
            Buffer = Buffer.substring(min).trim();
            return temp;
        } else if (Buffer.equals(".")) {
            Buffer = "";
            return ".";
        } else {
            return "";
        }
    }
}
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package compiler;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Scanner;
//
///**
// *
// * @author mohammad
// */
//public class pasScanner {
//
//    Scanner sc;
//
//    String Buffer = "";
//
//    ArrayList<String> twoDel = new ArrayList<>();
//    ArrayList<String> oneDel = new ArrayList<>();
//
//    public pasScanner(String filePath) throws FileNotFoundException {
//        sc = new Scanner(new File(filePath));
//        twoDel.add("<=");
//        twoDel.add(">=");
//        twoDel.add("..");
//        twoDel.add(":=");
//        twoDel.add("<>");
//
//        oneDel.add("<");
//        oneDel.add(">");
//        oneDel.add("=");
//        oneDel.add("+");
//        oneDel.add("-");
//        oneDel.add("*");
//        oneDel.add(".");
//        oneDel.add(";");
//        oneDel.add(",");
//        oneDel.add(":");
//        oneDel.add("[");
//        oneDel.add("]");
//        oneDel.add("(");
//        oneDel.add(")");
//
//    }
////
//
//    public String nextToken() {
//        sc.useDelimiter("");
//        while (Buffer.length() < 2) {
//            String add = sc.next();
//
//            Buffer += add;
//            
//        }
//
//        if (twoDel.contains(Buffer)) {
//            String temp = Buffer;
//            Buffer = "";
////            System.out.println(temp);
//            return temp;
//        }
//        if (oneDel.contains(Buffer.substring(0, 1))) {
//            String temp = Buffer;
//            Buffer = Buffer.substring(1);
//            return temp.substring(0, 1);
//        }
//        if (oneDel.contains(Buffer.substring(1))) {
//            String temp = Buffer;
//            Buffer = Buffer.substring(1);
//            return temp.substring(0, 1);
//        }
//
//        if (sc.hasNext(" |\n|<|>|=|:|;|,|\\+|\\*|\\.|\\[|\\]|\\(|\\)")) {
//            String temp = Buffer;
//            Buffer = "";
//            return temp;
//        } else {
//            sc.useDelimiter(" |\n|<|>|=|:|;|,|\\+|\\*|\\.|\\[|\\]|\\(|\\)");
//            String temp = Buffer + sc.next();
//            Buffer = "";
//            return temp;
//        }
//    }
//}

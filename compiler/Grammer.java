/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author mohammad
 */
public final class Grammer {

    ArrayList<Rule> rules = new ArrayList<>();

    public Grammer(String filePath) throws IOException {
        readFromFile(filePath);

        calcFirst();
        calcFollow();

//        for(NonTerminal i : NonTerminal.getNonTerminals())
//        {
//            System.out.println(i);
//        }
//        for (Rule i : rules) {
//            System.out.println("=======================");
//            System.out.println(i.getLeftSide() + "->" + i.getRightSide());
//            System.out.println("Firsts:" + i.getFirsts());
//            System.out.println("Follow:" + i.getFollows());
//            System.out.println("=======================");
//        }
    }

    public Grammer() {

    }

    public Set<Terminal> getFirsts(ArrayList<Token> B) {
        Set<Terminal> result = new HashSet<>();

        ArrayList<Token> A = new ArrayList<>();
        for (Token rh : B) {
            if (rh.getName().charAt(0) != '#') {
                A.add(rh);
            }
        }

        if (Terminal.isTerminal(A.get(0).getName())) {
            result.add((Terminal) A.get(0));
            return result;
        }

        Set<Terminal> firsts_of_first_element = getFirsts((NonTerminal) A.get(0));
        result.addAll(firsts_of_first_element);

        if (result.contains(Terminal.getTerminal("Epsilon")) && A.size() > 1) {
            ArrayList<Token> the_others = new ArrayList<>(A.subList(1, A.size()));
            result.remove(Terminal.getTerminal("Epsilon"));
            result.addAll(getFirsts(the_others));
        }

        return result;
    }

    public Set<Terminal> getFirsts(NonTerminal A) {
        Set<Terminal> result = new HashSet<>();
        ArrayList<Token> rhs;
        NonTerminal lhs;

        for (Rule rule : rules) {
            lhs = rule.getLeftSide();
            rhs = rule.getRightSide();
            ArrayList<Token> removed = new ArrayList<>();
            if (lhs.getName().equals(A.getName())) {
                for (Token rh : rhs) {
                    if (rh.getName().charAt(0) != '#') {
                        removed.add(rh);
                    }
                }
//                System.out.println(removed);
                result.addAll(getFirsts(removed));
            }
        }
        return result;
    }

    private <T> ArrayList<Integer> getIndexOfAll(ArrayList<T> a, T b) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).equals(b)) {
                ans.add(i);
            }
        }
        return ans;
    }

    public Set<Terminal> getFollows(NonTerminal A) {
        NonTerminal lhs;
        ArrayList<Token> rhs;
        Set<Terminal> result = new HashSet<>();

        for (Rule rule : rules) {
            lhs = rule.getLeftSide();
            rhs = rule.getRightSide();
            ArrayList<Token> removed = new ArrayList<>();
            for (int i = 0; i < rhs.size(); i++) {
                if (rhs.get(i).getName().charAt(0) != '#') {
                    removed.add(rhs.get(i));
                }
            }
            ArrayList<Integer> indexOfAll = getIndexOfAll(removed, A);
            for (int i : indexOfAll) {
                if (removed.size() > i + 1) {
                    ArrayList<Token> rest = new ArrayList<>(removed.subList(i + 1, removed.size()));
                    Set<Terminal> firstsOfRest = getFirsts(rest);
                    if (firstsOfRest.contains(Terminal.getTerminal("Epsilon"))) {
                        firstsOfRest.remove(Terminal.getTerminal("Epsilon"));
                        if (!lhs.equals(A)) {
                            result.addAll(getFollows(lhs));
                        }
                    }
                    result.addAll(firstsOfRest);
                } else {
                    if (!lhs.equals(A)) {
                        result.addAll(getFollows(lhs));
                    }
                }
            }
        }

        return result;

    }

    public void calcFirst() {
        for (NonTerminal i : NonTerminal.getNonTerminals()) {
            i.setFirsts(getFirsts(i));
        }

    }

    public void calcFollow() {
        for (NonTerminal i : NonTerminal.getNonTerminals()) {
            i.setFollows(getFollows(i));
        }
    }

    private void readFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int number_of_ids;
        int number_of_descs;
        int number_of_rules;
        String[] tokenized;

        String line = reader.readLine();;

        number_of_ids = Integer.parseInt(line);
        for (int counter = 0; counter < number_of_ids; counter++) {
            line = reader.readLine();
            tokenized = line.split("@");
            if (tokenized.length > 1) {
                Terminal.addTerminal(tokenized[0], tokenized[1]);
            } else {
                Terminal.addTerminal(tokenized[0], "");
            }
        }

        line = reader.readLine();

        //we will read grammars rules and save them
        number_of_rules = Integer.parseInt(line);
        for (int counter = 0; counter < number_of_rules; counter++) {
//            System.out.println(counter);
            line = reader.readLine();
            rules.add(new Rule(line));
        }

        line = reader.readLine();

        number_of_descs = Integer.parseInt(line);

        for (int counter = 0; counter < number_of_descs; counter++) {
            line = reader.readLine();
            tokenized = line.split("->");

            tokenized[0] = tokenized[0].trim();
            tokenized[1] = tokenized[1].trim();

            NonTerminal.getNonTerminal(tokenized[0]).setDesc(tokenized[1]);
        }

    }

}

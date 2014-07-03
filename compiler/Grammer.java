/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mohammad
 */
public class Grammer {

    ArrayList<Rule> rules = new ArrayList<>();

    public Grammer(String filePath) throws IOException {
        readFromFile(filePath);
        calcFirst();
        calcFollow();
        for (NonTerminal i : NonTerminal.getNonTerminals()){
            System.out.println("=======================");
            System.out.println(i);
            System.out.println("Firsts:" + i.getFirsts());
            System.out.println("Follow:" + i.getFollows());
            System.out.println("=======================");
        }
    }

    public Grammer() {

    }

    public void parseFile(String filePath) {

    }

    public Set<Terminal> getFirsts(NonTerminal A) {
        Set<Terminal> result = new HashSet<>();
        ArrayList<Token> rhs;
        NonTerminal lhs;

        for (int counter = 0; counter < rules.size(); counter++) {
            lhs = rules.get(counter).getLeftSide();
            rhs = rules.get(counter).getRightSide();

            if (lhs.getName().equals(A.getName())) {
                for (int i = rhs.size() - 1; i > -1; i--) {
                    if (rhs.get(i).getName().charAt(0) == '#') {
                        rhs.remove(i);
                    }
                }
                result.addAll(getFirsts(rhs));
            }
        }
        return result;
    }

    public Set<Terminal> getFirsts(ArrayList<Token> A) {
        Set<Terminal> result = new HashSet<>();
        if (Terminal.isTerminal(A.get(0).getName())) {
            result.add((Terminal) A.get(0));
            return result;
        }
        Set<Terminal> firsts_of_first_element = getFirsts((NonTerminal) A.get(0));
        result.addAll(firsts_of_first_element);

        if (result.contains(Terminal.getTerminal("Epsilon")) && A.size() > 1) {
            ArrayList<Token> the_others = new ArrayList<>( A.subList(1, A.size() ));
            result.remove(Terminal.getTerminal("Epsilon"));
            result.addAll(getFirsts(the_others));
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

        for (int counter = 0; counter < rules.size(); counter++) {
            lhs = rules.get(counter).getLeftSide();
            rhs = rules.get(counter).getRightSide();

            for (int i = rhs.size() - 1; i > -1; i--) {
                if (rhs.get(i).getName().charAt(0) == '#') {
                    rhs.remove(i);
                }
            }

            ArrayList<Integer> indexOfAll = getIndexOfAll(rhs, A);

            for (int i : indexOfAll) {
                if (rhs.size() > i + 1) {
                    ArrayList<Token> rest = new ArrayList<>(rhs.subList(i + 1, rhs.size() )) ;
                    Set<Terminal> firstsOfRest = getFirsts(rest);
                    if (firstsOfRest.contains(Terminal.getTerminal("Epsilon"))) {
                        firstsOfRest.remove(Terminal.getTerminal("Epsilon"));
                        if ( !lhs.equals(A) )
                            result.addAll(getFollows(lhs));
                    }
                    result.addAll(firstsOfRest);
                } else {
                    if ( !lhs.equals(A) )
                        result.addAll(getFollows(lhs));
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
        int number_of_rules;
        String[] tokenized;

        String line = null;

        line = reader.readLine();

        //we will read the used IDs and their Regex
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
            line = reader.readLine();
            rules.add(new Rule(line));

        }

    }

}

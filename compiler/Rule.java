/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.util.ArrayList;

/**
 *
 * @author mohammad
 */
public class Rule {

    private NonTerminal leftSide;
    private ArrayList<Token> rightSide;
    private String temp;

    public Rule(String rule) {
        this.rightSide = new ArrayList<>();
        parseRule(rule);
    }

    private void parseRule(String rule) {
        String[] tokenized = rule.split(":=");
        leftSide = NonTerminal.getNonTerminal(tokenized[0]);
        temp = tokenized[1];
        tokenized = temp.split(" ");

        for (int counter = 0; counter < tokenized.length; counter++) {
            if (Terminal.isTerminal(tokenized[counter])) {
                rightSide.add(Terminal.getTerminal(tokenized[counter]));
            } else {
                if (tokenized[counter].charAt(0) != '#') {
                    rightSide.add(NonTerminal.getNonTerminal(tokenized[counter]));
                } else {
                    rightSide.add(Procedure.getProcedure(tokenized[counter]));
                }
            }
        }

    }

    public NonTerminal getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(NonTerminal leftSide) {
        this.leftSide = leftSide;
    }

    public ArrayList<Token> getRightSide() {
        return rightSide;
    }

    public void setRightSide(ArrayList<Token> rightSide) {
        this.rightSide = rightSide;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mohammad
 */
public class NonTerminal extends Token {

    private static ArrayList<NonTerminal> nonTerminals = new ArrayList<>();
    private Set<Terminal> firsts = new HashSet<>();
    private Set<Terminal> follows = new HashSet<>();
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
    public Set<Terminal> getFirsts() {
        return firsts;
    }

    public void setFirsts(Set<Terminal> firsts) {
        this.firsts = firsts;
    }

    public Set<Terminal> getFollows() {
        return follows;
    }

    public void setFollows(Set<Terminal> follows) {
        this.follows = follows;
    }

    public static NonTerminal getNonTerminal(String name) {
        if (nonTerminals.contains(new NonTerminal(name))) {
            return nonTerminals.get(nonTerminals.indexOf(new NonTerminal(name)));
        } else {
            NonTerminal newOne = new NonTerminal(name);
            nonTerminals.add(newOne);
            return newOne;
        }
    }

    public static ArrayList<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public static void setNonTerminals(ArrayList<NonTerminal> nonTerminals) {
        NonTerminal.nonTerminals = nonTerminals;
    }

    private NonTerminal(String name) {
        super(name);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mohammad
 */
public class Parser {

    pasScanner sc;
    String lookAhead = "";
    CodeGenerator pc = new CodeGenerator();

    Grammer gr;

    public void dumpResult()
    {
        pc.dumpProgramBlock();
        pc.dumpSymbolTable();
    }
    
    public Parser(Grammer g) {
        this.gr = g;
    }

    public String getNextToken()
    {
        String lookahead = sc.nextToken();
//        System.out.println("next Lookahead :" + lookahead);
        return lookahead;
    }
    
    public void matchTerminal(Terminal ter) {
        if (ter.getName().equals("Epsilon")) {
            return;
        }
        if (!lookAhead.matches(ter.getRegex())) {
            System.err.println("input wrong expected : " + ter.getName() + " get : " + lookAhead + " ,insert it");
        } else {
            lookAhead = getNextToken();
        }
    }

    public void match(ArrayList<Token> rhs) {
        outer:
        for (Token i : rhs) {
//            System.out.println("i:" + i);
            if (i.getClass().getName().equals("compiler.Terminal")) {
                matchTerminal((Terminal) i);
            } else if (i.getClass().getName().equals("compiler.NonTerminal")) {
                NonTerminal iNonTerminal = (NonTerminal) i;
                ArrayList<Integer> lhses = getIndexOfRulesWithLeftHandSide(gr.rules, iNonTerminal);
                for (Integer j : lhses) {
                    if (gr.getFirsts(gr.rules.get(j).getRightSide()).contains(Terminal.getTerminalbyString(lookAhead))) {
//                        System.out.println("using rule:" + gr.rules.get(j).getRightSide());
                        match(gr.rules.get(j).getRightSide());
                        continue outer;
                    }
                }

                /// no rule with input in first
                if (iNonTerminal.getFirsts().contains(Terminal.getTerminal("Epsilon"))) {
                    if (iNonTerminal.getFollows().contains(Terminal.getTerminalbyString(lookAhead))) {
                        ArrayList<Integer> indexOfRulesWithLeftHandSide = getIndexOfRulesWithLeftHandSide(gr.rules, iNonTerminal);
                        for (Integer j : indexOfRulesWithLeftHandSide) {
                            if (gr.getFirsts(gr.rules.get(j).getRightSide()).contains(Terminal.getTerminal("Epsilon"))) {
                                match(gr.rules.get(j).getRightSide());
                                continue outer;
                            }
                        }
                    }
                }
                
                if (iNonTerminal.getFollows().contains(Terminal.getTerminalbyString(lookAhead))) { 
                    System.err.println("missing " + iNonTerminal.getDesc() + " ,insert it");                    
                } else {
                    System.err.println("misplaced " + lookAhead + " I'm Skipping it!"); 
                    lookAhead=getNextToken();
                }
                

            } else if (i.getClass().getName().equals("compiler.Procedure")) {
                runProcedure(i.getName());
            }
        }
    }

    private void runProcedure(String name) {
        try {
            pc.getClass().getMethod(name.substring(1), String.class).invoke(pc, lookAhead);
        } catch (NoSuchMethodException ex) {
//            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
//            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
//            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
//            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
//        pc.dumpSymbolTable();
//        pc.dumpStack();
//        pc.dumpProgramBlock();

    }

    public void parseFile(String filePath) throws FileNotFoundException {
        sc = new pasScanner(filePath);
        lookAhead = sc.nextToken();
        ArrayList<Token> first = gr.rules.get(0).getRightSide();
        match(first);
    }

    private ArrayList<Integer> getIndexOfRulesWithLeftHandSide(ArrayList<Rule> a, NonTerminal b) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getLeftSide().equals(b)) {
                ans.add(i);
            }
        }
        return ans;
    }
}

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

    public Parser(Grammer g) {
        this.gr = g;
    }

    public void matchTerminal(Terminal ter) {
        if ( ter.getName().equals("Epsilon") )
        {
            System.out.println("match " + ter.getName());
            return;
        }
        if (!lookAhead.matches(ter.getRegex())) {

            System.out.println("input wrong expected : " + ter.getName() + " get : " + lookAhead);
        } else {
            System.out.println("match " + ter.getName());
        }

        lookAhead = sc.nextToken();
        System.out.println("next Lookahead :" + lookAhead);
    }

    public void match(ArrayList<Token> rhs) {
        outer:
        for (Token i : rhs) {
            System.out.println("i:" + i);
            if (i.getClass().getName().equals("compiler.Terminal")) {
                matchTerminal((Terminal) i);
            } else if (i.getClass().getName().equals("compiler.NonTerminal")) {
                NonTerminal iNonTerminal = (NonTerminal) i;
                ArrayList<Integer> lhses = getIndexOfRulesWithLeftHandSide(gr.rules, iNonTerminal);
                boolean findAny = false;
                for (Integer j : lhses) {
                    if (gr.getFirsts(gr.rules.get(j).getRightSide()).contains(Terminal.getTerminalbyString(lookAhead))) {
                        findAny = true;
                        System.out.println("using rule:" + gr.rules.get(j).getRightSide());
                        match(gr.rules.get(j).getRightSide());
                        continue outer;
                    }
                }
                if (!findAny) {
                    if (iNonTerminal.getFirsts().contains(Terminal.getTerminal("Epsilon"))) {
                        if (!iNonTerminal.getFollows().contains(Terminal.getTerminalbyString(lookAhead))) {
                            System.err.println("lookeahead not match follow");
                        } else {
                            ArrayList<Integer> indexOfRulesWithLeftHandSide = getIndexOfRulesWithLeftHandSide(gr.rules, iNonTerminal);
                            for(Integer j : indexOfRulesWithLeftHandSide){
//                                System.out.println(gr.getFirsts(gr.rules.get(j).getRightSide()));
                                if ( gr.getFirsts(gr.rules.get(j).getRightSide()).contains(Terminal.getTerminal("Epsilon")) )
                                {
                                    System.out.println("using epsilon rule : " + gr.rules.get(j).getRightSide());
                                    match(gr.rules.get(j).getRightSide());
                                }
                            }
                        }
                    } else {
                        System.out.println("this rule not contains epsilon");
                    }
                }
            } else if (i.getClass().getName().equals("compiler.Procedure")) {
                runProcedure(i.getName());
//                System.err.println(i.getName());
            }
        }
    }

    private void runProcedure(String name) {
        try {
            pc.getClass().getMethod(name.substring(1), String.class).invoke(pc, lookAhead);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        pc.dumpSymbolTable();
        pc.dumpStack();
        pc.dumpProgramBlock();

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

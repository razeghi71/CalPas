/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

/**
 *
 * @author mohammad
 */
public class Rule {
    private NonTerminal leftSide;
    private Token[] RightSide;
    
    public Rule(String rule)
    {
        parseRule(rule);
    }
    
    private void parseRule(String rule)
    {
        
    }
}

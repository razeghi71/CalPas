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
public class NonTerminal extends Token{
	
	private static ArrayList<NonTerminal> nonTerminals = new ArrayList<>();
	Set<Terminal> firsts = new HashSet<>();
	Set<Terminal> follows = new HashSet<>();
	
	public static  NonTerminal getNonTerminal ( String name )
	{
		if ( nonTerminals.contains(name) )
		{
			return nonTerminals.get(nonTerminals.indexOf(new NonTerminal(name))); 
		} else
		{
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

	private NonTerminal(String name)
    {
        super(name);
    }
	

	
}

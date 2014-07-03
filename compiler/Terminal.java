/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mohammad
 */
public class Terminal extends Token{
    String regex;
	private static ArrayList<Terminal> terminals = new ArrayList<>();
	
    private Terminal(String name, String regex)
    {
        super(name);
    	this.regex = regex;
    }
    
    private Terminal (String name)
    {
    	super(name);
    }
    
	public static  Terminal addTerminal ( String name, String regex)
	{
		if ( terminals.contains(name) )
		{
			return terminals.get(terminals.indexOf(new Terminal(name))); 
		} else
		{
			Terminal newOne = new Terminal(name,regex);
			terminals.add(newOne);
			return newOne;
		}
	}
	
	public static Terminal getTerminal(String name)
	{
		return terminals.get(terminals.indexOf(new Terminal(name)));
	}
	
	
    
    
    public static ArrayList<Terminal> getTerminals() {
		return terminals;
	}

	public static void setTerminals(ArrayList<Terminal> terminals) {
		Terminal.terminals = terminals;
	}

	public static boolean isTerminal(String name)
    {
    	return terminals.contains(new Terminal(name));
    }
    
    public static String getRegex(String name)
    {
    	return terminals.get(terminals.indexOf(new Terminal(name))).regex;
    }
    
}

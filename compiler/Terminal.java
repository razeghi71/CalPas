/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mohammad
 */
public class Terminal extends Token{
    static Map<String,String> nameToRegex = new HashMap<>();
    
    
    public Terminal(String name)
    {
        super(name);
    }
    
    public static String getRegexByName(String name)
    {
        return nameToRegex.get(name);
    }
    
    public static void setRegexByName(String name, String regex )
    {
        nameToRegex.put(name, regex);
    }
    
}

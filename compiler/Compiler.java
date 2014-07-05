/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author mohammad
 */
public class Compiler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            Parser parse = new Parser(new Grammer("src/input_grammer.txt"));
            parse.parseFile("a.pascal");
            parse.dumpResult();
        } catch (FileNotFoundException ex) {
            System.err.println("Input File Not Found");
        } catch (IOException ex) {
            System.err.println("Grammer File Not Found");
        }
        
    }

}

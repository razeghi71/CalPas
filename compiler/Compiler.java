/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            new Grammer("src/input_grammer.txt").parseFile("a.pascal");
        } catch (FileNotFoundException ex) {
            System.err.println("Input File Not Found");
        } catch (IOException ex) {
            System.err.println("Grammer File Not Found");
        }
        
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

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
            new Grammer("src/input_grammer.txt").parseFile("a.pascal");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("error");
        }
    }

}

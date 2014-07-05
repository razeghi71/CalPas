/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.util.ArrayList;

/**
 *
 * @author mohammad
 */
public class Procedure extends Token {

    private static ArrayList<Procedure> procedures = new ArrayList<>();

    public static Procedure getProcedure(String name) {
        if (procedures.contains(new Procedure(name))) {
            return procedures.get(procedures.indexOf(new Procedure(name)));
        } else {
            Procedure newOne = new Procedure(name);
            procedures.add(newOne);
            return newOne;
        }
    }

    public static ArrayList<Procedure> getProcedures() {
        return procedures;
    }

    public static void setProcedures(ArrayList<Procedure> procedures) {
        Procedure.procedures = procedures;
    }

    private Procedure(String name) {
        super(name);
    }

}

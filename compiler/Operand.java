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
public class Operand {
    Type type;

    @Override
    public String toString() {
        return "Operand{" + "type=" + type + ", value=" + value + '}';
    }
    int value;

    public Operand(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
}

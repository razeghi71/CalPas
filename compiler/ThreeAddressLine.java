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
public class ThreeAddressLine {
    Operator operator;

    @Override
    public String toString() {
        return "ThreeAddressLine{" + "operator=" + operator + ", op1=" + op1 + ", op2=" + op2 + ", op3=" + op3 + '}';
    }

    
    Operand op1;
    Operand op2;
    Operand op3;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operand getOp1() {
        return op1;
    }

    public void setOp1(Operand op1) {
        this.op1 = op1;
    }

    public Operand getOp2() {
        return op2;
    }

    public void setOp2(Operand op2) {
        this.op2 = op2;
    }

    public Operand getOp3() {
        return op3;
    }

    public void setOp3(Operand op3) {
        this.op3 = op3;
    }
    
    
}

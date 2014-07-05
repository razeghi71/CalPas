/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author mohammad
 */
public class CodeGenerator {

    Stack<String> ss = new Stack();
    ArrayList<ThreeAddressLine> PB = new ArrayList<>();
    ArrayList<SymbolTableLine> ST = new ArrayList<>();
    int tempCounter = 500;
    int curMemory = 0;

    public void dumpSymbolTable() {
        System.out.println("===Start Table===");
        for (SymbolTableLine stl : ST) {
            if (!stl.getLexme().equals("$main$")) {
                System.out.println(stl);
            }
        }
        System.out.println("===End Table===");
    }

    public void dumpStack() {
        System.out.println("*** Start Stack ***");
        for (Iterator it = ss.iterator(); it.hasNext();) {
            String i = (String) it.next();
            System.out.println(i);

        }
        System.out.println("*** End Stack ***");
    }

    public void dumpProgramBlock() {
        System.out.println("### Start PB ###");
        int j = 0;
        for (ThreeAddressLine i : PB) {
            System.out.println(j++ + " : " + i);
        }
        System.out.println("### End PB ###");
    }

    public void add_program_st(String lookahead) {
        SymbolTableLine stl = new SymbolTableLine();
        stl.setType("Program");
        stl.setDimension(0);
        stl.setLexme(lookahead);
        stl.setPBAddress(PB.size());
        stl.isSep(true);
        stl.setMemoryAddress(0);
        stl.setNrOfCells(0);
        ST.add(stl);
    }

    public void push_start_id_stack(String lookahead) {
        ss.push("PSIS");
    }

    public void add_ids_to_st(String lookahead) {
        String type = (String) ss.pop();
        String varType = (String) ss.pop();

        if (type.equals("single")) {
            String name;
            outer:
            while (!(name = (String) ss.pop()).equals("PSIS")) {
                for (int i = ST.size() - 1; i != 0; i--) {
                    if (ST.get(i).isSep()) {
                        break;
                    } else if (ST.get(i).getLexme().equals(name)) {
                        System.err.println("Variable " + name + " is already defined.");
                        continue outer;
                    }
                }
                SymbolTableLine stl = new SymbolTableLine();
                stl.setLexme(name);
                stl.setType(varType);
                stl.setMemoryAddress(curMemory);
                stl.setNrOfCells(1);
                if (varType.equals("real")) {
                    curMemory += 2;
                    stl.setDimension(1);
                } else if (varType.equals("integer")) {
                    curMemory += 1;
                    stl.setDimension(0);
                }
                ST.add(stl);

            }
        } else if (type.equals("array")) {
            String tmp;
            int mult = 1;
            int begin = 0;
            while (!(tmp = (String) ss.pop()).equals("PSD")) {
                int first = Integer.parseInt((String) ss.pop());
                mult *= (Integer.parseInt(tmp) - first) + 1;
                begin = first;
            }
            String name;
            outer:
            while (!(name = (String) ss.pop()).equals("PSIS")) {
                for (int i = ST.size() - 1; i != 0; i--) {
                    if (ST.get(i).isSep()) {
                        break;
                    } else if (ST.get(i).getLexme().equals(name)) {
                        System.out.println("Variable " + name + " is already defined.");
                        continue outer;
                    }
                }
                SymbolTableLine stl = new SymbolTableLine();
                stl.setLexme(name);
                stl.setType(varType);
                stl.setMemoryAddress(curMemory);
                stl.setNrOfCells(mult);
                stl.setArrayStartIndex(begin);
                if (varType.equals("real")) {
                    curMemory += 2 * mult;
                    stl.setDimension(1);
                } else if (varType.equals("integer")) {
                    curMemory += mult;
                    stl.setDimension(0);
                }
                ST.add(stl);
            }

        }

    }

    public void push_id(String lookahead) {
        ss.push(lookahead);
    }

    public void push_single(String lookahead) {
        ss.push("single");
    }

    public void push_array(String lookahead) {
        ss.push("array");
    }

    public void push_start_dim(String lookahead) {
        ss.push("PSD");
    }

    public void add_function_st(String lookahead) {
        SymbolTableLine stl = new SymbolTableLine();
        ss.push(lookahead);
        stl.setDimension(0);
        stl.setLexme(lookahead);
        stl.setType("");
        stl.isSep(true);
        stl.setPBAddress(PB.size());
        ST.add(stl);
    }

    public void add_function_ret_st(String lookahead) {
        String type = ss.pop();
        String functionId = ss.peek();

        for (SymbolTableLine i : ST) {
            if (i.getLexme().equals(functionId)) {
                i.setMemoryAddress(curMemory);
                if (type.equals("real")) {
                    curMemory += 2;
                    i.setDimension(1);
                } else {
                    curMemory += 1;
                    i.setDimension(0);
                }
                return;
            }
        }
    }

    public void add_proc_st(String lookahead) {
        SymbolTableLine stl = new SymbolTableLine();
        ss.push(lookahead);
        stl.setDimension(0);
        stl.setMemoryAddress(0);
        stl.setType("");
        stl.isSep(true);
        stl.setLexme(lookahead);
        stl.setPBAddress(PB.size());
        ST.add(stl);
    }

    public void push_address(String lookahead) {
        for (SymbolTableLine i : ST) {
            if (i.getLexme().equals(lookahead)) {
                ss.push(Integer.toString(i.getMemoryAddress()));
                return;
            }
        }
    }

    public void calc_array_pos(String lookahead) {
        int expr = Integer.parseInt(ss.pop());
        String arrayAddress = ss.pop();
        SymbolTableLine arrLine = null;

        for (SymbolTableLine i : ST) {
            if (i.getMemoryAddress() == Integer.parseInt(arrayAddress)) {
                arrLine = i;
                break;
            }
        }

        ThreeAddressLine minusByArrayIndex = new ThreeAddressLine();
        minusByArrayIndex.operator = Operator.MINUS;
        minusByArrayIndex.op1 = new Operand(Type.DIRECT, expr);
        minusByArrayIndex.op2 = new Operand(Type.IMIDIATE, arrLine.getArrayStartIndex());
        minusByArrayIndex.op3 = new Operand(Type.DIRECT, tempCounter);
        tempCounter += 2;
        PB.add(minusByArrayIndex);

        ThreeAddressLine multipyByMemorySize = new ThreeAddressLine();
        multipyByMemorySize.operator = Operator.MULT;
        multipyByMemorySize.op1 = new Operand(Type.DIRECT, tempCounter - 2);
        multipyByMemorySize.op2 = new Operand(Type.IMIDIATE, arrLine.getType().equals("integer") ? 1 : 2);
        multipyByMemorySize.op3 = new Operand(Type.DIRECT, tempCounter);
        tempCounter += 2;
        PB.add(multipyByMemorySize);

        ThreeAddressLine addStartAndOffset = new ThreeAddressLine();
        addStartAndOffset.operator = Operator.PLUS;
        addStartAndOffset.op1 = new Operand(Type.DIRECT, tempCounter - 2);
        addStartAndOffset.op2 = new Operand(Type.DIRECT, arrLine.getMemoryAddress());
        addStartAndOffset.op3 = new Operand(Type.DIRECT, tempCounter);
        tempCounter += 2;
        PB.add(addStartAndOffset);

        ss.push(Integer.toString(tempCounter - 2));
    }

    public void add_return_addr_st(String lookahead) {
        String functionId = ss.pop();

        SymbolTableLine arrLine = null;

        for (SymbolTableLine i : ST) {
            if (i.getLexme().equals(functionId)) {
                arrLine = i;
                break;
            }
        }

        arrLine.setType("Function");
        arrLine.setReturnAddress(curMemory++); // return address is always int

        ThreeAddressLine returnToCallee = new ThreeAddressLine();
        returnToCallee.operator = Operator.JP;
        returnToCallee.op1 = new Operand(Type.INDIRECT, arrLine.getReturnAddress());

        PB.add(returnToCallee);
    }

    public void assignop(String lookahead) {
        int getAddr = Integer.parseInt(ss.pop());
        int setAddr = Integer.parseInt(ss.pop());

        ThreeAddressLine assign = new ThreeAddressLine();
        assign.operator = Operator.ASSIGN;
        assign.op1 = new Operand(Type.DIRECT, getAddr);
        assign.op2 = new Operand(Type.DIRECT, setAddr);
        PB.add(assign);
    }

    public void assign_jmp(String lookahead) {
        ArrayList<String> params = new ArrayList<>();
        String param = "";
        while (!(param = ss.pop()).equals("$$$$")) {
            params.add(param);
        }
        String functionId = ss.pop();

        int stLine = 0;
        for (int i = 0; i < ST.size(); i++) {
            if (ST.get(i).getLexme().equals(functionId)) {
                stLine = i;
                break;
            }
        }

        if (ST.get(stLine).getNrOfArgs() != params.size()) {
            System.err.println("call function : " + functionId + " number of arguments does not match");
        }
        for (int i = stLine + 1; i < stLine + params.size() + 1; i++) {
            int val = Integer.parseInt(params.get(i - (stLine + 1)));
            ThreeAddressLine assignParameter = new ThreeAddressLine();
            assignParameter.operator = Operator.ASSIGN;
            assignParameter.op1 = new Operand(Type.DIRECT, val);
            assignParameter.op2 = new Operand(Type.DIRECT, ST.get(i).getMemoryAddress());
            PB.add(assignParameter);
        }

        ThreeAddressLine assignReturnAddress = new ThreeAddressLine();
        assignReturnAddress.operator = Operator.ASSIGN;
        assignReturnAddress.op1 = new Operand(Type.IMIDIATE, PB.size() + 2);
        assignReturnAddress.op2 = new Operand(Type.DIRECT, ST.get(stLine).getReturnAddress());
        PB.add(assignReturnAddress);

        ThreeAddressLine jumpToFunction = new ThreeAddressLine();
        jumpToFunction.operator = Operator.JP;
        jumpToFunction.op1 = new Operand(Type.IMIDIATE, ST.get(stLine).getPBAddress());
        PB.add(jumpToFunction);
    }

    public void do_logic(String lookahead) {
        String sp1 = ss.pop();
        String relop = ss.pop();
        String sp2 = ss.pop();

        ThreeAddressLine relopCode = new ThreeAddressLine();

        relopCode.op1 = new Operand(Type.DIRECT, Integer.parseInt(sp2));
        relopCode.op2 = new Operand(Type.DIRECT, Integer.parseInt(sp1));
        relopCode.op3 = new Operand(Type.DIRECT, tempCounter);
        tempCounter += 2;

        ss.push(Integer.toString(tempCounter - 2));

        Operator op = null;

        switch (relop) {
            case "=":
                op = Operator.EQ;
                break;
            case "<=":
                op = Operator.LE;
                break;
            case ">=":
                op = Operator.GE;
                break;
            case "<":
                op = Operator.LT;
                break;
            case ">":
                op = Operator.GT;
                break;
            case "<>":
                op = Operator.NQ;
                break;
        }

        relopCode.operator = op;
        PB.add(relopCode);
    }

    public void set_term_sign(String lookahead) {
        String term = ss.pop();
        String sign = ss.pop();

        if (sign.equals("-")) {

            ThreeAddressLine multiplyByMinusOne = new ThreeAddressLine();

            multiplyByMinusOne.operator = Operator.MULT;
            multiplyByMinusOne.op1 = new Operand(Type.DIRECT, -1);
            multiplyByMinusOne.op2 = new Operand(Type.DIRECT, Integer.parseInt(term));
            multiplyByMinusOne.op3 = new Operand(Type.DIRECT, tempCounter);
            tempCounter += 2;
            ss.push(Integer.toString(tempCounter - 2));
            PB.add(multiplyByMinusOne);

        }
    }

    public void sum_calc(String lookahead) {
        String sp1 = ss.pop();
        String addop = ss.pop();
        String sp2 = ss.pop();

        ThreeAddressLine addCode = new ThreeAddressLine();

        addCode.op1 = new Operand(Type.DIRECT, Integer.parseInt(sp2));
        addCode.op2 = new Operand(Type.DIRECT, Integer.parseInt(sp1));
        addCode.op3 = new Operand(Type.DIRECT, tempCounter);
        tempCounter += 2;

        ss.push(Integer.toString(tempCounter - 2));

        Operator op = null;

        switch (addop) {
            case "+":
                op = Operator.PLUS;
                break;
            case "-":
                op = Operator.MINUS;
                break;
            case "or":
                op = Operator.OR;
                break;
        }

        addCode.operator = op;
        PB.add(addCode);
    }

    public void mul_calc(String lookahead) {
        String sp1 = ss.pop();
        String mulop = ss.pop();
        String sp2 = ss.pop();

        ThreeAddressLine mulCode = new ThreeAddressLine();

        mulCode.op1 = new Operand(Type.DIRECT, Integer.parseInt(sp2));
        mulCode.op2 = new Operand(Type.DIRECT, Integer.parseInt(sp1));
        mulCode.op3 = new Operand(Type.DIRECT, tempCounter);
        tempCounter += 2;

        ss.push(Integer.toString(tempCounter - 2));

        Operator op = null;

        switch (mulop) {
            case "*":
                op = Operator.MULT;
                break;
            case "/":
                op = Operator.IDIV;
                break;
            case "div":
                op = Operator.DIV;
                break;
            case "mod":
                op = Operator.MOD;
                break;
            case "and":
                op = Operator.AND;
                break;
        }

        mulCode.operator = op;
        PB.add(mulCode);
    }

    public void do_not(String lookahead) {
        String factor = ss.pop();

        ThreeAddressLine notCode = new ThreeAddressLine();

        notCode.operator = Operator.NOT;
        notCode.op1 = new Operand(Type.DIRECT, Integer.parseInt(factor));
        notCode.op2 = new Operand(Type.DIRECT, tempCounter);
        tempCounter += 2;

        ss.push(Integer.toString(tempCounter - 2));
        PB.add(notCode);
    }

    public void push_start_of_array_or_call(String lookahead) {
        ss.push("PSOAOC");
    }

    public void call_or_calc_array(String lookahead) {
        ArrayList<String> params = new ArrayList<>();
        String param;
        while (!(param = ss.pop()).equals("PSOAOC")) {
            params.add(param);
        }
        
        String id = ss.pop();

        int stLine = -1;

        for (int i = 0; i < ST.size(); i++) {
            if (ST.get(i).getLexme().equals(id)) {
                stLine = i;
                break;
            }
        }
        
//        if ( id.equals("minusminus") )
//        {
//            System.out.println(params.size());
//        }
        
        if ( !ST.get(stLine).getType().equals("Function") ) {
            ss.push(Integer.toString(ST.get(stLine).getMemoryAddress()));
            return;
        }
//        System.out.println("is in this " + id + " " + params.size());
        if (ST.get(stLine).getType().equals("Function")) {
            if (params.size() != ST.get(stLine).getNrOfArgs() )
            {
                System.err.println("call function : " + id + " number of arguments does not match");
            }
            for (int i = stLine + 1; i < stLine + params.size() + 1; i++) {
                int val = Integer.parseInt(params.get(i - (stLine + 1)));
                ThreeAddressLine assignParameter = new ThreeAddressLine();
                assignParameter.operator = Operator.ASSIGN;
                assignParameter.op1 = new Operand(Type.DIRECT, val);
                assignParameter.op2 = new Operand(Type.DIRECT, ST.get(i).getMemoryAddress());
                PB.add(assignParameter);
            }

            ThreeAddressLine assignReturnAddress = new ThreeAddressLine();
            assignReturnAddress.operator = Operator.ASSIGN;
            assignReturnAddress.op1 = new Operand(Type.IMIDIATE, PB.size() + 2);
            assignReturnAddress.op2 = new Operand(Type.DIRECT, ST.get(stLine).getReturnAddress());
            PB.add(assignReturnAddress);

            ThreeAddressLine jumpToFunction = new ThreeAddressLine();
            jumpToFunction.operator = Operator.JP;
            jumpToFunction.op1 = new Operand(Type.IMIDIATE, ST.get(stLine).getPBAddress());
            PB.add(jumpToFunction);
            ss.push(Integer.toString(ST.get(stLine).getMemoryAddress()));
        } else {
            ThreeAddressLine minusByArrayIndex = new ThreeAddressLine();
            minusByArrayIndex.operator = Operator.MINUS;
            minusByArrayIndex.op1 = new Operand(Type.DIRECT, Integer.parseInt(params.get(0)));
            minusByArrayIndex.op2 = new Operand(Type.IMIDIATE, ST.get(stLine).getArrayStartIndex());
            minusByArrayIndex.op3 = new Operand(Type.DIRECT, tempCounter);
            tempCounter += 2;
            PB.add(minusByArrayIndex);

            ThreeAddressLine multipyByMemorySize = new ThreeAddressLine();
            multipyByMemorySize.operator = Operator.MULT;
            multipyByMemorySize.op1 = new Operand(Type.DIRECT, tempCounter - 2);
            multipyByMemorySize.op2 = new Operand(Type.IMIDIATE, ST.get(stLine).getType().equals("integer") ? 1 : 2);
            multipyByMemorySize.op3 = new Operand(Type.DIRECT, tempCounter);
            tempCounter += 2;
            PB.add(multipyByMemorySize);

            ThreeAddressLine addStartAndOffset = new ThreeAddressLine();
            addStartAndOffset.operator = Operator.PLUS;
            addStartAndOffset.op1 = new Operand(Type.DIRECT, tempCounter - 2);
            addStartAndOffset.op2 = new Operand(Type.DIRECT, ST.get(stLine).getMemoryAddress());
            addStartAndOffset.op3 = new Operand(Type.DIRECT, tempCounter);
            tempCounter += 2;
            PB.add(addStartAndOffset);

            ss.push(Integer.toString(tempCounter - 2));
        }
    }

    public void label(String lookahead) {
        ss.push(Integer.toString(PB.size()));
    }

    public void save(String lookahead) {
        ss.push(Integer.toString(PB.size()));
        PB.add(new ThreeAddressLine());
    }

    public void while_filler(String lookahead) {
        int save = Integer.parseInt(ss.pop());
        int expr = Integer.parseInt(ss.pop());
        int label = Integer.parseInt(ss.pop());

        PB.get(save).operator = Operator.JPF;
        PB.get(save).op1 = new Operand(Type.DIRECT, expr);
        PB.get(save).op2 = new Operand(Type.DIRECT, PB.size());

        ThreeAddressLine whileUnconditionalJump = new ThreeAddressLine();
        whileUnconditionalJump.operator = Operator.JP;
        whileUnconditionalJump.op1 = new Operand(Type.IMIDIATE, label);
        PB.add(whileUnconditionalJump);
    }

    public void jpf_save(String lookahead) {
        int save = Integer.parseInt(ss.pop());
        int expr = Integer.parseInt(ss.pop());

        PB.get(save).operator = Operator.JPF;
        PB.get(save).op1 = new Operand(Type.DIRECT, expr);
        PB.get(save).op2 = new Operand(Type.DIRECT, PB.size() + 1);

        save(lookahead);
    }

    public void jp(String lookahead) {
        int save = Integer.parseInt(ss.pop());
        PB.get(save).operator = Operator.JP;
        PB.get(save).op1 = new Operand(Type.IMIDIATE, PB.size());
    }

    public void push_num(String lookahead) {
        ThreeAddressLine assignToTemp = new ThreeAddressLine();
        assignToTemp.operator = Operator.ASSIGN;
        assignToTemp.op1 = new Operand(Type.IMIDIATE, Integer.parseInt(lookahead));
        assignToTemp.op2 = new Operand(Type.DIRECT, tempCounter);
        tempCounter += 2;
        PB.add(assignToTemp);
        ss.push(Integer.toString(tempCounter - 2));
    }

    public void push_intelligent(String lookahead) {
        int stLinei = -1;

        boolean isSep = false;

        int line = 0;
        for (SymbolTableLine stl : ST) {
            if (stl.getLexme().equals(lookahead) && stl.isSep()) {
                stLinei = line;
                isSep = true;
                break;
            }
            line++;

        }

        if (!isSep) {
            for (int i = ST.size() - 1; i != 0; i--) {
                if (ST.get(i).isSep()) {
                    break;
                }
                if (ST.get(i).getLexme().equals(lookahead)) {
                    stLinei = i;
                }
            }
            if (stLinei == -1) {
                for (int i = 1; i < ST.size(); i++) {
                    if (ST.get(i).isSep()) {
                        break;
                    }
                    if (ST.get(i).getLexme().equals(lookahead)) {
                        stLinei = i;
                    }
                }
            }
        }

        if (stLinei != -1) {
            SymbolTableLine stLine = ST.get(stLinei);

            if (stLine.isSep()) {
                ss.push(lookahead);
                ss.push("$$$$");
                return;
            } else {
                ss.push(Integer.toString(stLine.getMemoryAddress()));
                return;
            }
        } else {
            System.err.println("variable " + lookahead + " not defined");
        }
        ss.push(lookahead);
    }

    public void jmp_to_start(String lookahead) {
        ss.pop();

        ThreeAddressLine jumpToStart = new ThreeAddressLine();

        PB.get(0).setOperator(Operator.JP);
        PB.get(0).setOp1(new Operand(Type.DIRECT, PB.size()));

//        System.out.println("=========================");
        SymbolTableLine virtualFunc = new SymbolTableLine();
        virtualFunc.setLexme("$main$");
        virtualFunc.setType("Function");
        virtualFunc.isSep(true);
        ST.add(virtualFunc);

    }

    public void push_id_and_check(String lookahead) {
        ss.push(lookahead);
        int stLinei = -1;

        boolean isSep = false;

        int line = 0;
        for (SymbolTableLine stl : ST) {
            if (stl.getLexme().equals(lookahead) && stl.isSep()) {
                stLinei = line;
                isSep = true;
                break;
            }
            line++;

        }

        if (!isSep) {
            for (int i = ST.size() - 1; i != 0; i--) {
                if (ST.get(i).isSep()) {
                    break;
                }
                if (ST.get(i).getLexme().equals(lookahead)) {
                    stLinei = i;
                }
            }
            if (stLinei == -1) {
                for (int i = 1; i < ST.size(); i++) {
                    if (ST.get(i).isSep()) {
                        break;
                    }
                    if (ST.get(i).getLexme().equals(lookahead)) {
                        stLinei = i;
                    }
                }
            }
        }

        if (stLinei == -1) {
            System.err.println("variable " + lookahead + " not defined");
        }
    }

    public void set_arg_nr(String lookahead) {
        String functionId = ss.peek();

        int c = 0;
        for (int i = ST.size() - 1; i != 0; i--) {
            if (ST.get(i).isSep()) {
                break;
            }
            c++;
        }

        System.out.println(functionId + " " + c );

        for (SymbolTableLine stl : ST) {
            if (stl.getLexme().equals(functionId)) {
                stl.setNrOfArgs(c);
            }
        }

    }
}

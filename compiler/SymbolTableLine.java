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
public class SymbolTableLine {
    private String lexme = "";
    private String Type = "";
    private int Dimension;
    private int nrOfCells;
    private int memoryAddress;
    private int PBAddress;
    private int ArrayStartIndex;
    private int returnAddress;
    private int nrOfArgs;

    public int getNrOfArgs() {
        return nrOfArgs;
    }

    @Override
    public String toString() {
        return "SymbolTableLine{" + "lexme=" + lexme + ", Type=" + Type + ", Dimension=" + Dimension + ", nrOfCells=" + nrOfCells + ", memoryAddress=" + memoryAddress + ", PBAddress=" + PBAddress + ", ArrayStartIndex=" + ArrayStartIndex + ", returnAddress=" + returnAddress + ", nrOfArgs=" + nrOfArgs + '}';
    }

    public void setNrOfArgs(int nrOfArgs) {
        this.nrOfArgs = nrOfArgs;
    }
    
    private boolean sep = false ;

    public boolean isSep() {
        return sep;
    }

    public void isSep(boolean isSep) {
        this.sep = isSep;
    }
    

    public int getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(int returnAddress) {
        this.returnAddress = returnAddress;
    }

    

    public int getArrayStartIndex() {
        return ArrayStartIndex;
    }

    public void setArrayStartIndex(int ArrayStartIndex) {
        this.ArrayStartIndex = ArrayStartIndex;
    }

    public String getLexme() {
        return lexme;
    }

    public void setLexme(String lexme) {
        this.lexme = lexme;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public int getDimension() {
        return Dimension;
    }

    public void setDimension(int Dimension) {
        this.Dimension = Dimension;
    }

    public int getNrOfCells() {
        return nrOfCells;
    }

    public void setNrOfCells(int nrOfCells) {
        this.nrOfCells = nrOfCells;
    }

    public int getMemoryAddress() {
        return memoryAddress;
    }

    public void setMemoryAddress(int memoryAddress) {
        this.memoryAddress = memoryAddress;
    }

    public int getPBAddress() {
        return PBAddress;
    }

    public void setPBAddress(int PBAddress) {
        this.PBAddress = PBAddress;
    }
    
    
    
}

package semantic.entities;

import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;

public class ConstructorST extends RoutineST implements EntityST {

    public ConstructorST(String name) {
        super(name);
    }
    @Override
    public void checkDeclarations() {

    }

    @Override
    public void consolidate() {

    }
    public String toString() {
        String toReturn = routineName+'(';
        for (ParameterST p : parameters.values()) {
            toReturn += p.toString()+" ,";
        }
        if (!parameters.isEmpty()) {
            toReturn += toReturn.substring(0,toReturn.length()-2);
        }
        toReturn += ")";
        return toReturn;
    }
    public void generateCode() throws CodeGenerationException {
        CodeGenerator.getInstance().addLine(".CODE");
        CodeGenerator.getInstance().addLine("");
        if (blockAST == null) {
            generateDefaultConstructorCode();
        } else {
            begginingCode();
            blockAST.generateCode();
            CodeGenerator.getInstance().addLine("FMEM "+blockAST.getAmountOfVariables());
            CodeGenerator.getInstance().addLine("STOREFP");
            CodeGenerator.getInstance().addLine("RET "+(getParameterTypeList().size()+1));
        }
        CodeGenerator.getInstance().addLine("");
    }

    private void begginingCode() throws CodeGenerationException {
        CodeGenerator.getInstance().addLine(CodeGenerator.generateLabelForConstructor(this)+": "+"LOADFP ");
        CodeGenerator.getInstance().addLine("LOADSP");
        CodeGenerator.getInstance().addLine("STOREFP");
    }

    private void generateDefaultConstructorCode() throws CodeGenerationException {
        begginingCode();
        CodeGenerator.getInstance().addLine("FMEM 0");
        CodeGenerator.getInstance().addLine("STOREFP");
        CodeGenerator.getInstance().addLine("RET 1");
    }
}

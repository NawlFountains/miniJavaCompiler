package ast.nodes;

import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.SemanticException;
import lexical.Token;
import semantic.PrimitiveType;
import semantic.ReferenceType;
import semantic.SymbolTable;
import semantic.Type;
import semantic.entities.AttributeST;
import semantic.entities.RoutineST;

public class NodeVariableLocal extends NodeCompoundExpression implements Node{
    protected Token variableIdToken;
    protected NodeCompoundExpression assignmentExpression;
    protected Type variableType;
    public NodeVariableLocal(Token variableIdToken, NodeCompoundExpression assignmentExpression) {
        this.variableIdToken = variableIdToken;
        this.assignmentExpression = assignmentExpression;
    }
    public NodeVariableLocal(Token variableIdToken, Type type) {
        this.variableIdToken = variableIdToken;
        variableType = type;
        returnType = type;
    }
    public void addParentBlock(NodeBlock parentNode) {
        super.addParentBlock(parentNode);
        if (assignmentExpression != null)
            assignmentExpression.addParentBlock(parentNode);
    }
    public String getName() {
        return variableIdToken.getLexeme();
    }
    @Override
    public void check() throws SemanticException{
        //Check if the assignmentExpression is correct
        if (assignmentExpression != null) {
            assignmentExpression.check();
            if (variableType == null) {
                variableType = assignmentExpression.getReturnType();
                returnType = variableType;
                if (variableType.toString().equals("void") || variableType.toString().equals("null"))
                    throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Una variable no puede ser de tipo void ni de tipo null");
            } else {
                //If doing optional must check type declared and assignment match
            }
        }

        NodeBlock rootBlock = getRootBlock();

        RoutineST referenceEnviroment = rootBlock.getRoutineEnvironment();
        //Check name doesn't collide with a parameter for this method
        if (!referenceEnviroment.existParameter(variableIdToken.getLexeme())) {
            //Check name doesn't collide with an existing attribute
            for (AttributeST at : referenceEnviroment.getOwnerClass().getAttributes()) {
                if (variableIdToken.getLexeme().equals(at.getAttributeName())) {
                    throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe un atributo con el identificador "+variableIdToken.getLexeme());
                }
            }
            //Check name doesn't collide with an existing local variable
            NodeBlock pivotBlock = getParentBlock();
            boolean found = false;
            while (!pivotBlock.isRoot() && !found) {
                if (pivotBlock.existsVariableWithName(variableIdToken.getLexeme())) {
                    found = true;
                } else {
                    pivotBlock = pivotBlock.getParentBlock();
                }
            }
            if (!found && pivotBlock.existsVariableWithName(variableIdToken.getLexeme())) {
                found = true;
            }
            if (found)
                throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe una variable local con el identificador "+variableIdToken.getLexeme());
            else
                this.getParentBlock().insertLocalVariable(this);
        } else {
            throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe un parametro con el identificador "+variableIdToken.getLexeme());
        }
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "Local variable "+variableIdToken.getLexeme()+" = ";
        if (assignmentExpression != null) {
            toReturn += assignmentExpression.getStructure();
        } else {
            toReturn += variableType.toString();
        }
        return toReturn;
    }
    @Override
    public void generateCode() throws CodeGenerationException {
        CodeGenerator.getInstance().addLine("RMEM 1 ; Reservamos lugar en memoria para almacenar el valor de la var local "+variableIdToken.getLexeme());
        if (variableType instanceof ReferenceType) {
            //Creation of CIRC
            System.out.println("generateCode:NodeVariableLocal:ReferenceType:"+variableType);
            CodeGenerator.getInstance().addLine("RMEM 1  ; Reservamos memoria para el resultado del malloc (la referencia al nuevo CIR de "+variableType.toString()+")");
            CodeGenerator.getInstance().addLine("PUSH "+ (SymbolTable.getInstance().getClassWithName(variableType.toString()).getAttributes().size()+1) +"  ;  Apilo la cantidad de var de instancia del CIR de "+variableType.toString()+" +1 por VT");
            CodeGenerator.getInstance().addLine("PUSH "+CodeGenerator.getLabelForMalloc()+"  ; La dirección de la rutina para alojar memoria en el heap");
            CodeGenerator.getInstance().addLine("CALL ");
            CodeGenerator.getInstance().addLine("DUP");
            CodeGenerator.getInstance().addLine("PUSH "+CodeGenerator.generateLabelForVT(SymbolTable.getInstance().getClassWithName(variableType.toString())));
            CodeGenerator.getInstance().addLine("STOREREF 0 ; Guardamos la referencia a la VT en el CIR recien creado");
            CodeGenerator.getInstance().addLine("DUP");
            System.out.println("generateCode:NodeVariableLocal:end");
        } else {

            System.out.println("generateCode:NodeVariableLocal:PrimitiveType:"+variableType);
        }
        assignmentExpression.generateCode();
        CodeGenerator.getInstance().addLine("STORE "+parentBlock.getLocalVariablePosition(variableIdToken.getLexeme())+" ; Almaceno el valor de la expresion en la var local "+variableIdToken.getLexeme());
    }
}

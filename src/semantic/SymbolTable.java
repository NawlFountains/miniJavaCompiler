package semantic;

import semantic.entities.ClassST;
import semantic.entities.EntityST;

import java.util.Hashtable;

public class SymbolTable implements EntityST {
    public Hashtable<String,ClassST> classes;
    public Hashtable<String,ClassST> interfaces;
    public ClassST actualClass;

    public SymbolTable() {

    }

    private void insertClass(String lexeme,ClassST classST) {
        classes.put(lexeme,classST);
    }
    public void checkDeclarations(){}
    public void isCorrectlyDeclared(){}
    public void consolidate(){}
}

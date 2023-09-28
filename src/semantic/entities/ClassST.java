package semantic.entities;

import java.util.Hashtable;

public class ClassST implements EntityST {
    public String className;
    public String superClassName;
    public Hashtable<String,AttributeST> attributes;
    public Hashtable<String,MethodST> methods;

    public ClassST(String className) {
        this.className = className;
    }

    public void inheritsFrom(String superClassName) {
        this.superClassName = superClassName;
    }

    public void isCorrectlyDeclared() {

    }

    public void consolidate() {

    }
}

package semantic;

public abstract class Type {
    protected String typeName;
    public boolean equals(Type type) {
        return typeName.equals(type.toString());
    }
    public boolean compatibleWith(Type type,String operand) {
        boolean compatible = false;
//        if (operand.equals("opLess") || operand.equals("opLessEq") || operand.equals("opEq") || operand.equals("opGreaterEq")) {
//            if ((typeName.equals("int") || typeName.equals("float")) && (type.typeName.equals("int") || type.typeName.equals("float")))
//                compatible = true;
//        } else {
//            if (operand.eq)
//        }
        return compatible;
    }
}

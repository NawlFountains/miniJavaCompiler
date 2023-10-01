package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class RoutineST {
    protected HashMap<String,ParameterST> parameters;
    protected List<Type> parametersTypeList;
    protected ParameterST actualParameter;
    protected String routineName;

    protected RoutineST(String routineName) {
        parameters = new HashMap<>();
        parametersTypeList = new ArrayList<>();
        this.routineName = routineName;
    }
    public void insertParameter(Token token, ParameterST parameterST) throws SemanticException {
        if (!existParameter(token.getLexeme())) {
            parameters.put(token.getLexeme(),parameterST);
            parametersTypeList.add(parametersTypeList.size(),parameterST.getParameterType());
            this.actualParameter = parameterST;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un parametro en este constructor con el identificador "+token.getLexeme());
        }
    }
    public List<Type> getParameterTypeList() {
        return parametersTypeList;
    }
    public boolean existParameter(String parameterName) {
        return parameters.get(parameterName) != null;
    }
    public String getName() {
        return routineName;
    }
    public boolean equals(RoutineST compare) {
        return sameList(parametersTypeList,compare.getParameterTypeList());
    }

    private boolean sameList(List listOne, List listTwo) {
        boolean same = true;
        int i = 0;
        if(listOne.size() == listTwo.size()) {
            while (!same && i <= listOne.size()) {
                if (!listOne.get(i).equals(listTwo.get(i))) {
                    same = false;
                }
                i++;
            }
        } else {
            same = false;
        }
        return same;
    }
}

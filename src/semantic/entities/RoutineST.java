package semantic.entities;

import lexical.SemanticException;
import lexical.Token;

import java.util.HashMap;

public class RoutineST {
    protected HashMap<String,ParameterST> parameters;
    protected ParameterST actualParameter;

    protected RoutineST() {
        parameters = new HashMap<>();
    }
    public void insertParameter(Token token, ParameterST parameterST) throws SemanticException {
        if (!existParameter(token.getLexeme())) {
            parameters.put(token.getLexeme(),parameterST);
            this.actualParameter = parameterST;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un parametro en este constructor con el identificador "+token.getLexeme());
        }
    }
    public boolean existParameter(String parameterName) {
        return parameters.get(parameterName) != null;
    }
}

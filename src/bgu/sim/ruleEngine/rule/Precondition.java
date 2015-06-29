package bgu.sim.ruleEngine.rule;

import bgu.sim.data.DataFunctionHadlers;
import org.josql.Query;
import org.josql.QueryParseException;

public class Precondition {

    private String _expression;
    private Query _query;

    public Precondition(final String expression) throws QueryParseException {

        if (expression == null) {
            throw new AssertionError("expression may not be null");
        }
        this._expression = expression;
        
        if (!"".equals(this._expression)) {
            this._query = new Query();
            this._query.addFunctionHandler(new DataFunctionHadlers());
            // Parse the SQL you are going to use.
            this._query.parse(this._expression);
        }
    }

    @Override
    public String toString() {
        return "Expression= [" + _expression + "]";
    }

    public String getExpression() {
        return _expression;
    }

    public void setExpression(String expression) throws QueryParseException {
        this._expression = expression;
        if (!"".equals(this._expression)) {
            this._query = new Query();
            this._query.addFunctionHandler(new DataFunctionHadlers());
            // Parse the SQL you are going to use.
            this._query.parse(this._expression);
        }
    }

    public Query getQuery() {
        return _query;
    }
}

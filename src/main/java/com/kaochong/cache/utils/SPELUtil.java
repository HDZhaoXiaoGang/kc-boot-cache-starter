package com.kaochong.cache.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SPELUtil {


    public static String getSPELString(String[] parameterNames, Object[] args, String key) {
        if ("".equals(key)) {
            return "";
        }
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        Expression expression = parser.parseExpression(key);
        String value = expression.getValue(context, String.class);
        return value;
    }

    public static boolean getSPELBoolean(Object obj, String key) {
        if ("".equals(key)) {
            return true;
        }
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("result", obj);
        return parser.parseExpression(key).getValue(context, Boolean.class);
    }

    public static boolean getSPELBoolean(String[] parameterNames, Object[] args, String key) {
        if ("".equals(key)) {
            return true;
        }
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, Boolean.class);
    }
}

package com.digdes.school.conditions;

import com.digdes.school.exceptions.OperatorNotSupportTypeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IlikeCondition implements Condition{
    /**
     * Метод для проверки условия 'ilike' между двумя значениями.
     * Применяется на типы данных:
     * String
     *
     * @param compared Значение, с которым производится сравнение
     * @param value    Значение из таблицы
     * @return {@code true}, если условие выполняется, иначе {@code false}
     * @throws OperatorNotSupportTypeException Если оператор условия не поддерживает тип данных значения
     */
    @Override
    public boolean check(Object compared, Object value) throws OperatorNotSupportTypeException {
        if(value == null)
            return false;
        try {
            String ilike = ((String) compared).replaceAll("%", "\\.*");
            ilike = String.format("(?i)^%s$", ilike);
            Pattern pattern = Pattern.compile(ilike, Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher((String) value);
            return matcher.find();
        } catch (ClassCastException e){
            throw new OperatorNotSupportTypeException("Operator 'ilike' supports only the String type");
        }
    }
}

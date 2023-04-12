package com.digdes.school.conditions;

import com.digdes.school.exceptions.OperatorNotSupportTypeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LikeCondition implements Condition {
    /**
     * Метод для проверки условия 'like' между двумя значениями.
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
        if (value == null)
            return false;
        try {
            String like = ((String) compared).replaceAll("%", "\\.*");
            like = String.format("^%s$", like);
            Pattern pattern = Pattern.compile(like);
            Matcher matcher = pattern.matcher((String) value);
            return matcher.find();
        } catch (ClassCastException e) {
            throw new OperatorNotSupportTypeException("Operator 'like' supports only the String type");
        }
    }
}

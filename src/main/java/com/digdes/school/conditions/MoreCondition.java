package com.digdes.school.conditions;

import com.digdes.school.exceptions.OperatorNotSupportTypeException;

public class MoreCondition implements Condition {
    /**
     * Метод для проверки условия '>' между двумя значениями.
     * Применяется на типы данных:
     * Long, Double
     *
     * @param compared Значение, с которым производится сравнение
     * @param value    Значение из таблицы
     * @return {@code true}, если условие выполняется, иначе {@code false}
     * @throws OperatorNotSupportTypeException Если оператор условия не поддерживает тип данных значения
     */
    @Override
    public boolean check(Object compared, Object value) throws OperatorNotSupportTypeException {
        try {
            if (value != null)
                if (compared instanceof Long) {
                    return (long) value > (long) compared;
                } else if (compared instanceof Double) {
                    return (double) value > (double) compared;
                }
            return false;
        } catch (ClassCastException e) {
            throw new OperatorNotSupportTypeException("Operator '>' supports only Long and Double types");
        }
    }
}

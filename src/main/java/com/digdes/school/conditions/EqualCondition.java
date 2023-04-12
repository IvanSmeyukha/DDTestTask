package com.digdes.school.conditions;

public class EqualCondition implements Condition {
    /**
     * Метод для проверки условия '=' между двумя значениями.
     * Применяется на типы данных:
     * Boolean, String, Long, Double
     *
     * @param compared Значение, с которым производится сравнение
     * @param value    Значение из таблицы
     * @return {@code true}, если условие выполняется, иначе {@code false}
     */
    @Override
    public boolean check(Object compared, Object value) {
        if (value != null)
            if (value instanceof String) {
                return compared.equals(value);
            } else if (value instanceof Long) {
                return (long) compared == (long) value;
            } else if (value instanceof Double) {
                return (double) compared == (double) value;
            } else if (value instanceof Boolean) {
                return (boolean) compared == (boolean) value;
            }
        return false;
    }
}

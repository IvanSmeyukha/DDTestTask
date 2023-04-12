package com.digdes.school.commands;

import com.digdes.school.Database;
import com.digdes.school.exceptions.OperatorNotSupportTypeException;

import java.util.List;
import java.util.Map;

public class SelectCommand implements Command{

    /**
     * Метод для выполнения SELECT-запроса на основе списка условий и набора значений для обновления.
     *
     * @param values     Набор значений, представляющих собой карту ключей и соответствующих значений,
     *                   которые будут обновлены в каждой строке, соответствующей указанным условиям
     * @param conditions Список условий, представляющих собой вложенные списки и карты,
     *                   описывающие операторы, ключи и значения для сравнения
     * @return Список карт, представляющих результат SELECT-запроса, соответствующего указанным условиям
     * @throws OperatorNotSupportTypeException Если оператор условия не поддерживает тип данных значения
     */
    @Override
    public List<Map<String, Object>> execute(
            Map<String, Object> values,
            List<List<Map<String, Object>>> conditions
    ) throws OperatorNotSupportTypeException {
        Database db = Database.getInstance();
        return db.select(conditions);
    }
}

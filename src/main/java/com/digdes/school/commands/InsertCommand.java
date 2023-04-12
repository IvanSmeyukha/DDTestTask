package com.digdes.school.commands;

import com.digdes.school.Database;

import java.util.*;
import java.util.stream.Collectors;

public class InsertCommand implements Command {
    /**
     * Метод для выполнения INSERT-запроса на основе списка условий и набора значений для обновления.
     *
     * @param values     Набор значений, представляющих собой карту ключей и соответствующих значений,
     *                   которые будут обновлены в каждой строке, соответствующей указанным условиям
     * @param conditions Список условий, представляющих собой вложенные списки и карты,
     *                   описывающие операторы, ключи и значения для сравнения
     * @return Список карт, представляющих результат INSERT-запроса, соответствующего указанным условиям
     */
    @Override
    public List<Map<String, Object>> execute(
            Map<String, Object> values,
            List<List<Map<String, Object>>> conditions
    ) {
        Database db = Database.getInstance();
        values = values
                .entrySet()
                .stream()
                .filter(x -> x.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        db.getData().add(values);
        return List.of(values);
    }
}

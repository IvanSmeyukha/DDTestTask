package com.digdes.school;

import com.digdes.school.conditions.Condition;
import com.digdes.school.exceptions.OperatorNotSupportTypeException;
import com.digdes.school.factories.ConditionsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Database {
    private static final Database instance = new Database();
    private static final Set<String> columns = Set.of("id", "lastname", "cost", "age", "active");
    private final List<Map<String, Object>> data = new ArrayList<>();

    private Database(){
    }

    public static Database getInstance() {
        return instance;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public boolean containsColumn(String column){
        return columns.contains(column.toLowerCase());
    }

    /**
     * Метод для получения списка строк таблиы на основе списка условий.
     *
     * @param conditions Список условий, представляющих собой вложенные списки и карты,
     *                   описывающие операторы, ключи и значения для сравнения
     * @return Список карт, представляющих список строк таблицы, соответствующих указанным условиям
     * @throws OperatorNotSupportTypeException Если оператор условия не поддерживает тип данных значения
     */
    public List<Map<String, Object>> select(List<List<Map<String, Object>>> conditions) throws OperatorNotSupportTypeException {
        List<Map<String, Object>> selectResult = new ArrayList<>();
        ConditionsFactory factory = new ConditionsFactory();
        boolean isOk;
        if(conditions.isEmpty())
            return data;
        for(Map<String, Object> row : data){
            for(List<Map<String, Object>> andConditions : conditions){
                isOk = true;
                for(Map<String, Object> oneCondition : andConditions){
                    String operator = (String) oneCondition.get("operator");
                    Condition condition = factory.getCondition(operator);
                    Object comparedValue = oneCondition.get("value");
                    Object valueFromTable = row.get(oneCondition.get("key"));
                    if(!condition.check(comparedValue, valueFromTable)){
                        isOk = false;
                        break;
                    }
                }
                if(isOk){
                    selectResult.add(row);
                    break;
                }
            }
        }
        return selectResult;
    }
}

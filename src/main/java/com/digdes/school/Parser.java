package com.digdes.school;

import com.digdes.school.exceptions.IncorrectColumnNameException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    /**
     * Метод для разбора SQL-команды и извлечения значений и условий.
     *
     * @param command SQL-команда, которую необходимо разобрать
     * @return Карта, содержащая результаты разбора команды:
     *         - "command": тип SQL-команды (INSERT, UPDATE, DELETE, SELECT)
     *         - "values": карта со значениями VALUES
     *         - "conditions": список с условиями WHERE
     * @throws IncorrectColumnNameException Если в SQL-команде указано некорректное имя столбца
     */
    public static Map<String, Object> parseCommand(String command) throws IncorrectColumnNameException {
        Pattern insertPattern = Pattern.compile("(?i)^INSERT\\s+VALUES\\s+(.+)$");

        Pattern updatePattern = Pattern.compile("(?i)^UPDATE\\s+VALUES\\s+(.+)$");
        Pattern updateWherePattern = Pattern.compile("(?i)^UPDATE\\s+VALUES\\s+(.+)\\s+WHERE\\s+(.+)$");

        Pattern deletePattern = Pattern.compile("(?i)^DELETE(?:\\s+WHERE\\s+(.+))?$");

        Pattern selectPattern = Pattern.compile("(?i)^SELECT(?:\\s+WHERE\\s+(.+))?$");

        List<List<Map<String, Object>>> conditions = new ArrayList<>();
        Map<String, Object> values = new HashMap<>();
        command = command.trim();
        Matcher matcher = insertPattern.matcher(command);
        if (matcher.find()) {
            values = parseValues(matcher.group(1));
            return Map.of("command", "INSERT", "values", values, "conditions", conditions);
        }

        matcher = updatePattern.matcher(command);
        if (matcher.find()) {
            Matcher updateWhereMatcher = updateWherePattern.matcher(command);
            if (updateWhereMatcher.find()) {
                values = parseValues(updateWhereMatcher.group(1));
                conditions = parseCondition(updateWhereMatcher.group(2));
            } else {
                values = parseValues(matcher.group(1));
            }
            return Map.of("command", "UPDATE", "values", values, "conditions", conditions);
        }

        matcher = deletePattern.matcher(command);
        if (matcher.find()) {
            if (matcher.group(1) != null) {
                conditions = parseCondition(matcher.group(1));
            }
            return Map.of("command", "DELETE", "values", values, "conditions", conditions);
        }

        matcher = selectPattern.matcher(command);
        if (matcher.find()) {
            if (matcher.group(1) != null) {
                conditions = parseCondition(matcher.group(1));
            }
            return Map.of("command", "SELECT", "conditions", conditions);
        }

        throw new IllegalArgumentException("Invalid command format");
    }

    /**
     * Метод для разбора строки значений и преобразования ее в карту ключ-значение.
     *
     * @param valuesStr Строка значений, перечисленных через запятую
     * @return Карта, содержащая результаты разбора строки значений:
     *         - ключи: имена столбцов
     *         - значения: соответствующие значения столбцов
     * @throws IncorrectColumnNameException Если указано некорректное имя столбца
     */
    public static Map<String, Object> parseValues(String valuesStr) throws IncorrectColumnNameException {
        Pattern pairPattern = Pattern.compile("'(\\w+)'\\s*=\\s*('[^']*'|[^',]+)");
        Matcher matcher = pairPattern.matcher(valuesStr);
        Map<String, Object> values = new HashMap<>();
        Database database = Database.getInstance();
        while (matcher.find()) {
            String key = matcher.group(1);
            String valueStr = matcher.group(2);
            Object value;
            if (!database.containsColumn(key)) {
                throw new IncorrectColumnNameException(String.format("Incorrect column name: %s", key));
            }
            if (valueStr.startsWith("'")) {
                value = valueStr.replaceAll("'", "");
            } else if (valueStr.equals("null")) {
                value = null;
            } else {
                try {
                    value = Long.parseLong(valueStr);
                } catch (NumberFormatException e) {
                    try {
                        value = Double.parseDouble(valueStr);
                    } catch (NumberFormatException ex) {
                        value = Boolean.parseBoolean(valueStr);
                    }
                }
            }
            values.put(key, value);
        }
        return values;

    }

    /**
     * Метод для разбора строки условий
     *
     * @param conditionStr Строка условий, соединенных логическими операторами OR и AND
     * @return Список списков карт, содержащих результаты разбора строки условий:
     *         - внешний список: условия фильтрации между "OR"
     *         - внутренние списки: условия фильтрации между "AND"
     *         - каждая карта во внутренних списках содержит:
     *           - "key": имя столбца
     *           - "operator": оператор сравнения
     *           - "value": значение столбца
     * @throws IncorrectColumnNameException Если указано некорректное имя столбца
     */
    public static List<List<Map<String, Object>>> parseCondition(String conditionStr) throws IncorrectColumnNameException {
        Pattern pairPattern = Pattern.compile("(?i)'(\\w+)'\\s*(=|!=|>=|<=|<|>|like|ilike)\\s*('[^']*'|[^'(?:AND|OR)\\s]+)|(AND|OR)");
        Matcher matcher = pairPattern.matcher(conditionStr);
        List<List<Map<String, Object>>> conditions = new ArrayList<>();
        List<Map<String, Object>> conditionsBetweenOr = new ArrayList<>();
        Database database = Database.getInstance();
        while (matcher.find()) {
            if (matcher.group(4) == null) {
                String key = matcher.group(1);
                String operator = matcher.group(2);
                String valueStr = matcher.group(3);
                Object value;
                if (!database.containsColumn(key)) {
                    throw new IncorrectColumnNameException(String.format("Incorrect column name: %s", key));
                }
                if (valueStr.startsWith("'")) {
                    value = valueStr.replaceAll("'", "");
                } else {
                    try {
                        value = Long.parseLong(valueStr);
                    } catch (NumberFormatException e) {
                        try {
                            value = Double.parseDouble(valueStr);
                        } catch (NumberFormatException ex) {
                            value = Boolean.parseBoolean(valueStr);
                        }
                    }
                }
                Map<String, Object> pair = Map.of("key", key, "operator", operator, "value", value);
                conditionsBetweenOr.add(pair);
            } else if (matcher.group(4).equals("OR")) {
                conditions.add(conditionsBetweenOr);
                conditionsBetweenOr = new ArrayList<>();
            }
        }
        conditions.add(conditionsBetweenOr);
        return conditions;
    }
}

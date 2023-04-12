package com.digdes.school;

import com.digdes.school.commands.Command;
import com.digdes.school.factories.CommandsFactory;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    //Дефолтный конструктор
    public JavaSchoolStarter() {

    }

    //На вход запрос, на выход результат выполнения запроса

    /**
     * Метод для выполнения SQL-запросов.
     *
     * @param request SQL-запрос
     * @return Список карт, представляющих список строк таблицы, соответствующих результату запроса
     */
    public List<Map<String, Object>> execute(String request) throws Exception {
        //Здесь начало исполнения вашего кода
        Map<String, Object> parsedCommand = Parser.parseCommand(request);
        CommandsFactory factory = new CommandsFactory();
        Command command = factory.getCommand((String) parsedCommand.get("command"));

        return command.execute(
                (Map<String, Object>)parsedCommand.get("values"),
                (List<List<Map<String, Object>>>)parsedCommand.get("conditions")
        );
    }

}

package com.digdes.school.commands;

import com.digdes.school.exceptions.OperatorNotSupportTypeException;

import java.util.List;
import java.util.Map;

public interface Command {
    List<Map<String, Object>> execute(Map<String, Object> values, List<List<Map<String, Object>>> conditions)  throws OperatorNotSupportTypeException;
}

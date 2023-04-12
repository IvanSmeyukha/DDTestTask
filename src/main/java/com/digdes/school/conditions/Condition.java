package com.digdes.school.conditions;

import com.digdes.school.exceptions.OperatorNotSupportTypeException;

public interface Condition {
    boolean check(Object compared, Object value) throws OperatorNotSupportTypeException;
}

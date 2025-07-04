package util;

import exceptions.DataNotFoundException;

import java.util.List;

public class GlobalValidator {
    public static <T> T dataRequireNonNull(T obj, String msg) {
        if(obj == null) throw new DataNotFoundException(msg);
        return obj;
    }

    public static <T> List<T> listRequireNotEmpty(List<T> list, String msg) {
        if (list == null || list.isEmpty()) throw new DataNotFoundException(msg);
        return list;
    }
}

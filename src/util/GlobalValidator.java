package util;

import exceptions.DataNotFoundException;

public class GlobalValidator {
    public static <T> T dataRequireNonNull(T obj, String msg) {
        if(obj == null) {
            throw new DataNotFoundException(msg);
        }
        return obj;
    }
}

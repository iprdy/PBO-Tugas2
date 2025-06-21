package util;

import exceptions.DataNotFoundException;
import models.Villas;

import java.sql.SQLException;

public class Validator {
    public static <T> T dataRequireNonNull(T obj, String msg) {
        if(obj == null) {
            throw new DataNotFoundException(msg);
        }
        return obj;
    }
}

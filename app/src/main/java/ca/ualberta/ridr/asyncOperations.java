package ca.ualberta.ridr;
/**
 * Created by Justin on 2016-11-10.
 */

public class asyncOperation<T> {
    T baseType;
    AsyncDatabaseController<T> controller;

    public T get(String type, String id) {
        baseType =(T) controller.query(id, type);
        return baseType;
    }
}
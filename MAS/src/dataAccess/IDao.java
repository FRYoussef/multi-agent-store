package dataAccess;

import java.util.HashSet;

public interface IDao<T> {

    /**
     * Reads from DB an object given an ID.
     * @param id of the object
     * @return If it was found, an instance of the object, otherwise, an instance with default values.
     */
    T get(int id);

    /**
     * Reads from DB all objects
     * @return a set of all objects
     */
    HashSet<T> getAll();

    /**
     * Writes 'obj' to DB.
     * @param obj
     */
    void write(T obj);

    /**
     * Writes all instances in the DB.
     * @param objs
     */
    void writeAll(HashSet<T> objs);
}
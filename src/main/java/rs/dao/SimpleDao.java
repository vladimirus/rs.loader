package rs.dao;


import java.util.Collection;

public interface SimpleDao<T> {
    void save(T object);
    Collection<T> get(int pageNumber, int size);
}

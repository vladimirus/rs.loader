package rs.dao;


import java.util.Collection;

public interface SimpleDao<T> {
    void save(T object);
    void save(Collection<T> collection);
    Collection<T> get(int pageNumber, int size);
}

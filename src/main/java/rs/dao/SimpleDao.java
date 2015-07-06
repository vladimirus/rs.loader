package rs.dao;


public interface SimpleDao<T> {
    void save(T object);
}

package rs.service;

import java.util.Collection;

public interface SimpleManager<T> {
    void save(T o);
    void save(Collection<T> collection);
    Collection<T> get(int pageNumber, int size);
}

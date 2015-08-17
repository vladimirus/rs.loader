package rs.loader.service.validator;

@FunctionalInterface
public interface Validator<T> {
    boolean isValid(T object);
}

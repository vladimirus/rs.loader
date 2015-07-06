package rs.service.convert;

@FunctionalInterface
public interface Converter<F, T> {
    T convert(F from);
}

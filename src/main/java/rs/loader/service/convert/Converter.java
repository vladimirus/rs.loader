package rs.loader.service.convert;

@FunctionalInterface
public interface Converter<F, T> {
    T convert(F from);
}

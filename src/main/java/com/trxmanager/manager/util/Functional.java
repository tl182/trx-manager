package com.trxmanager.manager.util;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Functional {

    public static <T, S> Optional<S> safeGet(T t, Function<T, S> fieldExtractor) {
        try {
            return Optional.ofNullable(fieldExtractor.apply(t));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public static <T> Predicate<T> fieldNonNull(Function<T, ?> fieldExtractor) {
        return t -> safeGet(t, fieldExtractor).isPresent();
    }

    public static <T> Predicate<T> numericFieldGtOrEq(Function<T, BigDecimal> fieldExtractor, BigDecimal value) {
        return t -> safeGet(t, fieldExtractor)
                .filter(number -> number.compareTo(value) >= 0)
                .isPresent();
    }

    public static <T> Predicate<T> numericFieldGt(Function<T, BigDecimal> fieldExtractor, BigDecimal value) {
        return t -> safeGet(t, fieldExtractor)
                .filter(number -> number.compareTo(value) > 0)
                .isPresent();
    }
}

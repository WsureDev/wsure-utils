package top.wsure.common.utils;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnumUtils {

    public static <E extends Enum<E>> E query(Class<E> e, Predicate<E> filter) {
        return querySet(e,filter)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public static <E extends Enum<E>> Set<E> querySet(Class<E> e, Predicate<E> filter) {
        assert filter != null;
        return EnumSet.allOf(e)
                .stream()
                .filter(filter)
                .collect(Collectors.toSet());
    }

    public static <T,E extends Enum<E>> T get(Class<E> e,Predicate<E> filter,Function<? super E,? extends T> get){
        return NullableUtils.get(query(e, filter),get);
    }

    public static <T,E extends Enum<E>> E eq(Class<E> e, Function<? super E,? extends T> get, T input){
        return query(e, ei -> get.apply(ei).equals(input));
    }

    public static <T extends Comparable<T>,E extends Enum<E>> E rangeOf(Class<E> e, Function<? super E,? extends T> get, T min, T max){
        return query(e,ei -> get.apply(ei).compareTo(min) >= 0 && get.apply(ei).compareTo(max) <= 0 );
    }

    public static <T extends Comparable<T>,E extends Enum<E>> Set<E> rangeOfSet(Class<E> e,Function<? super E,? extends T> get, T min,T max){
        return querySet(e,ei -> get.apply(ei).compareTo(min) >= 0 && get.apply(ei).compareTo(max) <= 0 );
    }

    public static <T extends Comparable<T>,E extends Enum<E>> E range(Class<E> e, Function<? super E,? extends T> min, Function<? super E,? extends T> max, T val){
        return query(e,ei -> min.apply(ei).compareTo(val) <= 0 && max.apply(ei).compareTo(val) >= 0 );
    }

    public static <T extends Comparable<T>,E extends Enum<E>> Set<E> rangeSet(Class<E> e, Function<? super E,? extends T> min, Function<? super E,? extends T> max, T val){
        return querySet(e,ei -> min.apply(ei).compareTo(val) <= 0 && max.apply(ei).compareTo(val) >= 0 );
    }
}

package top.wsure.common.utils;

import java.util.Optional;
import java.util.function.Function;

public class NullableUtils {

    public static <T,V> V get(T obj, Function<? super T,? extends V> mapper){
        return Optional.ofNullable(obj).map(mapper).orElse(null);
    }
}

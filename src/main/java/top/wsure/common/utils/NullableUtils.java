package top.wsure.common.utils;

import java.util.Optional;
import java.util.function.Function;

public class NullableUtils {

    public static <T,V> V get(T obj, Function<? super T,? extends V> mapper){
        return Optional.ofNullable(obj).map(mapper).orElse(null);
    }

    public static <T,V,E extends Throwable> V getOrThrow(T obj, Function<? super T,? extends V> mapper,E throwable) throws E {
        return Optional.ofNullable(obj).map(mapper).orElseThrow(()->throwable);
    }

    public static <T> NullNode<T> build(T t){
        return new NullNode<T>(t);
    }

    public static class NullNode<T> {
        T t;
        public NullNode(T t){
            this.t = t;
        }

        public <V> NullNode<V> next(Function<? super T,? extends V> mapper){
            return new NullNode<V>(NullableUtils.get(t, mapper));
        }

        public <V,E extends Throwable> NullNode<V> nextOrThrow(Function<? super T,? extends V> mapper,E throwable) throws E {
            return new NullNode<V>(NullableUtils.getOrThrow(t, mapper,throwable));
        }

        public T get(){
            return t;
        }
    }
}

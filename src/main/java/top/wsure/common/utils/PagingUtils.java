package top.wsure.common.utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * FileName: PagingUtils
 * Author:   wsure
 * Date:     2021/3/18 10:37 下午
 * Description:
 */
public class PagingUtils {

    public static <T> void shardingExecution(long pageSize, List<T> objList, Consumer<List<T>> mapper) {
        shardingExecution(pageSize, objList, mapper, ArrayList::new);
    }

    public static <T, V> List<V> shardingExecution(long pageSize, List<T> objList, Function<List<T>, List<V>> mapper) {
        return shardingExecutionWithResult(pageSize, objList, mapper, ArrayList::new, ArrayList::new);
    }

    public static <T> void shardingExecution(long pageSize, Set<T> objSet, Consumer<Set<T>> mapper) {
        shardingExecution(pageSize, objSet, mapper, HashSet::new);
    }

    public static <T, V> Set<V> shardingExecution(long pageSize, Set<T> objSet, Function<Set<T>, Set<V>> mapper) {
        return shardingExecutionWithResult(pageSize, objSet, mapper, HashSet::new, HashSet::new);
    }

    public static <T, V, C extends Collection<T>, D extends Collection<V>> D shardingExecutionWithResult(long pageSize, C objList, Function<C, D> mapper, Supplier<C> inputCollectionFactory, Supplier<D> outputCollectionFactory) {
        Objects.requireNonNull(objList);
        long totalPage = getTotalPage(objList.size(), pageSize);
        return LongStream.range(1, totalPage + 1)
                .parallel()
                .mapToObj(index -> {
                    C page = getPage(index, pageSize, objList, inputCollectionFactory);
                    return mapper.apply(page);
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(outputCollectionFactory));
    }

    public static <T, C extends Collection<T>> void shardingExecution(long pageSize, C objList, Consumer<C> mapper, Supplier<C> inputCollectionFactory) {
        Objects.requireNonNull(objList);
        long totalPage = getTotalPage(objList.size(), pageSize);
        LongStream.range(1, totalPage + 1).parallel().forEach(index -> {
            C page = getPage(index, pageSize, objList, inputCollectionFactory);
            mapper.accept(page);
        });
    }

    public static <T, C extends Collection<T>> C getPage(long pageNum, long pageSize, C dataList, Supplier<C> collectionFactory) {
        return dataList.stream()
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toCollection(collectionFactory));
    }

    public static long getTotalPage(long total, long pageSize) {
        assert pageSize >= 0;
        return total / pageSize + (total % pageSize == 0 ? 0 : 1);
    }
}

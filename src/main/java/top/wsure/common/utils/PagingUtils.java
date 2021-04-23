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

    /**
     * Waring! This method is using stream.parallel()
     *
     * @param pageSize
     * @param objList
     * @param mapper
     * @param <T>
     */
    public static <T> void shardingExecutionParallel(long pageSize, List<T> objList, Consumer<List<T>> mapper) {
        shardingExecution(pageSize, objList, mapper, ArrayList::new, true);
    }

    /**
     * Waring! This method is using stream.parallel()
     *
     * @param pageSize
     * @param objList
     * @param mapper
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> List<V> shardingExecutionParallel(long pageSize, List<T> objList, Function<List<T>, List<V>> mapper) {
        return shardingExecutionWithResult(pageSize, objList, mapper, ArrayList::new, ArrayList::new, true);
    }

    /**
     * Waring! This method is using stream.parallel()
     *
     * @param pageSize
     * @param objSet
     * @param mapper
     * @param <T>
     */
    public static <T> void shardingExecutionParallel(long pageSize, Set<T> objSet, Consumer<Set<T>> mapper) {
        shardingExecution(pageSize, objSet, mapper, HashSet::new, true);
    }

    /**
     * Waring! This method is using stream.parallel()
     *
     * @param pageSize
     * @param objSet
     * @param mapper
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> Set<V> shardingExecutionParallel(long pageSize, Set<T> objSet, Function<Set<T>, Set<V>> mapper) {
        return shardingExecutionWithResult(pageSize, objSet, mapper, HashSet::new, HashSet::new, true);
    }

    public static <T> void shardingExecution(long pageSize, List<T> objList, Consumer<List<T>> mapper) {
        shardingExecution(pageSize, objList, mapper, ArrayList::new, false);
    }

    public static <T, V> List<V> shardingExecution(long pageSize, List<T> objList, Function<List<T>, List<V>> mapper) {
        return shardingExecutionWithResult(pageSize, objList, mapper, ArrayList::new, ArrayList::new, false);
    }

    public static <T> void shardingExecution(long pageSize, Set<T> objSet, Consumer<Set<T>> mapper) {
        shardingExecution(pageSize, objSet, mapper, HashSet::new, false);
    }

    public static <T, V> Set<V> shardingExecution(long pageSize, Set<T> objSet, Function<Set<T>, Set<V>> mapper) {
        return shardingExecutionWithResult(pageSize, objSet, mapper, HashSet::new, HashSet::new, false);
    }

    public static <T, V, C extends Collection<T>, D extends Collection<V>> D shardingExecutionWithResult(long pageSize, C objList, Function<C, D> mapper, Supplier<C> inputCollectionFactory, Supplier<D> outputCollectionFactory, boolean isParallel) {
        Objects.requireNonNull(objList);
        long totalPage = getTotalPage(objList.size(), pageSize);
        return createLongStream(totalPage, isParallel)
                .mapToObj(index -> {
                    C page = getPage(index, pageSize, objList, inputCollectionFactory);
                    return mapper.apply(page);
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(outputCollectionFactory));
    }

    public static <T, C extends Collection<T>> void shardingExecution(long pageSize, C objList, Consumer<C> mapper, Supplier<C> inputCollectionFactory, boolean isParallel) {
        Objects.requireNonNull(objList);
        long totalPage = getTotalPage(objList.size(), pageSize);
        createLongStream(totalPage, isParallel).forEach(index -> {
            C page = getPage(index, pageSize, objList, inputCollectionFactory);
            mapper.accept(page);
        });
    }

    public static LongStream createLongStream(long totalPage, boolean isParallel) {
        LongStream stream = LongStream.range(1, totalPage + 1);
        return isParallel ? stream.parallel() : stream;
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

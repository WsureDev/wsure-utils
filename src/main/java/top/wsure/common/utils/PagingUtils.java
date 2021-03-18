package top.wsure.common.utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
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
        Objects.requireNonNull(objList);
        long totalPage = getTotalPage(objList.size(), pageSize);
        LongStream.range(1, totalPage + 1).parallel().forEach(index -> {
            List<T> page = getPage(index,pageSize,objList);
            mapper.accept(page);
        });
    }

    public static <T,V> List<V> shardingExecution(long pageSize, List<T> objList, Function<List<T>,List<V>> mapper){
        Objects.requireNonNull(objList);
        long totalPage = getTotalPage(objList.size(), pageSize);
        return LongStream.range(1, totalPage + 1).parallel()
                .mapToObj( index -> {
                    List<T> page = getPage(index,pageSize,objList);
                    return mapper.apply(page);
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static <T> List<T> getPage(long pageNum,long pageSize,List<T> dataList){
       return dataList.stream()
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public static long getTotalPage(long total, long pageSize) {
        return total / pageSize + (total % pageSize == 0 ? 0 : 1);
    }
}

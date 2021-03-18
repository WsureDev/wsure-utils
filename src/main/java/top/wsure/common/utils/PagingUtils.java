package top.wsure.common.utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
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
            List<T> page = objList.stream()
                    .skip((index - 1) * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());

            mapper.accept(page);
        });
    }


    public static long getTotalPage(long total, long pageSize) {
        return total / pageSize + (total % pageSize == 0 ? 0 : 1);
    }
}

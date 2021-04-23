package top.wsure.common.utils;

import top.wsure.common.functions.Interval;
import top.wsure.common.functions.Range;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * FileName: IntervalUtils
 * Author:   wsure
 * Date:     2021/4/22 3:21 下午
 * Description:
 */
public class IntervalUtils {

    public static <T> List<T> intervalCollectList(long start, long total, long step, Interval<Long, Long, List<T>> fun) {
        return execute(start, total, step, fun, ArrayList::new, false);
    }

    public static <T> Set<T> intervalCollectSet(long start, long total, long step, Interval<Long, Long, Set<T>> fun) {
        return execute(start, total, step, fun, HashSet::new, false);
    }

    public static <T> List<T> intervalCollectListParallel(long start, long total, long step, Interval<Long, Long, List<T>> fun) {
        return execute(start, total, step, fun, ArrayList::new, true);
    }

    public static <T> Set<T> intervalCollectSetParallel(long start, long total, long step, Interval<Long, Long, Set<T>> fun) {
        return execute(start, total, step, fun, HashSet::new, true);
    }

    public static <T, C extends Collection<T>> C execute(long start, long total, long step, Interval<Long, Long, C> fun, Supplier<C> collectionFactory, boolean isParallel) {
        long totalPage = PagingUtils.getTotalPage(total - start + 1, step);
        return PagingUtils.createLongStream(totalPage, isParallel)
                .mapToObj(index -> fun.acquire(start + (index - 1) * step, Math.min(start + index * step - 1, total)))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(collectionFactory));
    }

    public static void intervalExecute(long start, long total, long step, Range<Long, Long> fun) {
        execute(start, total, step, fun, false);
    }

    public static void intervalExecuteParallel(long start, long total, long step, Range<Long, Long> fun) {
        execute(start, total, step, fun, true);
    }

    public static void execute(long start, long total, long step, Range<Long, Long> fun, boolean isParallel) {
        long totalPage = PagingUtils.getTotalPage(total - start + 1, step);
        PagingUtils.createLongStream(totalPage, isParallel)
                .forEach(index -> fun.acquire(start + (index - 1) * step, Math.min(start + index * step - 1, total)));
    }

    public static <T> List<T> intervalCollectList(LocalDateTime start, LocalDateTime end, long delay, Interval<LocalDateTime, LocalDateTime, List<T>> fun) {
        return execute(start, end, delay, fun, ArrayList::new, false);
    }

    public static <T> Set<T> intervalCollectSet(LocalDateTime start, LocalDateTime end, long delay, Interval<LocalDateTime, LocalDateTime, Set<T>> fun) {
        return execute(start, end, delay, fun, HashSet::new, false);
    }

    public static <T> List<T> intervalCollectListParallel(LocalDateTime start, LocalDateTime end, long delay, Interval<LocalDateTime, LocalDateTime, List<T>> fun) {
        return execute(start, end, delay, fun, ArrayList::new, true);
    }

    public static <T> Set<T> intervalCollectSetParallel(LocalDateTime start, LocalDateTime end, long delay, Interval<LocalDateTime, LocalDateTime, Set<T>> fun) {
        return execute(start, end, delay, fun, HashSet::new, true);
    }

    public static <T, C extends Collection<T>> C execute(LocalDateTime start, LocalDateTime end, long delay, Interval<LocalDateTime, LocalDateTime, C> fun, Supplier<C> collectionFactory, boolean isParallel) {
        long totalPage = PagingUtils.getTotalPage(dateToLong(end) - dateToLong(start) + 1, delay);
        return PagingUtils.createLongStream(totalPage, isParallel)
                .mapToObj(index -> fun.acquire(longToDate(dateToLong(start) + (index - 1) * delay), longToDate(Math.min(dateToLong(start) + index * delay - 1, dateToLong(end)))))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(collectionFactory));
    }

    public static void intervalExecute(LocalDateTime start, LocalDateTime end, long delay, Range<LocalDateTime, LocalDateTime> fun) {
        execute(start, end, delay, fun, false);
    }

    public static void intervalExecuteParallel(LocalDateTime start, LocalDateTime end, long delay, Range<LocalDateTime, LocalDateTime> fun) {
        execute(start, end, delay, fun, true);
    }

    public static void execute(LocalDateTime start, LocalDateTime end, long delay, Range<LocalDateTime, LocalDateTime> fun, boolean isParallel) {
        long totalPage = PagingUtils.getTotalPage(dateToLong(end) - dateToLong(start) + 1, delay);
        PagingUtils.createLongStream(totalPage, isParallel)
                .forEach(index -> fun.acquire(longToDate(dateToLong(start) + (index - 1) * delay), longToDate(Math.min(dateToLong(start) + index * delay - 1, dateToLong(end)))));
    }

    public static LocalDateTime longToDate(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static long dateToLong(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}

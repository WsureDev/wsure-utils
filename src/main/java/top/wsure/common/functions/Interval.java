package top.wsure.common.functions;

/**
 * FileName: Interval
 * Author:   wsure
 * Date:     2021/4/22 3:18 下午
 * Description:区间
 */
@FunctionalInterface
public interface Interval<S,E,V> {
    V acquire(S start,E end);
}

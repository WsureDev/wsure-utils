package top.wsure.common.functions;

/**
 * FileName: Range
 * Author:   wsure
 * Date:     2021/4/22 8:58 下午
 * Description:
 */
@FunctionalInterface
public interface Range<S,E> {
    void acquire(S start,E end);
}

# Wsure-Utils
    一些简单的工具封装，减少开发过程中的重复冗长代码
## 注意⚠️ ：本项目还在开发中，如果没有测试过代码，请勿轻易使用

## 功能分类
1. 分页工具 (PagingUtils)
    1. `分页执行-无返回`: `PagingUtils.shardingExecution(long pageSize, List<T> objList, Consumer<List<T>> mapper)`
    2. `分页执行-有返回`: `List<V> PagingUtils.shardingExecution(long pageSize, List<T> objList, Function<List<T>,List<V>> mapper)` 
2. Nullable对象操作工具 (NullableUtils)
    1. `从Nullable对象中获取属性`: `V NullableUtils.get(T obj, Function<? super T,? extends V> mapper)`
3. 枚举工具 (EnumUtils)
    1.  `从枚举类中获取目标枚举值`: `E EnumUtils.query(Class<E> e, Predicate<E> filter)`
4. 重试工具 (RetryUtils)
    1.  `重试n次并每次都等待m毫秒`: `T RetryUtils.retry(Supplier<? extends T> retry, int tryTimes, long waitTime)`
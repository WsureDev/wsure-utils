package top.wsure.common.utils;

import java.util.EnumSet;
import java.util.function.Predicate;

public class EnumUtils {

    public static <E extends Enum<E>> E query(Class<E> e, Predicate<E> filter) {
        return EnumSet.allOf(e)
                .stream()
                .filter(filter)
                .findFirst()
                .orElse(null);
    }

    public static void main(String[] args) {
        TestEnum testEnum = query(TestEnum.class, e -> e.getDesc().equals("A13"));
        System.out.println(NullableUtils.get(testEnum, TestEnum::getDesc));
    }
}

enum TestEnum {
    T1("A1"),
    T2("A2"),
    ;

    public String getDesc() {
        return desc;
    }

    private final String desc;

    TestEnum(String desc) {
        this.desc = desc;
    }
}

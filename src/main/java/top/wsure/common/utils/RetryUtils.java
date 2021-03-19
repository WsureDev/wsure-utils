package top.wsure.common.utils;

import java.util.function.Supplier;

public class RetryUtils {

    public static <T> T retry(Supplier<? extends T> retry, int tryTimes, long waitTime){
        T res = null;

        for(int index = 0; index < tryTimes; index ++){
            try {
                res = retry.get();
                break;
            } catch (Exception e){
                e.printStackTrace();
                waitForRetry(waitTime);
            }
        }

        return res;
    }

    private static void waitForRetry(long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

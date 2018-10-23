package uk.co.kenfos.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TimeUtils {

    public static void sleepFor(long period, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(period);
        } catch (InterruptedException exception) {
            log.debug(exception.getMessage());
        }
    }
}
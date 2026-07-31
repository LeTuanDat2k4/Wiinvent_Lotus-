package com.Wiinvent.Lotus.core.ratelimit;

import org.redisson.api.RateIntervalUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    String key() default "";

    long rate() default 10;

    long rateInterval() default 1;

    RateIntervalUnit rateIntervalUnit() default RateIntervalUnit.MINUTES;

    RateLimitType type() default RateLimitType.IP;
}

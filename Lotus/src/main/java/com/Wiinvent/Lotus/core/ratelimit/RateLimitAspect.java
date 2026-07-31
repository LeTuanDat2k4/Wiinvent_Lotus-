package com.Wiinvent.Lotus.core.ratelimit;

import com.Wiinvent.Lotus.core.exception.RateLimitExceededException;
import com.Wiinvent.Lotus.core.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private static final String RATE_LIMIT_PREFIX = "ratelimit:";
    private final RedissonClient redissonClient;

    @Before("@annotation(rateLimit)")
    public void interceptRateLimit(JoinPoint joinPoint, RateLimit rateLimit) {
        String identifier = resolveIdentifier(rateLimit.type());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String keyPrefix = StringUtils.hasText(rateLimit.key())
                ? rateLimit.key()
                : method.getDeclaringClass().getSimpleName() + ":" + method.getName();

        String rateKey = RATE_LIMIT_PREFIX + keyPrefix + ":" + identifier;

        RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateKey);
        rateLimiter.trySetRate(
                RateType.OVERALL,
                rateLimit.rate(),
                Duration.ofMillis(rateLimit.rateIntervalUnit().toMillis(rateLimit.rateInterval()))
        );

        boolean acquired = rateLimiter.tryAcquire(1);
        if (!acquired) {
            throw new RateLimitExceededException("Bạn đã vượt quá số lần yêu cầu cho phép. Vui lòng thử lại sau.");
        }
    }

    private String resolveIdentifier(RateLimitType type) {
        if (type == RateLimitType.USER) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
                return "user:" + principal.getId();
            }
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (StringUtils.hasText(xForwardedFor)) {
                return "ip:" + xForwardedFor.split(",")[0].trim();
            }
            return "ip:" + request.getRemoteAddr();
        }

        return "unknown";
    }
}

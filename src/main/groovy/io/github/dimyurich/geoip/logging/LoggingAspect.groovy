package io.github.dimyurich.geoip.logging

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class)

    @Around("@annotation(LogExecutionTime)")
    Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis()
        Throwable thrown = null

        Object proceed = null
        try {
            proceed = joinPoint.proceed()
        } catch (Throwable t) {
            thrown = t
        }

        long executionTime = System.currentTimeMillis() - start

        logger.info("{} for ipAddress={} executed in reqTime={} ms", joinPoint.getSignature(), joinPoint.getArgs()[0], executionTime)

        if (thrown != null) {
            throw thrown
        }

        return proceed

    }
}
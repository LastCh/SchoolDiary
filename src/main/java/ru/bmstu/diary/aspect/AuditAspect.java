package ru.bmstu.diary.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    private static final String LOG_PATH = "logs/app.log";

    @AfterReturning(pointcut = "execution(public * ru.bmstu.diary.service.*.*(..))", returning = "result")
    public void logSuccess(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();
        String args = Arrays.toString(joinPoint.getArgs());
        String timestamp = LocalDateTime.now().toString();

        String log = String.format("[OK] %s | %s | Args: %s | Result: %s%n", timestamp, method, args, result);
        writeLog(log);
    }

    @AfterThrowing(pointcut = "execution(public * ru.bmstu.diary.service.*.*(..))", throwing = "e")
    public void logError(JoinPoint joinPoint, Throwable e) {
        String method = joinPoint.getSignature().toShortString();
        String args = Arrays.toString(joinPoint.getArgs());
        String timestamp = LocalDateTime.now().toString();

        String log = String.format("[ERROR] %s | %s | Args: %s | Exception: %s%n", timestamp, method, args, e);
        writeLog(log);
    }

    private void writeLog(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_PATH, true))) {
            writer.write(message);
        } catch (IOException ex) {
            System.err.println("Failed to write audit log: " + ex.getMessage());
        }
    }
}

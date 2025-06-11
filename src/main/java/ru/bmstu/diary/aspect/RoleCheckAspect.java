package ru.bmstu.diary.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RoleCheckAspect {
    @Around("@annotation(ru.bmstu.diary.annotation.TeacherOnly)")
    public Object checkTeacherRole(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String role = request.getHeader("X-Role");
        if (!"TEACHER".equalsIgnoreCase(role)) {
            throw new SecurityException("Only teachers can perform this action");
        }
        return joinPoint.proceed();
    }
}
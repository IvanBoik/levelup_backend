package com.boiko_ivan.spring.levelup_back.aop;

import com.boiko_ivan.spring.levelup_back.annotations.ValidateMultipartFile;
import com.boiko_ivan.spring.levelup_back.exceptions.InvalidMultipartFileException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;


@Aspect
@Component
public class MultipartFileValidatorAspect {

    @Before("@annotation(com.boiko_ivan.spring.levelup_back.annotations.ValidateMultipartFile)")
    public void validateMultipartFileAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String validType = methodSignature.getMethod().getAnnotation(ValidateMultipartFile.class).value().value;

        List<String> contentTypes = Arrays.stream(joinPoint.getArgs())
                .filter(x -> x instanceof MultipartFile)
                .map(x -> ((MultipartFile) x).getContentType())
                .toList();

        for (String contentType : contentTypes) {
            if (contentType == null) {
                throw new InvalidMultipartFileException("Unknown content type");
            }
            if (!contentType.contains(validType)) {
                throw new InvalidMultipartFileException("Unsupported content type: %s".formatted(contentType));
            }
        }
    }
}

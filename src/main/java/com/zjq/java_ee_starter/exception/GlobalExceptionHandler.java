package com.zjq.java_ee_starter.exception;

import java.lang.reflect.Method;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Object handleException(
            Exception e,
            HandlerMethod handlerMethod,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 检查处理器方法是否标注了 @ResponseBody 或 @RestController
        boolean returnsJson = isRestController(handlerMethod);

        if (returnsJson) {
            return handleApiException(e, request, response);
        } else {
            return handleViewException(e, request, response);
        }
    }

    private ResponseEntity<String> handleApiException(
            Exception e,
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.internalServerError()
                .body(e.getMessage());
    }

    private ModelAndView handleViewException(
            Exception e,
            HttpServletRequest request,
            HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.addObject("exception", e);
        mav.addObject("exceptionMessage", e.getMessage());
        mav.addObject("exceptionType", e.getClass().getSimpleName());
        mav.addObject("fullException", e.toString());
        mav.setViewName("error");
        return mav;
    }

    private boolean isRestController(HandlerMethod handlerMethod) {
        // 检查类级别
        Class<?> beanType = handlerMethod.getBeanType();

        // 如果有 @RestController 注解
        if (beanType.isAnnotationPresent(RestController.class)) {
            return true;
        }

        // 如果有 @ResponseBody 注解
        if (handlerMethod.hasMethodAnnotation(ResponseBody.class) ||
                beanType.isAnnotationPresent(ResponseBody.class)) {
            return true;
        }

        // 检查方法返回类型
        Method method = handlerMethod.getMethod();
        Class<?> returnType = method.getReturnType();

        // 如果返回的是 ResponseEntity 或类似API响应类型
        if (ResponseEntity.class.isAssignableFrom(returnType) ||
                returnType == String.class && method.isAnnotationPresent(ResponseBody.class)) {
            return true;
        }

        return false;
    }

}
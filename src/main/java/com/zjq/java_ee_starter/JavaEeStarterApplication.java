package com.zjq.java_ee_starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import org.springframework.boot.context.event.ApplicationFailedEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class JavaEeStarterApplication {

    private static final AtomicReference<String> exitReason = new AtomicReference<>("正常退出");

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JavaEeStarterApplication.class);

        // 添加事件监听器
        app.addListeners(
                (ApplicationListener<ApplicationFailedEvent>) event -> {
                    exitReason.set("启动失败: " + event.getException().getMessage());
                },
                (ApplicationListener<ContextClosedEvent>) event -> {
                    // 可以在这里记录更多关闭上下文信息
                    System.out.println("应用上下文关闭，退出原因: " + exitReason.get());
                });

        try {

            ApplicationContext ctx = app.run(args);
            // 检查控制器是否被注册
            RequestMappingHandlerMapping mapping = ctx.getBean(RequestMappingHandlerMapping.class);
            System.out.println("Registered controllers: " + mapping.getHandlerMethods().keySet());

        } catch (Exception ex) {
            System.out.println("启动失败: " + ex.getMessage());
            exitReason.set("启动失败: " + ex.getMessage());
            throw ex;
        }

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("关闭原因: " + exitReason.get());
            System.out.println("执行资源清理...");
        }));
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

}

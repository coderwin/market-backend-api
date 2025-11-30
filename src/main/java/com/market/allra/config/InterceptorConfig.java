package com.market.allra.config;

import com.market.allra.web.interceptor.SessionInitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig  implements WebMvcConfigurer {

    private final SessionInitInterceptor sessionInitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInitInterceptor)
                .addPathPatterns("/**");
    }
}

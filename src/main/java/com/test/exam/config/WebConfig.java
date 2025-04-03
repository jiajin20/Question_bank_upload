package com.test.exam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")  // 限制跨域配置应用于特定路径
                    .allowedOrigins("http://127.0.0.1:8081", "null","http://localhost:8081")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .exposedHeaders("Authorization", "Content-Disposition");
        }



    // 配置静态资源路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "file:" + System.getProperty("user.dir") + "/static/img/");
    }

    // 配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor)
//                .addPathPatterns("/**")  // 拦截所有请求
//                .excludePathPatterns(  // 排除不需要认证的路径
//                        "/static/**",
//                        "/",
//                        "/user",
//                        "/user/loginjson",
//                        "/user/loginform",
//                        "/user/teacherregisterform",
//                        "/user/teacherregisterjson",
//                        "/user/studentregisterjson",
//                        "/user/studentregisterform",
//                        "/user/logout",
//                        "/user/getuser",
//                        "/user/updatapasswordform",
//                        "/user/updatapasswordjson",
//                        "/swagger-resources/**",
//                        "/v3/api-docs",
//                        "/webjars/**",
//                        "/doc.html"
//                );
    }
}

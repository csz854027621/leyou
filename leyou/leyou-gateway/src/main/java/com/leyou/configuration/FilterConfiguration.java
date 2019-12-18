package com.leyou.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class FilterConfiguration {

    /**
     * 使用 cors 方法允许 manage.leyou.com 跨域
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("http://manage.leyou.com");  //可被跨域的域名
        configuration.addAllowedOrigin("http://www.leyou.com");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //被允许的所有资源
        CorsFilter corsFilter = new CorsFilter(source);

        return corsFilter;
    }


}

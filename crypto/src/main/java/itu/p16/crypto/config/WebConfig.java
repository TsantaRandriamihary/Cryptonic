package itu.p16.crypto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import itu.p16.crypto.interceptor.SessionInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**") // Appliquer à toutes les URL
                .excludePathPatterns(
                    "/",          // Exclure la page d'accueil
                    "/pin/confirm",          // Exclure la page d'accueil
                    "/backoffice/login",          // Exclure la page de login
                    "/backoffice/login/login",          // Exclure la page de login
                    "/frontoffice/login",          // Exclure la page de login
                    "/frontoffice/login/login",          // Exclure la page de login
                    "/signup/**",      // Exclure les ressources publiques
                    "/css/**",         // Exclure les fichiers CSS
                    "/js/**",           // Exclure les fichiers JS
                    "/bootstrap/**",           // Exclure les fichiers bootstrap
                    "/chart.js/**",           // Exclure les fichiers chart.js
                    "/datatables/**",           // Exclure les fichiers datatables
                    "/fontawesome-free/**",           // Exclure les fichiers fontawesome-free
                    "/img/**",           // Exclure les fichiers img
                    "/jquery/**",           // Exclure les fichiers jquery
                    "/jquery-easing/**",           // Exclure les fichiers jquery-easing
                    "/new/**",           // Exclure les fichiers new
                    "/scss/**"           // Exclure les fichiers scss
                );
    }
}

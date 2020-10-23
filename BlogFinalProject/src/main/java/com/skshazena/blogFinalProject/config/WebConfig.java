package com.skshazena.blogFinalProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 23, 2020
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    public final static String IMAGE_RESOURCE_BASE = "/images/";
    public final static String IMAGE_FILE_BASE = "/Users/Shazena/Documents/GITHUB/CMS-SG-FinalProject/images/";
    public final static String BASE_URL = "http://localhost:8080";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(IMAGE_RESOURCE_BASE + "**")
                .addResourceLocations("file:" + IMAGE_FILE_BASE);
    }
}

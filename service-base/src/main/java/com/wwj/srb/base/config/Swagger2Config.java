package com.wwj.srb.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * /admin back-end server interface,/api front-end service interface
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * Generate interface document
     *
     * @return
     */
    @Bean
    public Docket adminApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    /**
     * Generate interface document
     *
     * @return
     */
    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }

    /**
     * Encapsulate service back-end interface document information
     *
     * @return
     */
    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("srb backend API documents")
                .description("The doc describes the way to invoke the backend interface of srb")
                .version("1.0")
                .contact(new Contact("zhezhang", null, "zhangzhe7373@gamil.com"))
                .build();
    }

    /**
     * Encapsulate service front-end interface document information
     *
     * @return
     */
    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("srb frontend API documents")
                .description("The doc describes the way to invoke the frontend interface of srb")
                .version("1.0")
                .contact(new Contact("zhezhang", null, "zhangzhe7373@gamil.com"))
                .build();
    }
}

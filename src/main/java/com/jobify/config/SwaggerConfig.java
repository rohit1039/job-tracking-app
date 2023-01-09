package com.jobify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig
{

    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * @return
     */
    private ApiKey apiKeys()
    {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    /**
     * @return
     */
    private List<SecurityContext> securityContexts()
    {
        return Arrays.asList(SecurityContext.builder().securityReferences(sr()).build());
    }

    /**
     * @return
     */
    private List<SecurityReference> sr()
    {

        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");

        return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[]{scope}));
    }

    /**
     * @return
     */
    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo())
                                                      .securityContexts(securityContexts())
                                                      .securitySchemes(Arrays.asList(apiKeys()))
                                                      .select().apis(RequestHandlerSelectors.any())
                                                      .apis(RequestHandlerSelectors.basePackage("org.springframework.boot").negate()) // remove basic error controller from Swagger UI
                                                      .paths(PathSelectors.any()).build();
    }

    /**
     * @return
     */
    private ApiInfo getInfo()
    {

        return new ApiInfo("Jobify", "This project is developed by Rohit Parida", "1.0", "Terms of service",
            new Contact("Rohit Parida", "www.github.com/rohit1039", "rohitparida0599@gmail.com"), "License",
            "APIs license", Collections.emptyList());
    }

}

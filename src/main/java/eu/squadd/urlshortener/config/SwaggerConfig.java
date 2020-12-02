package eu.squadd.urlshortener.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfig {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metadata())
                .select()
                .apis(RequestHandlerSelectors.basePackage("eu.squadd.urlshortener"))
                .build();
    }

    private ApiInfo metadata(){
        return new ApiInfoBuilder()
                .title("Url Converter API")
                .description("API reference for developers")
                .version("1.0")
                .build();
    }
}

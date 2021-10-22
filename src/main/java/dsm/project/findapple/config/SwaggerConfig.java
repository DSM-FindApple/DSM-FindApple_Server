package dsm.project.findapple.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket apiAuth() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("apis")
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, getAuthResponses())
                .globalResponseMessage(RequestMethod.DELETE, getAuthResponses())
                .globalResponseMessage(RequestMethod.PUT, getAuthResponses())
                .globalResponseMessage(RequestMethod.GET, getAuthResponses())
                .apiInfo(this.getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("dsm.project.findapple.controller"))
                .build();
    }

    //error option
    public List<ResponseMessage> getAuthResponses() {
        List<ResponseMessage> builders = new ArrayList<>();
        builders.add(
                new ResponseMessageBuilder()
                        .code(500)
                        .message("server error")
                        .responseModel(new ModelRef("Error"))
                        .build()
        );
        builders.add(
                new ResponseMessageBuilder()
                        .code(403)
                        .message("Forbidden(no token)")
                        .build()
        );
        builders.add(
                new ResponseMessageBuilder()
                        .code(401)
                        .message("Unauthorized")
                        .build()
        );
        builders.add(
                new ResponseMessageBuilder()
                        .code(400)
                        .message("Bad request")
                        .build()
        );
        return builders;
    }

    //error option
    public List<ResponseMessage> getUserResponses() {
        List<ResponseMessage> builders = new ArrayList<>();
        builders.add(
                new ResponseMessageBuilder()
                        .code(400)
                        .message("Bad Ex Request")
                        .build()
        );
        builders.add(
                new ResponseMessageBuilder()
                        .code(400)
                        .message("Bad request user image")
                        .build()
        );
        builders.add(
                new ResponseMessageBuilder()
                        .code(409)
                        .message("user already signed")
                        .build()
        );
        builders.add(
                new ResponseMessageBuilder()
                        .code(500)
                        .message("server error")
                        .responseModel(new ModelRef("Error"))
                        .build()
        );

        return builders;
    }

    //api info
    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "FindApple API Docs",
                "FindApple Project Docs",
                "1.0.0",
                "Terms of Service URL",
                "Contact Name",
                "License",
                "License URL"
        );
    }

    //resource handlers
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
}

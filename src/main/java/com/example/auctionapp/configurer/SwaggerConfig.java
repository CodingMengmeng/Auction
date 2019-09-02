package com.example.auctionapp.configurer;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger 配置文件
 * @Author: chenjing
 * @CreateDate: 2018/2/14
 * @Version: 1.0
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {


	@Bean
	public Docket api(){
		//添加head参数
		ParameterBuilder parameterBuilder = new ParameterBuilder();
		List<Parameter> parameterList = new ArrayList<>();
		parameterBuilder.name("userId").description("当前登录人id").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue("35").build();
		ParameterBuilder parameterBuilder1 = new ParameterBuilder();
		parameterBuilder1.name("token").description("当前登录人token").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue("0de39ffa0fde4ac5a4aa939632d06fed").build();
		parameterList.add(parameterBuilder.build());
		parameterList.add(parameterBuilder1.build());
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.example.auctionapp.controller"))
				.paths(PathSelectors.any())
//				.paths(PathSelectors.regex("^\\/(auth|my|pay|bankCard|remittance|deal).*$"))
				.build()
				.globalOperationParameters(parameterList)
//				.securitySchemes(securitySchemes())
//				.securityContexts(securityContexts())
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
				.title("Spring Boot中使用Swagger2构建RESTful APIs")
				.description("注：此处所有链接在真实调用时，需去掉url中的/api")
				.termsOfServiceUrl("http://127.0.0.1:8066/api")
				.version("1.0")
				.build();
	}

	private List<ApiKey> securitySchemes(){
		ApiKey apiKey = new ApiKey("userId", "userId", "header");
		ApiKey apiKey1 = new ApiKey("token", "token", "header");
		List<ApiKey> list = new ArrayList<>();
		list.add(apiKey);
		list.add(apiKey1);
		return list;
	}

	private List<SecurityContext> securityContexts() {
		SecurityContext securityContext = SecurityContext.builder()
				.securityReferences(defaultAuth())
//				.forPaths(PathSelectors.regex("^(?!auth).*$"))
				.forPaths(PathSelectors.any())
				.build();
		List<SecurityContext> list = new ArrayList<>();
		list.add(securityContext);
		return list;
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		SecurityReference securityReference = new SecurityReference("Authorization", authorizationScopes);
		List<SecurityReference> list = new ArrayList<>();
		list.add(securityReference);
		return list;
	}
}

package com.notice_board.common.config;

import com.notice_board.common.annotation.ApiErrorCodeExample;
import com.notice_board.common.annotation.ApiErrorCodeExamples;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.CommonExceptionResultMessage;
import com.notice_board.common.component.JSONResult;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.notice_board.common.component.CommonExceptionResultMessage.SUCCESS;

/**
 * <pre>
 * Swagger Config
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-12-02 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("NOTICE BOARD")
                .description("NOTICE BOARD REST API")
                .version("1.0.0");
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeExamples apiErrorCodeExamples = handlerMethod.getMethodAnnotation(ApiErrorCodeExamples.class);

            if (apiErrorCodeExamples != null) {
                generateErrorCodeResponseExample(operation, apiErrorCodeExamples.value());
            } else {
                ApiErrorCodeExample apiErrorCodeExample = handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);

                if (apiErrorCodeExample != null) {
                    generateErrorCodeResponseExample(operation, apiErrorCodeExample.value());
                }
            }

            return operation;
        };
    }

    private void generateErrorCodeResponseExample(Operation operation, CommonExceptionResultMessage[] errorCodes) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<JSONResult>> statusWithExampleHolders = Arrays.stream(errorCodes)
                .map(
                        errorCode -> JSONResult.builder()
                                .holder(getSwaggerExample(errorCode))
                                .status(errorCode.getStatus().value())
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                )
                .collect(Collectors.groupingBy(JSONResult::getStatus));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private void generateErrorCodeResponseExample(Operation operation, CommonExceptionResultMessage errorCode) {
        ApiResponses responses = operation.getResponses();
        JSONResult exampleHolder = JSONResult.builder()
                .holder(getSwaggerExample(errorCode))
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        addExamplesToResponses(responses, exampleHolder);
    }

    private Example getSwaggerExample(CommonExceptionResultMessage errorCode) {
        BaseResponse res = new BaseResponse();
        res.setJsonResult(JSONResult.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
        Example example = new Example();
        example.setValue(res);
        return example;
    }

    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<JSONResult>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getMessage(),
                                    exampleHolder.getHolder()
                            )
                    );
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(String.valueOf(status), apiResponse);
                }
        );
    }

    private void addExamplesToResponses(ApiResponses responses, JSONResult exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getMessage(), exampleHolder.getHolder());
        content.addMediaType("application/json", mediaType);
        apiResponse.content(content);
        responses.addApiResponse(String.valueOf(exampleHolder.getStatus()), apiResponse);
    }
}

package com.notice_board.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		return new OpenAPI()
				.components(new Components())
				.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
				.title("NOTICE BOARD")
				.description("NOTICE BOARD REST API")
				.version("1.0.0");
	}
}

package com.notice_board.common.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *  응답 BASE DTO
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-11-30 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */

@Schema(description = "기본 응답 객체")
@Data
public class BaseResponse<T> implements Serializable {
    @JsonProperty("result")
    @Schema(description = "응답 상태", implementation = JSONResult.class)
    private JSONResult jsonResult; // 응답상태

    @Schema(description = "응답 데이터")
    private T data; // 응답데이터

    public BaseResponse() {
    }

    private BaseResponse(T data) {
        this.jsonResult = JSONResult.successBuilder();
        this.data = data;
    }

    public static <T> BaseResponse<T> from(T data) {
        return new BaseResponse<>(data);
    }
}

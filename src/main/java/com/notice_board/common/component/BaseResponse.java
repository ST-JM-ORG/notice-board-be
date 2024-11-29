package com.notice_board.common.component;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Data
public class BaseResponse implements Serializable {
    @JsonProperty("result")
    private JSONResult jsonResult; // 응답상태

    private Object data; // 응답데이터
}

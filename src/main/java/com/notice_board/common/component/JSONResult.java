package com.notice_board.common.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notice_board.common.exception.CustomException;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.examples.Example;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataAccessException;

import static com.notice_board.common.component.CommonExceptionResultMessage.*;


/**
 * <pre>
 *	Ajax 결과
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-11-30 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JSONResult {

	@Schema(hidden = true)
	@JsonIgnore
	private Example holder;

	@Schema(description = "HTTP 상태 코드", example = "200")
	private int status;

	@Schema(description = "응답 코드", example = "A000")
	private String code;

	@Schema(description = "응답 메시지", example = "요청 처리 성공")
	private String message;

	public static JSONResult successBuilder() {
		return JSONResult.builder()
				.status(SUCCESS.getStatus().value())
				.code(SUCCESS.getCode())
				.message(SUCCESS.getMessage())
				.build();
	}
	public static JSONResult successBuilderToMsg(String msg) {
		return JSONResult.builder()
				.status(SUCCESS.getStatus().value())
				.code(SUCCESS.getCode())
				.message(msg)
				.build();
	}

	public static JSONResult failBuilder(CustomException e) {
		return JSONResult.builder()
				.status(e.getResultMessage().getStatus().value())
				.code(e.getResultMessage().getCode())
				.message(e.getResultMessage().getMessage())
				.build();
	}
    public static JSONResult failBuilder(CustomException e, String message) {
        return JSONResult.builder()
            .status(e.getResultMessage().getStatus().value())
            .code(e.getResultMessage().getCode())
            .message(message)
            .build();
    }


	public static JSONResult failBuilder(Exception e) {
		return JSONResult.builder()
				.status(FAIL.getStatus().value())
				.code(FAIL.getCode())
				.message(FAIL.getMessage())
				.build();
	}

	public static JSONResult dbFailBuilder(DataAccessException e) {
		return JSONResult.builder()
				.status(DB_FAIL.getStatus().value())
				.code(DB_FAIL.getCode())
				.message(DB_FAIL.getMessage())
				.build();
	}

    public static JSONResult fileUploadExtFailBuilder(Exception e) {
        return JSONResult.builder()
            .status(FILE_UPLOAD_FAIL.getStatus().value())
            .code(FILE_UPLOAD_FAIL.getCode())
            .message("허용되지 않은 첨부파일 확장자입니다.")
            .build();
    }

    public static JSONResult fileUploadFailBuilder(Exception e) {
        return JSONResult.builder()
            .status(FILE_UPLOAD_FAIL.getStatus().value())
            .code(FILE_UPLOAD_FAIL.getCode())
            .message("첨부파일 업로드에 실패하였습니다.")
            .build();
    }

    public static JSONResult pwMismatchBuilder() {
        return JSONResult.builder()
            .status(PW_MISMATCH.getStatus().value())
            .code(PW_MISMATCH.getCode())
            .message("비밀번호가 일치하지 않습니다.")
            .build();
    }

    public static JSONResult commentsFailBuilderToMsg(String msg) {
        return JSONResult.builder()
            .status(FAIL.getStatus().value())
            .code(FAIL.getCode())
            .message(msg)
            .build();
    }

	public static JSONResult notFoundBuilder(Exception e) {
		return JSONResult.builder()
				.status(NOT_FOUND.getStatus().value())
				.code(NOT_FOUND.getCode())
				.message(NOT_FOUND.getMessage())
				.build();
	}

	public static JSONResult accessDenied() {
		return JSONResult.builder()
				.status(ACCESS_DENIED.getStatus().value())
				.code(ACCESS_DENIED.getCode())
				.message(ACCESS_DENIED.getMessage())
				.build();
	}

	public static JSONResult maxFileSize() {
		return JSONResult.builder()
				.status(MAX_FILE_SIZE.getStatus().value())
				.code(MAX_FILE_SIZE.getCode())
				.message(MAX_FILE_SIZE.getMessage())
				.build();
	}

	public static JSONResult entityNotFoundBuilder(EntityNotFoundException e) {
		return JSONResult.builder()
				.status(NOT_FOUND.getStatus().value())
				.code(NOT_FOUND.getCode())
				.message(NOT_FOUND.getMessage())
				.build();
	}

	public static JSONResult entityNotFoundBuilder(EntityNotFoundException e, String message) {
		return JSONResult.builder()
				.status(NOT_FOUND.getStatus().value())
				.code(NOT_FOUND.getCode())
				.message(message)
				.build();
	}
}

package com.notice_board.common.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * <pre>
 *	요청 처리 결과 메세지
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
@ToString
@AllArgsConstructor
public enum CommonExceptionResultMessage {

	/* JSON 결과 */
	SUCCESS(HttpStatus.OK, "A000", "요청 처리 성공"),
	FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E000", "요청 처리 실패"),
	DB_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "요청 DB 처리 실패"),
	FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E002", "파일 업로드에 실패하였습니다."),
	FILE_EXT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E003", "허용되지 않은 첨부파일 확장자입니다."),
	VALID_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E004", "유효성 검증에 실패하였습니다."),
	EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E005", "메일 전송에 실패하였습니다."),
	MAX_FILE_SIZE(HttpStatus.INTERNAL_SERVER_ERROR, "E006", "첨부파일 용량 초과입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "E404", "NOT FOUND"),

	/* 인증 */
	LOGIN_SUCCESS(HttpStatus.OK, "E100", "로그인 성공"),
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "E101", "아이디(로그인 이메일) 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요."),
	AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "E102", "Authentication failed."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "E103", "Access Denied."),
	JWT_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "E104", "Token has expired."),
	PW_MISMATCH(HttpStatus.BAD_REQUEST, "E106", "비밀번호가 일치하지않습니다."),
	EMAIL_DUPLICATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E110", "사용 중인 이메일입니다."),

	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E999", "알 수 없는 오류");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

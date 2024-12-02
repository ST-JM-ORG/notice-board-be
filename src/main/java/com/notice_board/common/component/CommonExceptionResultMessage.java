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
	FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E002", "파일 업로드 실패"),
	IMG_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E003", "이미지 업로드 실패"),
	VALID_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E004", "입력 필수 값 미입력"),
	EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E005", "메일 전송 실패"),
	MAX_FILE_SIZE(HttpStatus.INTERNAL_SERVER_ERROR, "E006", "첨부파일 용량 초과"),
	PROMOTION_MISMATCH(HttpStatus.INTERNAL_SERVER_ERROR, "E007", "관할기관 미일치"),
	INPUT_VALID_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E008", "잘못된 입력값"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "E404", "NOT FOUND"),

	/* 인증 */
	LOGIN_SUCCESS(HttpStatus.OK, "E100", "로그인 성공"),
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "E101", "로그인 실패"),
	AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "E102", "Authentication failed."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "E103", "Access Denied."),
	JWT_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "E104", "Token has expired."),
	SESSION_EXPIRED(HttpStatus.FORBIDDEN, "E105", "Invalid Session"),
	PW_MISMATCH(HttpStatus.UNAUTHORIZED, "E106", "비밀번호 미일치"),
	ACCOUNT_WAIT(HttpStatus.UNAUTHORIZED, "E107", "승인 대기중인 계정"),
	ACCOUNT_REJECT(HttpStatus.UNAUTHORIZED, "E108", "반려 계정"),
	VERIFY_FAIL(HttpStatus.UNAUTHORIZED, "E109", "SMS 인증 실패"),
	EMAIL_DUPLICATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E110", "사용 중인 이메일"),

	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E999", "알 수 없는 오류");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

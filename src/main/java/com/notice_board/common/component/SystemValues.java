package com.notice_board.common.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * <pre>
 *	시스템 String 상수
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
@Getter
@ToString
@AllArgsConstructor
public enum SystemValues {

	// 스레드
	PREFIX_THREAD_NAME("project-async-"),
	PREFIX_SCHEDULER_NAME("project-schedule-"),

	// 회원가입 인증
	SIGNUP_LOGIN_ID("signupId"),


    // 로그인
    COOKIE_REMEMBER_ME("remember-me");

	private final String value;
}

package com.notice_board.common.exception;

import com.notice_board.common.component.CommonExceptionResultMessage;
import lombok.Getter;

/**
 * <pre>
 *  유효성 검증 Exception
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2025-01-01 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Getter
public class ValidException extends RuntimeException {

	private final CommonExceptionResultMessage resultMessage;

    private final String target;

    public ValidException(CommonExceptionResultMessage resultMessage, String target) {
        this.resultMessage = resultMessage;
        this.target = target;
    }

    public ValidException(CommonExceptionResultMessage resultMessage, String target, String message) {
        super(message);
        this.resultMessage = resultMessage;
        this.target = target;
    }

    public ValidException(CommonExceptionResultMessage resultMessage, String target, String message, Throwable cause) {
        super(message, cause);
        this.resultMessage = resultMessage;
        this.target = target;
    }

    public ValidException(CommonExceptionResultMessage resultMessage, String target, Throwable cause) {
        super(cause);
        this.resultMessage = resultMessage;
        this.target = target;
    }

    public ValidException(CommonExceptionResultMessage resultMessage, String target, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.resultMessage = resultMessage;
        this.target = target;
    }
}

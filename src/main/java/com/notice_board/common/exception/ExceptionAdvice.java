package com.notice_board.common.exception;

import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.JSONResult;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

/**
 * <pre>
 *	전역 예외 처리기
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-11-30 초기작성
 * </pre>
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@ControllerAdvice
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class ExceptionAdvice {
	@ExceptionHandler(CustomException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Object osdException(HttpServletRequest request, CustomException e) {
		log.error("Exception URI : " + request.getRequestURI());
		log.error("OsdException : " + e.getMessage(), e);
		BaseResponse res = new BaseResponse();
		if (StringUtils.isEmpty(e.getMessage())) {
			res.setJsonResult(JSONResult.failBuilder(e));
		} else {
			res.setJsonResult(JSONResult.failBuilder(e, e.getMessage()));
		}
		return ResponseEntity.ok(res);
	}

	@ExceptionHandler({
			HttpClientErrorException.class,
			MissingServletRequestParameterException.class,
			NoResourceFoundException.class
	})
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Object httpClientErrorException(HttpServletRequest request, Exception e) {
		log.error("Exception URI : " + request.getRequestURI());
		log.error("NotFound : " + e.getMessage(), e);
		BaseResponse res = new BaseResponse();
		res.setJsonResult(JSONResult.notFoundBuilder(e));
		return ResponseEntity.ok(res);
	}

	@ExceptionHandler(DataAccessException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleDataAccessException(HttpServletRequest request, DataAccessException e) {
		log.error("Exception URI : " + request.getRequestURI());
		log.error("DataAccessException : " + e.getMessage(), e);

		BaseResponse res = new BaseResponse();
		res.setJsonResult(JSONResult.dbFailBuilder(e));
		return ResponseEntity.ok(res);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public Object handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
		log.error("AccessDeniedException URI : " + request.getRequestURI());
		log.error("AccessDeniedException : " + e.getMessage(), e);
		BaseResponse res = new BaseResponse();
		res.setJsonResult(JSONResult.accessDenied());
		return ResponseEntity.ok(res);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleMaxUploadSizeExceededException(HttpServletRequest request, MaxUploadSizeExceededException e) {
		log.error("MaxUploadSizeExceededException URI : " + request.getRequestURI());
		log.error("MaxUploadSizeExceededException : " + e.getMessage(), e);
		BaseResponse res = new BaseResponse();
		res.setJsonResult(JSONResult.maxFileSize());
		return ResponseEntity.ok(res);

	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Object handleEntityNotFoundException(HttpServletRequest request, EntityNotFoundException e) {
		log.error("EntityNotFoundException URI : " + request.getRequestURI());
		log.error("EntityNotFoundException : " + e.getMessage(), e);
		BaseResponse res = new BaseResponse();

		if (StringUtils.isEmpty(e.getMessage())) {
			res.setJsonResult(JSONResult.entityNotFoundBuilder(e));
		} else {
			res.setJsonResult(JSONResult.entityNotFoundBuilder(e, e.getMessage()));
		}
		return ResponseEntity.ok(res);

	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Object exception(HttpServletRequest request, Exception e) {
		log.error("Exception URI : " + request.getRequestURI());
		log.error("Exception : " + e.getMessage(), e);
		BaseResponse res = new BaseResponse();
		res.setJsonResult(JSONResult.failBuilder(e));
		return ResponseEntity.ok(res);

	}
}

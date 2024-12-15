package com.notice_board.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notice_board.common.component.BaseResponse;
import com.notice_board.common.component.JSONResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

@Slf4j(topic = "ACCESS_DENIED_EXCEPTION_HANDLER") // 403
@AllArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Access Denied Request", accessDeniedException);

        BaseResponse res = new BaseResponse();
        res.setJsonResult(JSONResult.accessDenied());

        String responseBody = objectMapper.writeValueAsString(res);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}

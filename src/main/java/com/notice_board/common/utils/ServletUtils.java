package com.notice_board.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * <pre>
 * Spring에서 제공하는 RequestContextHolder 를 이용하여
 * request, session 객체를 service까지 전달하지 않고 사용할 수 있게 해줌
 * </pre>
 * <pre>
 * <b>History:</b>
 *    Park Jun Mo, 1.0, 2024-11-30 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @since 1.0
 * @version 1.0
 * @see RequestContextHolder#getRequestAttributes()
 * @see ServletRequestAttributes#getRequest()
 */
@UtilityClass
public class ServletUtils {

	private final String X_REQUESTED_WITH = "X-Requested-With";
	private final String XMLHTTPREQUEST = "XMLHTTPREQUEST";

	/**
	 * Request 객체  반환
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(requestAttributes != null) {
			return requestAttributes.getRequest();
		}

		throw new RuntimeException("HttpServletRequest is null !!");
	}

	/**
	 * Request에서 도메인 URL 가져옴
	 * @return String
	 */
	public String getDomainUrl2String() {
		return getRequest().getRequestURL().toString().replaceAll(getRequest().getRequestURI(), "");
	}

	public URL getDomainUrl() {
		try {
			return new URL(getRequest().getRequestURL().toString().replaceAll(getRequest().getRequestURI(), ""));
		} catch (MalformedURLException e) {
			return getURL();
		}
	}

	/**
	 * <pre>
	 * Request 도메인으로 URL 객체 생성
	 * </pre>
	 * @return URL
	 */
	public URL getURL() {
		try {
			return new URL(getRequest().getRequestURL().toString());
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * <pre>
	 * URL String 생성
	 * </pre>
	 * @param path Url
	 * @return String
	 */
	public String getCreateRootUrlPath(String path) {
		try {
			return new URL(getDomainUrl(), Optional.ofNullable(path).map(String::trim).orElse("/")).getPath();
		} catch (MalformedURLException e) {
			URL domainUrl = getDomainUrl();
			if(domainUrl != null) {
				return domainUrl.getPath();
			}

			throw new RuntimeException("getDomainUrl is null !!");
		}
	}

	/**
	 * <pre>
	 * URL String 생성
	 * </pre>
	 * @param path Url
	 * @return String
	 */
	public String getCreateUrlPath(String path) {
		try {
			return new URL(getURL(), Optional.ofNullable(path).map(String::trim).orElse("/")).getPath();
		} catch (MalformedURLException e) {
			URL url = getURL();
			if(url != null) {
				return url.getPath();
			}

			throw new RuntimeException("URL is null !!");
		}
	}

	/**
	 * Request에서 사용자 IP 가져옴
	 * @return String
	 */
	public String getClientIP() {
		HttpServletRequest request = getRequest();

		String clientIP = request.getHeader("X-Forwarded-For");
		String unknown = "unknown";

		if(StringUtils.isEmpty(clientIP) || unknown.equalsIgnoreCase(clientIP)) {
			clientIP = request.getHeader("Proxy-Client-IP");
		}
		if(StringUtils.isEmpty(clientIP) || unknown.equalsIgnoreCase(clientIP)) {
			clientIP = request.getHeader("WL-Proxy-Client-IP");
		}
		if(StringUtils.isEmpty(clientIP) || unknown.equalsIgnoreCase(clientIP)) {
			clientIP = request.getHeader("HTTP_CLIENT_IP");
		}
		if(StringUtils.isEmpty(clientIP) || unknown.equalsIgnoreCase(clientIP)) {
			clientIP = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if(StringUtils.isEmpty(clientIP) || unknown.equalsIgnoreCase(clientIP)) {
			clientIP = request.getRemoteAddr();
		}

		return clientIP;
	}

	/**
	 * Ajax 요청인지 확인
	 * @return boolean
	 */
	public boolean isAjaxRequest() {
		return getRequest().getHeader(X_REQUESTED_WITH) != null && getRequest().getHeader(X_REQUESTED_WITH).equalsIgnoreCase(XMLHTTPREQUEST);
	}

	/**
	 * Ajax 요청인지 확인
	 * @param request HttpServletRequest
	 * @return boolean
	 */
	public boolean isAjaxRequest(HttpServletRequest request) {
		return request.getHeader(X_REQUESTED_WITH) != null && request.getHeader(X_REQUESTED_WITH).equalsIgnoreCase(XMLHTTPREQUEST);
	}

	/**
	 * Request의 속성값 가져옴
	 * @param name 속성명
	 * @return Object
	 */
	public Object getRequestAttribute(String name) {
		return getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

	/**
	 * Request의 전체 속성명 가져옴
	 * @return String Array
	 */
	public String[] getRequestAttributeNames() {
		return getAttributeNames(RequestAttributes.SCOPE_REQUEST);
	}

    /**
     * Request의 속성값 설정
     * @param name 속성명
     * @param object 데이터
     */
    public void setRequestAttribute(String name, Object object) {
		setAttribute(name, object, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * Request의 속성값 삭제
     * @param name 속성명
     */
    public void removeRequestAttribute(String name) {
		removeAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

	/**
	 * Session 객체 반환
	 * @return HttpSession
	 */
	public HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * Session에서 Document Root 가져옴
	 * @return String
	 */
	public String getRealPath() {
		return getSession().getServletContext().getRealPath("/");
	}

	/**
	 * Session 초기화
	 */
	public void sessionInvalidate() {
		getRequest().getSession().invalidate();
	}

	/**
	 * Session의 속성값 가져옴
	 * @param name 속성명
	 * @return Object
	 */
	public Object getSessionAttribute(String name) {
        return getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

	/**
	 * Session의 전체 속성명 가져옴
	 * @return String Array
	 */
	public String[] getSessionAttributeNames() {
		return getAttributeNames(RequestAttributes.SCOPE_SESSION);
	}

    /**
     * Session의 속성값 설정
     * @param name 속성명
     * @param object 데이터
     */
    public void setSessionAttribute(String name, Object object) {
        setAttribute(name, object, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * Session의 속성값 삭제
     * @param name 속성명
     */
    public void removeSessionAttribute(String name) {
        removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }


    /**
     * Session ID
     * @return String
     */
    public String getSessionId() {
    	RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

    	if(requestAttributes != null) {
    		return requestAttributes.getSessionId();
    	} else {
    		return StringUtils.EMPTY;
    	}
    }

    private Object getAttribute(String name, int scope) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

		if(requestAttributes != null) {
			return requestAttributes.getAttribute(name, scope);
		} else {
			return null;
		}
    }

	private String[] getAttributeNames(int scope) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

		if(requestAttributes != null) {
			return requestAttributes.getAttributeNames(scope);
		} else {
			return new String[0];
		}
	}

	private void setAttribute(String name, Object object, int scope) {
    	RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

    	if(requestAttributes != null) {
    		requestAttributes.setAttribute(name, object, scope);
    	}
    }

    private void removeAttribute(String name, int scope) {
    	RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

    	if(requestAttributes != null) {
    		requestAttributes.removeAttribute(name, scope);
    	}
    }
}

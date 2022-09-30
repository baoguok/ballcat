package org.ballcat.springsecurity.oauth2.server.authorization.web.filter;

import cn.hutool.core.text.CharSequenceUtil;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.ballcat.security.captcha.CaptchaValidateResult;
import org.ballcat.security.captcha.CaptchaValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hccake 2021/1/11
 * @version 1.0
 */
@RequiredArgsConstructor
public class LoginCaptchaFilter extends OncePerRequestFilter {

	private final RequestMatcher requestMatcher;

	private final CaptchaValidator captchaValidator;

	private static final String GRANT_TYPE_PASSWORD = "password";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!this.requestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 只对 password 的 grant_type 进行拦截处理
		String grantType = request.getParameter("grant_type");
		if (!GRANT_TYPE_PASSWORD.equals(grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 获取当前客户端
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		// 测试客户端 跳过验证码（swagger 或 postman测试时使用）
		if (registeredClient != null && registeredClient.getScopes().contains("skip_captcha")) {
			filterChain.doFilter(request, response);
			return;
		}

		CaptchaValidateResult captchaValidateResult = captchaValidator.validate(request);
		if (captchaValidateResult.isSuccess()) {
			filterChain.doFilter(request, response);
		}
		else {
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			R<String> r = R.failed(SystemResultCode.UNAUTHORIZED,
					CharSequenceUtil.blankToDefault(captchaValidateResult.getMessage(), "Captcha code error"));
			response.getWriter().write(JsonUtils.toJson(r));
		}

	}

	private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
			Authentication authentication) {

		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
			return (OAuth2ClientAuthenticationToken) authentication;
		}

		OAuth2ClientAuthenticationToken clientPrincipal = null;

		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
		}

		if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}

		throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
	}

}

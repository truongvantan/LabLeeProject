package com.lablee.client.config;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lablee.client.service.VisitService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VisitInterceptor implements HandlerInterceptor {
	private final VisitService visitService;

	private static final Set<String> EXCLUDED_URIS = Set.of("/favicon.ico", "/error");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// Only count GET request
		if (!"GET".equalsIgnoreCase(request.getMethod())) {
			return true;
		}

		String uri = request.getRequestURI();

		// Ignore static resources
		if (EXCLUDED_URIS.contains(uri) || uri.contains(".")) {
			return true;
		}

		// Identify page key from Controller mapping
		String pageKey = getPageKeyFromHandler(handler);
		if (pageKey == null) {
			return true; // don't count unknown pages
		}

		visitService.recordVisit(request, pageKey);
		return true;
	}

	private String getPageKeyFromHandler(Object handler) {
		if (!(handler instanceof HandlerMethod)) {
			return null;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;

		// Get class + method in controller
		Class<?> controllerClass = handlerMethod.getBeanType();
		String controllerName = controllerClass.getSimpleName().replace("Controller", "").toUpperCase();

		String methodName = handlerMethod.getMethod().getName().toUpperCase();

		// Page key = CONTROLLERNAME_METHODNAME
		return controllerName + "_" + methodName;
	}

}

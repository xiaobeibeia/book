package com.tsbg.mis.config.exception;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.util.CommonUtil;
import com.tsbg.mis.util.ResultUtils;
import com.tsbg.mis.util.constants.ErrorEnum;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 *  统一异常拦截
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private final static String CODE = "code";

	private final static String MESSAGE = "message";

	private final static String ERROR_LOCATION = "errorLocation";

	private final static String INFO = "info";

	private final static String CHINESE_ERROR_LOCATION = "    错误位置:";

	private final static String CHINESE_EXCEPTION = "异常";

	@ExceptionHandler(value = Exception.class)
	public JSONObject defaultErrorHandler(HttpServletRequest req, Exception e) {
		String errorPosition = "";
		//如果错误堆栈信息存在
		if (e.getStackTrace().length > 0) {
			StackTraceElement element = e.getStackTrace()[0];
			String fileName = element.getFileName() == null ? "未找到错误文件" : element.getFileName();
			int lineNumber = element.getLineNumber();
			errorPosition = fileName + ":" + lineNumber;
		}
		Object permIsNotEnough = req.getAttribute("PermIsNotEnough");
		Object tokenIsInvalid = req.getAttribute("TokenIsInvalid");
		JSONObject jsonObject = new JSONObject();
		if (permIsNotEnough!=null && !permIsNotEnough.equals("")){
			if (permIsNotEnough.equals(1)){
				jsonObject.put(CODE, ErrorEnum.E_502.getErrorCode());
				jsonObject.put(MESSAGE, ErrorEnum.E_502.getErrorMessage());
				JSONObject errorObject = new JSONObject();
				errorObject.put(ERROR_LOCATION, e.toString() + CHINESE_ERROR_LOCATION + errorPosition);
				jsonObject.put(INFO, errorObject);
				logger.error(CHINESE_EXCEPTION, e);
				return jsonObject;
			}
		}
		if (tokenIsInvalid!=null && !tokenIsInvalid.equals("")){
			if (tokenIsInvalid.equals(1)){
				jsonObject.put(CODE, ErrorEnum.E_401.getErrorCode());
				jsonObject.put(MESSAGE, ErrorEnum.E_401.getErrorMessage());
				JSONObject errorObject = new JSONObject();
				errorObject.put(ERROR_LOCATION, e.toString() + CHINESE_ERROR_LOCATION + errorPosition);
				jsonObject.put(INFO, errorObject);
				logger.error(CHINESE_EXCEPTION, e);
				return jsonObject;
			}
		}
		jsonObject.put(CODE, ErrorEnum.E_400.getErrorCode());
		jsonObject.put(MESSAGE, ErrorEnum.E_400.getErrorMessage());
		JSONObject errorObject = new JSONObject();
		errorObject.put(ERROR_LOCATION, e.toString() + CHINESE_ERROR_LOCATION + errorPosition);
		jsonObject.put(INFO, errorObject);
		logger.error(CHINESE_EXCEPTION, e);
		return jsonObject;
	}

	/**
	 * GET/POST请求方法错误的拦截器
	 * 因为开发时可能比较常见,而且发生在进入controller之前,上面的拦截器拦截不到这个错误
	 * 所以定义了这个拦截器
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public JSONObject httpRequestMethodHandler() {
		return CommonUtil.errorJson(ErrorEnum.E_500);
	}

	/**
	 * 本系统自定义错误的拦截器
	 * 拦截到此错误之后,就返回这个类里面的json给前端
	 * 常见使用场景是参数校验失败,抛出此错,返回错误信息给前端
	 */
	@ExceptionHandler(CommonJsonException.class)
	public JSONObject commonJsonExceptionHandler(CommonJsonException commonJsonException) {
		return commonJsonException.getResultJson();
	}

	/**
	 * 权限不足报错拦截
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public JSONObject unauthorizedExceptionHandler() {
		return CommonUtil.errorJson(ErrorEnum.E_502);
	}

	/**
	 * 未登录报错拦截
	 * 在请求需要权限的接口,而连登录都还没登录的时候,会报此错
	 */
	@ExceptionHandler(UnauthenticatedException.class)
	public JSONObject unauthenticatedException() {
		return CommonUtil.errorJson(ErrorEnum.E_20011);
	}

	/**
	 * 方法参数校验
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResultUtils handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		logger.error(e.getMessage(), e);
		String defaultMessage = null;
		if(e.getBindingResult() != null && e.getBindingResult().getFieldError() != null){
			defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
		}
		return new ResultUtils(500, defaultMessage);
	}

	/**
	 * ValidationException
	 */
	@ExceptionHandler(ValidationException.class)
	public ResultUtils handleValidationException(ValidationException e) {
		logger.error(e.getMessage(), e);
		String message = null;
		if(e.getCause() != null){
			message = e.getCause().getMessage();
		}
		return new ResultUtils(500, message);
	}

	/**
	 * ConstraintViolationException
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResultUtils handleConstraintViolationException(ConstraintViolationException e) {
		logger.error(e.getMessage(), e);
		String message = null;
		if(e.getCause() != null){
			message = e.getCause().getMessage();
		}
		return new ResultUtils(500, message);
	}
}

package com.guojin.srm.oss.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.guojin.srm.api.bean.org.response.OemResponse;
import com.guojin.srm.api.bean.sys.response.SysPartyResponse;
import com.guojin.srm.api.bean.sys.response.SysUserResponse;
import com.guojin.srm.api.biz.org.IOemBiz;
import com.guojin.srm.api.biz.sys.ISysPartyBiz;
import com.guojin.srm.api.biz.sys.ISysUserBiz;
import com.guojin.srm.common.constant.CommonConstants;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.OrganStatusEnum;
import com.guojin.srm.common.enums.OssExcCodesEnum;
import com.guojin.srm.common.exception.*;
import com.guojin.srm.common.session.SessParty;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.common.shiro.ShiroUtils;
import com.guojin.srm.common.utils.ResultBuilder;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.common.utils.WriteExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.OutputStream;
import java.rmi.ServerException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@RestController
@Slf4j
public class BaseBusinessController {
	@Autowired
	protected HttpServletRequest httpServletRequest;
	@Autowired
	protected HttpServletResponse httpServletResponse;

	@Reference
	protected ISysPartyBiz iSysPartyBiz;
	@Reference
	protected ISysUserBiz iSysUserBiz;
	@Reference
	protected IOemBiz iOemBiz;

	protected static SerializerFeature[] features = { // SerializerFeature.WriteNullNumberAsZero,
	SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty,
			SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat,
			SerializerFeature.WriteNullBooleanAsFalse };

	protected ResultDto<T> success() {
		return new ResultDto<T>(ResultBuilder.SUCC_CODE, null, null);
	}

	@SuppressWarnings("hiding")
	protected <T> ResultDto<T> success(T data) {
		return new ResultDto<T>(ResultBuilder.SUCC_CODE, null, data);
	}
	
	@SuppressWarnings("unchecked")
	protected ResultDto<T> success(String code, String msg) {
		return ResultBuilder.buildResult(code, null, msg);
	}

	@SuppressWarnings("rawtypes")
	protected ResultDto fail(BizExcCodes appExc) {
		try {
			SessUser user = getUser();
			if(user != null && BaseExcCodesEnum.NO_DATA_RIGHT.getCode().equals(appExc.getCode())) {
				log.error("越权异常,url:"+httpServletRequest.getRequestURI()+",user:"+user.getUsername());
			}
		} catch (Exception e) {
			//处理直接返回fail的异常
		}
		return ResultBuilder.buildResult(appExc);
	}

	@SuppressWarnings("rawtypes")
	protected ResultDto fail(String msg) {
		return ResultBuilder.buildResult(ResultBuilder.FAIL_CODE, null, msg);
	}
	
	@SuppressWarnings("rawtypes")
	protected ResultDto fail(String code, String msg) {
		return ResultBuilder.buildResult(code, null, msg);
	}
	
	@SuppressWarnings("rawtypes")
	protected ResultDto fail() {
		return ResultBuilder.buildResult(ResultBuilder.FAIL_CODE, null, null);
	}

	protected void setSessUser(SessUser sessUser)throws BizException{
		ShiroUtils.setUser(sessUser);
	}

	protected SessUser getUser() throws BizException {
		SessUser sessUser = ShiroUtils.getUser();
		if(sessUser == null){
			throw new BizException(BaseExcCodesEnum.SESSION_TIMEOUT);
		}
		//验证用户状态
		try {
			SysUserResponse userBean = iSysUserBiz.queryUserByName(sessUser.getUsername(),sessUser.getOemCode());
			if(userBean == null){
				throw new BizException(BaseExcCodesEnum.STATUS_INVALID,"用户");
			}
			if(!Objects.equals(userBean.getStatus(),2)){//用户状态2=正常
				throw new BizException(BaseExcCodesEnum.STATUS_ABNORMAL_ERR,"用户");
			}
			return sessUser;
		}catch (BizException e) {
			if(BaseExcCodesEnum.STATUS_INVALID.getCode().equals(e.getCode())) {
				//机构状态不正确的情况，直接踢出
				throw new BizException(BaseExcCodesEnum.SESSION_TIMEOUT);
			}
			throw e;
		}
	}

	protected Long getUserId() throws BizException {
		return getUser().getId();
	}


	protected void setParty(SessParty party) throws BizException {
		ShiroUtils.setParty(party);
	}

	protected SessParty getParty() throws BizException {
		SessParty sessParty = ShiroUtils.getParty();
		if (sessParty == null) {
			return null;
		}
		try {
			SysPartyResponse partyBean = iSysPartyBiz.queryPartyByCode(sessParty.getPartyCode());
			if(partyBean == null){
				throw new BizException(BaseExcCodesEnum.STATUS_INVALID,"机构");
			}
			if(!Objects.equals(partyBean.getStatus(),OrganStatusEnum.NORMAL.getValue())){
				throw new BizException(BaseExcCodesEnum.STATUS_ABNORMAL_ERR,"机构");
			}
			return sessParty;
		}catch (BizException e) {
			if(BaseExcCodesEnum.STATUS_INVALID.getCode().equals(e.getCode())) {
				//机构状态不正确的情况，直接踢出
				throw new BizException(BaseExcCodesEnum.SESSION_TIMEOUT);
			}
			throw e;
		}
	}

	/**
	 * 获取oemCode，如果是平台返回null
	 * @return
	 * @throws BizException
	 */
	protected String getOemCode() throws BizException{
		String oemCode = httpServletRequest.getHeader(CommonConstants.OEM_CODE);
		if (StringUtils.isBlank(oemCode)) {
			throw new BizException(BaseExcCodesEnum.HEAD_OEM_NULL);
		}
		OemResponse oem = iOemBiz.queryByOemCode(oemCode);
		if(oem == null){
			throw new BizException(BaseExcCodesEnum.ENTITY_NOT_EXISTS,"OEM机构");
		}
		if(!Objects.equals(OrganStatusEnum.NORMAL.getValue(),oem.getStatus())){
			throw new BizException(BaseExcCodesEnum.STATUS_ABNORMAL_ERR,"OEM机构");
		}
		return oemCode;
	}

	private String getControllerPath() {
		RequestMapping mapping = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
		if ((mapping != null) && (mapping.value().length > 0)) {
			return mapping.value()[0];
		}
		return "";
	}

	/**
	 *<p>下载文件</p>
	 * @param fileName - 下载的文件名称
	 * @param titles - 下载文件列表的表头列表
	 * @param values - 下载文件列表的内容
	 * @param response
	 */
	protected void _downloadFile(String fileName,String[] titles,List<List<Object>> values,HttpServletResponse response){
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        WriteExcelUtil util = new WriteExcelUtil();
        OutputStream os = null;
        try {
        	util.createWorkbook(WriteExcelUtil.FILE_SUFFIX_XLS, Arrays.asList(titles));
        	util.writeBody(values);
        	os = response.getOutputStream();
        	util.write(os);
        	os.flush();
        }catch (Exception e) {
        	log.error("文件下载异常",e);
		}finally {
			try {
				util.closeWorkbook();
			}catch (Exception e) {
			}
		}
	}

	/**
	 * 统一异常处理
	 *
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	@ResponseBody
	public ResultDto exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		//未登录
		if (ex instanceof NotLoginException) {
			return fail(BaseExcCodesEnum.SESSION_TIMEOUT);
		}
		if (ex instanceof UnauthenticatedException) {
			return fail(BaseExcCodesEnum.SESSION_TIMEOUT);
		}
		if (ex instanceof UnknownSessionException) {
			return fail(BaseExcCodesEnum.SESSION_TIMEOUT);
		}

		log.error("ExceptionHandlerAdvice uri:" + request.getRequestURI());
		log.error("异常:", ex);
		if (ex instanceof MethodArgumentNotValidException) {
			return fail(((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().get(0).getDefaultMessage());
		}
		if (ex instanceof AuthenticationException) {
			return fail(OssExcCodesEnum.PASSWORD_ERROR);
		}
		if (ex instanceof NoPermissionException) {
			return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
		}
		if (ex instanceof UnauthorizedException) {
			return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
		}
		if (ex instanceof BizException) {
			return fail(((BizException) ex).getCode(),ex.getMessage());
		}
		if (ex instanceof ApiException) {
			return fail(ex.getMessage());
		}
		if (ex instanceof RedisLockException) {
			return fail(ex.getMessage());
		}
		if (ex instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) ex;
			Set<ConstraintViolation<?>> set = cve.getConstraintViolations();
			if (CollectionUtil.isNotEmpty(set)) {
				return fail(set.stream().findFirst().get().getMessageTemplate());
			}
		}
		if (ex instanceof ServerException) {
			ServerException se = (ServerException) ex;
			return fail(se.getMessage());
		}

		response.setCharacterEncoding("utf-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "content-type,content-security-policy,Origin,Accept,x-requested-with,Content-Type,X-AUTH-SESSION,Content-Security-Policy,X-Content-Type-Options,X-XSS-Protection,token,workToken,oemCode");
		response.setHeader("Access-Control-Expose-Headers", "content-type,content-security-policy,Origin,Accept,x-requested-with,Content-Type,X-AUTH-SESSION,Content-Security-Policy,X-Content-Type-Options,X-XSS-Protection,token,workToken,oemCode");
		response.setStatus(HttpStatus.OK.value());
		return fail(BaseExcCodesEnum.SYSTEM_ERROR);
	}

}

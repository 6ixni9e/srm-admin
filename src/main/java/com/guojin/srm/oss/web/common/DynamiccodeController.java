package com.guojin.srm.oss.web.common;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.AddDynamiccodeRequest;
import com.guojin.srm.api.bean.common.request.SendDynamicCodeRequest;
import com.guojin.srm.api.bean.sys.response.SysUserResponse;
import com.guojin.srm.api.biz.common.IDynamicCodeBiz;
import com.guojin.srm.api.biz.sys.ISysUserBiz;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.DynamicCodeTypeEnum;
import com.guojin.srm.common.enums.OssExcCodesEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.common.utils.StringUtil;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author bealon
 * @email ccsu123456@qq.com
 * @date 2019年01月08日 19时38分36秒
 */

@Api(value = "DynamiccodeController", tags = { "动态码管理" })
@RestController
@RequestMapping("/api/dynamiccode")
@Slf4j
public class DynamiccodeController extends BaseBusinessController {

	@Reference
	private ISysUserBiz iSysUserBiz;
	@Reference
	private IDynamicCodeBiz iDynamicCodeBiz;

	private final String MSG_PREFIX = "验证码已发送到手机：";
	@ApiOperation(value = "获取登录短信验证码", notes = "获取登录短信验证码")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "SendDynamicCodeRequest")
	@PostMapping("/loginCode")
	public ResultDto loginCode(@RequestBody @Valid SendDynamicCodeRequest request) throws BizException {
		String oemCode = getOemCode();
		SysUserResponse userBean = iSysUserBiz.queryUserByName(request.getUserName(),oemCode);
		if (userBean == null) {
			throw new BizException(BaseExcCodesEnum.ENTITY_NOT_EXISTS, "用户");
		}
		String message = sendDynamicCode(request,DynamicCodeTypeEnum.LOGIN_CODE,oemCode);
		return success(message);
	}


	@ApiOperation(value = "获取实名认证短信验证码", notes = "获取实名认证短信验证码")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "SendDynamicCodeRequest")
	@PostMapping("/authCode")
	public ResultDto authCode(@RequestBody @Valid SendDynamicCodeRequest request) throws BizException {
		String message = sendDynamicCode(request,DynamicCodeTypeEnum.AUTH_CODE,getOemCode());
		return success(message);
	}

	@ApiOperation(value = "获取通用短信验证码", notes = "获取通用短信验证码")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "SendDynamicCodeRequest")
	@PostMapping("/generate")
	public ResultDto genVerifycode(@RequestBody @Valid SendDynamicCodeRequest request) throws BizException {
		String message = sendDynamicCode(request,DynamicCodeTypeEnum.VERIFY_CODE,getOemCode());
		return success(message);
	}

	@ApiOperation(value = "获取忘记密码验证码", notes = "获取忘记密码验证码")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "SendDynamicCodeRequest")
	@PostMapping("/forgetPwd")
	public ResultDto forgetPassWord(@RequestBody @Valid SendDynamicCodeRequest request) throws BizException {
		SysUserResponse userBean = iSysUserBiz.queryUserByName(request.getUserName(),getOemCode());
		if (userBean == null) {
			throw new BizException(BaseExcCodesEnum.ENTITY_NOT_EXISTS, "用户");
		}
		String message = sendDynamicCode(request,DynamicCodeTypeEnum.FORGET_PASSWORD,getOemCode());
		return success(message);
	}

	@ApiOperation(value = "获取复核验证码", notes = "获取复核验证码")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "SendDynamicCodeRequest")
	@PostMapping("/payVerifyCode")
	public ResultDto payVerify(@RequestBody @Valid SendDynamicCodeRequest request) throws BizException {
		SessUser sessUser = getUser();
		request.setUserName(sessUser.getUsername());
		String message = sendDynamicCode(request,DynamicCodeTypeEnum.PAYAUTH_CODE,getOemCode());
		return success(message);
	}

	protected String sendDynamicCode(SendDynamicCodeRequest request,DynamicCodeTypeEnum dynamicType,String oemCode)throws BizException{
		//发送登录验证码
		AddDynamiccodeRequest addDynamiccodeRequest = new AddDynamiccodeRequest();
		addDynamiccodeRequest.setUserId(request.getUserName());
		addDynamiccodeRequest.setPhone(request.getUserName());
		addDynamiccodeRequest.setBizType(dynamicType.getValue());
		addDynamiccodeRequest.setMemo(dynamicType.getRemark());
		addDynamiccodeRequest.setOemCode(oemCode);
		iDynamicCodeBiz.getCode(addDynamiccodeRequest);
		return MSG_PREFIX + StringUtil.mark(request.getUserName(), '*', 3, 7);
	}
}

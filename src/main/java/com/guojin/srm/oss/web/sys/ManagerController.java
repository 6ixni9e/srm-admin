package com.guojin.srm.oss.web.sys;

import cn.hutool.json.JSONUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.guojin.srm.api.bean.common.request.QueryDynamiccodeRequest;
import com.guojin.srm.api.bean.sys.request.ForgetUserPassWordRequest;
import com.guojin.srm.api.bean.sys.request.LoginRequest;
import com.guojin.srm.api.bean.sys.request.SetUserPassWordRequest;
import com.guojin.srm.api.bean.sys.response.*;
import com.guojin.srm.api.biz.common.IDynamicCodeBiz;
import com.guojin.srm.api.biz.common.ISysConfigBiz;
import com.guojin.srm.api.biz.sys.*;
import com.guojin.srm.common.base.OrganNoParamRequest;
import com.guojin.srm.common.constant.CommonConstants;
import com.guojin.srm.common.constant.DictCodeConstant;
import com.guojin.srm.common.enums.*;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.redis.RedisService;
import com.guojin.srm.common.session.SessParty;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.common.shiro.ShiroRedisCache;
import com.guojin.srm.common.utils.BeanUtil;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.common.utils.StringUtil;
import com.guojin.srm.oss.shiro.UserNamePassWordRealm;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 管理相关
 */
@Api(value="ManagerController", tags={"登录登出改密切换机构管理"})
@RestController
@RequestMapping("/sys/manager")
@Slf4j
public class ManagerController extends BaseBusinessController {


	@Reference
	private ISysUserBiz iSysUserBiz;
	@Reference
	private ISysPartyBiz iSysPartyBiz;
	@Reference
	private ISysRoleBiz iSysRoleBiz;
	@Reference
	private IDynamicCodeBiz iDynamicCodeBiz;
	@Reference
	private ISysUserPartyBiz iSysUserPartyBiz;
	@Reference
	private ISysPartyBiz isysPartyBiz;
	@Reference
	private ISysDictCodeBiz iSysDictCodeBiz;

	@Autowired
	private RedisService redisService;

	/**
	 * 用户密码登录
	 */
    @ApiOperation(value="用户登录", notes="用户登录接口",httpMethod="POST")
    @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "LoginRequest")
	@ResponseBody
	@PostMapping(value = "/login")
	public ResultDto<UserBean> login(@RequestBody @Valid LoginRequest request) throws BizException {
    	log.info("ManagerController login request = {}", JSON.toJSONString(request));
    	String oemCode = getOemCode();
		String redisKey = "SRM_OSS_" + request.getUsername();
		String redisTime = String.valueOf(System.currentTimeMillis() + 60000);
		redisService.redisLock(redisKey, redisTime, 60, "您提交过于频繁，请休息一下！");
		try {
			// 校验用户是否存在
			SysUserResponse userObj = iSysUserBiz.queryUserByName(request.getUsername(),oemCode);
			if (userObj == null) {
				throw new BizException(BaseExcCodesEnum.ENTITY_NOT_EXISTS, "用户");
			}
			String password = request.getPassword();
			//短信登录验证
			if(Objects.equals(1,request.getLoginType())){
				// 验证登录验证码
				QueryDynamiccodeRequest queryDynamiccodeRequest = new QueryDynamiccodeRequest();
				queryDynamiccodeRequest.setUserId(userObj.getUsername());
				queryDynamiccodeRequest.setBizType(DynamicCodeTypeEnum.LOGIN_CODE.getValue());
				queryDynamiccodeRequest.setDynaCode(request.getLoginCode());
				queryDynamiccodeRequest.setRefNo(userObj.getMobile());
				queryDynamiccodeRequest.setOemCode(oemCode);
				//不抛出异常为验证通过
				iDynamicCodeBiz.validateCode(queryDynamiccodeRequest);
				//短信验证的情况下，固定password值
				password = CommonConstants.AUTH_NOPWD;
			}

			List<SysPartyResponse> partyList = null;
			SysPartyResponse partyBean = null;
			if (!userObj.getUsertype().equals(UserTypeEnum.SUPER_ADMIN.getType())) {
				Integer partyType = null;
				if(Objects.equals(0,request.getPlatformType())){
					//系统平台管理员 - 系统管理
					partyType = PartyTypeEnum.PLATFORM.getValue();
				}else if(Objects.equals(1,request.getPlatformType())){
					//OEM机构  - 我是平台
					partyType = PartyTypeEnum.OEM.getValue();
				}else {
					//企业管理  - 我是企业
					partyType = PartyTypeEnum.MCHNT.getValue();
				}
				partyList = iSysPartyBiz.queryPartyByUserId(userObj.getId(),partyType, OrganStatusEnum.NORMAL.getValue());
				if(CollectionUtils.isEmpty(partyList)){
					return fail("用户无此平台管理权限");
				}
				//默认选择用户机构的某一个
				partyBean = partyList.get(0);
			}
			//初始化创建Session对象
			Session session = SecurityUtils.getSubject().getSession();
			UsernamePasswordToken token = new UsernamePasswordToken(userObj.getUsername(), password, false,oemCode);
			try {
				SecurityUtils.getSubject().login(token);
			} catch (AuthenticationException e) {
				log.error("登录异常", e);
				throw new BizException(BaseExcCodesEnum.LOGIN_ERR,e.getMessage());
			}
			// 刷新权限
			SessUser sessUser = (SessUser) SecurityUtils.getSubject().getPrincipal();
			ShiroRedisCache<Object, Object> cache = (ShiroRedisCache<Object, Object>) ((DefaultWebSecurityManager) SecurityUtils
					.getSecurityManager()).getCacheManager().getCache(UserNamePassWordRealm.class.getName() + ".authorizationCache");
			cache.remove(sessUser.getId());

			UserBean userBean = _cacheUserParty(sessUser,partyBean,true);
			userBean.setPlatformType(request.getPlatformType());
			userBean.setHasPassword(0);//是否有密码：0-无，1-有
			if(!Objects.isNull(userObj.getPassword())){
				userBean.setHasPassword(1);
			}
			return success(userBean);
		}finally {
			redisService.unLock(redisKey, redisTime);
		}
	}



	@ApiOperation(value = "获取用户企业机构列表", notes = "获取用户企业机构列表")
	@RequiresPermissions("sys:sysparty:userPartyList")
	@PostMapping("/userPartyList")
	public ResultDto<List<SysPartyResponse>> userPartyList() throws BizException {
		//查询列表数据
		List<SysPartyResponse> userPartyList = isysPartyBiz.queryPartyByUserId(getUser().getId(),PartyTypeEnum.MCHNT.getValue(), OrganStatusEnum.NORMAL.getValue());
		return success(userPartyList);
	}


	@ApiOperation(value = "企业平台切换企业主体", notes = "企业平台切换企业主体")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "OrganNoParamRequest")
	@RequiresPermissions("sys:sysparty:changeParty")
	@PostMapping("/changeParty")
	public ResultDto<UserBean> changeParty(@Valid @RequestBody OrganNoParamRequest request) throws BizException {
		//获取用户信息
		SessUser user = getUser();
		//获取机构信息
		SysPartyResponse partyBean = isysPartyBiz.queryPartyByCode(request.getOrganNo());
		if(partyBean == null){
			return fail("商户企业记录不存在");
		}
		if(!Objects.equals(partyBean.getStatus(),OrganStatusEnum.NORMAL.getValue())){
			return fail("商户企业状态异常，请联系管理员");
		}
		//验证机构关系
		SysUserPartyResponse userParty = iSysUserPartyBiz.queryByUserPartyId(user.getId(),partyBean.getId());
		if(userParty == null){
			throw new BizException(BaseExcCodesEnum.NO_DATA_RIGHT);
		}
		UserBean userBean = _cacheUserParty(user,partyBean,false);
		//企业平台才可以切换，切换后默认赋值2
		userBean.setPlatformType(2);
		return success(userBean);
	}

	@ApiOperation(value = "企业平台切换企业主体", notes = "企业平台切换企业主体")
	@RequiresPermissions("sys:sysparty:changeParty")
	@PostMapping("/testSess")
	public ResultDto testSess() throws BizException {
		//获取用户信息
		SessUser user = getUser();
		log.info("====系统缓存中的用户信息:"+ JSONUtil.toJsonStr(user));
		SessParty party = getParty();
		log.info("====系统缓存中的机构信息:"+ JSONUtil.toJsonStr(party));
		return success();
	}

	@ApiOperation(value = "查询当前平台匹配的OEM机构编码", notes = "查询当前平台匹配的OEM机构编码")
	@PostMapping("/queryOemCode")
	public ResultDto<String> queryOemCode()throws BizException{
		StringBuffer buffer = httpServletRequest.getRequestURL();
		log.info("请求获取OEM机构编号的url地址为："+buffer.toString());
		List<SysDictCodeResponse> dictCodeList = iSysDictCodeBiz.queryByGroupCode(DictCodeConstant.GROUP_OEM);
		if(CollectionUtils.isEmpty(dictCodeList)){
			return fail("映射OEM机构信息配置尚未配置");
		}
		String oemCode = null;
		for (SysDictCodeResponse item:dictCodeList) {
			if(buffer.toString().indexOf(item.getDictCode()) != -1){
				oemCode = item.getDictValue();
				break ;
			}
		}
		if(StringUtil.isEmpty(oemCode)){
			return fail("未找到当前平台对应的OEM机构");
		}
		return success(oemCode);
	}


	@ApiOperation(value = "忘记密码(仅验证短信码正确与否)", notes = "忘记密码(验证短信)")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "ForgetUserPassWordRequest")
	@ResponseBody
	@PostMapping("/forgetPassword")
	public ResultDto forgetPassword(@RequestBody @Valid ForgetUserPassWordRequest request) throws BizException{
		iSysUserBiz.forgetPassword(request,getOemCode());
		return success();
	}

	@ApiOperation(value = "设置密码", notes = "设置密码")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "SetUserPassWordRequest")
	@ResponseBody
	@PostMapping("/setPassword")
	public ResultDto setPassword(@RequestBody @Valid SetUserPassWordRequest request) throws BizException{
		iSysUserBiz.setPassword(request,getOemCode());
		return success();
	}

	/**
	 * 退出
	 */
    @ApiOperation(value="用户注销", notes="用户注销",httpMethod="POST")
    @ResponseBody
    @PostMapping(value = "/logout")
	public ResultDto<Integer> logout() throws BizException{
		SessUser user = getUser();
		if(user == null){
			//超时情况下，跳转到OEM机构登录页面
			return success(1);
		}
		SessParty party = getParty();
    	if(party == null || PartyTypeEnum.PLATFORM.getValue() == party.getPartyType()){
    		//未超时情况下，party为空或者类型为平台时，跳转到管理平台入口
			return success(0);
		}
    	SecurityUtils.getSubject().logout();
    	//默认跳转到OEM机构登录页面
		return success(1);
	}

	/**
	 * 缓存sessUser和sessParty
	 * @param partyBean
	 * @return
	 * @throws BizException
	 */
	private UserBean _cacheUserParty(SessUser sessUser,SysPartyResponse partyBean,boolean refreshPermissions)throws BizException{
		UserBean userBean = new UserBean();
		BeanUtil.copyProperties(sessUser, userBean);
		// 缓存默认机构
		if (!userBean.getUsertype().equals(UserTypeEnum.SUPER_ADMIN.getType())) {
			SessParty sessParty = new SessParty();
			BeanUtil.copyProperties(partyBean, sessParty);
			userBean.setPartyId(partyBean.getId());
			userBean.setPartyType(partyBean.getPartyType());
			userBean.setPartyCode(partyBean.getPartyCode());
			userBean.setPartyName(partyBean.getNameCn());
			// 设置默认机构
			setParty(sessParty);
			//将机构ID保存在sessUser
			sessUser.setPartyId(partyBean.getId());
			// 查询所属角色（当前默认机构下）
//			List<Long> roleIdList = new ArrayList<>();
			List<SysRoleResponse> roleList = iSysRoleBiz.getUserPartyRole(userBean);
			if (null != roleList && roleList.size() != 0) {
				userBean.setRoleName(roleList.get(0).getName());
//				for (SysRoleResponse roleBean : roleList) {
//					roleIdList.add(roleBean.getId());
//				}
			}
//			userBean.setRoleIdList(roleIdList);
		} else {
			// 超级管理员-1
			userBean.setPartyType(-1);
		}
		setSessUser(sessUser);
		if(refreshPermissions){
			//清除权限缓存重新加载
			RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
			UserNamePassWordRealm realm = (UserNamePassWordRealm) realmSecurityManager.getRealms().iterator().next();
			realm.clearCachedAuthorization();
		}
		return userBean;
	}

}

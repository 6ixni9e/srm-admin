package com.guojin.srm.oss.web.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.sys.request.AddSysUserPartyRequest;
import com.guojin.srm.api.bean.sys.request.QuerySysUserPartyRequest;
import com.guojin.srm.api.bean.sys.request.UpdateSysUserPartyRequest;
import com.guojin.srm.api.bean.sys.response.SysUserPartyResponse;
import com.guojin.srm.api.bean.sys.response.UserBean;
import com.guojin.srm.api.biz.sys.ISysUserBiz;
import com.guojin.srm.api.biz.sys.ISysUserPartyBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.enums.UserTypeEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessParty;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.common.utils.StringUtil;
import com.guojin.srm.oss.properties.SysConfigProperties;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月11日 13时37分47秒
 */

@Api(value = "SysUserPartyController", tags = {"机构用户管理"})
@RestController
@RequestMapping("/sys/userParty")
@Slf4j
public class SysUserPartyController extends BaseBusinessController {

    @Reference
    private ISysUserPartyBiz iSysUserPartyBiz;

    @Autowired
    private SysConfigProperties sysConfigProperties;


    @ApiOperation(value = "获取机构用户列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QuerySysUserPartyRequest")
    @RequiresPermissions("sys:sysuser:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<SysUserPartyResponse>> list(@Valid @RequestBody QuerySysUserPartyRequest request) throws BizException {
		SessParty sessParty = getParty();
		// 只能查看当前机构人员
		if (null != sessParty) {
			request.setPartyId(sessParty.getId());
		}
		//不是管理人员，能查看普通用户
		SessUser user = getUser();
		//如果不是管理员 只能查看普通用户
		if(!user.getUsertype().equals(UserTypeEnum.SUPER_ADMIN.getType())
				&& !user.getUsertype().equals(UserTypeEnum.SYSTEM_ADMIN.getType())){
			request.setUsertype(UserTypeEnum.COMMON_USER.getType());
		}
        //查询列表数据
        PageResultVO<SysUserPartyResponse> result = iSysUserPartyBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "查看机构角色详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysuser:info")
    @PostMapping("/info")
    public ResultDto<UserBean> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        UserBean userBean = iSysUserPartyBiz.queryPartyUser(request);
        return success(userBean);
    }


    @ApiOperation(value = "新增机构角色", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddSysUserPartyRequest")
    @RequiresPermissions("sys:sysuser:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddSysUserPartyRequest request) throws BizException {
        if(StringUtil.isEmpty(request.getOemCode())){
            request.setOemCode(getOemCode());
        }
        //超管无机构ID
        Long partyId = null;
        if(getParty() != null){
            partyId = getParty().getId();
        }
        iSysUserPartyBiz.save(request,getUser(),partyId,sysConfigProperties.getDefaultPartyId());
        return success();
    }

    @ApiOperation(value = "修改机构角色", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateSysUserPartyRequest")
    @RequiresPermissions("sys:sysuser:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateSysUserPartyRequest request) throws BizException {
        iSysUserPartyBiz.update(request,getUser(),getParty());
        return success();
    }

    @ApiOperation(value = "删除机构角色", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("sys:sysuser:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        //超管无机构ID，删除时默认删除的是平台的用户关系
        Long partyId = sysConfigProperties.getDefaultPartyId();
        if(getParty() != null){
            partyId = getParty().getId();
        }
        iSysUserPartyBiz.deleteUserParty(request.getId(),getUser(),partyId);
        return success();
    }

}

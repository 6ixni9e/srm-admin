package com.guojin.srm.oss.web.auth;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.auth.request.AddUserAuthRequest;
import com.guojin.srm.api.bean.auth.request.QueryUserAuthRequest;
import com.guojin.srm.api.bean.auth.request.UpdateUserAuthRequest;
import com.guojin.srm.api.bean.auth.response.UserAuthResponse;
import com.guojin.srm.api.biz.auth.IUserAuthBiz;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.oss.biz.AliyunOssBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.ResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时40分14秒
 */

@Api(value = "UserAuthController", tags = {"个人认证表"})
@RestController
@RequestMapping("/auth/userAuth")
@Slf4j
public class UserAuthController extends BaseBusinessController {

    @Reference
    private IUserAuthBiz iuserAuthBiz;

    @Autowired
    private AliyunOssBiz aliyunOssBiz;



    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryUserAuthRequest")
    @RequiresPermissions("auth:userauth:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<UserAuthResponse>> list(@Valid @RequestBody QueryUserAuthRequest request) throws BizException {
        //查询列表数据
        if(PartyTypeEnum.OEM.getValue() == getParty().getPartyType()){
            request.setOemCode(getUser().getOemCode());
        }
        PageResultVO<UserAuthResponse> result = iuserAuthBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("auth:userauth:info")
    @PostMapping("/info")
    public ResultDto<UserAuthResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        UserAuthResponse userAuthResponse = iuserAuthBiz.queryUserAuth(request.getId());
        userAuthResponse.setIdcardBack(aliyunOssBiz.getPrivateUrl(null,userAuthResponse.getIdcardBack()));
        userAuthResponse.setIdcardFront(aliyunOssBiz.getPrivateUrl(null,userAuthResponse.getIdcardFront()));
        String[] idcardUrl = new String[]{userAuthResponse.getIdcardFront(),userAuthResponse.getIdcardBack()};
        userAuthResponse.setIdcardUrl(idcardUrl);
        return success(userAuthResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddUserAuthRequest")
    @RequiresPermissions("auth:userauth:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddUserAuthRequest request) throws BizException {
        iuserAuthBiz.save(request,getUser(),getParty());
        return success();
    }

/*    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateUserAuthRequest")
    @RequiresPermissions("auth:userauth:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateUserAuthRequest request) throws BizException {
        iuserAuthBiz.update(request,getUser());
        return success();
    }*/

 /*   @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("auth:userauth:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        iuserAuthBiz.deleteById(request.getId());
        return success();
    }*/
}

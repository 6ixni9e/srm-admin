package com.guojin.srm.oss.web.common;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.AddBankcardRequest;
import com.guojin.srm.api.bean.common.request.QueryBankcardRequest;
import com.guojin.srm.api.bean.common.request.UpdateBankcardRequest;
import com.guojin.srm.api.bean.common.response.BankcardResponse;
import com.guojin.srm.api.biz.common.IBankcardBiz;
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
 * @date 2021年10月26日 13时42分11秒
 */

@Api(value = "BankcardController", tags = {"银行卡管理"})
@RestController
@RequestMapping("/common/bankcard")
@Slf4j
public class BankcardController extends BaseBusinessController {

    @Reference
    private IBankcardBiz ibankcardBiz;


    @ApiOperation(value = "获取银行卡列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryBankcardRequest")
    @RequiresPermissions("common:bankcard:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<BankcardResponse>> list(@Valid @RequestBody QueryBankcardRequest request) throws BizException {
        //查询列表数据
        PageResultVO<BankcardResponse> result = ibankcardBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取银行卡详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("common:bankcard:info")
    @PostMapping("/info")
    public ResultDto<BankcardResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        BankcardResponse bankcardResponse = ibankcardBiz.queryBankcard(request.getId());
        return success(bankcardResponse);
    }

//
//	          @ApiOperation(value="新增银行卡", notes="获取新增信息")
//        @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "AddBankcardRequest")
//           @RequiresPermissions("common:bankcard:save") 	@PostMapping("/save")
//	public ResultDto save(@RequestBody @Valid AddBankcardRequest request) throws BizException{
//		ibankcardBiz.save(request);
//		return success();
//	}
//
//	         @ApiOperation(value="修改银行卡", notes="修改")
//        @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "UpdateBankcardRequest")
//            @RequiresPermissions("common:bankcard:update") 	@PostMapping("/update")
//	public ResultDto update(@RequestBody @Valid UpdateBankcardRequest request) throws BizException{
//		ibankcardBiz.update(request);
//		return success();
//	}
//
//         @ApiOperation(value="删除", notes="删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//               @RequiresPermissions("common:bankcard:delete")
//         @PostMapping("/delete")
//    public ResultDto delete(@Valid  @RequestBody IDParamRequest request) throws BizException{
//        ibankcardBiz.deleteById(request.getId());
//       return success();
//}
}

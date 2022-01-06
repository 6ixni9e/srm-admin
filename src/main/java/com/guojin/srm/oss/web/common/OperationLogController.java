package com.guojin.srm.oss.web.common;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.request.QueryOperationLogRequest;
import com.guojin.srm.api.bean.common.response.OperationLogResponse;
import com.guojin.srm.api.biz.common.IOperationLogBiz;
import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时42分11秒
 */

@Api(value = "OperationLogController", tags = {"操作日志表"})
@RestController
@RequestMapping("/common/operationLog")
@Slf4j
public class OperationLogController extends BaseBusinessController {

    @Reference
    private IOperationLogBiz ioperationLogBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryOperationLogRequest")
    @RequiresPermissions("common:operationlog:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<OperationLogResponse>> list(@Valid @RequestBody QueryOperationLogRequest request) throws BizException {
        //查询列表数据
        PageResultVO<OperationLogResponse> result = ioperationLogBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("common:operationlog:info")
    @PostMapping("/info")
    public ResultDto<OperationLogResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        OperationLogResponse operationLogResponse = ioperationLogBiz.queryOperationLog(request.getId());
        return success(operationLogResponse);
    }



}

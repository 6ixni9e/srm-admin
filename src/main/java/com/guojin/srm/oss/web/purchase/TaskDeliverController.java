package com.guojin.srm.oss.web.purchase;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.QueryTaskDeliverRequest;
import com.guojin.srm.api.bean.purchase.response.TaskDeliverResponse;
import com.guojin.srm.api.biz.purchase.ITaskDeliverBiz;
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
 * @date 2021年10月26日 13时47分39秒
 */

@Api(value = "TaskDeliverController", tags = {"任务交付记录表"})
@RestController
@RequestMapping("/purchase/taskDeliver")
@Slf4j
public class TaskDeliverController extends BaseBusinessController {

    @Reference
    private ITaskDeliverBiz itaskDeliverBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryTaskDeliverRequest")
    @RequiresPermissions("purchase:taskdeliver:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<TaskDeliverResponse>> list(@Valid @RequestBody QueryTaskDeliverRequest request) throws BizException {
        //查询列表数据
        PageResultVO<TaskDeliverResponse> result = itaskDeliverBiz.queryList(request);
        return success(result);
    }


//    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("purchase:taskdeliver:info")
//    @PostMapping("/info")
//    public ResultDto<TaskDeliverResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
//        TaskDeliverResponse taskDeliverResponse = itaskDeliverBiz.queryTaskDeliver(request.getId());
//        return success(taskDeliverResponse);
//    }
//
//
//
//
//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateTaskDeliverRequest")
//    @RequiresPermissions("purchase:taskdeliver:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdateTaskDeliverRequest request) throws BizException {
//        itaskDeliverBiz.update(request);
//        return success();
//    }

//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("purchase:taskdeliver:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        itaskDeliverBiz.deleteById(request.getId());
//        return success();
//    }
}

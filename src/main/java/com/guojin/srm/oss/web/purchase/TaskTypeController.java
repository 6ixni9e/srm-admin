package com.guojin.srm.oss.web.purchase;

import javax.validation.Valid;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.AddTaskTypeRequest;
import com.guojin.srm.api.bean.purchase.request.QueryTaskTypeRequest;
import com.guojin.srm.api.bean.purchase.request.UpdateTaskTypeRequest;
import com.guojin.srm.api.bean.purchase.response.TaskTypeResponse;
import com.guojin.srm.api.biz.purchase.ITaskTypeBiz;
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
 * @date 2021年10月26日 13时49分59秒
 */

@Api(value = "TaskTypeController", tags = {"任务类型表"})
@RestController
@RequestMapping("/purchase/taskType")
@Slf4j
public class TaskTypeController extends BaseBusinessController {

    @Reference
    private ITaskTypeBiz itaskTypeBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryTaskTypeRequest")
    @RequiresPermissions("purchase:tasktype:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<TaskTypeResponse>> list(@Valid @RequestBody QueryTaskTypeRequest request) throws BizException {
        //查询列表数据
        PageResultVO<TaskTypeResponse> result = itaskTypeBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("purchase:tasktype:info")
    @PostMapping("/info")
    public ResultDto<TaskTypeResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        TaskTypeResponse taskTypeResponse = itaskTypeBiz.queryTaskType(request.getId());
        return success(taskTypeResponse);
    }


    @ApiOperation(value = "新增", notes = "获取新增信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddTaskTypeRequest")
    @RequiresPermissions("purchase:tasktype:save")
    @PostMapping("/save")
    public ResultDto save(@RequestBody @Valid AddTaskTypeRequest request) throws BizException {
        itaskTypeBiz.save(request);
        return success();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateTaskTypeRequest")
    @RequiresPermissions("purchase:tasktype:update")
    @PostMapping("/update")
    public ResultDto update(@RequestBody @Valid UpdateTaskTypeRequest request) throws BizException {
        itaskTypeBiz.update(request);
        return success();
    }

  /*  @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("purchase:tasktype:delete")
    @PostMapping("/delete")
    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
        itaskTypeBiz.deleteById(request.getId());
        return success();
    }*/
}

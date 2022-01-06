package com.guojin.srm.oss.web.purchase;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.QueryTaskCheckRequest;
import com.guojin.srm.api.bean.purchase.response.TaskCheckResponse;
import com.guojin.srm.api.biz.purchase.ITaskCheckBiz;
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
 * @date 2021年10月26日 13时47分38秒
 */

@Api(value = "TaskCheckController", tags = {"任务验收记录表"})
@RestController
@RequestMapping("/purchase/taskCheck")
@Slf4j
public class TaskCheckController extends BaseBusinessController {

    @Reference
    private ITaskCheckBiz itaskCheckBiz;


    @ApiOperation(value = "获取列表信息", notes = "获取列表信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryTaskCheckRequest")
    @RequiresPermissions("purchase:taskcheck:list")
    @PostMapping("/list")
    public ResultDto<PageResultVO<TaskCheckResponse>> list(@Valid @RequestBody QueryTaskCheckRequest request) throws BizException {
        //查询列表数据
        PageResultVO<TaskCheckResponse> result = itaskCheckBiz.queryList(request);
        return success(result);
    }


    @ApiOperation(value = "获取详细信息", notes = "获取详细信息")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
    @RequiresPermissions("purchase:taskcheck:info")
    @PostMapping("/info")
    public ResultDto<TaskCheckResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
        TaskCheckResponse taskCheckResponse = itaskCheckBiz.queryTaskCheck(request.getId());
        return success(taskCheckResponse);
    }

//
//    @ApiOperation(value = "新增", notes = "获取新增信息")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddTaskCheckRequest")
//    @RequiresPermissions("purchase:taskcheck:save")
//    @PostMapping("/save")
//    public ResultDto save(@RequestBody @Valid AddTaskCheckRequest request) throws BizException {
//        itaskCheckBiz.save(request);
//        return success();
//    }
//
//    @ApiOperation(value = "修改", notes = "修改")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateTaskCheckRequest")
//    @RequiresPermissions("purchase:taskcheck:update")
//    @PostMapping("/update")
//    public ResultDto update(@RequestBody @Valid UpdateTaskCheckRequest request) throws BizException {
//        itaskCheckBiz.update(request);
//        return success();
//    }

//    @ApiOperation(value = "删除", notes = "删除")
//    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
//    @RequiresPermissions("purchase:taskcheck:delete")
//    @PostMapping("/delete")
//    public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
//        itaskCheckBiz.deleteById(request.getId());
//        return success();
//    }
}

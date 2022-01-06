package com.guojin.srm.oss.web.purchase;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.purchase.request.AddProjectRequest;
import com.guojin.srm.api.bean.purchase.request.QueryProjectRequest;
import com.guojin.srm.api.bean.purchase.request.UpdateProjectRequest;
import com.guojin.srm.api.bean.purchase.response.ProjectInfoResponse;
import com.guojin.srm.api.bean.purchase.response.ProjectResponse;
import com.guojin.srm.api.biz.purchase.IProjectBiz;
import com.guojin.srm.common.enums.PartyTypeEnum;
import com.guojin.srm.oss.web.BaseBusinessController;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guojin.srm.common.base.IDParamRequest;
import com.guojin.srm.common.base.PageResultVO;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.ProjectStatusEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessParty;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.common.utils.ResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时47分37秒
 */

@Api(value = "ProjectController", tags = { "商户项目表" })
@RestController
@RequestMapping("/purchase/project")
@Slf4j
public class ProjectController extends BaseBusinessController {

	@Reference
	private IProjectBiz iprojectBiz;

	@ApiOperation(value = "获取列表信息", notes = "获取列表信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryProjectRequest")
	@RequiresPermissions("purchase:project:list")
	@PostMapping("/list")
	public ResultDto<PageResultVO<ProjectResponse>> list(@Valid @RequestBody QueryProjectRequest request)
			throws BizException {
		// 查询列表数据
		SessParty sessParty = getParty();
		if (sessParty == null) {
			return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
		}
		if(PartyTypeEnum.OEM.getValue() == sessParty.getPartyType()) {
			//OEM机构查询所有OEM机构下的数据
			request.setOemCode(getOemCode());
		}else if(PartyTypeEnum.MCHNT.getValue() == sessParty.getPartyType()) {
			//企业查询归属企业的数据
			request.setMerchantNo(sessParty.getPartyCode());
		}
		//平台查询所有数据
		PageResultVO<ProjectResponse> result = iprojectBiz.queryList(request);
		return success(result);
	}

	@ApiOperation(value = "获取详细信息", notes = "获取详细信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:project:info")
	@PostMapping("/info")
	public ResultDto<ProjectInfoResponse> info(@Valid @RequestBody IDParamRequest request) throws BizException {
		ProjectInfoResponse projectResponse = iprojectBiz.queryProjectInfo(request.getId());
		return success(projectResponse);
	}

	@ApiOperation(value = "新增", notes = "获取新增信息")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "AddProjectRequest")
	@RequiresPermissions("purchase:project:save")
	@PostMapping("/save")
	public ResultDto save(@RequestBody @Valid AddProjectRequest request) throws BizException {
		SessUser sessUser = new SessUser();
		iprojectBiz.save(request, sessUser);
		return success();
	}

	@ApiOperation(value = "修改", notes = "修改")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "UpdateProjectRequest")
	@RequiresPermissions("purchase:project:update")
	@PostMapping("/update")
	public ResultDto update(@RequestBody @Valid UpdateProjectRequest request) throws BizException {
		SessUser sessUser = new SessUser();
		iprojectBiz.update(request,sessUser);
		return success();
	}

	/*@ApiOperation(value = "删除", notes = "删除")
	@ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "IDParamRequest")
	@RequiresPermissions("purchase:project:delete")
	@PostMapping("/delete")
	public ResultDto delete(@Valid @RequestBody IDParamRequest request) throws BizException {
		iprojectBiz.deleteById(request.getId());
		return success();
	}*/
	
	
	@ApiOperation(value = "导出列表", notes = "导出列表")
    @ApiImplicitParam(name = "request", value = "请求对象", required = true, dataType = "QueryProjectRequest")
	@RequiresPermissions("purchase:project:download")
    @PostMapping("/download")
    public ResultDto download(@RequestBody QueryProjectRequest request, HttpServletResponse response) throws BizException{
		// 查询列表数据
		request.setPageSize(0); //设置不分页
		// 查询列表数据
		SessParty sessParty = getParty();
		if (sessParty == null) {
			return fail(BaseExcCodesEnum.NO_OPERATE_RIGHT);
		}
		if(PartyTypeEnum.OEM.getValue() == sessParty.getPartyType()) {
			//OEM机构查询所有OEM机构下的数据
			request.setOemCode(getOemCode());
		}else if(PartyTypeEnum.MCHNT.getValue() == sessParty.getPartyType()) {
			//企业查询归属企业的数据
			request.setMerchantNo(sessParty.getPartyCode());
		}
		//平台查询所有数据
		PageResultVO<ProjectResponse> result = iprojectBiz.queryList(request);
		if (result.getTotalCount() > 0) {
			//文件名
			String fileName = "Project_"+getParty().getPartyCode()+".xls";
			// 循环数据处理
			List<List<Object>> values = new ArrayList<>();
			for (ProjectResponse item : result.getList()) {
				List<Object> temp = new ArrayList<>();
				temp.add(item.getProjectNo()); //项目编号
				temp.add(item.getMerchantName()); //企业名称
				temp.add(item.getProjectName()); //项目名称
				temp.add(item.getCreateTime()); //创建时间
				temp.add(ProjectStatusEnum.getByType(item.getStatus()).getDesc()); //项目状态
				values.add(temp);
			}
			_downloadFile(fileName,TITLES,values,response);
		}
		return success();
    }
	private static final String[] TITLES = new String[] {"项目编号","所属公司","项目名称","创建时间","项目状态"};
}

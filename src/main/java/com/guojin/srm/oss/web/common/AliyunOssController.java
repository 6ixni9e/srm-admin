package com.guojin.srm.oss.web.common;

import com.guojin.srm.api.bean.common.request.FileSaveKeyParamRequest;
import com.guojin.srm.api.bean.common.request.FileSaveRequest;
import com.guojin.srm.common.alipay.FileUploadSaveResponse;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.DateUtil;
import com.guojin.srm.common.utils.FileUtil;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.common.utils.StringUtil;
import com.guojin.srm.oss.biz.AliyunOssBiz;
import com.guojin.srm.oss.web.BaseBusinessController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时42分11秒
 */
@Api(value = "AliyunOssController", tags = {"阿里OSS文件上传"})
@RestController
@RequestMapping("/common/ossfile")
@Slf4j
public class AliyunOssController extends BaseBusinessController {

    @Autowired
    private AliyunOssBiz aliyunOssBiz;

    @ApiOperation(value = "通用文件上传(文件名上传后会带时间戳)", notes = "通用文件上传(文件名上传后会带时间戳)")
    @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "FileSaveRequest")
    @PostMapping(value = "/fileSave")
    public ResultDto<FileUploadSaveResponse> fileSave(@Valid FileSaveRequest request) throws BizException {
        if(request.getFile().isEmpty()){
            return fail(BaseExcCodesEnum.ENTITY_NOT_EXISTS.getCode(), "文件为空或不存在");
        }
        try {
            File f = FileUtil.imageMultipartFileToFile(request.getFile(), "file");
            // 在原文件名基础上追加时间戳，避免文件覆盖
            String originFileName = request.getFile().getOriginalFilename();
            log.info("FileSave FileName==>" + originFileName);
            int i = originFileName.lastIndexOf(".");
            String timeStr = DateUtil.format(DateUtil.FULL_TIMESTAMP, new Date());
            String newFileName = originFileName.substring(0, i) + "_" + timeStr + originFileName.substring(i);
            // 图片默认为 {env}/file 目录
            String fileKey = "fileupload" +File.separator;
            if(StringUtil.isNotBlank(request.getBizCode())){
                fileKey += request.getBizCode() + File.separator;
            }
            fileKey += newFileName;
            log.info("FileSave FileKey==>" + fileKey);
            byte[] data = FileUtil.readFile(f);
            FileUploadSaveResponse result = null;
            if(Objects.equals(2,request.getType())){
                result = aliyunOssBiz.privateUpload(null,fileKey, data);
            }else{
                result = aliyunOssBiz.publicUpload(null,fileKey,data);
            }
            if (f != null && f.exists()) {
                f.delete();
            }
            return success(result);
        } catch (Exception e) {
            return fail(BaseExcCodesEnum.SYSTEM_ERROR);
        }
    }

    @ApiOperation(value = "aliyunOSS文件下载", notes = "aliyunOSS文件下载")
    @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "FileSaveKeyParamRequest")
    @PostMapping("/fileDown")
    public ResultDto fileDown(@Valid @RequestBody FileSaveKeyParamRequest request, HttpServletResponse response) throws BizException {
        //下载文件到本地
        String tmpDir=System.getProperty("java.io.tmpdir");
        String localFilePath = tmpDir + request.getSaveKey();
        File tmpFile = new File(localFilePath.substring(0,localFilePath.lastIndexOf("/")));
        if(!tmpFile.exists()) {
            tmpFile.mkdirs();
        }

        boolean bool = aliyunOssBiz.privateDownload(null,request.getSaveKey(),localFilePath);
        if(bool) {
            byte[] data = cn.hutool.core.io.FileUtil.readBytes(localFilePath);
            String fileName = request.getSaveKey().substring(request.getSaveKey().lastIndexOf("/")+1);
            response.addHeader("filename", fileName);
            response.addHeader("Access-Control-Expose-Headers", "filename");
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setCharacterEncoding("utf-8");
            OutputStream os;
            try {
                os = response.getOutputStream();
                os.write(data);
                os.flush();
            } catch (IOException e) {
                log.error("文件["+request.getSaveKey()+"]下载异常",e);
                return fail("文件下载异常");
            }
        }else {
            return fail("文件下载失败");
        }
        return null;
    }

    @ApiOperation(value = "aliyunOSS私有域文件展示", notes = "aliyunOSS私有域文件展示")
    @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "FileSaveKeyParamRequest")
    @PostMapping("/privateUrlShow")
    public ResultDto<String> privateUrlShow(@Valid @RequestBody FileSaveKeyParamRequest request, HttpServletResponse response) throws BizException {
        String url = aliyunOssBiz.getPrivateUrl(null, request.getSaveKey());
        return success(url);
    }

    @ApiOperation(value = "aliyunOSS私有域文件展示", notes = "aliyunOSS私有域文件展示")
    @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "FileSaveKeyParamRequest")
    @PostMapping("/publicUrlShow")
    public ResultDto<String> publicUrlShow(@Valid @RequestBody FileSaveKeyParamRequest request,HttpServletResponse response) throws BizException {
        String url = aliyunOssBiz.getPublicUrl(null, request.getSaveKey());
        return success(url);
    }
}

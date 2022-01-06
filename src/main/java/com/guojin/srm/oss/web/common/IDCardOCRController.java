package com.guojin.srm.oss.web.common;

import com.alibaba.fastjson.JSON;
import com.guojin.srm.api.bean.common.request.FileSaveKeyParamRequest;
import com.guojin.srm.api.bean.common.request.FileSaveRequest;
import com.guojin.srm.api.bean.common.response.OcrIDCardBackResponse;
import com.guojin.srm.api.bean.common.response.OcrIDCardFrontResponse;
import com.guojin.srm.common.alipay.FileUploadSaveResponse;
import com.guojin.srm.common.dbkj.ocr.IdcardBack;
import com.guojin.srm.common.dbkj.ocr.IdcardFront;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.OssExcCodesEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.DateUtil;
import com.guojin.srm.common.utils.FileUtil;
import com.guojin.srm.common.utils.ResultDto;
import com.guojin.srm.common.utils.StringUtil;
import com.guojin.srm.oss.biz.AliyunOssBiz;
import com.guojin.srm.oss.biz.OCRBiz;
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
import java.util.regex.Pattern;

/**
 * @author yuqian
 * @email yuqian@99366.com
 * @date 2021年10月26日 13时42分11秒
 */
@Api(value = "IDCardOCRController", tags = {"身份证OCR识别"})
@RestController
@RequestMapping("/common/idcardOcr")
@Slf4j
public class IDCardOCRController extends BaseBusinessController {

    @Autowired
    private AliyunOssBiz aliyunOssBiz;
    @Autowired
    private OCRBiz ocrBiz;


    @ApiOperation(value = "上传身份证正面照片", notes = "上传身份证正面照片")
    @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "FileSaveRequest")
    @PostMapping("/idcardFront")
    public ResultDto<OcrIDCardFrontResponse> idcardFront(@Valid FileSaveRequest request) throws BizException {
        if(request.getFile().isEmpty()){
            return fail(BaseExcCodesEnum.ENTITY_NOT_EXISTS.getCode(), "文件为空或不存在");
        }
        OcrIDCardFrontResponse response = new OcrIDCardFrontResponse();

        try {
            String originFileName = request.getFile().getOriginalFilename();
            log.info("身份证正面照片名称:"+request.getFile().getName()+",大小:"+request.getFile().getSize());
            File f = FileUtil.imageMultipartFileToFile(request.getFile(),"image");
            byte[] frontImageBytes = FileUtil.readFile(f);

            // 身份证正面OCR识别
            IdcardFront frontInfo = ocrBiz.idcardFront(null,frontImageBytes);
            log.info("--身份证正面识别信息:"+JSON.toJSONString(frontInfo));
            if(frontInfo == null ||StringUtil.isBlank(frontInfo.getName())
                    ||StringUtil.isBlank(frontInfo.getIdcardNo())
                    ||frontInfo.getIdcardNo().length() != 18) {
                return fail("IFE001","身份证正面识别错误，请重新操作");
            }
            response.setFrontInfo(frontInfo);
            // 文件上传
            int i = originFileName.lastIndexOf(".");
            String timeStr = DateUtil.format(DateUtil.FULL_TIMESTAMP, new Date());
            String newFileName = originFileName.substring(0, i) + "_" + timeStr + originFileName.substring(i);
            String fileKey = "userAuth" +File.separator;
            if(StringUtil.isNotBlank(request.getBizCode())){
                fileKey += request.getBizCode() + File.separator;
            }
            fileKey += newFileName;
            try {
                FileUploadSaveResponse res = aliyunOssBiz.privateUpload(null,fileKey, frontImageBytes);
                log.info("--身份证正面识别信息 - 上传阿里云 ：" + JSON.toJSONString(res));
                response.setFile(res);
                return success(response);
            } catch (Exception e) {
                log.error("文件上传失败", e);
                return fail(OssExcCodesEnum.FILE_UPLOAD_FAIL);
            } finally {
                if (f != null && f.exists()) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            return fail(BaseExcCodesEnum.SYSTEM_ERROR);
        }
    }


    @ApiOperation(value = "上传身份证背面照片", notes = "上传身份证背面照片")
    @ApiImplicitParam(name ="request", value = "请求对象", required = true, dataType = "FileSaveRequest")
    @PostMapping("/idcardBack")
    public ResultDto<OcrIDCardBackResponse> idcardBack(@Valid FileSaveRequest request) throws BizException {
        if(request.getFile().isEmpty()){
            return fail(BaseExcCodesEnum.ENTITY_NOT_EXISTS.getCode(), "文件为空或不存在");
        }
        OcrIDCardBackResponse response = new OcrIDCardBackResponse();

        try {
            String originFileName = request.getFile().getOriginalFilename();
            log.info("身份证反面照片名称:"+request.getFile().getName()+",大小:"+request.getFile().getSize());
            File f = FileUtil.imageMultipartFileToFile(request.getFile(),"image");
            byte[] imageBytes = FileUtil.readFile(f);

            // 身份证正面OCR识别
            IdcardBack backInfo = ocrBiz.idcardBack(null,imageBytes);
            log.info("--身份证反面识别信息:"+JSON.toJSONString(backInfo));
            if(backInfo == null || StringUtil.isBlank(backInfo.getSignDate())
                    ||StringUtil.isBlank(backInfo.getInvalidDate())) {
                return fail("IFE002","身份证反面识别错误，请重新操作");
            }
            //验证日期的格式
            if(!Pattern.matches("^\\d{4}\\.\\d{2}\\.\\d{2}$",backInfo.getSignDate())
                    || !Pattern.matches("^(\\d{4}\\.\\d{2}\\.\\d{2})|(长期)|([\u4e00-\u9fa5]+)$",backInfo.getInvalidDate())) {
                return fail("IFE003","身份证反面识别错误，请重新操作");
            }
            response.setBackInfo(backInfo);
            // 文件上传
            int i = originFileName.lastIndexOf(".");
            String timeStr = DateUtil.format(DateUtil.FULL_TIMESTAMP, new Date());
            String newFileName = originFileName.substring(0, i) + "_" + timeStr + originFileName.substring(i);
            String fileKey = "userAuth" +File.separator;
            if(StringUtil.isNotBlank(request.getBizCode())){
                fileKey += request.getBizCode() + File.separator;
            }
            fileKey += newFileName;
            try {
                FileUploadSaveResponse res = aliyunOssBiz.privateUpload(null,fileKey, imageBytes);
                log.info("--身份证反面识别信息 - 上传阿里云 ：" + JSON.toJSONString(res));
                response.setFile(res);
                return success(response);
            } catch (Exception e) {
                log.error("文件上传失败", e);
                return fail(OssExcCodesEnum.FILE_UPLOAD_FAIL);
            } finally {
                if (f != null && f.exists()) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            return fail(BaseExcCodesEnum.SYSTEM_ERROR);
        }
    }
}

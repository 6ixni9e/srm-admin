package com.guojin.srm.oss.biz;

import cn.hutool.json.JSONUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.StringUtils;
import com.guojin.srm.api.bean.common.response.SysConfigResponse;
import com.guojin.srm.api.biz.common.ISysConfigBiz;
import com.guojin.srm.common.alipay.AliyunOssConfig;
import com.guojin.srm.common.alipay.AliyunOssSupport;
import com.guojin.srm.common.alipay.AliyunOssUtils;
import com.guojin.srm.common.alipay.FileUploadSaveResponse;
import com.guojin.srm.common.constant.SysConfigConstants;
import com.guojin.srm.common.dbkj.DbkjConfigProperties;
import com.guojin.srm.common.dbkj.ocr.IdcardBack;
import com.guojin.srm.common.dbkj.ocr.IdcardFront;
import com.guojin.srm.common.dbkj.ocr.OcrRequest;
import com.guojin.srm.common.dbkj.ocr.OcrSupport;
import com.guojin.srm.common.enums.OssExcCodesEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.FileUtil;
import com.guojin.srm.common.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class AliyunOssBiz {

    @Reference
    private ISysConfigBiz iSysConfigBiz;

    private Map<String,AliyunOssConfig> configMap = new HashMap<>();

    public FileUploadSaveResponse publicUpload(String oemCode,String ossFileKey, File file) throws BizException {
        return AliyunOssSupport.publicUpload(_getAliyunOssConfig(oemCode),ossFileKey,file);
    }

    public FileUploadSaveResponse privateUpload(String oemCode,String ossFileKey, File file) throws BizException {
        return AliyunOssSupport.privateUpload(_getAliyunOssConfig(oemCode),ossFileKey,file);
    }

    public FileUploadSaveResponse publicUpload(String oemCode,String ossFileKey, byte[] data) throws BizException {
        return AliyunOssSupport.publicUpload(_getAliyunOssConfig(oemCode),ossFileKey,data);
    }

    public FileUploadSaveResponse privateUpload(String oemCode,String ossFileKey, byte[] data) throws BizException {
        return AliyunOssSupport.privateUpload(_getAliyunOssConfig(oemCode),ossFileKey,data);
    }

    public String getPublicUrl(String oemCode,String ossFileKey) throws BizException {
        return AliyunOssSupport.getPublicUrl(_getAliyunOssConfig(oemCode),ossFileKey);
    }

    public String getPrivateUrl(String oemCode,String ossFileKey) throws BizException {
        return AliyunOssSupport.getPrivateUrl(_getAliyunOssConfig(oemCode),ossFileKey);
    }

    public boolean publicDelete(String oemCode,String ossFileKey) throws BizException {
        return AliyunOssSupport.publicDelete(_getAliyunOssConfig(oemCode),ossFileKey);
    }

    public boolean privateDelete(String oemCode,String ossFileKey) throws BizException {
        return AliyunOssSupport.privateDelete(_getAliyunOssConfig(oemCode),ossFileKey);
    }

    public boolean publicDownload(String oemCode,String ossFileKey, String fileLocalPath) throws BizException {
        return AliyunOssSupport.publicDownload(_getAliyunOssConfig(oemCode),ossFileKey,fileLocalPath);
    }

    public boolean privateDownload(String oemCode,String ossFileKey, String fileLocalPath) throws BizException {
        return AliyunOssSupport.privateDownload(_getAliyunOssConfig(oemCode),ossFileKey,fileLocalPath);
    }

    /**
     * 从配置中读取阿里云服务参数
     * @param oemCode
     * @return
     * @throws BizException
     */
    protected AliyunOssConfig _getAliyunOssConfig(String oemCode)throws BizException{
        if(!configMap.containsKey(oemCode)){
            SysConfigResponse configBean = iSysConfigBiz.findByConfigCode(SysConfigConstants.CONFIG_ALIOSS,oemCode);
            AliyunOssConfig ossConfig = JSONUtil.toBean(configBean.getConfigValue(),AliyunOssConfig.class);
            configMap.put(oemCode,ossConfig);
        }
        return configMap.get(oemCode);
    }
}

package com.guojin.srm.oss.biz;

import cn.hutool.json.JSONUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.common.response.SysConfigResponse;
import com.guojin.srm.api.biz.common.ISysConfigBiz;
import com.guojin.srm.common.constant.SysConfigConstants;
import com.guojin.srm.common.dbkj.DbkjConfigProperties;
import com.guojin.srm.common.dbkj.ocr.IdcardBack;
import com.guojin.srm.common.dbkj.ocr.IdcardFront;
import com.guojin.srm.common.dbkj.ocr.OcrRequest;
import com.guojin.srm.common.dbkj.ocr.OcrSupport;
import com.guojin.srm.common.exception.BizException;
import org.springframework.stereotype.Component;

@Component
public class OCRBiz {

    @Reference
    private ISysConfigBiz iSysConfigBiz;

    /**
     *<p>识别并返回身份证正面信息</p>
     * @param imageBytes
     * @return
     */
    public IdcardFront idcardFront(String oemCode,byte[] imageBytes) throws BizException {
        OcrRequest req = _buildOcrRequest(oemCode);
        req.setImageBytes(imageBytes);
        IdcardFront frontInfo = OcrSupport.idCardFront(req);
        return frontInfo;
    }

    /**
     *<p>识别并返回身份证背面信息</p>
     * @param imageBytes
     * @return
     */
    public IdcardBack idcardBack(String oemCode,byte[] imageBytes) throws BizException {
        OcrRequest req = _buildOcrRequest(oemCode);
        req.setImageBytes(imageBytes);
        IdcardBack backInfo = OcrSupport.idCardBack(req);
        return backInfo;
    }

    protected OcrRequest _buildOcrRequest(String oemCode)throws BizException{
        SysConfigResponse configBean = iSysConfigBiz.findByConfigCode(SysConfigConstants.CONFIG_DBKJ,oemCode);
        DbkjConfigProperties dbkjConfig = JSONUtil.toBean(configBean.getConfigValue(),DbkjConfigProperties.class);
        OcrRequest req = new OcrRequest();
        req.setUrl(dbkjConfig.getAddress()+DbkjConfigProperties.URI_OCR);
        req.setMchntNo(dbkjConfig.getMchntNo());
        req.setSignKey(dbkjConfig.getSignKey());
        req.setKeyNum(dbkjConfig.getKeyNum());
        req.setSrvPublicKey(dbkjConfig.getSrvPublicKey());
        req.setSerialNo("SRM" + System.currentTimeMillis());
        return req;
    }
}

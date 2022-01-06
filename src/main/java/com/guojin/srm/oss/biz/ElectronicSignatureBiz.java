package com.guojin.srm.oss.biz;

import com.guojin.srm.common.alipay.FileUploadSaveResponse;
import com.guojin.srm.common.electronic.Seal;
import com.guojin.srm.common.electronic.SealCircle;
import com.guojin.srm.common.electronic.SealFont;
import com.guojin.srm.common.enums.BizExcCodesEnum;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.utils.SignatureImageUtils2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ElectronicSignatureBiz {

    @Autowired
    private AliyunOssBiz aliyunOssBiz;

    /**
     * 获取个人签名图片
     *  -- 生成签名图片并上传到阿里云私有服务，返回saveKey
     * @param custName
     * @return
     * @throws BizException
     */
    public String getPersonSignatureUrl(String custName)throws BizException {
        String ylFontPath = SignatureImageUtils2.class.getResource("/font/yl.ttf").getPath();
        byte[] fontBytes = cn.hutool.core.io.FileUtil.readBytes(ylFontPath);
        byte[] bytes = SignatureImageUtils2.genSignImage(custName,fontBytes);
        FileUploadSaveResponse res = aliyunOssBiz.privateUpload(null,"signature/"+custName+"-"+System.currentTimeMillis()+".png",bytes);
        return res.getSaveKey();
    }

    /**
     * 获取企业/个体电子签章图片
     *  -- 生成签章图片并上传到阿里云私有服务，返回saveKey
     * @param orgName
     * @return
     * @throws BizException
     */
    public String getCompanySignatureUrl(String orgName)throws BizException{
        try{
            byte[] bytes = Seal.builder().size(300).borderCircle(SealCircle.builder().line(5).width(140).height(140).build())
                    .mainFont(SealFont.builder().text(orgName).size(35).space(35.0).margin(10).build())
                    .centerFont(SealFont.builder().text("★").size(100).build())
                    .titleFont(SealFont.builder().text("电子签章").size(22).space(10.0).margin(68).build()).build()
                    .draw();
            FileUploadSaveResponse res = aliyunOssBiz.privateUpload(null,"signature/"+orgName+"-"+System.currentTimeMillis()+".png",bytes);
            return res.getSaveKey();
        }catch (Exception ex){
            log.error("生成企业电子签章异常",ex);
            throw new BizException(BizExcCodesEnum.CREATE_SIGNATUREPIC_ERR);
        }
    }
}

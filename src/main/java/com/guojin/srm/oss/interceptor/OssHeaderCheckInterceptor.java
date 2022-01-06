package com.guojin.srm.oss.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.org.response.OemResponse;
import com.guojin.srm.api.biz.org.IOemBiz;
import com.guojin.srm.common.constant.CommonConstants;
import com.guojin.srm.common.enums.BaseExcCodesEnum;
import com.guojin.srm.common.enums.OrganStatusEnum;
import com.guojin.srm.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


/**
 * 运营后台请求头检查
 *  1、主要检查oemCode是否丢失
 * @author k
 */
@Slf4j
public class OssHeaderCheckInterceptor implements HandlerInterceptor {

	@Reference
	private IOemBiz iOemBiz;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		// 如果不是映射到方法直接通过
		if(!(object instanceof HandlerMethod)){
			return true;
		}
		String oemCode = request.getHeader(CommonConstants.OEM_CODE);
		if (StringUtils.isBlank(oemCode)) {
			throw new BizException("BASE.999","请求机构编号丢失(请重新登录重试)");
		}
		OemResponse oem = iOemBiz.queryByOemCode(oemCode);
		if(oem == null){
			throw new BizException(BaseExcCodesEnum.ENTITY_NOT_EXISTS,"OEM机构");
		}
		if(!Objects.equals(OrganStatusEnum.NORMAL.getValue(),oem.getStatus())){
			throw new BizException(BaseExcCodesEnum.STATUS_ABNORMAL_ERR,"OEM机构");
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}

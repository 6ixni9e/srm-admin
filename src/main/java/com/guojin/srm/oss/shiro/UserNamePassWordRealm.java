package com.guojin.srm.oss.shiro;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guojin.srm.api.bean.sys.response.SysUserResponse;
import com.guojin.srm.api.biz.sys.ISysUserBiz;
import com.guojin.srm.common.exception.BizException;
import com.guojin.srm.common.session.SessUser;
import com.guojin.srm.common.shiro.AbstractUserRealm;
import com.guojin.srm.common.shiro.ShiroUtils;
import com.guojin.srm.common.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Lazy;

import java.util.Set;


/**
 * 认证
 * 
 */
@Slf4j
public class UserNamePassWordRealm extends AbstractUserRealm {
  
    @Reference
    @Lazy
    private ISysUserBiz iSysUserBiz;

    
    @Override
    protected Set<String> getStringPermissions(PrincipalCollection principals) throws BizException {
    	log.info("UserNamePassWordRealm getStringPermissions");
//        SessUser opUser = (SessUser) principals.getPrimaryPrincipal();
        //从shiro的session中获取
        SessUser opUser = ShiroUtils.getUser();
        Set<String> permissions = iSysUserBiz.queryAllPerms(opUser);
        log.info("加载权限开始==================");
        for (String premission:permissions) {
            log.info("======"+premission);
        }
        log.info("加载权限结束==================");
        return permissions;
    }

    @Override
    protected SessUser getSessUser(AuthenticationToken token) throws BizException {
    	log.info("UserNamePassWordRealm doGetAuthenticationInfo");
    	String userName = ((UsernamePasswordToken) token).getUsername();
        String password = new String(((UsernamePasswordToken) token).getPassword());
        String oemCode = ((UsernamePasswordToken) token).getHost();
        //查询用户信息
        SessUser user = new SessUser();
        SysUserResponse userBean = iSysUserBiz.userLogin(userName,password,oemCode);
        if(null !=userBean){
            BeanUtil.copyProperties(userBean, user);
        }
        return user;
    }

    /**
     * 清除权限缓存
     * @throws BizException
     */
    public void clearCachedAuthorization(){
        clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}

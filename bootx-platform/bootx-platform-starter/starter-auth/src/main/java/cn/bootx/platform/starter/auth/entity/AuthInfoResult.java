package cn.bootx.platform.starter.auth.entity;

import cn.bootx.platform.core.entity.UserDetail;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 认证返回结果
 *
 * @author xxm
 * @since 2021/7/30
 */
@Data
@Accessors(chain = true)
public class AuthInfoResult {

    /** 用户id */
    private Object id;

    /** 认证终端(例如管理端/小程序/h5等) */
    private String client = "";

    /** 登录方式(例如web/开放平台/app等) */
    private String loginType = "";

    /** 用户对象 */
    private UserDetail userDetail;

}

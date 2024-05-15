package cn.bootx.platform.starter.auth.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 三方登录认证码
 *
 * @author xxm
 * @since 2022/6/29
 */
@Data
@Accessors(chain = true)
public class ThirdAuthCode {

    /** 登录方式 */
    private String loginType;

    /** 认证码 */
    private String code;

    /**
     * 访问AuthorizeUrl后回调时带的参数state，用于和请求AuthorizeUrl前的state比较，防止CSRF攻击
     */
    private String state;

}

package cn.bootx.platform.iam.core.security.user.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.iam.core.security.user.convert.LoginSecurityConfigConvert;
import cn.bootx.platform.iam.dto.security.LoginSecurityConfigDto;
import cn.bootx.platform.iam.param.security.LoginSecurityConfigParam;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbComment;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 登录安全策略
 * @author xxm
 * @since 2023/8/17
 */
//@DbTable(comment = "登录安全策略")
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_login_security_config")
public class LoginSecurityConfig extends MpBaseEntity implements EntityBaseFunction<LoginSecurityConfigDto> {

    /** 关联终端ID */
    @DbComment("关联终端ID")
    private Long clientId;

    /** 修改密码是否需要重新登录 */
    @DbComment("修改密码是否需要重新登录")
    private Boolean  requireLoginChangePwd;

    /** 默认启用验证码 */
    @DbComment("默认启用验证码")
    private Boolean captchaEnable;

    /** 出现验证码的错误次数 0 表示不启用 */
    @DbColumn(comment = "出现验证码的错误次数")
    private Integer maxCaptchaErrorCount;

    /** 同端是否允许同时登录 */
    @DbComment("同端是否允许同时登录")
    private  Boolean  allowMultiLogin;

    /** 多终端是否允许同时登录 */
    @DbComment("多终端是否允许同时登录")
    private Boolean  allowMultiTerminalLogin;

    /** 创建对象 */
    public static LoginSecurityConfig init(LoginSecurityConfigParam in) {
        return LoginSecurityConfigConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public LoginSecurityConfigDto toDto() {
        return LoginSecurityConfigConvert.CONVERT.convert(this);
    }
}

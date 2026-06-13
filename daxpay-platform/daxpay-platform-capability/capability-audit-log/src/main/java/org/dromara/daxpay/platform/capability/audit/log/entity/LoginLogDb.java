package org.dromara.daxpay.platform.capability.audit.log.entity;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpIdEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.capability.audit.log.convert.LogConvert;
import org.dromara.daxpay.platform.capability.audit.log.result.LoginLogResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 登录日志
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("starter_audit_login_log")
public class LoginLogDb extends MpIdEntity implements ToResult<LoginLogResult> {

    /// 用户账号id
    private Long userId;

    /// 用户名称
    private String account;

    /// 登录成功状态
    private Boolean login;

    /// 登录终端
    private String client;

    /// 登录方式
    private String loginType;

    /// 登录IP地址
    private String ip;

    /// 登录地点
    private String loginLocation;

    /// 浏览器类型
    private String browser;

    /// 操作系统
    private String os;

    /// 提示消息
    private String msg;

    /// 访问时间 (UTC)
    private OffsetDateTime loginTime;

    @Override
    public LoginLogResult toResult() {
        return LogConvert.CONVERT.convert(this);
    }

}

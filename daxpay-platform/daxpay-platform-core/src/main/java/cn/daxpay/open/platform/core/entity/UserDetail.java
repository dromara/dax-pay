package cn.daxpay.open.platform.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 用户登录会话信息
///
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class UserDetail {

    /// 用户id
    private Long id;

    /// 用户名称
    private String name;

    /// 终端编码
    private String clientCode;

    /// 账号
    private String account;

    /// 是否管理员
    private boolean admin;

    /// 账号状态
    private String status;

    /// 密码是否过期
    private Boolean passwordExpired;

    /// 是否初始密码
    private Boolean initialPassword;

    /// 密码过期时间 (UTC)
    private OffsetDateTime passwordExpireTime;

    /// 是否需要修改密码
    public boolean needChangePassword() {
        return Boolean.TRUE.equals(passwordExpired) || Boolean.TRUE.equals(initialPassword);
    }

    /// 构建标准登录会话用户
    public static UserDetail of(Long id, String name, String clientCode, String account,
                                boolean admin, String status) {
        return new UserDetail()
                .setId(id)
                .setName(name)
                .setClientCode(clientCode)
                .setAccount(account)
                .setAdmin(admin)
                .setStatus(status);
    }

}


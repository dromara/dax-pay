package cn.bootx.platform.starter.audit.log.core.mongo.entity;

import cn.bootx.platform.starter.audit.log.core.mongo.convert.LogConvert;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.starter.audit.log.dto.LoginLogDto;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author xxm
 * @since 2021/12/2
 */
@Data
@Accessors(chain = true)
@Document(collection = "starter_audit_login_log")
public class LoginLogMongo implements EntityBaseFunction<LoginLogDto> {

    @Id
    private Long id;

    /** 用户账号id */
    private Long userId;

    /** 用户名称 */
    private String account;

    /** 登录成功状态 */
    private Boolean login;

    /** 登录终端 */
    private String client;

    /** 登录方式 */
    private String loginType;

    /** 登录IP地址 */
    private String ip;

    /** 登录地点 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 提示消息 */
    private String msg;

    /** 访问时间 */
    private LocalDateTime loginTime;

    @Override
    public LoginLogDto toDto() {
        return LogConvert.CONVERT.convert(this);
    }

}

package cn.bootx.platform.starter.auth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证配置参数
 *
 * @author xxm
 * @since 2021/7/30
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "bootx-platform.starter.auth")
public class AuthProperties {

    /** 不进行鉴权的路径 */
    private List<String> ignoreUrls = new ArrayList<>();

    /** 盐值 */
    private String salt = "salt";

    /** 开启超级管理员(生产模式推荐关闭) */
    private boolean enableAdmin = true;

    /** 用户管理列表中是否显示 */
    private boolean adminInList = true;

}

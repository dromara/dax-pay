package cn.bootx.platform.starter.audit.log.param;

import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author xxm
 * @since 2025/2/12
 */
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Data
@Accessors(chain = true)
@Schema(description = "登录日志")
public class LoginLogQuery {

    @Schema(description = "用户账号id")
    private Long userId;

    @Schema(description = "用户账号")
    private String account;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "登录成功状态")
    private Boolean login;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "登录终端")
    private String client;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "登录方式")
    private String loginType;

    @Schema(description = "登录IP地址")
    private String ip;

    @Schema(description = "登录地点")
    private String loginLocation;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

}

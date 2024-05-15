package cn.bootx.platform.notice.param.mail;

import cn.bootx.platform.notice.code.MailCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/5/2 14:42
 */
@Data
@Schema(title = "邮箱配置 DTO")
public class MailConfigParam implements Serializable {

    private static final long serialVersionUID = 2322690493233843789L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "邮箱服务器 host")
    private String host;

    @Schema(description = "邮箱服务器 port")
    private Integer port;

    @Schema(description = "邮箱服务器 username")
    private String username;

    @Schema(description = "邮箱服务器 password")
    private String password;

    @Schema(description = "邮箱服务器 sender")
    private String sender;

    @Schema(description = "邮箱服务器 from")
    private String from;

    @Schema(description = "是否默认配置")
    private Boolean activity = false;

    @Schema(description = "安全方式")
    private Integer securityType = MailCode.SECURITY_TYPE_PLAIN;

}

package cn.bootx.platform.notice.dto.mail;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.notice.code.MailCode;
import cn.bootx.platform.starter.data.perm.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/5/2 14:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "邮箱配置")
@Accessors(chain = true)
public class MailConfigDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 2322690493233843789L;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "地址")
    private String host;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "账号")
    @SensitiveInfo
    private String username;

    @Schema(description = "password")
    @SensitiveInfo(SensitiveInfo.SensitiveType.PASSWORD)
    private String password;

    @Schema(description = "sender")
    private String sender;

    @Schema(description = "from")
    private String from;

    @Schema(description = "是否默认配置")
    private Boolean activity = false;

    /**
     * @see MailCode
     */
    @Schema(description = "安全方式")
    private Integer securityType;

}

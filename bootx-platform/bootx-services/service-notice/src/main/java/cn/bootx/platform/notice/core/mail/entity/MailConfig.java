package cn.bootx.platform.notice.core.mail.entity;

import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.notice.core.mail.convert.MailConvert;
import cn.bootx.platform.notice.param.mail.MailConfigParam;
import cn.bootx.platform.notice.dto.mail.MailConfigDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 邮件配置
 *
 * @author xxm
 * @since 2020/4/8 11:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("notice_mail_config")
public class MailConfig extends MpBaseEntity implements EntityBaseFunction<MailConfigDto> {

    /** 编号 */
    private String code;

    /** 名称 */
    private String name;

    /** 邮箱服务器 host */
    private String host;

    /** 邮箱服务器 端口 */
    private Integer port;

    /** 邮箱服务器 账号 */
    @EncryptionField
    private String username;

    /** 邮箱服务器 密码 */
    @EncryptionField
    private String password;

    /** 邮箱服务器 发送人名称 */
    private String sender;

    /** 邮箱服务器 发送人邮箱账号 */
    @TableField("from_")
    private String from;

    /** 是否默认配置 */
    private Boolean activity;

    /** 安全方式 */
    private Integer securityType;

    public static MailConfig init(MailConfigParam in) {
        return MailConvert.CONVERT.convert(in);
    }

    @Override
    public MailConfigDto toDto() {
        return MailConvert.CONVERT.convert(this);
    }

}

package cn.bootx.platform.notice.dto.template;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.notice.code.MessageTemplateCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 消息模板
 *
 * @author xxm
 * @since 2021/8/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "消息模板")
public class MessageTemplateDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = -1377790220501836009L;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 内容 */
    private String data;

    /** 备注 */
    private String remark;

    /**
     * 模板类型
     * @see MessageTemplateCode
     */
    private String type;

}

package cn.bootx.platform.notice.param.template;

import cn.bootx.platform.notice.code.MessageTemplateCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2021/8/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "消息模板")
public class MessageTemplateParam implements Serializable {

    private static final long serialVersionUID = 593034193370220643L;

    @Schema(description = "主键")
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 内容
     */
    private String data;

    /**
     * 备注
     */
    private String remark;

    /**
     * 模板类型
     * @see MessageTemplateCode
     */
    private String type;
}

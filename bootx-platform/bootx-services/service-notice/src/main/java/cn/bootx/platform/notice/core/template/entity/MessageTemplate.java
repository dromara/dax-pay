package cn.bootx.platform.notice.core.template.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.notice.core.template.convert.MessageTemplateConvert;
import cn.bootx.platform.notice.param.template.MessageTemplateParam;
import cn.bootx.platform.notice.code.MessageTemplateCode;
import cn.bootx.platform.notice.dto.template.MessageTemplateDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("notice_message_template")
@Accessors(chain = true)
public class MessageTemplate extends MpBaseEntity implements EntityBaseFunction<MessageTemplateDto> {

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

    public static MessageTemplate init(MessageTemplateParam in) {
        return MessageTemplateConvert.CONVERT.convert(in);
    }

    @Override
    public MessageTemplateDto toDto() {
        return MessageTemplateConvert.CONVERT.convert(this);
    }

}

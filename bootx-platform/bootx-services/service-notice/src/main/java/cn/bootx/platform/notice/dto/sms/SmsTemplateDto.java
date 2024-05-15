package cn.bootx.platform.notice.dto.sms;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 短信模板配置
 * @author xxm
 * @since 2023-08-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "短信模板配置")
@Accessors(chain = true)
public class SmsTemplateDto extends BaseDto {

    @Schema(description = "短信渠道商类型")
    private String supplierType;
    @Schema(description = "短信模板id")
    private String templateId;
    @Schema(description = "短信模板名称")
    private String name;
    @Schema(description = "短信模板内容")
    private String content;

}

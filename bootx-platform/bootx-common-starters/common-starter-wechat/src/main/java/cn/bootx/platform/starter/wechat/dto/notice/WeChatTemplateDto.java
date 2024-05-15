package cn.bootx.platform.starter.wechat.dto.notice;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信消息模板
 *
 * @author xxm
 * @since 2022-08-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "微信消息模板")
@Accessors(chain = true)
public class WeChatTemplateDto extends BaseDto {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "模板ID")
    private String templateId;

    @Schema(description = "模板标题")
    private String title;

    @Schema(description = "模板所属行业的一级行业")
    private String primaryIndustry;

    @Schema(description = "模板所属行业的二级行业")
    private String deputyIndustry;

    @Schema(description = "模板内容")
    private String content;

    @Schema(description = "示例")
    private String example;

}

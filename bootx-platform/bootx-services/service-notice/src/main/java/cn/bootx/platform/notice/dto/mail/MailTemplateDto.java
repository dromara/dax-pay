package cn.bootx.platform.notice.dto.mail;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "邮件模板")
public class MailTemplateDto extends BaseDto {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "内容")
    private String date;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "类型")
    private Integer type;

}

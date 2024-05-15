package cn.bootx.platform.notice.param.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 邮件模板
 *
 * @author xxm
 * @since 2021/8/5
 */
@Data
@Accessors(chain = true)
@Schema(title = "邮件模板")
public class MailTemplateParam {

    @Schema(description = "主键")
    private Long id;

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

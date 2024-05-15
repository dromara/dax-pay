package cn.bootx.platform.baseapi.dto.template;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通用模板管理
 * @author xxm
 * @since 2023-08-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "通用模板管理")
@Accessors(chain = true)
public class GeneralTemplateDto extends BaseDto {

    @Schema(description = "模板名称")
    private String name;
    @Schema(description = "模板代码")
    private String code;
    @Schema(description = "使用类型(导入/导出)")
    private String useType;
    @Schema(description = "模板类型")
    private String fileType;
    @Schema(description = "模板后缀名")
    private String fileSuffix;
    @Schema(description = "状态")
    private String state;
    @Schema(description = "文件ID")
    private Long fileId;
    @Schema(description = "备注")
    private String remark;

}

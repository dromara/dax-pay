package cn.bootx.platform.starter.code.gen.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 代码生成相关参数信息
 *
 * @author xxm
 * @since 2022/8/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "代码生成相关参数信息")
public class TableGenParamDto {

    @Schema(description = "实体类名称(大驼峰)")
    private String entityName;

    @Schema(description = "功能模块名称(全小写)")
    private String module;

}

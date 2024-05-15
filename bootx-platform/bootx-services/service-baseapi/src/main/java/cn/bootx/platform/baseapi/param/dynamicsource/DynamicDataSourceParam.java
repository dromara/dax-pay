package cn.bootx.platform.baseapi.param.dynamicsource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 动态数据源管理
 *
 * @author xxm
 * @since 2022-09-24
 */
@Data
@Schema(title = "动态数据源管理")
@Accessors(chain = true)
public class DynamicDataSourceParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "数据源编码")
    private String code;

    @Schema(description = "数据源名称")
    private String name;

    @Schema(description = "数据库类型")
    private String databaseType;

    @Schema(description = "驱动类")
    private String dbDriver;

    @Schema(description = "数据库地址")
    private String dbUrl;

    @Schema(description = "用户名")
    private String dbUsername;

    @Schema(description = "密码")
    private String dbPassword;

    @Schema(description = "备注")
    private String remark;

}

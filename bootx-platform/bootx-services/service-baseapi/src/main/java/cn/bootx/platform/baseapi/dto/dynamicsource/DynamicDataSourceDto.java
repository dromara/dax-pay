package cn.bootx.platform.baseapi.dto.dynamicsource;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.starter.data.perm.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 动态数据源管理
 *
 * @author xxm
 * @since 2022-09-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "动态数据源管理")
@Accessors(chain = true)
public class DynamicDataSourceDto extends BaseDto {

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

    @SensitiveInfo(SensitiveInfo.SensitiveType.PASSWORD)
    @Schema(description = "密码")
    private String dbPassword;

    @Schema(description = "备注")
    private String remark;

}

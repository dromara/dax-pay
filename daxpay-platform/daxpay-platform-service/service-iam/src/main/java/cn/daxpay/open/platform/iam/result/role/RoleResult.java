package cn.daxpay.open.platform.iam.result.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@Schema(title = "角色")
public class RoleResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "角色code")
    private String code;

    @Schema(description = "中文名称")
    private String nameCn;

    @Schema(description = "英文名称")
    private String nameEn;

    @Schema(description = "终端编码")
    private String clientCode;

    @Schema(description = "是否系统内置")
    private boolean internal;

    @Schema(description = "描述")
    private String remark;

}

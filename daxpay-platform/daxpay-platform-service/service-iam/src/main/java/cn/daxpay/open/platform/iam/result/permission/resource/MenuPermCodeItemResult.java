package cn.daxpay.open.platform.iam.result.permission.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 菜单权限码项
///
@Data
@Accessors(chain = true)
@Schema(title = "菜单权限码项")
public class MenuPermCodeItemResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "权限码")
    private String code;

    @Schema(description = "国际化key")
    private String i18nKey;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "是否系统内置")
    private boolean internal;

    @Schema(description = "备注")
    private String remark;
}

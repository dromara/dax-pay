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

    /// 过渡期保留：展示以 i18nKey 为准，文案真相源在语言包
    @Schema(description = "国际化key")
    private String i18nKey;

    @Schema(description = "身份域编码")
    private String clientCode;

    @Schema(description = "是否系统内置")
    private boolean internal;

    @Schema(description = "描述")
    private String remark;

}

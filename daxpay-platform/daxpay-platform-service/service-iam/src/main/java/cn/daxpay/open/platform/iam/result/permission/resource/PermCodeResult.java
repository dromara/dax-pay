package cn.daxpay.open.platform.iam.result.permission.resource;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 权限码
///
@Data
@Accessors(chain = true)
@Schema(title = "权限码")
public class PermCodeResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "权限码")
    private String code;

    /// 过渡期保留：展示以 i18nKey 为准，文案真相源在语言包
    @Schema(description = "国际化key")
    private String i18nKey;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "是否系统内置")
    private boolean internal;

    @Schema(description = "备注")
    private String remark;

    /// 显示标题（i18nKey 优先，fallback code）
    public String getTitle(){
        if (StrUtil.isNotBlank(i18nKey)){
            return i18nKey;
        }
        return code;
    }
}

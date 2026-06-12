package org.dromara.daxpay.platform.iam.result.permission.resource;

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

    @Schema(description = "中文名称")
    private String nameCn;

    @Schema(description = "英文名称")
    private String nameEn;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "是否系统内置")
    private boolean internal;

    @Schema(description = "备注")
    private String remark;

    /// 显示标题
    public String getTitle(){
        if (StrUtil.isBlank(code)){
            return nameCn;
        }
        return code + "(" + nameCn + ")";
    }
}

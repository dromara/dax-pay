package org.dromara.daxpay.platform.iam.param.role;

import org.dromara.daxpay.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 角色查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "角色查询参数")
public class RoleQuery extends SortParam {

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "中文名称")
    private String nameCn;

    @Schema(description = "英文名称")
    private String nameEn;

    @Schema(description = "终端编码")
    private String clientCode;

}

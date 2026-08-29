package cn.daxpay.open.platform.iam.param.role;

import cn.daxpay.open.platform.common.mybatisplus.query.entity.SortParam;
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

    @Schema(description = "身份域编码")
    private String clientCode;

}

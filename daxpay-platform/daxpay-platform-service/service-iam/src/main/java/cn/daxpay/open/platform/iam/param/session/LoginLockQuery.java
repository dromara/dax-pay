package cn.daxpay.open.platform.iam.param.session;

import cn.daxpay.open.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 登录锁定查询参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "登录锁定查询参数")
public class LoginLockQuery extends SortParam {

    @Schema(description = "用户名称")
    private String username;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "身份域编码")
    private String clientCode;

    @Schema(description = "锁定状态: locked-锁定中 expired-已到期 counting-计数中")
    private String status;
}

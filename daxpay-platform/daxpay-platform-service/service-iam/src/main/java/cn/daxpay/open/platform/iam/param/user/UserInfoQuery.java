package cn.daxpay.open.platform.iam.param.user;

import cn.daxpay.open.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户查询偶参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户查询偶参数")
public class UserInfoQuery extends SortParam {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "身份域编码")
    private String clientCode;

    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "账号状态")
    private String status;
}


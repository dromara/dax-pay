package cn.bootx.platform.iam.param.user;

import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户查询偶参数
 * @author xxm
 * @since 2024/8/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户查询偶参数")
public class UserInfoQuery extends SortParam {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;
}

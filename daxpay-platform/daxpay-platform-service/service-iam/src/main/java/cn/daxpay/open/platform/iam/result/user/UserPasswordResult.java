package cn.daxpay.open.platform.iam.result.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 用户初始密码结果
///
/// 重置密码 / 新建用户未指定密码时, 由后端生成随机密码并一次性返回明文,
/// 供管理员复制转告用户; 该密码视为初始密码, 用户首次登录将被强制要求自行修改。
/// 注意: 不加脱敏注解, 字段本身就是要在本次响应中明文透出的临时口令。
@Data
@Accessors(chain = true)
@Schema(title = "用户初始密码结果")
public class UserPasswordResult {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "登录账号")
    private String account;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "初始密码(明文, 仅本次响应返回一次, 请立即复制转交用户)")
    private String password;
}

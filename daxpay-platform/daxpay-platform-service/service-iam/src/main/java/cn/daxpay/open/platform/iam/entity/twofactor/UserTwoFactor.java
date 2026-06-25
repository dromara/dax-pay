package cn.daxpay.open.platform.iam.entity.twofactor;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import cn.daxpay.open.platform.common.mybatisplus.handler.type.JsonbStringTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户双因素认证绑定记录
///
/// 一对一关联用户, 记录存在即代表该用户已启用 TOTP 双因素认证。
/// secret 为 TOTP 密钥(Base32), 使用 AES-256-GCM 加密存储;
/// backup_codes 为 jsonb 数组, 元素结构见 [cn.daxpay.open.platform.iam.auth.service.twofactor.BackupCodeEntry],
/// 仅存哈希不存明文。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "iam_user_two_factor", autoResultMap = true)
public class UserTwoFactor extends MpBaseEntity {

    /// 用户ID(关联 iam_user_info.id)
    @TableField(updateStrategy = com.baomidou.mybatisplus.annotation.FieldStrategy.NEVER)
    private Long userId;

    /// TOTP 密钥(Base32, 加密存储 AES-256-GCM)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String secret;

    /// 备用验证码(JSON 数组原始文本, 元素 {hash,used}, 仅存哈希不存明文)
    @TableField(typeHandler = JsonbStringTypeHandler.class)
    private String backupCodes;

    /// 剩余可用备用验证码数量(冗余字段, 便于查询展示)
    private Integer backupCodesRemaining;
}

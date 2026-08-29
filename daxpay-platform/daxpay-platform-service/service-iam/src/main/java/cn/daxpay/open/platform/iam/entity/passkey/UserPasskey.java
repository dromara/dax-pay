package cn.daxpay.open.platform.iam.entity.passkey;

import java.time.OffsetDateTime;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户通行密钥绑定记录
///
/// 一条记录对应一个已注册的 WebAuthn 凭据(通行密钥)。
/// credentialId 为认证器生成的凭据唯一标识(base64url), 全局唯一不可重复绑定;
/// publicKey 为 COSE 格式公钥(base64url), 公开数据明文存储;
/// signCount 为签名计数器, 每次断言验证后更新, 用于检测认证器克隆。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_passkey")
public class UserPasskey extends MpBaseEntity {

    /// 用户ID(关联 iam_user_info.id)
    @TableField(updateStrategy = com.baomidou.mybatisplus.annotation.FieldStrategy.NEVER)
    private Long userId;

    /// 身份域编码(admin/merchant)
    @TableField(updateStrategy = com.baomidou.mybatisplus.annotation.FieldStrategy.NEVER)
    private String clientCode;

    /// WebAuthn 凭据ID(base64url)
    @TableField(updateStrategy = com.baomidou.mybatisplus.annotation.FieldStrategy.NEVER)
    private String credentialId;

    /// COSE 公钥(base64url)
    @TableField(updateStrategy = com.baomidou.mybatisplus.annotation.FieldStrategy.NEVER)
    private String publicKey;

    /// 签名计数(防认证器克隆)
    private Long signCount;

    /// 设备可辨识名(用户自定义)
    private String deviceName;

    /// 凭据传输方式(internal/hybrid/usb/nfc/ble, 逗号分隔)
    private String transports;

    /// 凭据是否可多设备同步(passkey)
    private Boolean backupEligible;

    /// 凭据当前是否处于同步状态
    private Boolean backupState;

    /// 最后使用时间
    private OffsetDateTime lastUsedTime;
}

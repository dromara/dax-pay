package cn.daxpay.open.platform.iam.auth.service.twofactor;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 备用验证码条目
///
/// 作为 [cn.daxpay.open.platform.iam.entity.twofactor.UserTwoFactor] 的 backup_codes(jsonb)
/// 数组元素, 仅存储哈希值不存明文, used 标记是否已消费。
///
@Data
@Accessors(chain = true)
public class BackupCodeEntry {

    /// 备用码明文的 SHA-256 哈希(十六进制)
    private String hash;

    /// 是否已使用(一次性消费)
    private boolean used;

    public BackupCodeEntry() {
    }

    public BackupCodeEntry(String hash, boolean used) {
        this.hash = hash;
        this.used = used;
    }
}

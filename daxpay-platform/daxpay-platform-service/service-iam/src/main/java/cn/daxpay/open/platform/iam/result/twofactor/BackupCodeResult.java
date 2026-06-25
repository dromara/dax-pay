package cn.daxpay.open.platform.iam.result.twofactor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 备用验证码(一次性返回)
///
/// 绑定确认或重新生成时一次性返回明文, 前端须提示用户立即保存。
/// 服务端仅存储哈希, 之后无法再次获取明文。
///
@Data
@Accessors(chain = true)
@Schema(title = "备用验证码")
public class BackupCodeResult {

    @Schema(description = "备用验证码明文列表(一次性返回, 请妥善保存)")
    private List<String> codes;

    @Schema(description = "备用验证码总数")
    private Integer total;
}

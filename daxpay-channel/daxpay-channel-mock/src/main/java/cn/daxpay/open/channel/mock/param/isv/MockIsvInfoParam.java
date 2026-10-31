package cn.daxpay.open.channel.mock.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商信息参数
///
/// create 与 update 合用: create 时 id 为空(服务商编号自动生成), enable/payEnable 不传默认 true;
/// update 时 id 必填(编辑 + 启停合一, 空字段不覆盖)。
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟服务商信息参数")
public class MockIsvInfoParam {

    /// 主键(更新时必传, 新增为空)
    @Schema(description = "主键")
    private Long id;

    /// 服务商名称
    @Schema(description = "服务商名称")
    @NotBlank(message = "{validation.field.isvName.notBlank}")
    private String isvName;

    /// 是否允许新建服务商通道商户(不传默认 true)
    @Schema(description = "是否允许新建服务商通道商户")
    private Boolean enable;

    /// 是否启用服务商支付(不传默认 true)
    @Schema(description = "是否启用服务商支付")
    private Boolean payEnable;
}

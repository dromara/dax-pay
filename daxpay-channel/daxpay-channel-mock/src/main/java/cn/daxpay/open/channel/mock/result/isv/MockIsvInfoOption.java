package cn.daxpay.open.channel.mock.result.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商下拉选项
///
/// 含停用行(前端须展示启停态), 平铺轻量结构, 供通道商户建档时的归属服务商下拉选择。
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟服务商下拉选项")
public class MockIsvInfoOption {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "服务商编号")
    private String isvNo;

    @Schema(description = "服务商名称")
    private String isvName;

    @Schema(description = "是否允许新建服务商通道商户")
    private Boolean enable;

    @Schema(description = "是否启用服务商支付")
    private Boolean payEnable;
}

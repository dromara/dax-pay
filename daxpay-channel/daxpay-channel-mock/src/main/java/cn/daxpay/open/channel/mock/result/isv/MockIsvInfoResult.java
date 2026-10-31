package cn.daxpay.open.channel.mock.result.isv;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟服务商信息")
public class MockIsvInfoResult extends BaseResult {

    @Schema(description = "服务商编号")
    private String isvNo;

    @Schema(description = "服务商名称")
    private String isvName;

    @Schema(description = "是否允许新建服务商通道商户")
    private Boolean enable;

    @Schema(description = "是否启用服务商支付")
    private Boolean payEnable;

    @Schema(description = "是否已配置服务商签名密钥(未配置时服务商产品回落全局密钥)")
    private Boolean credentialConfigured;
}

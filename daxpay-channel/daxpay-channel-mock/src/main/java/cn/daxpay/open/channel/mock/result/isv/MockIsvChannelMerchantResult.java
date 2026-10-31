package cn.daxpay.open.channel.mock.result.isv;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Mock 模拟服务商通道商户绑定
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟服务商通道商户绑定")
public class MockIsvChannelMerchantResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "归属模拟服务商ID")
    private Long isvId;

    /// 归属服务商编号/名称(Service 查详情时带出, 供详情工作台基本信息展示)
    @Schema(description = "归属服务商编号")
    private String isvNo;

    @Schema(description = "归属服务商名称")
    private String isvName;
}

package cn.daxpay.open.payment.unipay.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 网关预下单结果
@Data
@Accessors(chain = true)
@Schema(title = "网关预下单结果")
public class GatewayPrePayResult {

    @Schema(description = "平台网关单号")
    private String orderNo;

    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    @Schema(description = "业务状态")
    private String status;

    /// 网关支付类型(实际生效类型; 幂等命中时为已有订单的类型)
    @Schema(description = "网关支付类型")
    private String gatewayType;

    /// H5 落地页 URL(cashier → /cashier/, aggregate → /aggregate/)
    @Schema(description = "H5支付链接")
    private String h5Url;

    /// 小程序映射 URL(cashier → /cm/, aggregate → /am/ 前缀, 靠各平台「普通链接二维码」规则拉起小程序)
    @Schema(description = "小程序映射链接")
    private String miniUrl;

    @Schema(description = "过期时间")
    private OffsetDateTime expiredTime;
}

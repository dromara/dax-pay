package cn.daxpay.open.plugin.easypay.param.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/// # 易支付 H5 收银台拉起支付参数（内部接口）
///
@Data
@Schema(title = "易支付H5支付参数")
public class EasyPayH5PayParam {

    /// 协议订单主键
    @NotNull
    @Schema(description = "协议订单主键")
    private Long id;

    /// 用户 OpenId
    @Schema(description = "用户OpenId")
    private String openId;

    /// 支付场景 wechat_pay / alipay 等
    @Schema(description = "支付场景")
    private String scene;
}

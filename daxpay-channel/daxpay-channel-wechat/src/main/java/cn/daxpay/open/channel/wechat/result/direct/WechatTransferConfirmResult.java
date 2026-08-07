package cn.daxpay.open.channel.wechat.result.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信转账确认收款信息
///
/// 供 C 端收款人在微信内拉起确认收款页(`WeixinJSBridge.invoke('requestMerchantTransfer')`)使用。
/// [mchId]/[appId]/[packageInfo] 为拉起 JSAPI 必需参数, 全部由后端从订单与通道配置反查返回。
@Data
@Accessors(chain = true)
@Schema(title = "微信转账确认收款信息")
public class WechatTransferConfirmResult {

    @Schema(description = "微信商户号(拉起 requestMerchantTransfer 用)")
    private String mchId;

    @Schema(description = "商户 AppID(拉起 requestMerchantTransfer 用)")
    private String appId;

    @Schema(description = "拉起确认参数 package_info")
    private String packageInfo;

    @Schema(description = "转账金额(分)")
    private Long amount;

    @Schema(description = "转账标题")
    private String title;

    @Schema(description = "转账状态")
    private String status;

    @Schema(description = "是否已终态(不可再操作)")
    private boolean received;
}

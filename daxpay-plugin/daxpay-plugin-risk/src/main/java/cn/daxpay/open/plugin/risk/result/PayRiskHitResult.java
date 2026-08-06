package cn.daxpay.open.plugin.risk.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 风险命中结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "风险命中结果")
public class PayRiskHitResult extends BaseResult {

    @Schema(description = "阶段")
    private String phase;

    @Schema(description = "命中类型")
    private String hitType;

    @Schema(description = "命中值")
    private String hitValue;

    @Schema(description = "关联名单ID")
    private Long blacklistId;

    @Schema(description = "商户号")
    private String mchNo;

    /// 商户名称(由 mchNo 翻译, 走系统 @Trans 机制)
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "平台交易号")
    private String tradeNo;

    @Schema(description = "容器单号")
    private String orderNo;

    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    @Schema(description = "交易类型")
    private String tradeType;

    @Schema(description = "支付方式")
    private String method;

    @Schema(description = "支付产品")
    private String product;

    @Schema(description = "支付通道")
    private String channel;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "下单openId")
    private String openid;

    @Schema(description = "通道buyerId")
    private String buyerId;

    @Schema(description = "来源场景")
    private String scene;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "客户端IP归属城市")
    private String clientCity;

    @Schema(description = "门店所在城市")
    private String storeCity;

    @Schema(description = "门店号")
    private String storeNo;

    @Schema(description = "地理围栏命中时生效的策略")
    private String geoFenceStrategy;
}

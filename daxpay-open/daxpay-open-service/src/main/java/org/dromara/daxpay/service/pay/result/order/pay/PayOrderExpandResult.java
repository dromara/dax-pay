package org.dromara.daxpay.service.pay.result.order.pay;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2025/6/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付订单扩展信息")
public class PayOrderExpandResult extends BaseResult {

    /** 付款用户ID */
    @Schema(description = "付款用户ID")
    private String buyerId;

    /** 用户标识 */
    @Schema(description = "用户标识")
    private String userId;

    /**
     * 支付产品
     * 三方通道所使用的支付产品或类型
     */
    @Schema(description = "支付产品")
    private String tradeProduct;

    /**
     * 交易方式
     */
    @Schema(description = "交易方式")
    private String tradeWay;

    /**
     * 银行卡类型
     * 借记卡/贷记卡
     */
    @Schema(description = "银行卡类型")
    private String bankType;

    /**
     * 透传订单号
     * 三方通道使用微信/支付宝/银联支付时产生的订单号
     */
    @Schema(description = "透传订单号")
    private String transOrderNo;

    /** 参加活动类型 */
    @Schema(description = "参加活动类型")
    private String promotionType;

    /** 扩展参数存储字段 */
    @Schema(description = "扩展参数存储字段")
    private String ext;
}

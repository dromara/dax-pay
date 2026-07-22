package cn.daxpay.open.payment.trade.record.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeFlowTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 通道入站回调记录结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@FieldNameConstants
@Schema(title = "通道入站回调记录")
public class PayCallbackRecordResult extends MchBaseResult {

    /// 商户名称(由 mchNo 翻译)
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 通道商户名称(由 channelMchNo 翻译)
    @Trans(
            entity = ChannelMerchant.class,
            source = Fields.channelMchNo,
            on = ChannelMerchant.Fields.channelMchNo,
            result = ChannelMerchant.Fields.channelMerchantName)
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    @Schema(description = "平台交易号")
    private String tradeNo;

    @Schema(description = "通道交易号")
    private String outTradeNo;

    /// @see ProductEnum
    @Schema(description = "支付产品")
    private String product;

    /// @see TradeFlowTypeEnum
    @Schema(description = "回调类型")
    private String callbackType;

    @Schema(description = "通知消息内容(JSON)")
    private String notifyInfo;

    /// @see CallbackStatusEnum
    @Schema(description = "回调处理状态")
    private String status;

    @Schema(description = "错误信息")
    private String errorMsg;
}

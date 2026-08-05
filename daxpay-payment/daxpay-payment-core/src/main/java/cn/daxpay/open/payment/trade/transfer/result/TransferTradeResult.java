package cn.daxpay.open.payment.trade.transfer.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 转账资金凭证(跨通道)
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "转账资金凭证")
public class TransferTradeResult extends BaseResult {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户名称(由 mchNo 翻译, 走系统 @Trans 机制)
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 平台转账交易号
    @Schema(description = "平台转账交易号")
    private String tradeNo;

    /// 关联通道转账单ID
    @Schema(description = "关联通道转账单ID")
    private Long containerId;

    /// 所属通道(wechat/alipay/douyin)
    @Schema(description = "所属通道")
    private String containerChannel;

    /// 通道编码
    @Schema(description = "通道编码")
    private String channel;

    /// 钱包渠道
    @Schema(description = "钱包渠道")
    private String provider;

    /// 转账金额(分)
    @Schema(description = "转账金额(分)")
    private Long amount;

    /// 币种
    @Schema(description = "币种")
    private String currency;

    /// 转账状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    @Schema(description = "转账状态")
    private String status;

    /// 通道转账单号
    @Schema(description = "通道转账单号")
    private String outTransferNo;

    /// 实际上送通道的商户转账号
    @Schema(description = "实际上送通道关联号")
    private String relationNo;

    /// 转账完成时间
    @Schema(description = "转账完成时间")
    private OffsetDateTime finishTime;

    /// 转账标题
    @Schema(description = "转账标题")
    private String title;
}

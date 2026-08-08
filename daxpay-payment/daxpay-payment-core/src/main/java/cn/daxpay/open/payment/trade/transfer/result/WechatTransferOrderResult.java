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

/// # 微信转账单
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信转账单")
public class WechatTransferOrderResult extends BaseResult {

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

    /// 通道商户号
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 平台转账单号
    @Schema(description = "平台转账单号")
    private String transferNo;

    /// 商户转账号
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 通道转账单号(微信 paymentNo)
    @Schema(description = "通道转账单号")
    private String outTransferNo;

    /// 转账金额(分)
    @Schema(description = "转账金额(分)")
    private Long amount;

    /// 币种
    @Schema(description = "币种")
    private String currency;

    /// 转账标题
    @Schema(description = "转账标题")
    private String title;

    /// 转账原因/备注
    @Schema(description = "转账原因/备注")
    private String reason;

    /// 转账状态
    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    @Schema(description = "转账状态")
    private String status;

    /// 转账完成时间
    @Schema(description = "转账完成时间")
    private OffsetDateTime finishTime;

    /// 收款人微信 openid
    @Schema(description = "收款人微信openid")
    private String payeeOpenid;

    /// 转账场景
    @Schema(description = "转账场景")
    private String transferScene;

    /// 拉起转账确认参数
    @Schema(description = "拉起转账确认参数")
    private String transferBody;

    /// 收款人姓名
    @Schema(description = "收款人姓名")
    private String userName;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    /// 商户附加参数
    @Schema(description = "商户附加参数")
    private String attach;

    /// 请求时间
    @Schema(description = "请求时间")
    private OffsetDateTime reqTime;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;
}


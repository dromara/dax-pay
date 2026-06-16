package org.dromara.daxpay.payment.pay.trade.entity;

import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.payment.common.enums.PayFundStatusEnum;
import org.dromara.daxpay.payment.common.enums.PayTradeTypeEnum;

import java.time.OffsetDateTime;

/// # 资金交易凭证
///
/// 统一资金交易表，记录每一笔资金动作（普通支付/预授权冻结/预授权捕获/周期代扣/合单子单）
/// 与容器层（业务单/协议）分离，通过 trade_type + 容器关联字段建立联系
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_trade")
public class PayTrade extends MchAppBaseEntity {

    /// 支付交易号（统一雪花，无形态前缀）
    private String tradeNo;

    /// 交易形态
    /// @see PayTradeTypeEnum
    private String tradeType;

    /// 关联容器ID
    /// 根据 tradeType 决定关联哪种容器表：
    /// normal → pay_normal_order / authorize/capture → pay_auth / recurring → pay_recurring / combine_sub → pay_combine
    private Long containerId;

    /// 支付通道
    private String channel;

    /// 支付方式
    private String method;

    /// 支付渠道（微信/支付宝/银联等，三方通道透传时填）
    private String provider;

    /// 本次交易金额（最小货币单位）
    private Long amount;

    /// 币种 ISO 4217，默认 CNY
    private String currency;

    /// 可退金额（最小货币单位）
    private Long refundableBalance;

    /// 资金状态
    /// @see PayFundStatusEnum
    private String status;

    /// 过期时间
    private OffsetDateTime expiredTime;

    /// 支付成功时间
    private OffsetDateTime payTime;

    /// 关闭时间
    private OffsetDateTime closeTime;

    /// 订单来源
    private String source;

    /// 错误信息
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;
}

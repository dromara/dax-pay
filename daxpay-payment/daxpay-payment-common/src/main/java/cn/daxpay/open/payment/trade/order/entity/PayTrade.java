package cn.daxpay.open.payment.trade.order.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;

import java.time.OffsetDateTime;

/// # 资金交易凭证
///
/// 统一资金交易表，记录每一笔资金动作（普通支付/预授权冻结/预授权捕获/周期代扣/合单子单）
/// 与容器层（业务单/协议）分离，通过 trade_type + 容器关联字段建立联系。
/// 仅保留资金动作固有属性与通道调用命脉字段；业务上下文/路由参数/通道回执统一归容器。
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_trade")
public class PayTrade extends MchBaseEntity {

    /// 支付交易号（统一雪花，无形态前缀）
    private String tradeNo;

    /// 交易形态
    /// @see PayTradeTypeEnum
    private String tradeType;

    /// 关联容器ID
    /// 根据 tradeType 决定关联哪种容器表：
    /// normal → pay_normal_order / gateway → pay_gateway_order / authorize/capture → pay_auth / recurring → pay_recurring / combine_sub → pay_combine
    private Long containerId;

    /// 本次交易金额（最小货币单位）
    private Long amount;

    /// 币种 ISO 4217，默认 CNY
    private String currency;

    /// 可退金额（最小货币单位）
    private Long refundableBalance;

    /// 资金状态
    /// @see PayFundStatusEnum
    private String status;

    /// 支付成功时间
    private OffsetDateTime payTime;

    /// 关闭时间
    private OffsetDateTime closeTime;

    /// 订单来源
    private String source;

    /// 通道订单号（三方通道返回的订单号，通道 close/sync 调用参数 + 回调反查索引）
    private String outOrderNo;

    /// 支付参数体（如微信 prepay_id 组装串，非空表示已拉起支付，免重复请求通道）
    private String payBody;

    /// 支付参数体类型（jsapi/sdk/app）
    private String payBodyType;

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;
}

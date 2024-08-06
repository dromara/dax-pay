package cn.daxpay.multi.service.entity.reconcile;

import cn.daxpay.multi.service.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 对账差异记录
 * @author xxm
 * @since 2024/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_reconcile_discrepancy")
public class ReconcileDiscrepancy extends MchBaseEntity {

    /** 对账单ID */
    private Long reconcileId;

    /** 对账号 */
    private String reconcileNo;

    /* 平台侧信息 */
    /** 商户交易号 */
    private String TradeNo;

    /** 通道交易号(平台交易订单) */
    private String outTradeNo;

    /** 支付通道 */
    private String channel;

    /** 交易类型 */
    private String tradeType;

    /** 交易金额 */
    private BigDecimal tradeAmount;

    /** 交易状态 */
    private String tradeStatus;

    /** 交易时间 */
    private LocalDateTime tradeTime;

    /* 通道侧信息 */

    /** 通道交易号(对账文件中的平台交易号) */
    private String channelTradeNo;

    /** 通道交易号 */
    private String channelOutTradeNo;

    /** 通道交易类型 */
    private String channelTradeType;

    /** 通道交易金额 */
    private BigDecimal channelTradeAmount;

    /** 通道交易状态(转换为系统内的属性) */
    private String channelTradeStatus;

    /** 通道交易状态(原始) */
    private String channelTradeRawStatus;

    /** 通道交易时间 */
    private LocalDateTime channelTradeTime;

}

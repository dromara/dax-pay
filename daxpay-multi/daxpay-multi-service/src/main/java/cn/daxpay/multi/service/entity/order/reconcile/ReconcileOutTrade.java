package cn.daxpay.multi.service.entity.order.reconcile;

import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.service.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 对账-通道对账交易明细
 * 下载各通道对账交易明细后, 转换为通用的明细记录, 用于后续与本地交易进行对账处理
 * @author xxm
 * @since 2024/6/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_reconcile_channel_trade")
public class ReconcileOutTrade extends MchBaseEntity {
    /** 关联对账订单ID */
    private Long reconcileId;

    /** 商品名称 */
    private String title;

    /** 交易金额 */
    private Integer amount;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    private String type;

    /** 本地交易号 */
    private String tradeNo;

    /** 通道交易号 - 支付宝/微信的订单号 */
    private String outTradeNo;

    /** 交易时间 */
    private LocalDateTime tradeTime;

}

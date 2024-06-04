package cn.daxpay.multi.service.entity.order.reconcile;

import cn.daxpay.multi.service.code.TrandeTypeEnum;
import cn.daxpay.multi.service.common.entity.MchEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 对账差异
 * @author xxm
 * @since 2024/6/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_reconcile_diff")
public class ReconcileDiff extends MchEntity {

    /** 关联对账订单ID */
    private Long reconcileId;

    /** 商品名称 */
    private String title;

    /** 交易金额 */
    private Integer amount;

    /**
     * 交易类型
     * @see TrandeTypeEnum
     */
    private String type;

    /** 本地交易号 */
    private String tradeNo;

    /** 通道交易号 - 支付宝/微信的订单号 */
    private String outTradeNo;

    /** 交易时间 */
    private LocalDateTime tradeTime;

}

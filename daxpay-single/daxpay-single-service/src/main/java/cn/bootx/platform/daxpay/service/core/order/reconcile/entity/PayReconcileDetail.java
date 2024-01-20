package cn.bootx.platform.daxpay.service.core.order.reconcile.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付对账记录详情
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_reconcile_detail_record")
public class PayReconcileDetail extends MpCreateEntity {

    /** 交易状态 */
    private String status;

    /** 交易类型 支付/退款 */
    private String type;

    /** 订单号 - 支付ID/退款ID */
    private String paymentId;

    /** 网关订单号 - 支付宝/微信的订单号 */
    private String gatewayOrderNo;

    /** 交易金额 */
    private Integer amount;

    /** 手续费 */
    private Integer poundage;

    /** 费率 */
    private Integer rate;

    /** 商品名称 */
    private String title;

}

package cn.bootx.platform.daxpay.service.core.order.reconcile.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.service.core.order.reconcile.conver.PayReconcileConvert;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.PayReconcileDetailDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通用支付对账记录
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付对账记录")
@TableName("pay_reconcile_detail")
public class PayReconcileDetail extends MpCreateEntity implements EntityBaseFunction<PayReconcileDetailDto> {

    /** 关联对账订单ID */
    @DbColumn(comment = "关联对账订单ID")
    private Long recordOrderId;

    /** 商品名称 */
    @DbColumn(comment = "商品名称")
    private String title;

    /** 交易金额 */
    @DbColumn(comment = "交易金额")
    private Integer amount;

    /** 交易类型 pay/refund */
    @DbColumn(comment = "交易类型")
    private String type;

    /** 本地订单ID */
    @DbColumn(comment = "本地订单ID")
    private String paymentId;

    /** 本地退款ID */
    @DbColumn(comment = "本地退款ID")
    private String refundId;

    /** 网关订单号 - 支付宝/微信的订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;


    @Override
    public PayReconcileDetailDto toDto() {
        return PayReconcileConvert.CONVERT.convert(this);
    }
}

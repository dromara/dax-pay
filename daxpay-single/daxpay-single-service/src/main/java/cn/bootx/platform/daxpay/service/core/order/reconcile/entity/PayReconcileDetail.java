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

    /** 交易类型 支付/退款 */
    @DbColumn(comment = "交易类型")
    private String type;

    /** 订单id - 支付ID/退款ID等 */
    @DbColumn(comment = "订单id")
    private String orderId;

    /** 网关订单号 - 支付宝/微信的订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;

    /** 交易金额 */
    @DbColumn(comment = "交易金额")
    private Integer amount;

    /** 商品名称 */
    @DbColumn(comment = "商品名称")
    private String title;

    @Override
    public PayReconcileDetailDto toDto() {
        return PayReconcileConvert.CONVERT.convert(this);
    }
}

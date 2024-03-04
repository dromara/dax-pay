package cn.bootx.platform.daxpay.service.core.order.reconcile.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.service.core.order.reconcile.conver.ReconcileConvert;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileDetailDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
public class ReconcileDetail extends MpCreateEntity implements EntityBaseFunction<ReconcileDetailDto> {

    /** 关联对账订单ID */
    @DbColumn(comment = "关联对账订单ID")
    private Long recordOrderId;

    /** 商品名称 */
    @DbColumn(comment = "商品名称")
    private String title;

    /** 交易金额 */
    @DbColumn(comment = "交易金额")
    private Integer amount;

    /**
     * 交易类型
     * @see ReconcileTradeEnum
     */
    @DbColumn(comment = "交易类型")
    private String type;

    /** 本地订单ID */
    @DbColumn(comment = "本地订单ID")
    private String orderId;

    /** 网关订单号 - 支付宝/微信的订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;

    /** 订单时间 */
    @DbColumn(comment = "订单时间")
    private LocalDateTime orderTime;


    @Override
    public ReconcileDetailDto toDto() {
        return ReconcileConvert.CONVERT.convert(this);
    }
}

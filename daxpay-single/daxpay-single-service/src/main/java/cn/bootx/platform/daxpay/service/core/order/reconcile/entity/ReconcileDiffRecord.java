package cn.bootx.platform.daxpay.service.core.order.reconcile.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileDiffTypeEnum;
import cn.bootx.platform.daxpay.service.core.order.reconcile.conver.ReconcileConvert;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileDiffRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 对账差异单
 * @author xxm
 * @since 2024/2/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "对账差异单")
@TableName("pay_reconcile_diff_record")
public class ReconcileDiffRecord extends MpBaseEntity implements EntityBaseFunction<ReconcileDiffRecordDto> {

    /** 对账单ID */
    @DbColumn(comment = "对账单ID")
    private Long recordId;

    /** 对账单明细ID */
    @DbColumn(comment = "对账单明细ID")
    private Long detailId;

    /** 本地订单id */
    @DbColumn(comment = "本地订单id")
    private Long orderId;

    /** 订单标题 */
    @DbColumn(comment = "订单标题")
    private String title;

    /**
     * 对账订单类型
     * @see ReconcileTradeEnum
     */
    @DbColumn(comment = "对账订单类型")
    private String orderType;

    /**
     * 差异类型
     * @see ReconcileDiffTypeEnum
     */
    @DbColumn(comment = "差异类型")
    private String diffType;

    /**
     * 差异内容, 存储json字符串, 格式为
     * {属性: '标题', 本地字段值:'标题1', 网关字段值: '标题2'}
     * @see ReconcileDiff
     */
    @DbColumn(comment = "差异内容")
    private String diffContent;

    /** 网关订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;

    /** 交易金额 */
    @DbColumn(comment = "交易金额")
    private Integer amount;

    /** 订单时间 */
    @DbColumn(comment = "订单时间")
    private LocalDateTime orderTime;

    @Override
    public ReconcileDiffRecordDto toDto() {
        return ReconcileConvert.CONVERT.convert(this);
    }
}

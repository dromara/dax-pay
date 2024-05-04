package cn.bootx.platform.daxpay.service.core.order.reconcile.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileResultEnum;
import cn.bootx.platform.daxpay.service.core.order.reconcile.conver.ReconcileConvert;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 支付对账单订单
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付对账单订单")
@TableName(value = "pay_reconcile_order")
public class ReconcileOrder extends MpCreateEntity implements EntityBaseFunction<ReconcileOrderDto> {

    /** 对账号 */
    @DbColumn(comment = "对账号")
    private String reconcileNo;

    /** 日期 */
    @DbColumn(comment = "日期")
    private LocalDate date;

    /**
     * 通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "通道")
    private String channel;

    /** 明细对账文件是否下载成功 */
    @DbColumn(comment = "明细对账单下载")
    private boolean detailDown;

    /** 明细是否比对完成 */
    @DbColumn(comment = "明细对账单比对")
    private boolean detailCompare;

    /** 汇总对账文件是否下载成功 */
    @DbColumn(comment = "汇总对账单下载")
    private boolean totalDown;

    /** 汇总是否比对完成 */
    @DbColumn(comment = "汇总对账单比对")
    private boolean totalCompare;

    /**
     * 对账结果
     * @see ReconcileResultEnum
     */
    @DbColumn(comment = "对账结果")
    private String result;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    @Override
    public ReconcileOrderDto toDto() {
        return ReconcileConvert.CONVERT.convert(this);
    }
}

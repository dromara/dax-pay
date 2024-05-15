package cn.daxpay.single.service.core.order.reconcile.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.single.service.code.ReconcileFileTypeEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 原始对账单文件
 * @author xxm
 * @since 2024/5/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "原始对账单文件")
@TableName("pay_reconcile_file")
public class ReconcileFile extends MpIdEntity {

    @DbColumn(comment = "对账单ID")
    private Long reconcileId;

    /**
     * 明细/汇总
     * @see ReconcileFileTypeEnum
     */
    @DbColumn(comment = "类型")
    private String type;

    @DbColumn(comment = "对账单文件")
    private Long fileId;
}

package cn.bootx.platform.daxpay.service.core.record.reconcile.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付对账单记录
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付对账单记录")
@TableName("pay_reconcile_record")
public class PayReconcileRecord extends MpCreateEntity {
}

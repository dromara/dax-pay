package cn.bootx.platform.daxpay.service.core.record.reconcile.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

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

    /** 日期 */
    private LocalDate date;

    /** 通道 */
    private String channel;

    /** 对账单是否下载成功 */
    private boolean success;

    /** 错误信息 */
    private String errorMsg;

}

package cn.bootx.platform.daxpay.service.core.record.repair.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付修复记录
 * @author xxm
 * @since 2024/1/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_repair_record")
@DbTable(comment = "支付修复记录")
public class PayRepairRecord extends MpCreateEntity {

    /** 支付ID */
    @DbColumn(comment = "支付ID")
    private Long paymentId;

    /** 业务号 */
    @DbColumn(comment = "业务号")
    private String businessNo;

    /** 修复来源 */
    @DbColumn(comment = "修复来源")
    private String repairSource;

    /** 修复类型 */
    @DbColumn(comment = "修复类型")
    private String repairType;

    /** 修复前状态 */
    @DbColumn(comment = "修复前状态")
    private String beforeStatus;

    /** 修复后状态 */
    @DbColumn(comment = "修复后状态")
    private String afterStatus;

    /** 金额变动 */
    @DbColumn(comment = "金额变动")
    private Integer amount;

}

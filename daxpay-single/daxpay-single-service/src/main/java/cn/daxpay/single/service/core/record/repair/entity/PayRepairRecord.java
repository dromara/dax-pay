package cn.daxpay.single.service.core.record.repair.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.code.PayStatusEnum;
import cn.daxpay.single.service.code.PayRepairSourceEnum;
import cn.daxpay.single.service.code.PayRepairWayEnum;
import cn.daxpay.single.service.code.PaymentTypeEnum;
import cn.daxpay.single.service.code.RefundRepairWayEnum;
import cn.daxpay.single.service.core.record.repair.convert.PayRepairRecordConvert;
import cn.daxpay.single.service.dto.record.repair.PayRepairRecordDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付修复记录 包括支付修复记录和退款修复记录
 * @author xxm
 * @since 2024/1/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_repair_record")
@DbTable(comment = "支付修复记录")
public class PayRepairRecord extends MpCreateEntity implements EntityBaseFunction<PayRepairRecordDto> {

    /**
     * 修复号
     * 如果一次修复产生的修复记录有多个记录, 使用这个作为关联
     */
    @DbColumn(comment = "修复号", length = 32, isNull = false)
    private String repairNo;

    /** 支付ID/退款ID */
    @DbColumn(comment = "本地订单ID", isNull = false)
    private Long tradeId;

    /**
     * 本地交易号, 支付号/退款号
     */
    @DbMySqlIndex(comment = "本地交易号索引")
    @DbColumn(comment = "本地交易号", length = 32, isNull = false)
    private String tradeNo;

    /**
     * 修复类型 支付修复/退款修复
     * @see PaymentTypeEnum
     */
    @DbColumn(comment = "修复类型", length = 20, isNull = false)
    private String repairType;

    /**
     * 修复来源
     * @see PayRepairSourceEnum
     */
    @DbColumn(comment = "修复来源", length = 20, isNull = false)
    private String repairSource;

    /**
     * 修复方式
     * @see PayRepairWayEnum
     * @see RefundRepairWayEnum
     */
    @DbColumn(comment = "修复方式", length = 20, isNull = false)
    private String repairWay;

    /** 修复的通道 */
    @DbColumn(comment = "修复的通道", length = 20, isNull = false)
    private String channel;

    /**
     * 修复前状态
     * @see PayStatusEnum
     */
    @DbColumn(comment = "修复前状态", length = 20, isNull = false)
    private String beforeStatus;

    /**
     * 修复后状态
     * @see PayStatusEnum
     */
    @DbColumn(comment = "修复后状态", length = 20, isNull = false)
    private String afterStatus;

    /**
     * 转换
     */
    @Override
    public PayRepairRecordDto toDto() {
        return PayRepairRecordConvert.CONVERT.convert(this);
    }
}

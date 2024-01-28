package cn.bootx.platform.daxpay.service.core.record.repair.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairPayTypeEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairWayEnum;
import cn.bootx.platform.daxpay.service.code.RefundRepairWayEnum;
import cn.bootx.platform.daxpay.service.core.record.repair.convert.PayRepairRecordConvert;
import cn.bootx.platform.daxpay.service.dto.record.repair.PayRepairRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
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
     * 修复ID
     * 如果一次修复产生的修复记录只有一条, 则该字段为与ID一致
     * 如果一次修复产生的修复记录有多个, 则使用这个ID作为关联
     */
    @DbColumn(comment = "修复ID")
    private Long repairId;

    /** 支付ID/退款ID */
    @DbColumn(comment = "本地订单ID")
    private Long orderId;

    /**
     * 本地业务号, 支付业务号/退款号
     */
    @DbColumn(comment = "本地业务号")
    private String orderNo;

    /**
     * 修复类型 支付修复/退款修复
     * @see PayRepairPayTypeEnum
     */
    @DbColumn(comment = "修复类型")
    private String repairType;

    /**
     * 修复来源
     * @see PayRepairSourceEnum
     */
    @DbColumn(comment = "修复来源")
    private String repairSource;

    /**
     * 修复方式
     * @see PayRepairWayEnum
     * @see RefundRepairWayEnum
     */
    @DbColumn(comment = "修复方式")
    private String repairWay;

    /** 修复的异步通道 */
    @DbColumn(comment = "修复的异步通道")
    private String asyncChannel;

    /**
     * 修复前状态
     * @see PayStatusEnum
     */
    @DbColumn(comment = "修复前状态")
    private String beforeStatus;

    /**
     * 修复后状态
     * @see PayStatusEnum
     */
    @DbColumn(comment = "修复后状态")
    private String afterStatus;

    /**
     * 转换
     */
    @Override
    public PayRepairRecordDto toDto() {
        return PayRepairRecordConvert.CONVERT.convert(this);
    }
}

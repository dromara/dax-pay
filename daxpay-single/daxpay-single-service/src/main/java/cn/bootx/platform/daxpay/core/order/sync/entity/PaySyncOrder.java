package cn.bootx.platform.daxpay.core.order.sync.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.core.order.sync.convert.PaySyncOrderConvert;
import cn.bootx.platform.daxpay.dto.order.sync.PaySyncOrderDto;
import cn.bootx.table.modify.annotation.DbComment;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付同步订单
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "支付同步订单")
@Accessors(chain = true)
@TableName("pay_sync_order")
public class PaySyncOrder extends MpCreateEntity implements EntityBaseFunction<PaySyncOrderDto> {

    /** 支付记录id */
    @DbComment("支付记录id")
    private Long paymentId;


    /**
     * 支付渠道
     * @see PayChannelEnum#getCode()
     */
    @DbComment("支付渠道")
    private String channel;

    /** 通知消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbComment("通知消息")
    private String syncInfo;

    /**
     * 同步状态
     * @see PaySyncStatusEnum
     */
    @DbComment("同步状态")
    private String status;

    /**
     * 支付单如果状态不一致, 是否修复成功
     */
    private boolean repairOrder;

    @DbComment("错误消息")
    private String msg;

    /** 同步时间 */
    @DbComment("同步时间")
    private LocalDateTime syncTime;

    /**
     * 转换
     */
    @Override
    public PaySyncOrderDto toDto() {
        return PaySyncOrderConvert.CONVERT.convert(this);
    }
}

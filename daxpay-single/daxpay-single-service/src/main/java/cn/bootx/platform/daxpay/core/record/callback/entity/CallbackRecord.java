package cn.bootx.platform.daxpay.core.record.callback.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.table.modify.annotation.DbComment;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 回调通知
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CallbackRecord extends MpCreateEntity {
    /** 支付记录id */
    @DbComment("支付记录id")
    private Long paymentId;

    /**
     * 支付渠道
     * @see PayChannelEnum#getCode()
     */
    @DbComment("支付渠道")
    private String payChannel;

    /** 通知消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbComment("通知消息")
    private String notifyInfo;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @DbComment("支付状态")
    private String payStatus;

    /**
     * 回调处理状态
     * @see
     */
    @DbComment("回调处理状态")
    private String status;

    /** 提示信息 */
    @DbComment("提示信息")
    private String msg;

    /** 回调时间 */
    @DbComment("回调时间")
    private LocalDateTime notifyTime;
}

package cn.bootx.platform.daxpay.core.notify.entity;

import cn.bootx.mybatis.table.modify.annotation.DbComment;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlFieldType;
import cn.bootx.mybatis.table.modify.mybatis.mysq.constants.MySqlFieldTypeEnum;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.notify.convert.PayNotifyConvert;
import cn.bootx.platform.daxpay.dto.notify.PayNotifyRecordDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 回调记录
 *
 * @author xxm
 * @date 2021/6/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "回调记录")
@Accessors(chain = true)
@TableName("pay_pay_notify_record")
public class PayNotifyRecord extends MpBaseEntity implements EntityBaseFunction<PayNotifyRecordDto> {

    /** 支付记录id */
    @DbComment("支付记录id")
    private Long paymentId;

    /** 商户应用编码 */
    @DbComment("商户应用编码")
    private String mchAppCode;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    @DbComment("支付通道")
    private String payChannel;

    /** 通知消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbComment("通知消息")
    private String notifyInfo;

    /**
     * 处理状态
     * @see PayStatusCode#NOTIFY_PROCESS_SUCCESS
     */
    @DbComment("处理状态")
    private String status;

    /** 提示信息 */
    @DbComment("提示信息")
    private String msg;

    /** 回调时间 */
    @DbComment("回调时间")
    private LocalDateTime notifyTime;

    @Override
    public PayNotifyRecordDto toDto() {
        return PayNotifyConvert.CONVERT.convert(this);
    }

}

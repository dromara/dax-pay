package cn.bootx.platform.daxpay.core.sync.record.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.pay.PaySyncStatus;
import cn.bootx.platform.daxpay.core.sync.record.convert.PaySyncRecordConvert;
import cn.bootx.platform.daxpay.dto.sync.PaySyncRecordDto;
import cn.bootx.table.modify.annotation.DbComment;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@DbTable(comment = "支付同步记录")
@Accessors(chain = true)
@TableName("pay_sync_record")
public class PaySyncRecord  extends MpCreateEntity implements EntityBaseFunction<PaySyncRecordDto> {

    /** 支付记录id */
    @DbComment("支付记录id")
    private Long paymentId;

    /** 商户编码 */
    @DbComment("商户编码")
    private String mchCode;

    /** 商户应用编码 */
    @DbComment("商户应用编码")
    private String mchAppCode;

    /**
     * 支付渠道
     * @see PayChannelEnum#getCode()
     */
    @DbComment("支付渠道")
    private String payChannel;

    /** 通知消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbComment("通知消息")
    private String syncInfo;

    /**
     * 同步状态
     * @see PaySyncStatus#WAIT_BUYER_PAY
     */
    @DbComment("同步状态")
    private String status;

    @DbComment("错误消息")
    private String msg;

    /** 同步时间 */
    @DbComment("同步时间")
    private LocalDateTime syncTime;

    /**
     * 转换
     */
    @Override
    public PaySyncRecordDto toDto() {
        return PaySyncRecordConvert.CONVERT.convert(this);
    }
}

package cn.bootx.platform.daxpay.service.core.record.close.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.record.close.convert.PayCloseRecordConvert;
import cn.bootx.platform.daxpay.service.dto.record.close.PayCloseRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbComment;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付关闭记录")
@TableName("pay_close_record")
public class PayCloseRecord extends MpCreateEntity implements EntityBaseFunction<PayCloseRecordDto> {

    /** 支付记录id */
    @DbComment("支付记录id")
    private Long paymentId;

    /** 业务号 */
    @DbComment("业务号")
    private String businessNo;

    /**
     * 关闭的异步支付通道, 可以为空
     * @see PayChannelEnum#getCode()
     */
    @DbComment("关闭的异步支付通道")
    private String asyncChannel;

    /**
     * 是否关闭成功
     */
    @DbComment("是否关闭成功")
    private boolean closed;

    @DbComment("错误消息")
    private String errorMsg;

    /** 客户端IP */
    @DbColumn(comment = "客户端IP")
    private String clientIp;

    /** 请求链路ID */
    @DbColumn(comment = "请求链路ID")
    private String reqId;

    /**
     * 转换
     */
    @Override
    public PayCloseRecordDto toDto() {
        return PayCloseRecordConvert.CONVERT.convert(this);
    }
}

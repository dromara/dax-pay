package cn.bootx.platform.daxpay.service.core.record.close.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.record.close.convert.PayCloseRecordConvert;
import cn.bootx.platform.daxpay.service.dto.record.close.PayCloseRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
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

    /** 订单号 */
    @DbColumn(comment = "订单号")
    private String orderNo;

    /** 商户订单号 */
    @DbColumn(comment = "商户订单号")
    private String bizOrderNo;

    /**
     * 关闭的支付通道
     * @see PayChannelEnum
     */
    @DbColumn(comment = "关闭的异步支付通道")
    private String channel;

    /**
     * 是否关闭成功
     */
    @DbColumn(comment = "是否关闭成功")
    private boolean closed;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String code;

    /** 错误消息 */
    @DbColumn(comment = "错误消息")
    private String errorMsg;

    /** 客户端IP */
    @DbColumn(comment = "客户端IP")
    private String clientIp;

    /**
     * 转换
     */
    @Override
    public PayCloseRecordDto toDto() {
        return PayCloseRecordConvert.CONVERT.convert(this);
    }
}

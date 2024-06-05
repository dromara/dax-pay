package cn.daxpay.single.service.core.record.close.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.code.PayCloseTypeEnum;
import cn.daxpay.single.service.core.record.close.convert.PayCloseRecordConvert;
import cn.daxpay.single.service.dto.record.close.PayCloseRecordDto;
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
    @DbColumn(comment = "关闭的支付通道")
    private String channel;

    /**
     * 关闭类型 关闭/撤销
     * @see PayCloseTypeEnum
     */
    @DbColumn(comment = "关闭类型")
    private String closeType;

    /**
     * 是否关闭成功
     */
    @DbColumn(comment = "是否关闭成功")
    private boolean closed;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String errorCode;

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

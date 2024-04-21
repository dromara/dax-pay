package cn.bootx.platform.daxpay.service.core.order.pay.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import cn.bootx.platform.daxpay.service.core.order.pay.convert.PayOrderConvert;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayChannelOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 关联支付订单支付时通道信息
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付订单关联支付时通道信息")
@TableName("pay_channel_order")
@Deprecated
public class PayChannelOrder extends MpCreateEntity implements EntityBaseFunction<PayChannelOrderDto> {

    @DbColumn(comment = "支付id")
    private String paymentId;

    @DbColumn(comment = "异步支付方式")
    private boolean async;

    @DbColumn(comment = "通道")
    private String channel;

    @DbColumn(comment = "支付方式")
    private String payWay;

    @DbColumn(comment = "金额")
    private Integer amount;

    @DbColumn(comment = "可退款金额")
    private Integer refundableBalance;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @DbColumn(comment = "支付状态")
    private String status;

    @DbColumn(comment = "支付完成时间")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime payTime;


    /**
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    @DbColumn(comment = "附加支付参数")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String channelExtra;

    /**
     * 转换
     */
    @Override
    public PayChannelOrderDto toDto() {
        return PayOrderConvert.CONVERT.convert(this);
    }
}

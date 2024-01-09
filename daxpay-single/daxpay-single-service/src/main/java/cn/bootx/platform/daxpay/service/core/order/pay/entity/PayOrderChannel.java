package cn.bootx.platform.daxpay.service.core.order.pay.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import cn.bootx.platform.daxpay.service.core.order.pay.convert.PayOrderConvert;
import cn.bootx.platform.daxpay.service.dto.order.pay.PayOrderChannelDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 关联支付订单支付时通道信息
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付订单关联支付时通道信息")
@TableName("pay_order_channel")
public class PayOrderChannel extends MpCreateEntity implements EntityBaseFunction<PayOrderChannelDto> {

    @DbColumn(comment = "支付id")
    private Long paymentId;

    @DbColumn(comment = "通道")
    private String channel;

    @DbColumn(comment = "支付方式")
    private String payWay;

    @DbColumn(comment = "异步支付方式")
    private boolean async;

    @DbColumn(comment = "金额")
    private Integer amount;

    /**
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     * @see WalletPayParam
     */
    @DbColumn(comment = "附加支付参数")
    private String channelExtra;

    /**
     * 转换
     */
    @Override
    public PayOrderChannelDto toDto() {
        return PayOrderConvert.CONVERT.convert(this);
    }
}

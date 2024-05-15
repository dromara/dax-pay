package cn.daxpay.single.service.core.order.pay.entity;


import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.single.param.channel.AliPayParam;
import cn.daxpay.single.param.channel.WalletPayParam;
import cn.daxpay.single.param.channel.WeChatPayParam;
import cn.daxpay.single.service.core.order.pay.convert.PayOrderConvert;
import cn.daxpay.single.service.dto.order.pay.PayOrderExtraDto;
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
 * 支付订单扩展信息
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付订单扩展信息")
@TableName("pay_order_extra")
public class PayOrderExtra extends MpBaseEntity implements EntityBaseFunction<PayOrderExtraDto> {

    /** 同步跳转地址, 以最后一次为准 */
    @DbColumn(comment = "同步跳转地址")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String returnUrl;

    /** 异步通知地址,以最后一次为准 */
    @DbColumn(comment = "异步通知地址")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /**
     * 附加参数 以最后一次为准
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    @DbColumn(comment = "附加参数")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String extraParam;

    /** 商户扩展参数,回调时会原样返回, 以最后一次为准 */
    @DbColumn(comment = "商户扩展参数")
    private String attach;

    /** 请求时间，时间戳转时间, 以最后一次为准 */
    @DbColumn(comment = "请求时间，传输时间戳，以最后一次为准")
    private LocalDateTime reqTime;

    /** 支付终端ip 以最后一次为准 */
    @DbColumn(comment = "支付终端ip")
    private String clientIp;

    /**
     * 转换
     */
    @Override
    public PayOrderExtraDto toDto() {
        return PayOrderConvert.CONVERT.convert(this);
    }
}

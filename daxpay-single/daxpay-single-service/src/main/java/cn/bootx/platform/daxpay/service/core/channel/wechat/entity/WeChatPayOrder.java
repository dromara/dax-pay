package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.daxpay.service.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.service.core.channel.wechat.convert.WeChatConvert;
import cn.bootx.platform.daxpay.service.dto.channel.wechat.WeChatPayOrderDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2021/6/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "微信支付记录")
@Accessors(chain = true)
@TableName("pay_wechat_pay_order")
public class WeChatPayOrder extends BasePayOrder implements EntityBaseFunction<WeChatPayOrderDto> {

    /** 微信交易号 */
    @DbColumn(comment = "交易号")
    private String tradeNo;

    /** 所使用的支付方式 */
    @DbColumn(comment = "支付方式")
    private String payWay;


    @Override
    public WeChatPayOrderDto toDto() {
        return WeChatConvert.CONVERT.convert(this);
    }

}

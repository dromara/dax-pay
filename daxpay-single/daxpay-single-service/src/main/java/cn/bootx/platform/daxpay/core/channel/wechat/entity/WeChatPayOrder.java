package cn.bootx.platform.daxpay.core.channel.wechat.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.daxpay.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.core.channel.wechat.convert.WeChatConvert;
import cn.bootx.platform.daxpay.dto.channel.wechat.WeChatPayOrderDto;
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
@Accessors(chain = true)
@TableName("pay_wechat_payment")
public class WeChatPayOrder extends BasePayOrder implements EntityBaseFunction<WeChatPayOrderDto> {

    /**
     * 微信交易号
     */
    private String tradeNo;

    @Override
    public WeChatPayOrderDto toDto() {
        return WeChatConvert.CONVERT.convert(this);
    }

}

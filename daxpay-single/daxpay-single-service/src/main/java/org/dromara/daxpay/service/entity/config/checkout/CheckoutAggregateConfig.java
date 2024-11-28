package org.dromara.daxpay.service.entity.config.checkout;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.CheckoutAggregateEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.CheckoutAggregateConfigConvert;
import org.dromara.daxpay.service.param.config.checkout.CheckoutAggregateConfigParam;
import org.dromara.daxpay.service.result.config.checkout.CheckoutAggregateConfigVo;

/**
 * 收银台聚合支付配置
 * @author xxm
 * @since 2024/11/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_checkout_aggregate_config")
public class CheckoutAggregateConfig extends MchAppBaseEntity implements ToResult<CheckoutAggregateConfigVo> {

    /**
     * 聚合支付类型
     * @see CheckoutAggregateEnum
     */
    private String type;

    /**
     * 通道
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    private String payMethod;

    /** 自动拉起支付 */
    private boolean autoLaunch;

    public static CheckoutAggregateConfig init(CheckoutAggregateConfigParam param){
        return CheckoutAggregateConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CheckoutAggregateConfigVo toResult() {
        return CheckoutAggregateConfigConvert.CONVERT.toVo(this);
    }
}

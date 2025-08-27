package org.dromara.daxpay.service.merchant.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.AggregateBarPayTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.pay.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.merchant.convert.gateway.AggregateBarPayConfigConvert;
import org.dromara.daxpay.service.merchant.param.gateway.AggregateBarPayConfigParam;
import org.dromara.daxpay.service.merchant.result.gateway.AggregateBarPayConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关聚合付款码支付配置
 * @author xxm
 * @since 2025/3/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_aggregate_bar_pay_config")
public class AggregateBarPayConfig extends MchAppBaseEntity implements ToResult<AggregateBarPayConfigResult> {

    /**
     * 付款码类型
     * @see AggregateBarPayTypeEnum
     */
    private String barPayType;

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

    /**
     * 其他支付方式
     */
    private String otherMethod;

    /**
     * 初始化
     */
    public static AggregateBarPayConfig init(AggregateBarPayConfigParam param){
        return AggregateBarPayConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public AggregateBarPayConfigResult toResult() {
        return AggregateBarPayConfigConvert.CONVERT.toResult(this);
    }
}

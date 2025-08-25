package org.dromara.daxpay.service.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.AggregateBarPayTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.isv.convert.gateway.IsvAggregateBarPayConfigConvert;
import org.dromara.daxpay.service.isv.param.gateway.IsvAggregateBarPayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvAggregateBarPayConfigResult;
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
@TableName("pay_isv_aggregate_bar_pay_config")
public class IsvAggregateBarPayConfig extends MpBaseEntity implements ToResult<IsvAggregateBarPayConfigResult> {

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
    public static IsvAggregateBarPayConfig init(IsvAggregateBarPayConfigParam param){
        return IsvAggregateBarPayConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public IsvAggregateBarPayConfigResult toResult() {
        return IsvAggregateBarPayConfigConvert.CONVERT.toResult(this);
    }
}

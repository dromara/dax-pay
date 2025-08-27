package org.dromara.daxpay.service.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.AggregatePayTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.isv.convert.gateway.IsvAggregatePayConfigConvert;
import org.dromara.daxpay.service.isv.param.gateway.IsvAggregatePayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvAggregatePayConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_isv_aggregate_pay_config")
public class IsvAggregatePayConfig extends MpBaseEntity implements ToResult<IsvAggregatePayConfigResult> {

    /**
     * 聚合支付类型
     * @see AggregatePayTypeEnum
     */
    private String aggregateType;

    /**
     * 支付通道
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

    /** 自动拉起支付 */
    private Boolean autoLaunch;

    /** 需要获取OpenId */
    private Boolean needOpenId;

    /** OpenId获取方式 */
    private String openIdGetType;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    private String callType;


    public Boolean getNeedOpenId() {
        return Objects.equals(needOpenId,true);
    }

    public Boolean getAutoLaunch() {
        return Objects.equals(autoLaunch,true);
    }

    /**
     * 初始化
     */
    public static IsvAggregatePayConfig init(IsvAggregatePayConfigParam param){
        return IsvAggregatePayConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public IsvAggregatePayConfigResult toResult() {
        return IsvAggregatePayConfigConvert.CONVERT.toResult(this);
    }
}

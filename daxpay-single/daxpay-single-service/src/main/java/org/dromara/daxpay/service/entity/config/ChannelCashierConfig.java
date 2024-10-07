package org.dromara.daxpay.service.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CashierTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.ChannelCashierConfigConvert;
import org.dromara.daxpay.service.param.config.ChannelCashierConfigParam;
import org.dromara.daxpay.service.result.config.ChannelCashierConfigResult;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_channel_cashier_config")
public class ChannelCashierConfig extends MchAppBaseEntity implements ToResult<ChannelCashierConfigResult> {

    /**
     * 收银台类型
     * @see CashierTypeEnum
     */
    private String cashierType;

    /**
     * 收银台名称
     */
    private String cashierName;

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

    /** 是否开启分账 */
    private Boolean allocation;

    /** 自动分账 */
    private Boolean autoAllocation;

    /** 备注 */
    private String remark;

    public static ChannelCashierConfig init(ChannelCashierConfigParam param){
        return ChannelCashierConfigConvert.CONVERT.toEntity(param);
    }

    @Override
    public ChannelCashierConfigResult toResult() {
        return ChannelCashierConfigConvert.CONVERT.toResult(this);
    }
}

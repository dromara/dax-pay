package org.dromara.daxpay.service.entity.config.cashier;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CashierCodeTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.CashierCodeTypeConfigConvert;
import org.dromara.daxpay.service.param.config.cashier.CashierCodeTypeConfigParam;
import org.dromara.daxpay.service.result.config.cashier.CashierCodeTypeConfigResult;

/**
 * 特定类型码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_cashier_code_type_config")
public class CashierCodeTypeConfig extends MchAppBaseEntity implements ToResult<CashierCodeTypeConfigResult> {

    /** 码牌ID */
    private Long cashierCodeId;

    /**
     * 收银台类型
     * @see CashierCodeTypeEnum
     */
    private String type;

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
    private boolean allocation;

    /** 自动分账 */
    private boolean autoAllocation;

    public static CashierCodeTypeConfig init(CashierCodeTypeConfigParam param) {
        return CashierCodeTypeConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CashierCodeTypeConfigResult toResult() {
        return CashierCodeTypeConfigConvert.CONVERT.toResult(this);
    }
}

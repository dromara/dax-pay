package org.dromara.daxpay.payment.merchant.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.gateway.GatewayPayConfigConvert;
import org.dromara.daxpay.payment.merchant.result.gateway.GatewayPayConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_gateway_pay_config")
public class GatewayPayConfig extends MchAppBaseEntity implements ToResult<GatewayPayConfigResult> {

    /** PC收银台是否同时显示聚合收银码 */
    private boolean aggregateQrShow;

    /** h5收银台自动升级聚合支付 */
    private boolean h5AutoUpgrade;

    /**
     * 转换
     */
    @Override
    public GatewayPayConfigResult toResult() {
        return GatewayPayConfigConvert.CONVERT.toResult(this);
    }
}

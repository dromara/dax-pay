package org.dromara.daxpay.payment.merchant.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.gateway.GatewayPayReadConvert;
import org.dromara.daxpay.payment.merchant.result.gateway.GatewayPayReadConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关支付读取配置配置
 * @author xxm
 * @since 2025/10/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_gateway_pay_read_config")
public class GatewayPayReadConfig extends MchAppBaseEntity implements ToResult<GatewayPayReadConfigResult> {

    /** 网关支付是否读取系统 */
    private boolean gatewayReadSystem;

    /** H5收银台读取系统 */
    private boolean h5ReadSystem;

    /** Pc收银台读取系统 */
    private boolean pcReadSystem;

    /** 聚合扫码支付读取系统 */
    private boolean aggregateQrReadSystem;

    /** 聚合付款码支付读取系统 */
    private boolean aggregateBarReadSystem;

    /** 小程序快捷支付读取系统 */
    private boolean miniQuicklyReadSystem;

    /**
     * 转换
     */
    @Override
    public GatewayPayReadConfigResult toResult() {
        return GatewayPayReadConvert.CONVERT.toResult(this);
    }
}

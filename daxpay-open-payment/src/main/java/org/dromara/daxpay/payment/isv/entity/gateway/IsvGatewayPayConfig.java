package org.dromara.daxpay.payment.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.isv.convert.gateway.IsvGatewayPayConfigConvert;
import org.dromara.daxpay.payment.isv.param.gateway.IsvGatewayPayConfigParam;
import org.dromara.daxpay.payment.isv.result.gateway.IsvGatewayPayConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("pay_isv_gateway_config")
public class IsvGatewayPayConfig extends MpBaseEntity implements ToResult<IsvGatewayPayConfigResult> {

    /** PC收银台是否同时显示聚合收银码 */
    private boolean aggregateShow;

    /** h5收银台自动升级聚合支付 */
    private boolean h5AutoUpgrade;

    /**
     * 服务商号
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String isvNo;

    public static IsvGatewayPayConfig init(IsvGatewayPayConfigParam param) {
        return IsvGatewayPayConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public IsvGatewayPayConfigResult toResult() {
        return IsvGatewayPayConfigConvert.CONVERT.toResult(this);
    }
}

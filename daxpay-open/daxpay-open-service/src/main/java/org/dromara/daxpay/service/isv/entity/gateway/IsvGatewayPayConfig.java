package org.dromara.daxpay.service.isv.entity.gateway;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.isv.convert.gateway.IsvGatewayPayConfigConvert;
import org.dromara.daxpay.service.isv.param.gateway.IsvGatewayPayConfigParam;
import org.dromara.daxpay.service.isv.result.gateway.IsvGatewayPayConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

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
    private Boolean aggregateShow;

    /** PC收银台是否显示聚合付款码支付 */
    private Boolean barPayShow;

    /** h5收银台自动升级聚合支付 */
    private Boolean h5AutoUpgrade;

    /** 小程序开启分账 */
    private Boolean miniAppAllocation;

    /** 小程序自动分账 */
    private Boolean miniAppAutoAllocation;

    /** 限制小程序支付方式 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String miniAppLimitPay;


    public Boolean getAggregateShow() {
        return Objects.equals(aggregateShow, true);
    }

    public Boolean getH5AutoUpgrade() {
        return Objects.equals(h5AutoUpgrade, true);
    }

    public Boolean getBarPayShow() {
        return Objects.equals(h5AutoUpgrade, true);
    }

    public Boolean getMiniAppAllocation() {
        return Objects.equals(miniAppAllocation, true);
    }

    public Boolean getMiniAppAutoAllocation() {
        return Objects.equals(miniAppAutoAllocation, true);
    }

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

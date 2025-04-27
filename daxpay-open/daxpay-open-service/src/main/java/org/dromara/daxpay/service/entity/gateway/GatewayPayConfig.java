package org.dromara.daxpay.service.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.gateway.GatewayPayConfigConvert;
import org.dromara.daxpay.service.param.gateway.GatewayPayConfigParam;
import org.dromara.daxpay.service.result.gateway.config.GatewayPayConfigResult;
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
@TableName("pay_gateway_config")
public class GatewayPayConfig extends MchAppBaseEntity implements ToResult<GatewayPayConfigResult> {

    /** 显示名称 */
    private String name;

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

    /** 小程序关联终端号 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String miniAppTerminalNo;

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

    public static GatewayPayConfig init(GatewayPayConfigParam param) {
        return GatewayPayConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public GatewayPayConfigResult toResult() {
        return GatewayPayConfigConvert.CONVERT.toResult(this);
    }
}

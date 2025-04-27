package org.dromara.daxpay.service.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.gateway.CashierCodeConfigConvert;
import org.dromara.daxpay.service.param.gateway.CashierCodeConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierCodeConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_cashier_code_config")
public class CashierCodeConfig extends MchAppBaseEntity implements ToResult<CashierCodeConfigResult> {

    /** 码牌名称 */
    private String name;

    /** 码牌编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 是否启用 */
    private Boolean enable;

    /** 模板编号 */
    private String templateCode;

    /** 是否开启分账 */
    private Boolean allocation;

    /** 自动分账 */
    private Boolean autoAllocation;

    /** 限制用户支付方式 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String limitPay;

    /** 备注 */
    private String remark;

    public Boolean getAllocation() {
        return Objects.equals(allocation, true);
    }

    public Boolean getAutoAllocation() {
        return Objects.equals(autoAllocation, true);
    }

    public Boolean getEnable() {
        return Objects.equals(enable, true);
    }

    public static CashierCodeConfig init(CashierCodeConfigParam param) {
        return CashierCodeConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CashierCodeConfigResult toResult() {
        return CashierCodeConfigConvert.CONVERT.toResult(this);
    }

}

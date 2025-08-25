package org.dromara.daxpay.service.device.entity.qrcode.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeConfigParam;
import org.dromara.daxpay.service.device.result.qrcode.config.CashierCodeConfigResult;
import org.dromara.daxpay.service.device.convert.qrcode.CashierCodeConfigConvert;
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
public class CashierCodeConfig extends MpBaseEntity implements ToResult<CashierCodeConfigResult> {

    /** 配置名称 */
    private String name;

    /** 是否启用 */
    private Boolean enable;

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

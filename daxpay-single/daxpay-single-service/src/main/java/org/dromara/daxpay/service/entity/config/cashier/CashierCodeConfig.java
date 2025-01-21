package org.dromara.daxpay.service.entity.config.cashier;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.config.CashierCodeConfigConvert;
import org.dromara.daxpay.service.param.config.cashier.CashierCodeConfigParam;
import org.dromara.daxpay.service.result.config.cashier.CashierCodeConfigResult;

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

    /** 码牌code */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 模板编号 */
    private String templateCode;

    /** 是否启用 */
    private boolean enable;

    /** 备注 */
    private String remark;

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

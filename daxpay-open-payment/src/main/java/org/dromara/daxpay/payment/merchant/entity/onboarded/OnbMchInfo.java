package org.dromara.daxpay.payment.merchant.entity.onboarded;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.payment.merchant.common.entity.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.onboarded.OnbMchInfoConvert;
import org.dromara.daxpay.payment.merchant.result.onboarded.OnbMchInfoResult;

/**
 * 进件商户信息
 * @author xxm
 * @since 2025/11/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_onb_mch_info")
public class OnbMchInfo extends MchBaseEntity implements ToResult<OnbMchInfoResult> {

    /** 进件商户号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String onbMchNo;

    /** 商户名称 */
    private String onbMchName;

    /** 所属通道 */
    private String onbChannel;

    /**
     * 转换
     */
    @Override
    public OnbMchInfoResult toResult() {
        return OnbMchInfoConvert.CONVERT.toResult(this);
    }
}

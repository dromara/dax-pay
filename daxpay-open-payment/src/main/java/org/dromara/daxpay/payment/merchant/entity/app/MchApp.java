package org.dromara.daxpay.payment.merchant.entity.app;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.app.MchAppConvert;
import org.dromara.daxpay.payment.merchant.enums.MchAppStatusEnum;
import org.dromara.daxpay.payment.merchant.result.app.MchAppResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 商户应用
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("pay_mch_app")
public class MchApp extends MchAppBaseEntity implements ToResult<MchAppResult> {

    /** 应用名称 */
    private String appName;

    /**
     * 应用状态
     * @see MchAppStatusEnum
     */
    private String status;

    /**
     * 默认应用
     */
    private boolean defaultApp;

    /**
     * 通知地址, http 需要配置
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;


    @Override
    public MchAppResult toResult() {
        return MchAppConvert.CONVERT.toResult(this);
    }
}

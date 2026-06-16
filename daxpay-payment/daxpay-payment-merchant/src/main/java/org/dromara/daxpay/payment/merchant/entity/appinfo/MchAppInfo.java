package org.dromara.daxpay.payment.merchant.entity.appinfo;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.appinfo.MchAppInfoConvert;
import org.dromara.daxpay.platform.core.enums.merchant.MchAppStatusEnum;
import org.dromara.daxpay.payment.merchant.result.appinfo.MchAppInfoResult;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户应用信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("mch_app_info")
public class MchAppInfo extends MchBaseEntity implements ToResult<MchAppInfoResult> {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER, fill = FieldFill.INSERT)
    private String appId;

    /// 应用名称
    private String appName;

    /// 应用状态
    /// @see MchAppStatusEnum
    private String status;

    /// 默认应用
    private boolean defaultApp;

    @Override
    public MchAppInfoResult toResult() {
        return MchAppInfoConvert.CONVERT.toResult(this);
    }
}

package cn.daxpay.open.payment.merchant.entity.appinfo;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.appinfo.MchAppInfoConvert;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
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

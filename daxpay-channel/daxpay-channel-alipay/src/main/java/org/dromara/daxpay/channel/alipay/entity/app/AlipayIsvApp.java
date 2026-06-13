package org.dromara.daxpay.channel.alipay.entity.app;

import org.dromara.daxpay.channel.alipay.convert.AlipayIsvAppConvert;
import org.dromara.daxpay.channel.alipay.result.app.AlipayIsvAppResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_isv_app", autoResultMap = true)
public class AlipayIsvApp extends MpBaseEntity implements ToResult<AlipayIsvAppResult> {

    /// 应用名称
    private String appName;

    /// 支付宝应用ID
    private String aliAppId;

    /// 转换
    @Override
    public AlipayIsvAppResult toResult() {
        return AlipayIsvAppConvert.CONVERT.toResult(this);
    }
}

package cn.daxpay.open.channel.alipay.entity.isv;

import cn.daxpay.open.channel.alipay.convert.isv.AlipayIsvAppConvert;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAppResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用
///
/// 表示支付宝开放平台上注册的服务商(ISV)应用实体，服务商通过此应用代为商户发起支付请求。
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

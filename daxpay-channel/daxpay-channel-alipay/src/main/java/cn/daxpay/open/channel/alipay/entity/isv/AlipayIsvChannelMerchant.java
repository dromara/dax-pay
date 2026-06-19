package cn.daxpay.open.channel.alipay.entity.isv;

import cn.daxpay.open.channel.alipay.convert.isv.AlipayIsvChannelMerchantConvert;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商通道商户绑定
///
/// 一条记录代表"子商户挂靠在某个服务商应用下"的授权关系。
/// 同一子商户挂不同应用 = 不同行(不同 channelMchNo)。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_isv_channel_merchant", autoResultMap = true)
public class AlipayIsvChannelMerchant extends MchBaseEntity implements ToResult<AlipayIsvChannelMerchantResult> {

    /// 通道商户号(AISV+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 关联服务商应用ID(系统主键, 指向 alipay_isv_app.id)
    private Long isvAppId;

    /// 子商户支付宝识别码(2088开头的16位数字)
    private String alipayUserId;

    /// 应用授权令牌(服务商代子商户调用接口的凭据, 会过期/刷新)
    private String appAuthToken;

    /// 转换
    @Override
    public AlipayIsvChannelMerchantResult toResult() {
        return AlipayIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}

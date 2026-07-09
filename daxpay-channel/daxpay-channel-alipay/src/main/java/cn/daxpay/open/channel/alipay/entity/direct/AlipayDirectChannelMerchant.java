package cn.daxpay.open.channel.alipay.entity.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayDirectChannelMerchantConvert;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连通道商户绑定
///
/// 一个商户PID对应一个channelMchNo, 商户的多个应用共享此绑定。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_direct_channel_merchant", autoResultMap = true)
public class AlipayDirectChannelMerchant extends MchBaseEntity implements ToResult<AlipayDirectChannelMerchantResult> {

    /// 通道商户号(ALIPAY+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 支付宝商家唯一识别码(2088开头的16位数字)
    private String alipayUserId;

    /// 是否沙箱环境商户
    private boolean sandbox;

    /// 转换
    @Override
    public AlipayDirectChannelMerchantResult toResult() {
        return AlipayDirectChannelMerchantConvert.CONVERT.toResult(this);
    }
}

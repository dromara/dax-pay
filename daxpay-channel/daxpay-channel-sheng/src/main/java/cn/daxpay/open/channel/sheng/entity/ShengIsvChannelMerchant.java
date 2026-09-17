package cn.daxpay.open.channel.sheng.entity;

import cn.daxpay.open.channel.sheng.convert.ShengIsvChannelMerchantConvert;
import cn.daxpay.open.channel.sheng.result.ShengIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 盛付通服务商(ISV)通道商户绑定
///
/// 服务商模式下每个子商户一行绑定: 通用通道商户主表(mch_channel_merchant)持有一个通道商户号,
/// 本表按该通道商户号记录子商户身份(subMchId 必填 + sdpAppId 可空),
/// 签名密钥由产品级全局的服务商密钥配置([ShengIsvKeyConfig])提供。
///
/// 同一商户下子商户号唯一(uk_sheng_isv_mch_sub), 通道商户号全局唯一(uk_sheng_isv_cmchno)。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "sheng_isv_channel_merchant", autoResultMap = true)
public class ShengIsvChannelMerchant extends MchBaseEntity implements ToResult<ShengIsvChannelMerchantResult> {

    /// 通道商户号(关联 mch_channel_merchant, 创建后不可修改)
    private String channelMchNo;

    /// 所属支付产品(sheng_isv)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 子商户号(服务商代子商户发起交易, 必填)
    private String subMchId;

    /// 盛付通分配的子商户应用ID(可空)
    private String sdpAppId;

    @Override
    public ShengIsvChannelMerchantResult toResult() {
        return ShengIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}

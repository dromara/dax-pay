package cn.daxpay.open.channel.dougong.entity.isv;

import cn.daxpay.open.channel.dougong.convert.isv.DougongIsvChannelMerchantConvert;
import cn.daxpay.open.channel.dougong.result.isv.DougongIsvChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 斗拱通道商户绑定
///
/// 斗拱服务商模式下, 子商户绑定汇付商户号(merchantNo) + 商户 appId(BasePay.putMerConfigs 的 key),
/// 密钥由服务商全局配置([DougongIsvKeyConfig])提供。
/// 同一商户下汇付商户号不重复。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "dougong_isv_channel_merchant", autoResultMap = true)
public class DougongIsvChannelMerchant extends MchBaseEntity implements ToResult<DougongIsvChannelMerchantResult> {

    /// 通道商户号(DOUGONG+雪花)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 汇付商户号(merchantNo / huifuId)
    private String merchantNo;

    /// 商户 appId(汇付 SDK BasePay.putMerConfigs 的 key)
    private String appId;

    /// 转换
    @Override
    public DougongIsvChannelMerchantResult toResult() {
        return DougongIsvChannelMerchantConvert.CONVERT.toResult(this);
    }
}

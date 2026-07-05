package cn.daxpay.open.channel.ums.entity.direct;

import cn.daxpay.open.channel.ums.convert.direct.UmsDirectChannelMerchantConvert;
import cn.daxpay.open.channel.ums.result.direct.UmsDirectChannelMerchantResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 银联商务直连通道商户绑定
///
/// 一个银联商务商户号(merchantNo)对应一个 channelMchNo, 包含终端号与订单前缀。
/// 银联商务为聚合支付, 一个商户号同时支持支付宝/微信/银联的扫码与 H5。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "ums_direct_channel_merchant", autoResultMap = true)
public class UmsDirectChannelMerchant extends MchBaseEntity implements ToResult<UmsDirectChannelMerchantResult> {

    /// 通道商户号(系统生成雪花号)
    private String channelMchNo;

    /// 所属支付产品
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 银联商务商户号(mid)
    private String merchantNo;

    /// 终端号(tid)
    private String terminalNo;

    /// 订单号前缀(用于生成关联订单号)
    private String orderPrefix;

    /// 是否沙箱环境
    private boolean sandbox;

    @Override
    public UmsDirectChannelMerchantResult toResult() {
        return UmsDirectChannelMerchantConvert.CONVERT.toResult(this);
    }
}

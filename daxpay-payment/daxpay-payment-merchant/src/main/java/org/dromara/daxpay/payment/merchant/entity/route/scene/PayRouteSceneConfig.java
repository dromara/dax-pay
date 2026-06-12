package org.dromara.daxpay.payment.merchant.entity.route.scene;

import org.dromara.daxpay.payment.merchant.convert.route.scene.PayRouteSceneConfigConvert;
import org.dromara.daxpay.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付通道路由场景模式配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_route_scene_config")
public class PayRouteSceneConfig extends MpBaseEntity implements ToResult<PayRouteSceneConfigResult> {

    /// 路由策略ID
    private Long strategyId;

    /// 支付渠道, 空表示通用方式（qrcode/barcode 等）
    /// @see PayProviderEnum
    private String provider;

    /// 通道编码
    private String channel;

    /// 支付方式编码
    private String method;

    /// 产品编码
    private String product;

    @Override
    public PayRouteSceneConfigResult toResult() {
        return PayRouteSceneConfigConvert.CONVERT.toResult(this);
    }
}

package cn.daxpay.open.payment.route.entity.scene;

import cn.daxpay.open.payment.route.convert.scene.PayRouteSceneConfigConvert;
import cn.daxpay.open.payment.route.result.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
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

    /// 支付方式编码
    private String method;

    /// 通道商户号(唯一绑定一个支付产品，替代旧版 product 字段)
    private String channelMchNo;

    /// 支付能力编码(商户为该支付方式+通道商户选定的能力)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    private String capability;

    @Override
    public PayRouteSceneConfigResult toResult() {
        return PayRouteSceneConfigConvert.CONVERT.toResult(this);
    }
}

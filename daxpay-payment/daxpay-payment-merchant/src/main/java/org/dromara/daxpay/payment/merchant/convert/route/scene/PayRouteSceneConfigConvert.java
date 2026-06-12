package org.dromara.daxpay.payment.merchant.convert.route.scene;

import org.dromara.daxpay.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import org.dromara.daxpay.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 支付通道路由场景模式配置转换
///
@Mapper
public interface PayRouteSceneConfigConvert {

    PayRouteSceneConfigConvert CONVERT = Mappers.getMapper(PayRouteSceneConfigConvert.class);

    @Mapping(target = "strategyId", expression = "java(entity.getStrategyId() == null ? null : String.valueOf(entity.getStrategyId()))")
    PayRouteSceneConfigResult toResult(PayRouteSceneConfig entity);
}

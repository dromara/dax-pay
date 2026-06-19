package cn.daxpay.open.payment.merchant.convert.route.scene;

import cn.daxpay.open.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
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

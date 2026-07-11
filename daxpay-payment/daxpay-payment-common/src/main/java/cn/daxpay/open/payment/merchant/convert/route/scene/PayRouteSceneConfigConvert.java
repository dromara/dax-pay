package cn.daxpay.open.payment.merchant.convert.route.scene;

import cn.daxpay.open.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付通道路由场景模式配置转换
///
/// strategyId: 实体 Long → Result String，MapStruct 内置 null 安全数值转字符串
@Mapper
public interface PayRouteSceneConfigConvert {

    PayRouteSceneConfigConvert CONVERT = Mappers.getMapper(PayRouteSceneConfigConvert.class);

    PayRouteSceneConfigResult toResult(PayRouteSceneConfig entity);
}

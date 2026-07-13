package cn.daxpay.open.payment.route.convert.scene;

import cn.daxpay.open.payment.route.entity.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.route.result.scene.PayRouteSceneConfigResult;
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

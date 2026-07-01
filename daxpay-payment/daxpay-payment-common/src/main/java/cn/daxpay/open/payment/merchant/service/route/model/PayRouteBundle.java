package cn.daxpay.open.payment.merchant.service.route.model;

import cn.daxpay.open.payment.merchant.entity.route.basic.PayRouteBasicConfig;
import cn.daxpay.open.payment.merchant.entity.route.scene.PayRouteSceneConfig;
import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 通道路由数据包
///
/// 按应用聚合策略及基础/场景模式配置，供试算匹配使用。
@Data
@Accessors(chain = true)
public class PayRouteBundle {

    /// 应用路由策略（含生效模式）
    private PayRouteStrategy strategy;

    /// 场景模式配置列表
    private List<PayRouteSceneConfig> sceneConfigs = new ArrayList<>();

    /// 基础模式按支付渠道的产品配置列表
    private List<PayRouteBasicConfig> basicConfigs = new ArrayList<>();
}

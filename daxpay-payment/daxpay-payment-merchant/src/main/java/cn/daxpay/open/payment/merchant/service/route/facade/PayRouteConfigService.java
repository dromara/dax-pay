package cn.daxpay.open.payment.merchant.service.route.facade;

import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.convert.route.strategy.PayRouteStrategyConvert;
import cn.daxpay.open.payment.merchant.dao.route.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.merchant.entity.route.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.merchant.param.route.basic.PayRouteBasicConfigBatchParam;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneCapabilityBatchParam;
import cn.daxpay.open.payment.merchant.param.route.scene.PayRouteSceneConfigBatchParam;
import cn.daxpay.open.payment.merchant.param.route.strategy.PayRouteStrategyParam;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.payment.merchant.result.route.basic.PayRouteBasicConfigResult;
import cn.daxpay.open.payment.merchant.result.route.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.merchant.result.route.strategy.PayRouteStrategyResult;
import cn.daxpay.open.payment.merchant.service.route.basic.PayRouteBasicConfigService;
import cn.daxpay.open.payment.merchant.service.route.scene.PayRouteSceneConfigService;
import cn.daxpay.open.platform.core.enums.pay.route.PayRouteModeEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/// # 通道路由配置门面
///
/// 管理端通道路由唯一编排入口，按模式委托场景/基础配置服务，不承载匹配逻辑。
///
@Service
@RequiredArgsConstructor
public class PayRouteConfigService {

    private final PayRouteStrategyManager strategyManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final PayRouteSceneConfigService sceneConfigService;
    private final PayRouteBasicConfigService basicConfigService;
    /// 按应用号获取路由策略，不存在则创建默认基础模式策略
    @Transactional(rollbackFor = Exception.class)
    public PayRouteStrategyResult getOrInitByAppId(String appId) {
        mchAppInfoManager.requireByAppIdNotTenant(appId);
        return strategyManager.findByAppId(appId)
                .map(PayRouteStrategy::toResult)
                .orElseGet(() -> createDefaultStrategy(appId));
    }

    /// 创建默认路由策略（基础模式、已启用）
    private PayRouteStrategyResult createDefaultStrategy(String appId) {
        PayRouteStrategy strategy = new PayRouteStrategy();
        strategy.setAppId(appId);
        strategy.setMchNo(mchAppInfoManager.requireMchNoByAppIdNotTenant(appId));
        strategy.setMode(PayRouteModeEnum.BASIC.getCode());
        strategy.setEnable(true);
        strategy.setName("默认策略");
        strategyManager.save(strategy);
        return strategy.toResult();
    }

    /// 更新应用路由策略（模式、支付渠道、启用状态、名称）
    @Transactional(rollbackFor = Exception.class)
    public PayRouteStrategyResult updateStrategy(PayRouteStrategyParam param) {
        mchAppInfoManager.requireByAppIdNotTenant(param.getAppId());
        PayRouteStrategy strategy = strategyManager.findByAppId(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
        PayRouteStrategyConvert.CONVERT.copy(param, strategy);
        strategyManager.updateById(strategy);
        return strategy.toResult();
    }

    /// 查询场景模式配置列表
    public List<PayRouteSceneConfigResult> listSceneByAppId(String appId) {
        return sceneConfigService.listSceneByAppId(appId);
    }

    /// 批量保存场景模式配置（全量覆盖）
    public void saveSceneBatch(PayRouteSceneConfigBatchParam param) {
        sceneConfigService.saveSceneBatch(param);
    }

    /// 通道路由白名单目录下全部通道商户候选（批量）
    public Map<String, List<LabelValue>> listSceneChannelMchCandidatesBatch(String appId) {
        return sceneConfigService.listSceneChannelMchCandidatesBatch(appId);
    }

    /// 按目录项与通道商户批量返回支付能力候选
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(PayRouteSceneCapabilityBatchParam param) {
        return sceneConfigService.listSceneCapabilityCandidatesBatch(param);
    }

    /// 目录项下商户已开通的通道商户候选
    public List<LabelValue> listSceneChannelMchCandidatesForMethod(String appId, String provider, String method) {
        return sceneConfigService.listSceneChannelMchCandidatesForMethod(appId, provider, method);
    }

    /// 目录项与通道商户下支付能力候选
    public List<LabelValue> listSceneCapabilityCandidatesForMethod(
            String appId, String provider, String method, String channelMchNo) {
        return sceneConfigService.listSceneCapabilityCandidatesForMethod(appId, provider, method, channelMchNo);
    }

    /// 推断目录项与通道商户下唯一支付能力（仅供回显）
    public String inferSceneCapability(String appId, String provider, String method, String channelMchNo) {
        return sceneConfigService.inferSceneCapability(appId, provider, method, channelMchNo);
    }

    /// 查询基础模式各支付渠道对应产品
    public List<PayRouteBasicConfigResult> listBasicByAppId(String appId) {
        return basicConfigService.listBasicByAppId(appId);
    }

    /// 批量保存基础模式配置（全量覆盖）
    public void saveBasicBatch(PayRouteBasicConfigBatchParam param) {
        basicConfigService.saveBasicBatch(param);
    }
}

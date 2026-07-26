package cn.daxpay.open.payment.app.merchant.service.route;

import cn.daxpay.open.payment.admin.service.merchant.route.PayRouteConfigService;
import cn.daxpay.open.payment.masterdata.result.provider.PayProviderMethodResult;
import cn.daxpay.open.payment.masterdata.service.provider.PayProviderMethodService;
import cn.daxpay.open.payment.route.param.basic.PayRouteBasicConfigBatchParam;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneCapabilityBatchParam;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneConfigBatchParam;
import cn.daxpay.open.payment.route.param.strategy.PayRouteStrategyParam;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.payment.route.result.basic.PayRouteBasicConfigResult;
import cn.daxpay.open.payment.route.result.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.route.result.strategy.PayRouteStrategyResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/// # 商户移动端-通道路由配置服务
///
/// 转发至 [PayRouteConfigService] 和 [PayProviderMethodService]
@Service
@RequiredArgsConstructor
public class AppMerchantPayRouteService {

    private final PayRouteConfigService configService;
    private final PayProviderMethodService payProviderMethodService;

    /// 已启用渠道支付方式扁平列表
    public List<PayProviderMethodResult> listMethodDirectoryFlat() {
        return payProviderMethodService.listDirectoryFlat();
    }

    /// 获取或初始化应用路由策略
    public PayRouteStrategyResult getOrInitByAppId(String appId) {
        return configService.getOrInitByAppId(appId);
    }

    /// 更新路由策略
    public PayRouteStrategyResult updateStrategy(PayRouteStrategyParam param) {
        return configService.updateStrategy(param);
    }

    /// 查询场景模式配置列表
    public List<PayRouteSceneConfigResult> listSceneByAppId(String appId) {
        return configService.listSceneByAppId(appId);
    }

    /// 批量保存场景模式配置
    public void saveSceneBatch(PayRouteSceneConfigBatchParam param) {
        configService.saveSceneBatch(param);
    }

    /// 场景模式通道商户候选（批量）
    public Map<String, List<ChannelMchOption>> listSceneChannelMhCandidatesBatch(String appId) {
        return configService.listSceneChannelMchCandidatesBatch(appId);
    }

    /// 场景模式支付能力候选（批量）
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(PayRouteSceneCapabilityBatchParam param) {
        return configService.listSceneCapabilityCandidatesBatch(param);
    }

    /// 目录项下通道商户候选
    public List<ChannelMchOption> listSceneChannelMhCandidates(String appId, String provider, String method) {
        return configService.listSceneChannelMchCandidatesForMethod(appId, provider, method);
    }

    /// 目录项与通道商户下支付能力候选
    public List<LabelValue> listSceneCapabilityCandidates(String appId, String provider, String method, String channelMchNo) {
        return configService.listSceneCapabilityCandidatesForMethod(appId, provider, method, channelMchNo);
    }

    /// 查询基础模式配置列表
    public List<PayRouteBasicConfigResult> listBasicByAppId(String appId) {
        return configService.listBasicByAppId(appId);
    }

    /// 批量保存基础模式配置
    public void saveBasicBatch(PayRouteBasicConfigBatchParam param) {
        configService.saveBasicBatch(param);
    }
}

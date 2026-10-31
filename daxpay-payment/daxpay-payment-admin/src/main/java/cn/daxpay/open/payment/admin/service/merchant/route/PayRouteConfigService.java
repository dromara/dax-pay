package cn.daxpay.open.payment.admin.service.merchant.route;

import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.route.convert.strategy.PayRouteStrategyConvert;
import cn.daxpay.open.payment.route.dao.strategy.PayRouteStrategyManager;
import cn.daxpay.open.payment.route.entity.strategy.PayRouteStrategy;
import cn.daxpay.open.payment.route.param.basic.PayRouteBasicConfigBatchParam;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneCapabilityBatchParam;
import cn.daxpay.open.payment.route.param.scene.PayRouteSceneConfigBatchParam;
import cn.daxpay.open.payment.route.param.strategy.PayRouteStrategyParam;
import cn.daxpay.open.platform.core.rest.dto.ChannelMchOption;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.payment.route.result.basic.PayRouteBasicConfigResult;
import cn.daxpay.open.payment.route.result.scene.PayRouteSceneConfigResult;
import cn.daxpay.open.payment.route.result.strategy.PayRouteStrategyResult;
import cn.daxpay.open.payment.route.service.basic.PayRouteBasicConfigService;
import cn.daxpay.open.payment.route.service.scene.PayRouteSceneConfigService;
import cn.daxpay.open.payment.route.service.support.PayRouteBundleCacheEvictor;
import cn.daxpay.open.platform.core.enums.pay.route.PayRouteModeEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/// # 通道路由配置门面
///
/// 管理端通道路由唯一编排入口，按模式委托场景/基础配置服务，不承载匹配逻辑。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRouteConfigService {

    private final PayRouteStrategyManager strategyManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final PayRouteSceneConfigService sceneConfigService;
    private final PayRouteBasicConfigService basicConfigService;
    private final PayRouteBundleCacheEvictor routeBundleCacheEvictor;

    /// 按应用号获取路由策略，不存在则创建默认基础模式策略
    ///
    /// 刻意不加 @Transactional：并发兜底分支要在唯一索引冲突后重新查询，
    /// 若与 insert 同事务，冲突已把事务标记为回滚，重查结果不可用；单条 insert 本身即为原子操作。
    public PayRouteStrategyResult getOrInitByAppId(String appId) {
        // 配置态：运营端 ignoreTable / 租户内查应用
        mchAppInfoManager.requireByAppId(appId);
        return strategyManager.findByAppId(appId)
                .map(PayRouteStrategy::toResult)
                .orElseGet(() -> createDefaultStrategyQuietly(appId));
    }

    /// 创建默认路由策略（基础模式、已启用）
    private PayRouteStrategyResult createDefaultStrategy(String appId) {
        PayRouteStrategy strategy = new PayRouteStrategy();
        strategy.setAppId(appId);
        strategy.setMchNo(mchAppInfoManager.requireMchNoByAppId(appId));
        strategy.setMode(PayRouteModeEnum.BASIC.getCode());
        strategyManager.save(strategy);
        return strategy.toResult();
    }

    /// 并发首访问兜底：撞唯一索引 `uk_pay_route_strategy_app` 时重查已存在行返回
    ///
    /// 配置面板打开时同一 appId 可能被并发初始化（多个区块并行请求），
    /// 无兜底时后到的请求会拿到 DuplicateKeyException 变成 500。
    private PayRouteStrategyResult createDefaultStrategyQuietly(String appId) {
        try {
            return createDefaultStrategy(appId);
        } catch (DuplicateKeyException e) {
            log.info("通道路由策略并发初始化, 返回已存在行: appId={}", appId);
            return strategyManager.findByAppId(appId)
                    .map(PayRouteStrategy::toResult)
                    // 路由: 通道路由策略不存在
                    .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
        }
    }

    /// 更新应用路由策略（模式）
    ///
    /// 缓存失效在事务提交后执行，见 [PayRouteBundleCacheEvictor]。
    @Transactional(rollbackFor = Exception.class)
    public PayRouteStrategyResult updateStrategy(PayRouteStrategyParam param) {
        mchAppInfoManager.requireByAppId(param.getAppId());
        // 路由模式必须命中枚举：非法值落库后 matchByMode 会按「非 basic 即场景」静默走场景模式
        if (StrUtil.isNotBlank(param.getMode())) {
            PayRouteModeEnum.findByCode(param.getMode());
        }
        PayRouteStrategy strategy = strategyManager.findByAppId(param.getAppId())
                .orElseThrow(() -> new DataNotExistException("pay.route.error.routeStrategyNotExist"));
        PayRouteStrategyConvert.CONVERT.copy(param, strategy);
        strategyManager.updateById(strategy);
        // 缓存失效: 事务提交后执行(提交前失效会被并发回源回填旧配置)
        routeBundleCacheEvictor.evictAfterCommit(param.getAppId());
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
    public Map<String, List<ChannelMchOption>> listSceneChannelMchCandidatesBatch(String appId) {
        return sceneConfigService.listSceneChannelMchCandidatesBatch(appId);
    }

    /// 按目录项与通道商户批量返回支付能力候选
    public Map<String, List<LabelValue>> listSceneCapabilityCandidatesBatch(PayRouteSceneCapabilityBatchParam param) {
        return sceneConfigService.listSceneCapabilityCandidatesBatch(param);
    }

    /// 目录项下商户已开通的通道商户候选
    public List<ChannelMchOption> listSceneChannelMchCandidatesForMethod(String appId, String provider, String method) {
        return sceneConfigService.listSceneChannelMchCandidatesForMethod(appId, provider, method);
    }

    /// 目录项与通道商户下支付能力候选
    public List<LabelValue> listSceneCapabilityCandidatesForMethod(
            String appId, String provider, String method, String channelMchNo) {
        return sceneConfigService.listSceneCapabilityCandidatesForMethod(appId, provider, method, channelMchNo);
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

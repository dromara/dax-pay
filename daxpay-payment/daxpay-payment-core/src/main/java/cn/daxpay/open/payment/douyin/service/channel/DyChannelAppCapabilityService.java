package cn.daxpay.open.payment.douyin.service.channel;

import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.douyin.convert.channel.DyChannelAppCapabilityConvert;
import cn.daxpay.open.payment.douyin.dao.channel.DyChannelAppCapabilityManager;
import cn.daxpay.open.payment.douyin.dao.merchant.DyMchAppManager;
import cn.daxpay.open.payment.douyin.dao.platform.DyPlatformAppManager;
import cn.daxpay.open.payment.douyin.entity.channel.DyChannelAppCapability;
import cn.daxpay.open.payment.douyin.entity.merchant.DyMchApp;
import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformApp;
import cn.daxpay.open.payment.douyin.enums.DyAppScopeEnum;
import cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum;
import cn.daxpay.open.payment.douyin.param.channel.DyChannelAppCapabilityParam;
import cn.daxpay.open.payment.douyin.result.channel.DyChannelAppCapabilityResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 通道商户抖音应用能力绑定
///
/// 管理通道商户 × 支付能力 × 档位 对主数据的引用；先删后插全量覆盖。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DyChannelAppCapabilityService {

    private final DyChannelAppCapabilityManager capabilityManager;
    private final DyPlatformAppManager dyPlatformAppManager;
    private final DyMchAppManager dyMchAppManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 按通道商户号查询能力绑定列表，并填充应用展示信息
    public List<DyChannelAppCapabilityResult> listByChannelMchNo(String channelMchNo) {
        List<DyChannelAppCapability> rels = capabilityManager.listByChannelMchNo(channelMchNo);
        if (rels.isEmpty()) {
            return List.of();
        }
        Map<Long, DyPlatformApp> platformMap = dyPlatformAppManager.listAll().stream()
                .collect(Collectors.toMap(DyPlatformApp::getId, Function.identity()));
        // 通道绑定同属一商户，用首行 mchNo 批量加载商户应用
        String mchNo = rels.getFirst().getMchNo();
        Map<Long, DyMchApp> mchAppMap = dyMchAppManager.listByMchNo(mchNo).stream()
                .collect(Collectors.toMap(DyMchApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, platformMap, mchAppMap))
                .toList();
    }

    /// 按通道商户全量保存能力绑定（先清后插）
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(String mchNo, String channelMchNo, List<DyChannelAppCapabilityParam> items) {
        // mchNo 缺失直接归入"通道商户与商户号不匹配"，避免下方 equals 触发 NPE
        if (mchNo == null || mchNo.isBlank()) {
            // 抖音: 通道商户与商户号不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.douyin.channelMerchantMismatch");
        }
        // 校验通道商户存在且 mchNo 匹配
        ChannelMerchant channelMerchant = channelMerchantManager.findByChannelMchNo(channelMchNo)
                // 抖音: 通道商户不存在或商户号不匹配
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.channelMerchantMismatch"));
        if (!mchNo.equals(channelMerchant.getMchNo())) {
            // 抖音: 通道商户与商户号不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.douyin.channelMerchantMismatch");
        }
        // 先删该通道商户下全部绑定
        capabilityManager.deleteByChannelMchNo(channelMchNo);
        if (CollUtil.isEmpty(items)) {
            return;
        }
        Map<Long, DyPlatformApp> platformMap = dyPlatformAppManager.listAll().stream()
                .collect(Collectors.toMap(DyPlatformApp::getId, Function.identity()));
        Map<Long, DyMchApp> mchAppMap = dyMchAppManager.listByMchNo(mchNo).stream()
                .collect(Collectors.toMap(DyMchApp::getId, Function.identity()));
        // capability + scope 唯一
        HashSet<String> uniq = new HashSet<>();
        for (DyChannelAppCapabilityParam item : items) {
            DyAppScopeEnum scope = DyAppScopeEnum.findByCode(item.getAppScope());
            if (scope == null) {
                // 抖音: 档位不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.scopeNotExist");
            }
            // 通道能力绑仅允许商户档；服务商应用走产品级默认绑
            if (scope != DyAppScopeEnum.MERCHANT) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.scopeNotExist");
            }
            String uniqKey = item.getCapability() + "#" + scope.getCode();
            if (!uniq.add(uniqKey)) {
                // 抖音: 能力+档位重复
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.appTypeCapabilityMismatch");
            }
            String appType = this.resolveAndValidateRef(scope, item.getDyAppRefId(), mchNo, platformMap, mchAppMap);
            // 应用类型与能力强校验
            if (!DyAppTypeEnum.isCompatible(appType, item.getCapability())) {
                // 抖音: 应用类型与支付能力不匹配
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.appTypeCapabilityMismatch");
            }
            var entity = new DyChannelAppCapability()
                    .setChannelMchNo(channelMchNo)
                    .setCapability(item.getCapability())
                    .setAppScope(scope.getCode())
                    .setDyAppRefId(item.getDyAppRefId());
            // 运营端写 MchBaseEntity 必须显式 setMchNo
            entity.setMchNo(mchNo);
            capabilityManager.save(entity);
        }
    }

    /// 删除通道商户下全部能力绑定
    public void deleteByChannelMchNo(String channelMchNo) {
        capabilityManager.deleteByChannelMchNo(channelMchNo);
    }

    /// 校验档位引用存在，返回应用类型
    private String resolveAndValidateRef(DyAppScopeEnum scope, Long refId, String mchNo,
                                         Map<Long, DyPlatformApp> platformMap, Map<Long, DyMchApp> mchAppMap) {
        if (scope == DyAppScopeEnum.PLATFORM) {
            DyPlatformApp app = platformMap.get(refId);
            if (app == null) {
                // 抖音: 平台应用不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.appNotFound");
            }
            return app.getAppType();
        }
        DyMchApp app = mchAppMap.get(refId);
        if (app == null || !mchNo.equals(app.getMchNo())) {
            // 抖音: 商户应用不存在或不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.douyin.mchAppNotFound");
        }
        return app.getAppType();
    }

    /// 填充冗余展示字段
    private DyChannelAppCapabilityResult fillResult(DyChannelAppCapability rel,
                                                    Map<Long, DyPlatformApp> platformMap,
                                                    Map<Long, DyMchApp> mchAppMap) {
        DyChannelAppCapabilityResult result = DyChannelAppCapabilityConvert.CONVERT.toResult(rel);
        if (DyAppScopeEnum.PLATFORM.getCode().equals(rel.getAppScope())) {
            DyPlatformApp app = platformMap.get(rel.getDyAppRefId());
            if (app != null) {
                result.setAppName(app.getAppName())
                        .setDouyinAppId(app.getDouyinAppId())
                        .setAppType(app.getAppType());
            }
        }
        else {
            DyMchApp app = mchAppMap.get(rel.getDyAppRefId());
            if (app != null) {
                result.setAppName(app.getAppName())
                        .setDouyinAppId(app.getDouyinAppId())
                        .setAppType(app.getAppType());
            }
        }
        return result;
    }
}

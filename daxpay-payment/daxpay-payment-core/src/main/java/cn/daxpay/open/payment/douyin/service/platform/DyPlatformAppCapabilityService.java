package cn.daxpay.open.payment.douyin.service.platform;

import cn.daxpay.open.payment.masterdata.dao.capability.PayProductCapabilityManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProductCapability;
import cn.daxpay.open.payment.douyin.convert.platform.DyPlatformAppCapabilityConvert;
import cn.daxpay.open.payment.douyin.dao.platform.DyPlatformAppCapabilityManager;
import cn.daxpay.open.payment.douyin.dao.platform.DyPlatformAppManager;
import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformApp;
import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformAppCapability;
import cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppCapabilityBatchParam;
import cn.daxpay.open.payment.douyin.param.platform.DyPlatformAppCapabilityParam;
import cn.daxpay.open.payment.douyin.result.DyCapabilityOption;
import cn.daxpay.open.payment.douyin.result.platform.DyPlatformAppCapabilityResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 平台抖音应用默认能力绑定
///
/// 按支付产品管理「支付能力 → 平台抖音应用」绑定。
/// 支付时通过 [DyAppResolveService] 解析：通道绑 > 本产品默认绑 > appType 推导。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DyPlatformAppCapabilityService {

    private final DyPlatformAppCapabilityManager capabilityManager;
    private final DyPlatformAppManager dyPlatformAppManager;
    private final PayProductCapabilityManager payProductCapabilityManager;

    /// 按支付产品查询能力关联列表，并填充应用展示信息
    public List<DyPlatformAppCapabilityResult> listByProduct(String product) {
        if (StrUtil.isBlank(product)) {
            return List.of();
        }
        List<DyPlatformAppCapability> rels = capabilityManager.listByProduct(product);
        if (rels.isEmpty()) {
            return List.of();
        }
        Map<Long, DyPlatformApp> appMap = dyPlatformAppManager.listAll().stream()
                .collect(Collectors.toMap(DyPlatformApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, appMap.get(rel.getDyPlatformAppId())))
                .toList();
    }

    /// 按支付产品全量保存能力关联（先清该产品后插）
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(DyPlatformAppCapabilityBatchParam param) {
        String product = param.getProduct();
        if (StrUtil.isBlank(product)) {
            // 抖音: 参数校验失败
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.product.notBlank");
        }
        Set<String> allowedCapabilities = payProductCapabilityManager.listByProduct(product).stream()
                .map(PayProductCapability::getCapabilityCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<DyPlatformAppCapabilityParam> items = param.getItems();
        // 先清空该产品旧关联
        capabilityManager.deleteByProduct(product);
        if (CollUtil.isEmpty(items)) {
            return;
        }
        Map<Long, DyPlatformApp> appMap = dyPlatformAppManager.listAll().stream()
                .collect(Collectors.toMap(DyPlatformApp::getId, Function.identity()));
        HashSet<String> capabilitySet = new HashSet<>();
        for (DyPlatformAppCapabilityParam item : items) {
            // 能力须属于该产品白名单
            if (!allowedCapabilities.contains(item.getCapability())) {
                // 抖音: 能力不在产品白名单内
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.appTypeCapabilityMismatch");
            }
            // 同产品内能力不可重复
            if (!capabilitySet.add(item.getCapability())) {
                // 抖音: 同产品内能力不可重复
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.appTypeCapabilityMismatch");
            }
            DyPlatformApp app = appMap.get(item.getDyPlatformAppId());
            if (app == null) {
                // 抖音: 平台应用不存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.appNotFound");
            }
            if (!DyAppTypeEnum.isCompatible(app.getAppType(), item.getCapability())) {
                // 抖音: 应用类型与支付能力不匹配
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.douyin.appTypeCapabilityMismatch");
            }
            var entity = new DyPlatformAppCapability()
                    .setProduct(product)
                    .setCapability(item.getCapability())
                    .setDyPlatformAppId(item.getDyPlatformAppId());
            capabilityManager.save(entity);
        }
    }

    /// 按支付产品查询可绑定的能力候选（pay_md_product_capability 白名单）
    public List<DyCapabilityOption> listSupportedCapabilities(String product) {
        if (StrUtil.isBlank(product)) {
            return List.of();
        }
        return payProductCapabilityManager.listByProduct(product).stream()
                .map(PayProductCapability::getCapabilityCode)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .map(code -> {
                    PayCapabilityEnum cap = PayCapabilityEnum.findByCode(code);
                    String name = cap != null ? I18nUtil.getEnumName(cap) : code;
                    return new DyCapabilityOption(code, name);
                })
                .toList();
    }

    /// 填充关联结果的应用展示字段
    private DyPlatformAppCapabilityResult fillResult(DyPlatformAppCapability rel, DyPlatformApp app) {
        DyPlatformAppCapabilityResult result = DyPlatformAppCapabilityConvert.CONVERT.toResult(rel);
        if (app != null) {
            result.setAppName(app.getAppName())
                    .setDouyinAppId(app.getDouyinAppId())
                    .setAppType(app.getAppType());
        }
        return result;
    }
}

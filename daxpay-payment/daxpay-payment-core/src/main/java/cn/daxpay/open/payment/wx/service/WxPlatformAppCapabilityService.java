package cn.daxpay.open.payment.wx.service;

import cn.daxpay.open.payment.masterdata.dao.capability.PayProductCapabilityManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProductCapability;
import cn.daxpay.open.payment.wx.convert.WxPlatformAppCapabilityConvert;
import cn.daxpay.open.payment.wx.dao.WxPlatformAppCapabilityManager;
import cn.daxpay.open.payment.wx.dao.WxPlatformAppManager;
import cn.daxpay.open.payment.wx.entity.WxPlatformApp;
import cn.daxpay.open.payment.wx.entity.WxPlatformAppCapability;
import cn.daxpay.open.payment.wx.enums.WxAppTypeEnum;
import cn.daxpay.open.payment.wx.param.WxPlatformAppCapabilityBatchParam;
import cn.daxpay.open.payment.wx.param.WxPlatformAppCapabilityParam;
import cn.daxpay.open.payment.wx.result.WxCapabilityOption;
import cn.daxpay.open.payment.wx.result.WxPlatformAppCapabilityResult;
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
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 平台微信应用默认能力绑定
///
/// 按支付产品管理「支付能力 → 平台微信应用」绑定。
/// 支付时通过 [WxAppResolveService] 解析：通道绑 > 本产品默认绑 > appType 推导。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WxPlatformAppCapabilityService {

    private final WxPlatformAppCapabilityManager capabilityManager;
    private final WxPlatformAppManager wxPlatformAppManager;
    private final PayProductCapabilityManager payProductCapabilityManager;

    /// 按支付产品查询能力关联列表，并填充应用展示信息
    public List<WxPlatformAppCapabilityResult> listByProduct(String product) {
        if (StrUtil.isBlank(product)) {
            return List.of();
        }
        List<WxPlatformAppCapability> rels = capabilityManager.listByProduct(product);
        if (rels.isEmpty()) {
            return List.of();
        }
        Map<Long, WxPlatformApp> appMap = wxPlatformAppManager.listAll().stream()
                .collect(Collectors.toMap(WxPlatformApp::getId, Function.identity()));
        return rels.stream()
                .map(rel -> fillResult(rel, appMap.get(rel.getWxPlatformAppId())))
                .toList();
    }

    /// 按支付产品全量保存能力关联（先清该产品后插）
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(WxPlatformAppCapabilityBatchParam param) {
        String product = param.getProduct();
        if (StrUtil.isBlank(product)) {
            // 微信: 参数校验失败
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.product.notBlank");
        }
        Set<String> allowedCapabilities = payProductCapabilityManager.listByProduct(product).stream()
                .map(PayProductCapability::getCapabilityCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<WxPlatformAppCapabilityParam> items = param.getItems();
        // 先清空该产品旧关联
        capabilityManager.deleteByProduct(product);
        if (CollUtil.isEmpty(items)) {
            return;
        }
        Map<Long, WxPlatformApp> appMap = wxPlatformAppManager.listAll().stream()
                .collect(Collectors.toMap(WxPlatformApp::getId, Function.identity()));
        HashSet<String> capabilitySet = new HashSet<>();
        for (WxPlatformAppCapabilityParam item : items) {
            // 能力须属于该产品白名单
            if (!allowedCapabilities.contains(item.getCapability())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appTypeCapabilityMismatch");
            }
            // 同产品内能力不可重复
            if (!capabilitySet.add(item.getCapability())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appTypeCapabilityMismatch");
            }
            WxPlatformApp app = appMap.get(item.getWxPlatformAppId());
            if (app == null) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appNotFound");
            }
            if (!WxAppTypeEnum.isCompatible(app.getAppType(), item.getCapability())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.wx.appTypeCapabilityMismatch");
            }
            var entity = new WxPlatformAppCapability()
                    .setProduct(product)
                    .setCapability(item.getCapability())
                    .setWxPlatformAppId(item.getWxPlatformAppId());
            capabilityManager.save(entity);
        }
    }

    /// 按支付产品查询可绑定的能力候选（pay_md_product_capability 白名单）
    public List<WxCapabilityOption> listSupportedCapabilities(String product) {
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
                    return new WxCapabilityOption(code, name);
                })
                .toList();
    }

    /// 填充关联结果的应用展示字段
    private WxPlatformAppCapabilityResult fillResult(WxPlatformAppCapability rel, WxPlatformApp app) {
        WxPlatformAppCapabilityResult result = WxPlatformAppCapabilityConvert.CONVERT.toResult(rel);
        if (app != null) {
            result.setAppName(app.getAppName())
                    .setWxAppId(app.getWxAppId())
                    .setAppType(app.getAppType());
        }
        return result;
    }
}

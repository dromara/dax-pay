package cn.daxpay.open.payment.admin.service.merchant.gateway;

import cn.daxpay.open.payment.merchant.enums.CashierItemResolveModeEnum;
import cn.daxpay.open.payment.merchant.enums.CashierSceneEnum;
import cn.daxpay.open.payment.merchant.enums.GatewayCashierTypeEnum;
import cn.daxpay.open.payment.merchant.dao.gateway.GatewayCashierItemManager;
import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCashierItem;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayCashierItemParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCashierItemResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/// # 网关收银台配置服务
///
/// 管理应用级收银台支付项: H5 按终端 scene 分桶, WEB 扁平列表;
/// 每项支持 method / direct 两种支付解析模式。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayCashierConfigService {

    private final GatewayCashierItemManager itemManager;

    /// 按应用 + 收银台类型 + 场景列出支付项
    public List<GatewayCashierItemResult> list(String appId, String cashierType, String scene) {
        GatewayCashierTypeEnum typeEnum = GatewayCashierTypeEnum.findByCode(cashierType);
        String normalizedScene = normalizeSceneForQuery(typeEnum, scene);
        return itemManager.listByAppAndBucket(appId, typeEnum.getCode(), normalizedScene).stream()
                .map(this::toResult)
                .toList();
    }

    /// 按 id 查询
    public GatewayCashierItemResult findById(Long id) {
        return toResult(getRequired(id));
    }

    /// 新建支付项
    @Transactional(rollbackFor = Exception.class)
    public void save(GatewayCashierItemParam param) {
        NormalizedItem normalized = normalizeAndValidate(param);
        GatewayCashierItem entity = new GatewayCashierItem();
        applyNormalized(entity, normalized, param.getMchNo(), param.getAppId());
        itemManager.save(entity);
    }

    /// 更新支付项
    @Transactional(rollbackFor = Exception.class)
    public void update(GatewayCashierItemParam param) {
        GatewayCashierItem entity = getRequired(param.getId());
        // 应用号不可变更
        if (!Objects.equals(entity.getAppId(), param.getAppId())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierItemAppIdImmutable");
        }
        NormalizedItem normalized = normalizeAndValidate(param);
        applyNormalized(entity, normalized, entity.getMchNo(), entity.getAppId());
        itemManager.updateById(entity);
    }

    /// 删除支付项
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        getRequired(id);
        itemManager.deleteById(id);
    }

    private GatewayCashierItem getRequired(Long id) {
        return itemManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.gateway.cashierItemNotFound"));
    }

    /// 规范化 scene 查询参数
    private String normalizeSceneForQuery(GatewayCashierTypeEnum typeEnum, String scene) {
        if (typeEnum == GatewayCashierTypeEnum.WEB) {
            return null;
        }
        if (StrUtil.isBlank(scene)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierSceneRequired");
        }
        // 校验 scene 合法
        CashierSceneEnum.findByCode(scene);
        return scene;
    }

    /// 校验并规范化写入字段
    private NormalizedItem normalizeAndValidate(GatewayCashierItemParam param) {
        GatewayCashierTypeEnum typeEnum = GatewayCashierTypeEnum.findByCode(param.getCashierType());
        CashierItemResolveModeEnum resolveMode = CashierItemResolveModeEnum.findByCode(param.getResolveMode());

        String scene = param.getScene();
        if (typeEnum == GatewayCashierTypeEnum.WEB) {
            // WEB 固定无 scene
            scene = null;
        } else {
            if (StrUtil.isBlank(scene)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierSceneRequired");
            }
            CashierSceneEnum.findByCode(scene);
        }

        String method = StrUtil.trimToNull(param.getMethod());
        String channelMchNo = StrUtil.trimToNull(param.getChannelMchNo());
        String capability = StrUtil.trimToNull(param.getCapability());

        if (resolveMode == CashierItemResolveModeEnum.METHOD) {
            if (StrUtil.isBlank(method)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierItemMethodRequired");
            }
            // METHOD 模式清空 direct 字段
            channelMchNo = null;
            capability = null;
        } else {
            if (StrUtil.isBlank(channelMchNo)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierItemChannelMchRequired");
            }
            if (StrUtil.isBlank(capability)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierItemCapabilityRequired");
            }
            // DIRECT 模式清空 method
            method = null;
        }

        String name = StrUtil.trim(param.getName());
        if (StrUtil.isBlank(name)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierItemNameRequired");
        }

        Boolean recommend = param.getRecommend() != null && param.getRecommend();
        Integer sortNo = param.getSortNo() != null ? param.getSortNo() : 0;
        String icon = StrUtil.trimToNull(param.getIcon());

        return new NormalizedItem(typeEnum.getCode(), scene, name, icon, recommend, sortNo,
                resolveMode.getCode(), method, channelMchNo, capability);
    }

    private void applyNormalized(GatewayCashierItem entity, NormalizedItem n, String mchNo, String appId) {
        if (entity.getId() == null) {
            entity.setMchNo(mchNo);
            entity.setAppId(appId);
        }
        entity.setCashierType(n.cashierType());
        entity.setScene(n.scene());
        entity.setName(n.name());
        entity.setIcon(n.icon());
        entity.setRecommend(n.recommend());
        entity.setSortNo(n.sortNo());
        entity.setResolveMode(n.resolveMode());
        entity.setMethod(n.method());
        entity.setChannelMchNo(n.channelMchNo());
        entity.setCapability(n.capability());
    }

    private GatewayCashierItemResult toResult(GatewayCashierItem entity) {
        GatewayCashierItemResult result = new GatewayCashierItemResult();
        BeanUtil.copyProperties(entity, result);
        return result;
    }

    private record NormalizedItem(
            String cashierType,
            String scene,
            String name,
            String icon,
            Boolean recommend,
            Integer sortNo,
            String resolveMode,
            String method,
            String channelMchNo,
            String capability
    ) {
    }
}

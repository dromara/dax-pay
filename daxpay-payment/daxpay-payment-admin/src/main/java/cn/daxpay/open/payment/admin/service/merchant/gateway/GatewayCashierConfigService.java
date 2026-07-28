package cn.daxpay.open.payment.admin.service.merchant.gateway;

import cn.daxpay.open.payment.merchant.enums.CashierItemResolveModeEnum;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
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
import java.util.Set;

/// # 网关收银台配置服务
///
/// 管理应用级收银台支付项:
/// - H5: 按 clientEnv 五档分桶
/// - MINI: 按 clientEnv 分桶(wechat/alipay/union_pay/douyin, 不含 browser)
/// - WEB: 扁平列表, 无 clientEnv
/// 每项支持 method / direct 两种支付解析模式。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayCashierConfigService {

    /// 小程序收银台允许的 clientEnv(不含 browser)
    private static final Set<String> MINI_CLIENT_ENVS = Set.of(
            ClientEnvEnum.WECHAT.getCode(),
            ClientEnvEnum.ALIPAY.getCode(),
            ClientEnvEnum.UNION_PAY.getCode(),
            ClientEnvEnum.DOUYIN.getCode()
    );

    private final GatewayCashierItemManager itemManager;

    /// 按应用 + 收银台类型 + 客户端环境列出支付项
    public List<GatewayCashierItemResult> list(String appId, String cashierType, String clientEnv) {
        GatewayCashierTypeEnum typeEnum = GatewayCashierTypeEnum.findByCode(cashierType);
        String normalizedClientEnv = normalizeClientEnvForQuery(typeEnum, clientEnv);
        return itemManager.listByAppAndBucket(appId, typeEnum.getCode(), normalizedClientEnv).stream()
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
            // 网关: 收银台支付项应用号不可修改
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
                // 网关: 收银台支付项不存在
                .orElseThrow(() -> new DataNotExistException("pay.error.gateway.cashierItemNotFound"));
    }

    /// 规范化 clientEnv 查询参数: WEB 固定 null; H5/MINI 必填且按类型校验
    private String normalizeClientEnvForQuery(GatewayCashierTypeEnum typeEnum, String clientEnv) {
        if (!typeEnum.requiresClientEnv()) {
            return null;
        }
        if (StrUtil.isBlank(clientEnv)) {
            // 网关: H5收银台必须指定客户端环境
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvRequired");
        }
        return validateClientEnvForType(typeEnum, clientEnv);
    }

    /// 校验并规范化写入字段
    private NormalizedItem normalizeAndValidate(GatewayCashierItemParam param) {
        GatewayCashierTypeEnum typeEnum = GatewayCashierTypeEnum.findByCode(param.getCashierType());
        CashierItemResolveModeEnum resolveMode = CashierItemResolveModeEnum.findByCode(param.getResolveMode());

        String clientEnv = param.getClientEnv();
        if (!typeEnum.requiresClientEnv()) {
            // WEB 固定无 clientEnv
            clientEnv = null;
        } else {
            if (StrUtil.isBlank(clientEnv)) {
                // 网关: H5收银台必须指定客户端环境
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.clientEnvRequired");
            }
            clientEnv = validateClientEnvForType(typeEnum, clientEnv);
        }

        String method = StrUtil.trimToNull(param.getMethod());
        String channelMchNo = StrUtil.trimToNull(param.getChannelMchNo());
        String capability = StrUtil.trimToNull(param.getCapability());

        if (resolveMode == CashierItemResolveModeEnum.METHOD) {
            if (StrUtil.isBlank(method)) {
                // 网关: 指定支付方式时支付方式必填
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierItemMethodRequired");
            }
            // METHOD 模式清空 direct 字段
            channelMchNo = null;
            capability = null;
        } else {
            if (StrUtil.isBlank(channelMchNo)) {
                // 网关: 直接指定时通道商户号必填
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierItemChannelMchRequired");
            }
            if (StrUtil.isBlank(capability)) {
                // 网关: 直接指定时支付能力必填
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.gateway.cashierItemCapabilityRequired");
            }
            // DIRECT 模式清空 method
            method = null;
        }

        String name = StrUtil.trim(param.getName());
        if (StrUtil.isBlank(name)) {
            // 网关: 支付项名称不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.cashierItemNameRequired");
        }

        Boolean recommend = param.getRecommend() != null && param.getRecommend();
        Integer sortNo = param.getSortNo() != null ? param.getSortNo() : 0;
        String icon = StrUtil.trimToNull(param.getIcon());

        return new NormalizedItem(typeEnum.getCode(), clientEnv, name, icon, recommend, sortNo,
                resolveMode.getCode(), method, channelMchNo, capability);
    }

    /// 按收银台类型校验 clientEnv: H5 允许全部 ClientEnvEnum; MINI 四档(无 browser)
    private String validateClientEnvForType(GatewayCashierTypeEnum typeEnum, String clientEnv) {
        ClientEnvEnum env = ClientEnvEnum.findByCode(clientEnv);
        if (typeEnum == GatewayCashierTypeEnum.MINI && !MINI_CLIENT_ENVS.contains(env.getCode())) {
            // 网关: 不支持的客户端环境
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvNotSupport");
        }
        return env.getCode();
    }

    private void applyNormalized(GatewayCashierItem entity, NormalizedItem n, String mchNo, String appId) {
        if (entity.getId() == null) {
            entity.setMchNo(mchNo);
            entity.setAppId(appId);
        }
        entity.setCashierType(n.cashierType());
        entity.setClientEnv(n.clientEnv());
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
            String clientEnv,
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

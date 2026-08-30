package cn.daxpay.open.payment.merchant.service.device;

import cn.daxpay.open.payment.common.access.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindAppParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeBindStoreParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeClaimParam;
import cn.daxpay.open.payment.merchant.param.device.DeviceQrCodeParam;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayPayConfigResolveService;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocCapabilityService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// # 支付码牌管理(商户端)
///
/// 商户自助管理名下码牌: 业务配置编辑、启停、应用/门店绑定调整、码图链接获取、空白码认领。
/// 归属变更(划拨/回收)与码牌生成为运营端能力([cn.daxpay.open.payment.admin.service.device.DeviceQrCodeAdminService])。
///
/// [DeviceQrCode] 继承 [cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity],
/// 不参与租户拦截与 mchNo 自动填充, 所有读写均须显式走 [PaymentContext] 归属校验
@Slf4j
@Service
@RequiredArgsConstructor
public class MchDeviceQrCodeService {

    /// H5 码牌支付页路径前缀(与 dax-pay-h5 RoutePath.CODE_PAY 一致: /h/:code)
    private static final String CODE_H5_PATH = "/h/";

    /// 小程序码牌扫码 path 前缀(映射域名落地后置, 本期仅生成链接)
    private static final String CODE_MINI_PATH = "/m/";

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final MerchantContextLoader merchantContextLoader;
    private final PaymentContext paymentContext;
    private final TransService transService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final GatewayPayConfigResolveService gatewayPayConfigResolveService;
    private final PayRouteService payRouteService;
    private final AllocCapabilityService allocCapabilityService;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            // 商户上下文未装载, 无法进行数据隔离操作
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验码牌归属当前商户(空白码 mchNo 为空, 同样视为不可操作)
    private void checkQrCode(DeviceQrCode entity) {
        if (!Objects.equals(entity.getMchNo(), requireMchNo())) {
            // 码牌: 码牌不属于当前商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.mchNoMatch");
        }
    }

    /// 批量加载并校验归属当前商户
    private List<DeviceQrCode> requireOwnedQrCodes(List<Long> ids) {
        List<DeviceQrCode> list = deviceQrCodeManager.findAllByIds(ids);
        if (list.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        for (DeviceQrCode qrCode : list) {
            checkQrCode(qrCode);
        }
        return list;
    }

    /// 分页(强制隔离到当前商户)
    public PageResult<DeviceQrCodeResult> page(PageParam pageParam, DeviceQrCodeQuery query) {
        // 无论前端是否传入, 一律覆盖为当前商户号
        query.setMchNo(requireMchNo());
        PageResult<DeviceQrCodeResult> pageResult = MpUtil.toPageResult(deviceQrCodeManager.page(pageParam, query));
        // 翻译商户/应用/门店名称
        transService.translate(pageResult);
        return pageResult;
    }

    /// 根据id查询
    public DeviceQrCodeResult findById(Long id) {
        DeviceQrCodeResult result = requireOwnedQrCode(id).toResult();
        // 翻译商户/应用/门店名称
        transService.translate(result);
        return result;
    }

    /// 按主键加载并校验归属
    private DeviceQrCode requireOwnedQrCode(Long id) {
        DeviceQrCode entity = deviceQrCodeManager.findById(id)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        checkQrCode(entity);
        return entity;
    }

    /// 修改码牌业务配置(编码/归属不可改; 归属走 bind/unbind)
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceQrCodeParam param) {
        DeviceQrCode entity = requireOwnedQrCode(param.getId());
        // 金额类型校验
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(param.getAmountType());
        validateFixedAmount(amountType, param.getFixedAmount());
        // 仅更新业务配置, 不改 mchNo/appId/storeNo
        entity.setName(param.getName())
                .setAmountType(amountType.getCode())
                .setFixedAmount(amountType == QrCodeAmountTypeEnum.FIXED ? param.getFixedAmount() : null)
                .setAllocation(Boolean.TRUE.equals(param.getAllocation()))
                .setRemark(param.getRemark());
        deviceQrCodeManager.updateById(entity);
    }

    /// 修改状态(启用/停用)
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, String status) {
        QrCodeStatusEnum statusEnum = QrCodeStatusEnum.findByCode(status);
        DeviceQrCode entity = requireOwnedQrCode(id);
        entity.setStatus(statusEnum.getCode());
        deviceQrCodeManager.updateById(entity);
    }

    /// 批量绑定应用(校验应用归属当前商户; 空值走支付时默认应用)
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(DeviceQrCodeBindAppParam param) {
        String mchNo = requireMchNo();
        requireOwnedQrCodes(param.getIds());
        // 校验应用归属并取规范化 appId
        String appId = resolveAppId(mchNo, param.getAppId());
        deviceQrCodeManager.bindApp(param.getIds(), appId);
    }

    /// 批量解绑应用(仅清 appId, 支付时走默认应用)
    @Transactional(rollbackFor = Exception.class)
    public void unbindApp(List<Long> ids) {
        requireOwnedQrCodes(ids);
        deviceQrCodeManager.unbindApp(ids);
    }

    /// 批量绑定门店(校验门店归属当前商户; 启用态在下单校验)
    @Transactional(rollbackFor = Exception.class)
    public void bindStore(DeviceQrCodeBindStoreParam param) {
        String mchNo = requireMchNo();
        requireOwnedQrCodes(param.getIds());
        // 门店存在且归属商户
        validateStoreBelongToMch(param.getStoreNo(), mchNo);
        deviceQrCodeManager.bindStore(param.getIds(), param.getStoreNo());
    }

    /// 批量解绑门店(保留商户/应用)
    @Transactional(rollbackFor = Exception.class)
    public void unbindStore(List<Long> ids) {
        requireOwnedQrCodes(ids);
        deviceQrCodeManager.unbindStore(ids);
    }

    /// 认领空白码牌(平台未分配库存码 → 当前商户名下)
    ///
    /// 并发场景由 [DeviceQrCodeManager#claimBlank] 的条件更新兜底, 仅一个认领生效
    @Transactional(rollbackFor = Exception.class)
    public void claim(DeviceQrCodeClaimParam param) {
        DeviceQrCode qrCode = deviceQrCodeManager.findByCode(param.getCode())
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        // 空白码才可认领; 已分配商户(含已归属自己)按已被认领处理, 提示走运营划拨渠道
        if (StrUtil.isNotBlank(qrCode.getMchNo()) || !deviceQrCodeManager.claimBlank(qrCode.getId(), requireMchNo())) {
            // 码牌: 码牌已被认领或已分配商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.alreadyAssigned");
        }
    }

    /// 获取码牌扫码链接
    ///
    /// 同一 paymentGatewayBaseUrl 下按 programType 分流 path:
    /// - h5 → /h/{code}
    /// - mini_app → /m/{code}
    public String getCodeLink(String code) {
        DeviceQrCode qrCode = deviceQrCodeManager.findByCode(code)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        checkQrCode(qrCode);
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        String gatewayBase = urlConfig.getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 支付网关前端地址未配置
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.common.gatewayUrlNotConfigured");
        }
        // 按落地程序类型选 path; 历史空值按 H5
        String path = QrCodeProgramTypeEnum.MINI_APP.getCode().equals(qrCode.getProgramType())
                ? CODE_MINI_PATH
                : CODE_H5_PATH;
        return gatewayBase + path + code;
    }

    /// 分账能力预警(码牌开启分账前预检, 不阻断), 商户号取当前登录商户
    ///
    /// 与支付侧同路径解析, 网关配置未覆盖的场景跳过; 支付时降级不依赖此预检, 结果仅供前端提示
    public List<DeviceQrCodeAllocWarningResult> allocCapabilityWarning(String appId) {
        String mchNo = requireMchNo();
        // 与支付侧一致: appId 空取商户默认应用
        String resolvedAppId = resolveAppId(mchNo, appId);
        List<DeviceQrCodeAllocWarningResult> warnings = new ArrayList<>();
        for (CodePayFormEnum payForm : CodePayFormEnum.values()) {
            for (ClientEnvEnum clientEnv : ClientEnvEnum.values()) {
                if (clientEnv == ClientEnvEnum.BROWSER) {
                    continue;
                }
                GatewayPayConfigResolveService.Resolved resolved;
                try {
                    resolved = gatewayPayConfigResolveService.resolve(resolvedAppId, clientEnv, payForm);
                } catch (BizException e) {
                    // 该场景未配置支付方式(BizException 为预期常态, 商户通常只覆盖部分场景), debug 级避免日志噪音
                    log.debug("分账预警跳过未配置场景: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode());
                    continue;
                } catch (Exception e) {
                    // 非预期异常会让预警清单缺项, 误导分账开关决策, 须留痕
                    log.warn("分账预警场景解析异常, 场景被跳过: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode(), e);
                    continue;
                }
                // 跟随支付同路径路由出产品(路由失败同样跳过)
                NormalPayParam routeParam = new NormalPayParam();
                routeParam.setAppId(resolvedAppId);
                routeParam.setMethod(resolved.method());
                routeParam.setChannelMchNo(resolved.channelMchNo());
                routeParam.setCapability(resolved.capability());
                try {
                    payRouteService.resolve(routeParam);
                } catch (BizException e) {
                    // 路由失败(未启用/未配置)同属预期常态, debug 级
                    log.debug("分账预警跳过路由失败场景: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode());
                    continue;
                } catch (Exception e) {
                    // 非预期异常会让预警清单缺项, 须留痕
                    log.warn("分账预警路由检查异常, 场景被跳过: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode(), e);
                    continue;
                }
                String channel = ProductEnum.findByCode(routeParam.getProduct()).getChannel();
                if (!allocCapabilityService.supports(channel)) {
                    warnings.add(new DeviceQrCodeAllocWarningResult()
                            .setClientEnv(clientEnv.getCode())
                            .setPayForm(payForm.getCode())
                            .setProduct(routeParam.getProduct())
                            .setChannel(channel));
                }
            }
        }
        return warnings;
    }

    /// 应用解析: 复用 [MerchantContextLoader#resolveApp], appId 空取商户默认应用, 返回解析后的 appId
    private String resolveAppId(String mchNo, String appId) {
        MchAppInfoAccessInfo mchApp = merchantContextLoader.resolveApp(mchNo, appId);
        return mchApp.getAppId();
    }

    /// 门店存在且归属指定商户(绑定阶段不强制启用态)
    private void validateStoreBelongToMch(String storeNo, String mchNo) {
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        if (!Objects.equals(store.getMchNo(), mchNo)) {
            // 商户: 门店不属于当前商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.payment.merchant.storeNoMatch");
        }
    }

    /// 固定金额校验: fixed 类型必填且大于 0
    private void validateFixedAmount(QrCodeAmountTypeEnum amountType, Long fixedAmount) {
        if (amountType == QrCodeAmountTypeEnum.FIXED) {
            if (fixedAmount == null || fixedAmount <= 0) {
                // 码牌: 固定金额必须大于 0
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.fixedAmountInvalid");
            }
        }
    }
}

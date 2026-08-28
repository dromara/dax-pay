package cn.daxpay.open.payment.admin.service.device;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeProgramTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBatchParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindAppParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindMerchantParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindStoreParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeParam;
import cn.daxpay.open.payment.admin.result.device.DeviceQrCodeAllocWarningResult;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.common.access.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
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
import java.util.stream.IntStream;

/// # 支付码牌管理(运营端)
///
/// 维护码牌台账, 支持批量创建空白码、划拨绑定商户与门店。
/// 扫码后的支付编排由 unipay 模块 CodePayAssistService 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceQrCodeAdminService {

    /// H5 码牌支付页路径前缀(与 dax-pay-h5 RoutePath.CODE_PAY 一致: /h/:code)
    private static final String CODE_H5_PATH = "/h/";

    /// 小程序码牌扫码 path 前缀(映射域名落地后置, 本期仅生成链接)
    private static final String CODE_MINI_PATH = "/m/";

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final MerchantContextLoader merchantContextLoader;
    private final TransService transService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final GatewayPayConfigResolveService gatewayPayConfigResolveService;
    private final PayRouteService payRouteService;
    private final AllocCapabilityService allocCapabilityService;

    /// 批量创建空白码牌(不绑商户, 进入平台库存)
    @Transactional(rollbackFor = Exception.class)
    public void createBatch(DeviceQrCodeBatchParam param) {
        // 批次号唯一
        if (deviceQrCodeManager.existedByBatchNo(param.getBatchNo())) {
            // 码牌: 批次号已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.batchNoExists");
        }
        // 落地程序类型(创建后不可改)
        QrCodeProgramTypeEnum programType = QrCodeProgramTypeEnum.findByCode(param.getProgramType());
        // 金额类型校验
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(param.getAmountType());
        validateFixedAmount(amountType, param.getFixedAmount());
        Long fixedAmount = amountType == QrCodeAmountTypeEnum.FIXED ? param.getFixedAmount() : null;
        // 状态: 空则默认启用
        String status = StrUtil.isBlank(param.getStatus())
                ? QrCodeStatusEnum.ENABLED.getCode()
                : QrCodeStatusEnum.findByCode(param.getStatus()).getCode();
        // 编码: 批次号 + 三位序号(001-999)
        List<DeviceQrCode> list = IntStream.rangeClosed(1, param.getCount())
                .mapToObj(i -> new DeviceQrCode()
                        .setCode(param.getBatchNo() + String.format("%03d", i))
                        .setName(param.getName())
                        .setBatchNo(param.getBatchNo())
                        .setProgramType(programType.getCode())
                        .setAmountType(amountType.getCode())
                        .setFixedAmount(fixedAmount)
                        .setStatus(status)
                        // 空白码默认非分账, 划拨后由商户/运营按需开启
                        .setAllocation(false)
                        .setRemark(param.getRemark()))
                .toList();
        deviceQrCodeManager.saveAll(list);
    }

    /// 判断批次号是否已存在
    public boolean existsByBatchNo(String batchNo) {
        return deviceQrCodeManager.existedByBatchNo(batchNo);
    }

    /// 分账能力预警: 按当前网关支付配置解析各扫码场景路由出的产品, 返回不支持分账的场景清单
    ///
    /// 码牌开启分账开关前的预检提示(不阻断保存); 与支付侧同路径解析, 网关配置未覆盖的场景跳过。
    /// 支付时降级不依赖此预检(下单链路实时判定), 结果仅供前端提示。
    public List<DeviceQrCodeAllocWarningResult> allocCapabilityWarning(String mchNo, String appId) {
        // 与支付侧一致: appId 空取商户默认应用
        String resolvedAppId = this.resolveAppId(mchNo, appId);
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
                    // 该场景未配置支付方式(BizException 为预期常态, 商户通常只覆盖部分场景):
                    // 支付时会明确报错, 不属于分账预警范畴, debug 级避免日志噪音
                    log.debug("分账预警跳过未配置场景: appId={}, clientEnv={}, payForm={}",
                            resolvedAppId, clientEnv.getCode(), payForm.getCode());
                    continue;
                } catch (Exception e) {
                    // 非预期异常(DB 抖动/编程错误)会让预警清单缺项, 误导运营的分账开关决策, 须留痕
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

    /// 批量绑定商户(可覆盖已绑定归属; appId/storeNo 均可空, 空写 null 支付时再 resolve)
    @Transactional(rollbackFor = Exception.class)
    public void bindMerchant(DeviceQrCodeBindMerchantParam param) {
        // 应用: 有值则校验归属; 空则写 null(支付 resolveApp 取默认应用)
        String appId = resolveOptionalAppId(param.getMchNo(), param.getAppId());
        // 门店: 有值则校验归属新商户; 无值写 null 防止跨商户脏数据
        String storeNo = resolveStoreNoForBind(param.getStoreNo(), param.getMchNo());
        deviceQrCodeManager.bindMerchant(param.getIds(), param.getMchNo(), appId, storeNo);
    }

    /// 批量解绑商户(回空白库存, 保留编码/批次/金额配置; 同步清空应用与门店)
    @Transactional(rollbackFor = Exception.class)
    public void unbindMerchant(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        deviceQrCodeManager.unbindMerchant(ids);
    }

    /// 批量绑定应用(须已绑商户且勾选同商户)
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(DeviceQrCodeBindAppParam param) {
        String mchNo = requireSameAssignedMch(param.getIds());
        // 校验应用归属并取规范化 appId
        String appId = resolveAppId(mchNo, param.getAppId());
        deviceQrCodeManager.bindApp(param.getIds(), appId);
    }

    /// 批量解绑应用(仅清 appId, 支付时走默认应用)
    @Transactional(rollbackFor = Exception.class)
    public void unbindApp(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        deviceQrCodeManager.unbindApp(ids);
    }

    /// 批量绑定门店(须已绑商户且勾选同商户; 仅校验存在+归属, 启用态在下单校验)
    @Transactional(rollbackFor = Exception.class)
    public void bindStore(DeviceQrCodeBindStoreParam param) {
        String mchNo = requireSameAssignedMch(param.getIds());
        // 门店存在且归属商户
        validateStoreBelongToMch(param.getStoreNo(), mchNo);
        deviceQrCodeManager.bindStore(param.getIds(), param.getStoreNo());
    }

    /// 批量解绑门店(保留商户/应用)
    @Transactional(rollbackFor = Exception.class)
    public void unbindStore(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        deviceQrCodeManager.unbindStore(ids);
    }

    /// 修改码牌(编码/归属不可改; 归属走 bind/unbind)
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceQrCodeParam param) {
        DeviceQrCode entity = deviceQrCodeManager.findById(param.getId())
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        // 金额类型校验
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(param.getAmountType());
        validateFixedAmount(amountType, param.getFixedAmount());
        // 仅更新业务配置, 不改 mchNo/appId
        entity.setName(param.getName())
                .setAmountType(amountType.getCode())
                .setFixedAmount(amountType == QrCodeAmountTypeEnum.FIXED ? param.getFixedAmount() : null)
                .setAllocation(Boolean.TRUE.equals(param.getAllocation()))
                .setRemark(param.getRemark());
        deviceQrCodeManager.updateById(entity);
    }

    /// 分页
    public PageResult<DeviceQrCodeResult> page(PageParam pageParam, DeviceQrCodeQuery query) {
        PageResult<DeviceQrCodeResult> pageResult = MpUtil.toPageResult(deviceQrCodeManager.page(pageParam, query));
        // 翻译商户名称(mchNo -> mchName)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 根据id查询
    public DeviceQrCodeResult findById(Long id) {
        DeviceQrCodeResult result = deviceQrCodeManager.findById(id)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"))
                .toResult();
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 删除
    public void delete(Long id) {
        deviceQrCodeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        deviceQrCodeManager.deleteById(id);
    }

    /// 修改状态(启用/停用)
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, String status) {
        QrCodeStatusEnum statusEnum = QrCodeStatusEnum.findByCode(status);
        DeviceQrCode entity = deviceQrCodeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        entity.setStatus(statusEnum.getCode());
        deviceQrCodeManager.updateById(entity);
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

    /// 应用解析: 复用 [MerchantContextLoader#resolveApp], appId 空取商户默认应用, 返回解析后的 appId
    private String resolveAppId(String mchNo, String appId) {
        MchAppInfoAccessInfo mchApp = merchantContextLoader.resolveApp(mchNo, appId);
        return mchApp.getAppId();
    }

    /// 绑商户可选应用: 空写 null; 非空校验归属后返回 appId
    private String resolveOptionalAppId(String mchNo, String appId) {
        if (StrUtil.isBlank(appId)) {
            return null;
        }
        return resolveAppId(mchNo, appId);
    }

    /// 绑定商户时的门店: 空返回 null; 非空校验归属后返回
    private String resolveStoreNoForBind(String storeNo, String mchNo) {
        if (StrUtil.isBlank(storeNo)) {
            return null;
        }
        validateStoreBelongToMch(storeNo, mchNo);
        return storeNo;
    }

    /// 校验勾选码牌已分配商户且同一 mchNo, 返回该商户号
    private String requireSameAssignedMch(List<Long> ids) {
        List<DeviceQrCode> list = deviceQrCodeManager.findAllByIds(ids);
        if (list.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        String mchNo = null;
        for (DeviceQrCode qrCode : list) {
            if (StrUtil.isBlank(qrCode.getMchNo())) {
                // 码牌: 请先绑定商户
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.notAssigned");
            }
            if (mchNo == null) {
                mchNo = qrCode.getMchNo();
            } else if (!Objects.equals(mchNo, qrCode.getMchNo())) {
                // 码牌: 批量操作须选择同一商户下的码牌
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.storeBindMchInconsistent");
            }
        }
        return mchNo;
    }

    /// 门店存在且归属指定商户(绑定阶段不强制启用态)
    private void validateStoreBelongToMch(String storeNo, String mchNo) {
        MchStoreInfo store = mchStoreInfoManager.findByStoreNo(storeNo)
                // 商户: 门店不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.storeNotFound"));
        if (StrUtil.isNotBlank(mchNo) && !Objects.equals(store.getMchNo(), mchNo)) {
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

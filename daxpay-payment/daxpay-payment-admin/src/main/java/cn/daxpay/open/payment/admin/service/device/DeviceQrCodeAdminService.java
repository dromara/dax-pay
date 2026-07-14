package cn.daxpay.open.payment.admin.service.device;

import cn.daxpay.open.payment.common.assist.MerchantContextLoader;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBatchParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeBindMerchantParam;
import cn.daxpay.open.payment.admin.param.device.DeviceQrCodeParam;
import cn.daxpay.open.payment.device.qrcode.param.DeviceQrCodeQuery;
import cn.daxpay.open.payment.device.qrcode.result.DeviceQrCodeResult;
import cn.daxpay.open.payment.common.access.MchAppInfoAccessInfo;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
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

import java.util.List;
import java.util.stream.IntStream;

/// # 支付码牌管理(运营端)
///
/// 维护码牌台账, 支持批量创建空白码与划拨绑定商户。
/// 扫码后的支付编排由 unipay 模块 CodePayAssistService 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceQrCodeAdminService {

    /// H5 码牌支付页路径前缀(与 dax-pay-h5 RoutePath.CODE_PAY 一致)
    private static final String CODE_PAY_PATH = "/code-pay/";

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MerchantContextLoader merchantContextLoader;
    private final TransService transService;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 批量创建空白码牌(不绑商户, 进入平台库存)
    @Transactional(rollbackFor = Exception.class)
    public void createBatch(DeviceQrCodeBatchParam param) {
        // 批次号唯一
        if (deviceQrCodeManager.existedByBatchNo(param.getBatchNo())) {
            // 码牌: 批次号已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.batchNoExists");
        }
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
                        .setAmountType(amountType.getCode())
                        .setFixedAmount(fixedAmount)
                        .setStatus(status)
                        .setRemark(param.getRemark()))
                .toList();
        deviceQrCodeManager.saveAll(list);
    }

    /// 判断批次号是否已存在
    public boolean existsByBatchNo(String batchNo) {
        return deviceQrCodeManager.existedByBatchNo(batchNo);
    }

    /// 批量绑定商户(可覆盖已绑定归属)
    @Transactional(rollbackFor = Exception.class)
    public void bindMerchant(DeviceQrCodeBindMerchantParam param) {
        // 应用解析: appId 空则取商户默认应用
        String appId = resolveAppId(param.getMchNo(), param.getAppId());
        deviceQrCodeManager.bindMerchant(param.getIds(), param.getMchNo(), appId);
    }

    /// 批量解绑商户(回空白库存, 保留编码/批次/金额配置)
    @Transactional(rollbackFor = Exception.class)
    public void unbindMerchant(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            // 码牌: 请选择码牌
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.idsEmpty");
        }
        deviceQrCodeManager.unbindMerchant(ids);
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

    /// 获取码牌 H5 扫码链接
    ///
    /// 拼接规则: {paymentGatewayBaseUrl}/code-pay/{code}
    public String getCodeLink(String code) {
        deviceQrCodeManager.findByCode(code)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        String gatewayBase = urlConfig.getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            // 支付网关前端地址未配置
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.common.gatewayUrlNotConfigured");
        }
        return gatewayBase + CODE_PAY_PATH + code;
    }

    /// 应用解析: 复用 [MerchantContextLoader#resolveApp], appId 空取商户默认应用, 返回解析后的 appId
    private String resolveAppId(String mchNo, String appId) {
        MchAppInfoAccessInfo mchApp = merchantContextLoader.resolveApp(mchNo, appId);
        return mchApp.getAppId();
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

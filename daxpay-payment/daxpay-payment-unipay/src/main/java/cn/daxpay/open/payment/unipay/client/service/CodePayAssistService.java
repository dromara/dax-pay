package cn.daxpay.open.payment.unipay.client.service;

import cn.daxpay.open.payment.common.access.MerchantAccessInfo;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import cn.daxpay.open.payment.merchant.service.access.MerchantAccessQueryService;
import cn.daxpay.open.payment.merchant.service.gateway.ClientEnvPayResolveService;
import cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayService;
import cn.daxpay.open.payment.unipay.client.result.CodePayInfoResult;
import cn.daxpay.open.payment.unipay.param.device.CodePayParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 码牌支付编排(公开/H5/小程序侧)
///
/// - 查询: 按编码返回脱敏码牌信息
/// - 支付: 读应用聚合配置解析 method → 建普通订单(source=cashier_code) → [NormalPayService]
@Slf4j
@Service
@RequiredArgsConstructor
public class CodePayAssistService {

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MerchantAccessQueryService merchantAccessQueryService;
    private final MerchantContextLoader merchantContextLoader;
    private final ClientEnvPayResolveService clientEnvPayResolveService;
    private final NormalPayService normalPayService;

    /// 根据码牌编码查询支付信息(公开接口, 脱敏返回)
    ///
    /// 校验码牌存在、启用、已分配商户, 且商户存在。
    public CodePayInfoResult getByCode(String code) {
        DeviceQrCode entity = this.loadEnabledAssigned(code);
        // 查商户并校验存在
        MerchantAccessInfo merchant = merchantAccessQueryService.getMerchantByMchNo(entity.getMchNo());
        if (merchant == null) {
            // 码牌: 商户不存在
            throw new DataNotExistException("error.device.qrcode.mchNotFound");
        }
        return new CodePayInfoResult()
                .setCode(entity.getCode())
                .setName(entity.getName())
                .setAmountType(entity.getAmountType())
                .setFixedAmount(entity.getFixedAmount());
    }

    /// 码牌发起支付: 普通订单 + source=cashier_code, 通道路由复用聚合配置
    public NormalPayResult pay(CodePayParam param) {
        DeviceQrCode entity = this.loadEnabledAssigned(param.getCode());
        merchantContextLoader.initMch(entity.getMchNo());
        // 应用: 码牌 appId 空则商户默认应用
        var mchApp = merchantContextLoader.resolveApp(entity.getMchNo(), entity.getAppId());

        long amount = this.resolveAmount(entity, param.getAmount());
        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(param.getClientEnv());
        // browser 不支持一码通扫
        if (clientEnv == ClientEnvEnum.BROWSER) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.clientEnvNotSupport");
        }
        ClientRuntimeEnum runtime = ClientRuntimeEnum.ofOrDefault(param.getRuntime());
        // 无聚合配置时 AUTO 降级
        var resolved = clientEnvPayResolveService.resolve(mchApp.getAppId(), clientEnv, runtime);

        String codeName = StrUtil.blankToDefault(entity.getName(), entity.getCode());
        // 元展示: amount 为分
        String amountYuan = String.format("%.2f", amount / 100.0);
        String title = StrUtil.format("{} 码牌收款: {}元", codeName, amountYuan);

        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(entity.getMchNo());
        payParam.setAppId(mchApp.getAppId());
        // 平台生成商户业务单号, 每笔扫码收款独立
        payParam.setBizOrderNo(TradeNoGenerateUtil.order());
        payParam.setTitle(title);
        payParam.setDescription(param.getDescription());
        payParam.setAmount(amount);
        payParam.setMethod(resolved.method());
        payParam.setChannelMchNo(resolved.channelMchNo());
        payParam.setCapability(resolved.capability());
        payParam.setOpenId(param.getOpenId());
        payParam.setClientIp(StrUtil.blankToDefault(param.getClientIp(), WebServletUtil.getClientIp()));
        // 码牌来源
        payParam.setSource(TradeSourceEnum.CASHIER_CODE.getCode());

        return normalPayService.pay(payParam);
    }

    /// 加载启用且已划拨的码牌
    private DeviceQrCode loadEnabledAssigned(String code) {
        DeviceQrCode entity = deviceQrCodeManager.findByCode(code)
                // 码牌: 码牌不存在
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        // 码牌停用
        if (!QrCodeStatusEnum.ENABLED.getCode().equals(entity.getStatus())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.disabled");
        }
        // 空白码未划拨商户
        if (StrUtil.isBlank(entity.getMchNo())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.notAssigned");
        }
        return entity;
    }

    /// 固定金额取码牌配置; 自定义金额取入参并校验
    private long resolveAmount(DeviceQrCode entity, Long requestAmount) {
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(entity.getAmountType());
        if (amountType == QrCodeAmountTypeEnum.FIXED) {
            if (entity.getFixedAmount() == null || entity.getFixedAmount() <= 0) {
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.fixedAmountInvalid");
            }
            return entity.getFixedAmount();
        }
        // random
        if (requestAmount == null || requestAmount <= 0) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.amountRequired");
        }
        return requestAmount;
    }
}

package cn.daxpay.open.payment.unipay.client.service;

import cn.daxpay.open.payment.common.access.MerchantAccessInfo;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.device.enums.QrCodeAmountTypeEnum;
import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.merchant.service.access.MerchantAccessQueryService;
import cn.daxpay.open.payment.merchant.service.gateway.CodePayResolveService;
import cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayService;
import cn.daxpay.open.payment.unipay.client.result.CodePayInfoResult;
import cn.daxpay.open.payment.unipay.param.device.CodePayParam;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 码牌支付编排(公开/H5/小程序侧)
///
/// - 查询: 按编码返回脱敏码牌信息(含 programType)
/// - 支付: 读**码牌支付策略**解析 method(不读聚合配置); payForm 由 programType 映射
@Slf4j
@Service
@RequiredArgsConstructor
public class CodePayAssistService {

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MerchantAccessQueryService merchantAccessQueryService;
    private final MerchantContextLoader merchantContextLoader;
    private final CodePayResolveService codePayResolveService;
    private final NormalPayService normalPayService;

    /// 根据码牌编码查询支付信息(公开接口, 脱敏返回)
    public CodePayInfoResult getByCode(String code) {
        DeviceQrCode entity = this.loadEnabledAssigned(code);
        MerchantAccessInfo merchant = merchantAccessQueryService.getMerchantByMchNo(entity.getMchNo());
        if (merchant == null) {
            throw new DataNotExistException("error.device.qrcode.mchNotFound");
        }
        return new CodePayInfoResult()
                .setCode(entity.getCode())
                .setName(entity.getName())
                .setAmountType(entity.getAmountType())
                .setFixedAmount(entity.getFixedAmount())
                .setProgramType(entity.getProgramType());
    }

    /// 码牌发起支付: 普通订单 + source=cashier_code; 策略仅读码牌配置
    public NormalPayResult pay(CodePayParam param) {
        DeviceQrCode entity = this.loadEnabledAssigned(param.getCode());
        merchantContextLoader.initMch(entity.getMchNo());
        var mchApp = merchantContextLoader.resolveApp(entity.getMchNo(), entity.getAppId());

        long amount = this.resolveAmount(entity, param.getAmount());
        ClientEnvEnum clientEnv = ClientEnvEnum.findByCode(param.getClientEnv());
        if (clientEnv == ClientEnvEnum.BROWSER) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.clientEnvNotSupport");
        }

        // 策略形态由码牌 programType 决定, 不信任客户端擅自换形态
        CodePayFormEnum payForm = CodePayFormEnum.fromProgramType(entity.getProgramType());
        validateRuntimeMatchesPayForm(param.getRuntime(), payForm);

        var resolved = codePayResolveService.resolveRequired(mchApp.getAppId(), clientEnv, payForm);

        String codeName = StrUtil.blankToDefault(entity.getName(), entity.getCode());
        String amountYuan = String.format("%.2f", amount / 100.0);
        String title = StrUtil.format("{} 码牌收款: {}元", codeName, amountYuan);

        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(entity.getMchNo());
        payParam.setAppId(mchApp.getAppId());
        payParam.setBizOrderNo(TradeNoGenerateUtil.order());
        payParam.setTitle(title);
        payParam.setDescription(param.getDescription());
        payParam.setAmount(amount);
        payParam.setMethod(resolved.method());
        payParam.setChannelMchNo(resolved.channelMchNo());
        payParam.setCapability(resolved.capability());
        payParam.setOpenId(param.getOpenId());
        payParam.setClientIp(StrUtil.blankToDefault(param.getClientIp(), WebServletUtil.getClientIp()));
        payParam.setSource(TradeSourceEnum.CASHIER_CODE.getCode());

        return normalPayService.pay(payParam);
    }

    /// 请求 runtime 非空时须与 programType 映射的 payForm 一致
    private void validateRuntimeMatchesPayForm(String runtime, CodePayFormEnum payForm) {
        if (StrUtil.isBlank(runtime)) {
            return;
        }
        ClientRuntimeEnum reqRuntime;
        try {
            reqRuntime = ClientRuntimeEnum.findByCode(runtime);
        } catch (Exception e) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.codeRuntimeMismatch");
        }
        boolean expectMini = payForm == CodePayFormEnum.MINI;
        boolean reqMini = reqRuntime == ClientRuntimeEnum.MINI;
        if (expectMini != reqMini) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.codeRuntimeMismatch");
        }
    }

    private DeviceQrCode loadEnabledAssigned(String code) {
        DeviceQrCode entity = deviceQrCodeManager.findByCode(code)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        if (!QrCodeStatusEnum.ENABLED.getCode().equals(entity.getStatus())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.disabled");
        }
        if (StrUtil.isBlank(entity.getMchNo())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.notAssigned");
        }
        return entity;
    }

    private long resolveAmount(DeviceQrCode entity, Long requestAmount) {
        QrCodeAmountTypeEnum amountType = QrCodeAmountTypeEnum.findByCode(entity.getAmountType());
        if (amountType == QrCodeAmountTypeEnum.FIXED) {
            if (entity.getFixedAmount() == null || entity.getFixedAmount() <= 0) {
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.fixedAmountInvalid");
            }
            return entity.getFixedAmount();
        }
        if (requestAmount == null || requestAmount <= 0) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.qrcode.amountRequired");
        }
        return requestAmount;
    }
}

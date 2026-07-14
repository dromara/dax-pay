package cn.daxpay.open.payment.unipay.client.service;

import cn.daxpay.open.payment.device.enums.QrCodeStatusEnum;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.unipay.client.result.CodePayInfoResult;
import cn.daxpay.open.payment.common.access.MerchantAccessInfo;
import cn.daxpay.open.payment.merchant.service.access.MerchantAccessQueryService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 码牌支付编排(公开/H5 侧)
///
/// 一期: 仅提供按编码查询码牌信息(供 H5 扫码展示)。
/// 二期: 扩展 OAuth 重定向获取 openId 与发起 JSAPI 支付(复用 ChannelAuthService/NormalPayService)。
@Slf4j
@Service
@RequiredArgsConstructor
public class CodePayAssistService {

    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MerchantAccessQueryService merchantAccessQueryService;

    /// 根据码牌编码查询支付信息(公开接口, 脱敏返回)
    ///
    /// 校验码牌存在、启用、已分配商户, 且商户存在。
    public CodePayInfoResult getByCode(String code) {
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
        // 查商户并校验存在(启用校验留待二期支付链路 NormalPayService 统一处理)
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
}

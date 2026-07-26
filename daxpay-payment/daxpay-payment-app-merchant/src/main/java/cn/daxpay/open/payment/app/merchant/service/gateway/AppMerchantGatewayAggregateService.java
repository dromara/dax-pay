package cn.daxpay.open.payment.app.merchant.service.gateway;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayAggregateConfigParam;
import cn.daxpay.open.payment.merchant.result.appinfo.MchAppInfoResult;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayAggregateConfigResult;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayAggregateConfigService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-聚合扫码配置服务
///
/// 转发至 core [GatewayAggregateConfigService]；写操作强制当前上下文 mchNo。
@Service
@RequiredArgsConstructor
public class AppMerchantGatewayAggregateService {

    private final GatewayAggregateConfigService gatewayAggregateConfigService;
    private final MchAppInfoService mchAppInfoService;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 按应用查询聚合配置
    public GatewayAggregateConfigResult findByAppId(String appId) {
        // 校验 appId 属于当前商户
        mchAppInfoService.findByAppId(appId);
        return gatewayAggregateConfigService.findByAppId(appId);
    }

    /// 保存或更新聚合配置
    public void saveOrUpdate(GatewayAggregateConfigParam param) {
        MchAppInfoResult app = mchAppInfoService.findByAppId(param.getAppId());
        // 强制当前商户号，忽略客户端传入
        param.setMchNo(app.getMchNo() != null ? app.getMchNo() : requireMchNo());
        gatewayAggregateConfigService.saveOrUpdate(param);
    }
}

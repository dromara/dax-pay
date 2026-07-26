package cn.daxpay.open.payment.app.admin.service.masterdata.provider;

import cn.daxpay.open.payment.admin.service.masterdata.provider.PayProviderService;
import cn.daxpay.open.payment.masterdata.result.provider.PayProviderGroupResult;
import cn.daxpay.open.payment.masterdata.result.provider.PayProviderMethodResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 运营移动端-支付渠道服务
///
/// 转发至 [PayProviderService]
@Service
@RequiredArgsConstructor
public class AppAdminPayProviderService {

    private final PayProviderService payProviderService;

    /// 按支付渠道分组，返回各渠道下的支付方式
    public List<PayProviderGroupResult> listByProvider() {
        return payProviderService.listByProvider();
    }

    /// 切换支付渠道启停
    public void switchEnabled(String product, Boolean enabled) {
        payProviderService.switchEnabled(product, enabled);
    }

    /// 按支付渠道编码与支付方式编码查一条配置详情
    public PayProviderMethodResult get(String providerCode, String methodCode) {
        return payProviderService.get(providerCode, methodCode);
    }
}

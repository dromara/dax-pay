package cn.daxpay.open.payment.app.merchant.service.gateway;

import cn.daxpay.open.payment.admin.service.merchant.gateway.GatewayCashierConfigService;
import cn.daxpay.open.payment.merchant.param.gateway.GatewayCashierItemParam;
import cn.daxpay.open.payment.merchant.result.gateway.GatewayCashierItemResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 商户移动端-收银台配置服务
///
/// 转发至 [GatewayCashierConfigService]
@Service
@RequiredArgsConstructor
public class AppMerchantGatewayCashierService {

    private final GatewayCashierConfigService gatewayCashierConfigService;

    /// 查询收银台支付项列表
    public List<GatewayCashierItemResult> list(String appId, String cashierType, String clientEnv) {
        return gatewayCashierConfigService.list(appId, cashierType, clientEnv);
    }

    /// 新建收银台支付项
    public void save(GatewayCashierItemParam param) {
        gatewayCashierConfigService.save(param);
    }

    /// 更新收银台支付项
    public void update(GatewayCashierItemParam param) {
        gatewayCashierConfigService.update(param);
    }

    /// 删除收银台支付项
    public void delete(Long id) {
        gatewayCashierConfigService.delete(id);
    }
}

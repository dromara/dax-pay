package cn.daxpay.open.payment.app.admin.service.masterdata.product;

import cn.daxpay.open.payment.admin.service.masterdata.product.PayProductConfigService;
import cn.daxpay.open.payment.masterdata.param.product.PayProductConfigParam;
import cn.daxpay.open.payment.masterdata.result.product.PayProductConfigResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 运营移动端-支付产品配置服务
///
/// 转发至 [PayProductConfigService]
@Service
@RequiredArgsConstructor
public class AppAdminPayProductConfigService {

    private final PayProductConfigService payProductConfigService;

    /// 全量列表
    public List<PayProductConfigResult> listAll() {
        return payProductConfigService.listAll();
    }

    /// 切换环境
    public void switchEnv(String product, Boolean sandbox) {
        payProductConfigService.switchEnv(product, sandbox);
    }

    /// 切换启停(2026-09-13 启停入口随配置页迁移)
    public void switchEnabled(String product, Boolean enabled) {
        payProductConfigService.switchEnabled(product, enabled);
    }

    /// 保存配置
    public void saveOrUpdate(PayProductConfigParam param) {
        payProductConfigService.saveOrUpdate(param);
    }
}

package cn.daxpay.open.payment.appadmin.service.masterdata.product;

import cn.daxpay.open.payment.admin.service.masterdata.product.PayProductConfigService;
import cn.daxpay.open.payment.masterdata.constants.product.param.PayProductConfigParam;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductConfigResult;
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

    /// 保存配置
    public void saveOrUpdate(PayProductConfigParam param) {
        payProductConfigService.saveOrUpdate(param);
    }
}

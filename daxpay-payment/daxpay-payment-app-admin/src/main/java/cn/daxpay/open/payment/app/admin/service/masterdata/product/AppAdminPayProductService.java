package cn.daxpay.open.payment.app.admin.service.masterdata.product;

import cn.daxpay.open.payment.masterdata.constants.product.param.PayProductQuery;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductResult;
import cn.daxpay.open.payment.masterdata.constants.product.service.PayProductService;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 运营移动端-支付产品服务
///
/// 转发至 [PayProductService]
@Service
@RequiredArgsConstructor
public class AppAdminPayProductService {

    private final PayProductService payProductService;

    /// 分页
    public PageResult<PayProductResult> page(PageParam pageParam, PayProductQuery query, String name) {
        return payProductService.page(pageParam, query, name);
    }

    /// 按编码查详情
    public PayProductResult findByCode(String code) {
        return payProductService.findByCode(code);
    }

    /// 启用产品下拉
    public List<LabelValue> dropdown() {
        return payProductService.dropdown();
    }

    /// 切换启停
    public void switchEnabled(String product, Boolean enabled) {
        payProductService.switchEnabled(product, enabled);
    }

    /// 全量列表
    public List<PayProductResult> listAll() {
        return payProductService.listAll();
    }
}

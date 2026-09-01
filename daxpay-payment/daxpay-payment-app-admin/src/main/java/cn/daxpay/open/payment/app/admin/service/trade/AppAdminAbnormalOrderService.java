package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.AbnormalOrderAdminService;
import cn.daxpay.open.payment.trade.abnormal.param.AbnormalOrderQuery;
import cn.daxpay.open.payment.trade.abnormal.result.AbnormalOrderResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-异常订单服务
///
/// 转发至 [AbnormalOrderAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminAbnormalOrderService {

    private final AbnormalOrderAdminService abnormalOrderAdminService;

    /// 分页查询
    public PageResult<AbnormalOrderResult> page(PageParam pageParam, AbnormalOrderQuery query) {
        return abnormalOrderAdminService.page(pageParam, query);
    }

    /// 详情查询
    public AbnormalOrderResult findById(Long id) {
        return abnormalOrderAdminService.findById(id);
    }

    /// 确认成功(订单翻转为支付成功并补发通知)
    public void confirmSuccess(Long id, String remark) {
        abnormalOrderAdminService.confirmSuccess(id, remark);
    }

    /// 忽略(核实无需入账)
    public void ignore(Long id, String remark) {
        abnormalOrderAdminService.ignore(id, remark);
    }
}

package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.abnormal.convert.AbnormalOrderConvert;
import cn.daxpay.open.payment.trade.abnormal.dao.AbnormalOrderManager;
import cn.daxpay.open.payment.trade.abnormal.entity.AbnormalOrder;
import cn.daxpay.open.payment.trade.abnormal.param.AbnormalOrderQuery;
import cn.daxpay.open.payment.trade.abnormal.result.AbnormalOrderResult;
import cn.daxpay.open.payment.trade.abnormal.service.AbnormalOrderService;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 异常订单管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AbnormalOrderAdminService {

    private final AbnormalOrderManager abnormalOrderManager;
    private final AbnormalOrderService abnormalOrderService;
    private final TransService transService;

    /// 分页查询
    public PageResult<AbnormalOrderResult> page(PageParam pageParam, AbnormalOrderQuery query) {
        Page<AbnormalOrder> page = abnormalOrderManager.page(pageParam, query);
        var records = page.getRecords().stream().map(AbnormalOrderConvert.CONVERT::toResult).toList();
        PageResult<AbnormalOrderResult> pageResult = new PageResult<AbnormalOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public AbnormalOrderResult findById(Long id) {
        AbnormalOrder abnormal = abnormalOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.abnormal.notFound"));
        AbnormalOrderResult result = AbnormalOrderConvert.CONVERT.toResult(abnormal);
        transService.translate(result);
        return result;
    }

    /// 确认成功(人工核实通道已收款, 订单翻转为 SUCCESS 并补发通知)
    public void confirmSuccess(Long id, String remark) {
        abnormalOrderService.confirmSuccess(id, currentHandler(), remark);
    }

    /// 忽略(核实无需入账)
    public void ignore(Long id, String remark) {
        abnormalOrderService.ignore(id, currentHandler(), remark);
    }

    /// 当前操作人账号(留痕)
    private String currentHandler() {
        return SecurityUtil.getCurrentUser().map(UserDetail::getAccount).orElse("system");
    }
}

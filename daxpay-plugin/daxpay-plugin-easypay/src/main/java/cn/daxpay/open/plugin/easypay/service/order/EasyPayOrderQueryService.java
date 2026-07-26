package cn.daxpay.open.plugin.easypay.service.order;

import cn.daxpay.open.payment.trade.order.service.NormalPayOrderService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.param.order.EasyPayOrderQuery;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayOrderResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付订单管理端查询服务
///
/// 承接运营端/商户端管理后台的易支付订单管理入口：分页、详情、同步、关单。
/// 同步/关单透传内核 [cn.daxpay.open.payment.trade.order.service.NormalPayOrderService]。
/// 生命周期回写(支付成功/退款/关单钩子) 见 [EasyPayOrderService], 与本类解耦以避免循环依赖。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayOrderQueryService {

    private final EasyPayOrderManager easyPayOrderManager;
    private final NormalPayOrderService normalPayOrderService;
    private final TransService transService;
    private final ClientCodeService clientCodeService;

    /// 分页查询
    public PageResult<EasyPayOrderResult> page(PageParam pageParam, EasyPayOrderQuery query) {
        sanitizeQuery(query);
        Page<EasyPayOrder> page = easyPayOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(EasyPayOrder::toResult)
                .toList();
        var pageResult = new PageResult<EasyPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        translateIfAdmin(pageResult);
        return pageResult;
    }

    /// 详情查询
    public EasyPayOrderResult findById(Long id) {
        EasyPayOrder entity = easyPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        EasyPayOrderResult result = entity.toResult();
        translateIfAdmin(result);
        return result;
    }

    /// 同步支付状态(透传内核普通支付订单同步)
    public NormalPaySyncResult sync(Long id) {
        EasyPayOrder entity = easyPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        if (entity.getOrderId() == null) {
            throw new DataNotExistException("pay.error.payOrderNotExist");
        }
        return normalPayOrderService.sync(entity.getOrderId());
    }

    /// 关闭/撤销订单(透传内核普通支付订单关单)
    public void close(Long id, boolean useCancel) {
        EasyPayOrder entity = easyPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        if (entity.getOrderId() == null) {
            throw new DataNotExistException("pay.error.payOrderNotExist");
        }
        normalPayOrderService.close(entity.getOrderId(), useCancel);
    }

    private void sanitizeQuery(EasyPayOrderQuery query) {
        if (query == null) {
            return;
        }
        if (ClientEnum.MERCHANT.getCode().equals(clientCodeService.getClientCode())) {
            // 商户端强制按当前商户过滤
            query.setMchNo(null);
        }
    }

    private void translateIfAdmin(Object target) {
        if (ClientEnum.ADMIN.getCode().equals(clientCodeService.getClientCode())) {
            // 翻译商户名称(mchNo -> mchName)
            transService.translate(target);
        }
    }
}

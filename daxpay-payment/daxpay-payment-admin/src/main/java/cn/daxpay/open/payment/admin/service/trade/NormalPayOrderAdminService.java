package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.convert.NormalPayOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.trade.order.service.TradeOrderDetailAssembler;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 普通支付业务单管理服务
///
/// 运营端专属。跨商户查询，按 mchNo 翻译商户名称。
@Service
@RequiredArgsConstructor
public class NormalPayOrderAdminService {

    private final NormalPayOrderManager normalPayOrderManager;
    private final PaySyncService paySyncService;
    private final TransService transService;
    private final TradeOrderDetailAssembler tradeOrderDetailAssembler;

    /// 分页查询
    public PageResult<NormalPayOrderResult> page(PageParam pageParam, NormalPayOrderQuery query) {
        Page<NormalPayOrder> page = normalPayOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(NormalPayOrderConvert.CONVERT::toResult)
                .toList();
        PageResult<NormalPayOrderResult> pageResult = new PageResult<NormalPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询(联表资金凭证补充交易字段)
    public NormalPayOrderResult findById(Long id) {
        NormalPayOrder entity = normalPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        NormalPayOrderResult result = NormalPayOrderConvert.CONVERT.toResult(entity);
        tradeOrderDetailAssembler.fillFundOnNormal(result, id);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 同步支付状态(传入业务订单ID)
    public NormalPaySyncResult sync(Long id) {
        return paySyncService.syncByContainer(id, PayTradeTypeEnum.NORMAL.getCode());
    }
}

package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.merchant.convert.trade.MchPayTradeConvert;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 资金交易凭证查询(商户端)
///
/// 仅分页列表；行级隔离依赖 [MchBaseEntity] + 商户端 PaymentContext / TenantLine。
/// 禁止信任请求体中的 mchNo（查询条件亦不作为跨商户入口）。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchPayTradeService {

    private final PayTradeManager payTradeManager;

    /// 分页查询本商户资金交易
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        // 忽略入参 mchNo，避免越权指定他商户
        if (query != null) {
            query.setMchNo(null);
        }
        Page<PayTrade> page = payTradeManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(MchPayTradeConvert.CONVERT::toResult)
                .toList();
        return new PageResult<PayTradeResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }
}

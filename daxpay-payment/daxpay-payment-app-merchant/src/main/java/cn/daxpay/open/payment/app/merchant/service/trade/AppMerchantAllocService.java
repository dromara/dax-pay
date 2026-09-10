package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchAllocService;
import cn.daxpay.open.payment.trade.alloc.param.AllocOrderQuery;
import cn.daxpay.open.payment.trade.alloc.result.AllocDetailResult;
import cn.daxpay.open.payment.trade.alloc.result.AllocOrderResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 商户移动端-分账订单服务
///
/// 转发至 [MchAllocService]。仅查询与同步, 不做发起分账。
@Service
@RequiredArgsConstructor
public class AppMerchantAllocService {

    private final MchAllocService mchAllocService;

    /// 分页查询
    public PageResult<AllocOrderResult> page(PageParam pageParam, AllocOrderQuery query) {
        return mchAllocService.page(pageParam, query);
    }

    /// 详情查询
    public AllocOrderResult findById(Long id) {
        return mchAllocService.findById(id);
    }

    /// 分账明细分页
    public List<AllocDetailResult> details(String allocNo) {
        return mchAllocService.details(allocNo);
    }

    /// 同步分账状态
    public void sync(String allocNo) {
        mchAllocService.sync(allocNo);
    }
}

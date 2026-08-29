package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.flow.convert.FundFlowConvert;
import cn.daxpay.open.payment.trade.flow.dao.FundFlowManager;
import cn.daxpay.open.payment.trade.flow.entity.FundFlow;
import cn.daxpay.open.payment.trade.flow.param.FundFlowQuery;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 资金流水管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class FundFlowAdminService {

    private final FundFlowManager fundFlowManager;
    private final TransService transService;

    /// 分页查询
    public PageResult<FundFlowResult> page(PageParam pageParam, FundFlowQuery query) {
        Page<FundFlow> page = fundFlowManager.page(pageParam, query);
        var records = page.getRecords().stream().map(FundFlowConvert.CONVERT::toResult).toList();
        PageResult<FundFlowResult> pageResult = new PageResult<FundFlowResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public FundFlowResult findById(Long id) {
        FundFlow flow = fundFlowManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.fundFlow.notFound"));
        FundFlowResult result = FundFlowConvert.CONVERT.toResult(flow);
        transService.translate(result);
        return result;
    }
}

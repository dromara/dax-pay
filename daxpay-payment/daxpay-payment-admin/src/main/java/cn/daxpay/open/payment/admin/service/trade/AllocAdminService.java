package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.alloc.convert.AllocOrderConvert;
import cn.daxpay.open.payment.trade.alloc.dao.AllocDetailManager;
import cn.daxpay.open.payment.trade.alloc.dao.AllocOrderManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.param.AllocOrderQuery;
import cn.daxpay.open.payment.trade.alloc.result.AllocCreateResult;
import cn.daxpay.open.payment.trade.alloc.result.AllocDetailResult;
import cn.daxpay.open.payment.trade.alloc.result.AllocOrderResult;
import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocStartService;
import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocSyncService;
import cn.daxpay.open.payment.trade.alloc.param.AllocParam;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 分账订单管理服务(运营端)
///
/// 运营端专属。跨商户查询(传 mchNo 过滤), 可代商户发起分账。
/// 发起/同步编排委托 core 运行时服务。
@Service
@RequiredArgsConstructor
public class AllocAdminService {

    private final AllocOrderManager allocOrderManager;
    private final AllocDetailManager allocDetailManager;
    private final AllocStartService allocStartService;
    private final AllocSyncService allocSyncService;
    private final TransService transService;

    /// 分账订单分页
    public PageResult<AllocOrderResult> page(PageParam pageParam, AllocOrderQuery query) {
        Page<AllocOrder> page = allocOrderManager.page(pageParam, query);
        PageResult<AllocOrderResult> pageResult = toPageResult(page, AllocOrderConvert.CONVERT::toResult);
        transService.translate(pageResult);
        return pageResult;
    }

    /// 分账订单详情(含明细)
    public AllocOrderResult findById(Long id) {
        AllocOrder entity = allocOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.alloc.allocNotFound"));
        AllocOrderResult result = AllocOrderConvert.CONVERT.toResult(entity);
        // 明细列表单独装配
        result.setDetails(AllocOrderConvert.CONVERT.toDetailResults(
                allocDetailManager.findAllByAllocNo(entity.getAllocNo())));
        transService.translate(result);
        return result;
    }

    /// 分账明细列表
    public List<AllocDetailResult> details(String allocNo) {
        return AllocOrderConvert.CONVERT.toDetailResults(
                allocDetailManager.findAllByAllocNo(allocNo));
    }

    /// 发起分账(运营端代发, mchNo 必传)
    ///
    /// @return 平台分账单号
    public AllocCreateResult create(AllocParam param) {
        // 运营端无商户上下文, 代发必须显式指定商户号, 提前拦截避免误报"未找到商户配置"
        if (StrUtil.isBlank(param.getMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "validation.field.mchNo.notBlank");
        }
        String allocNo = allocStartService.start(param);
        return new AllocCreateResult()
                .setAllocNo(allocNo)
                .setBizAllocNo(param.getBizAllocNo());
    }

    /// 同步分账状态
    public void sync(String allocNo) {
        allocSyncService.sync(allocNo);
    }

    /// 分页实体 → 分页结果
    private <T, R> PageResult<R> toPageResult(Page<T> page, java.util.function.Function<T, R> mapper) {
        var records = page.getRecords().stream().map(mapper).toList();
        return new PageResult<R>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }
}

package org.dromara.daxpay.payment.pay.service.reconcile;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.payment.unipay.result.reconcile.ReconcileDownResult;
import org.dromara.daxpay.payment.pay.dao.reconcile.ReconcileStatementManager;
import org.dromara.daxpay.payment.pay.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.payment.pay.param.reconcile.ReconcileStatementQuery;
import org.dromara.daxpay.payment.pay.result.reconcile.ReconcileStatementResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/// # 对账查询服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileStatementQueryService {
    private final ReconcileStatementManager statementManager;

    /// 分页
    public PageResult<ReconcileStatementResult> page(PageParam pageParam, ReconcileStatementQuery query){
        return MpUtil.toPageResult(statementManager.page(pageParam, query));
    }

    /// 明细
    public ReconcileStatementResult findById(Long id){
        return statementManager.findById(id).map(ReconcileStatement::toResult)
                .orElseThrow(()->new DataNotExistException("error.payment.reconcile.reconcileOrderNotExist"));
    }

    /// 通道原始对账单下载链接
    public ReconcileDownResult getChannelDownUrl(String product, LocalDate date){
        // 首先查询今天所有的对账单, 然后查询已经下载和比对完成的对账单, 获取他的通道对账文件链接
        var url = statementManager.findByProductAndData(product,date).stream()
                .filter(o->o.isDownOrUpload()&&o.isCompare())
                .map(ReconcileStatement::getChannelFileUrl)
                .findFirst()
                .orElseThrow(()->new DataNotExistException("error.payment.reconcile.channelStatementUrlNotFound"));
        return new ReconcileDownResult().setFileUrl(url);
    }

    /// 平台对账单下载链接
    public ReconcileDownResult getPlatformDownUrl(String product, LocalDate date){
        // 首先查询今天所有的对账单, 然后查询已经下载和比对完成的对账单, 获取他的平台对账文件链接
        var url = statementManager.findByProductAndData(product,date).stream()
                .filter(o->o.isDownOrUpload()&&o.isCompare())
                .map(ReconcileStatement::getPlatformFileUrl)
                .findFirst()
                .orElseThrow(()->new DataNotExistException("error.payment.reconcile.platformStatementUrlNotFound"));
        return new ReconcileDownResult().setFileUrl(url);
    }
}

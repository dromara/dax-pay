package org.dromara.daxpay.service.service.reconcile;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.core.result.reconcile.ReconcileDownResult;
import org.dromara.daxpay.service.dao.reconcile.ReconcileStatementManager;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.service.param.reconcile.ReconcileStatementQuery;
import org.dromara.daxpay.service.result.reconcile.ReconcileStatementResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 对账查询服务
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileStatementQueryService {
    private final ReconcileStatementManager statementManager;

    /**
     * 分页
     */
    public PageResult<ReconcileStatementResult> page(PageParam pageParam, ReconcileStatementQuery query){
        return MpUtil.toPageResult(statementManager.page(pageParam, query));
    }

    /**
     * 明细
     */
    public ReconcileStatementResult findById(Long id){
        return statementManager.findById(id).map(ReconcileStatement::toResult)
                .orElseThrow(()->new DataNotExistException("对账订单不存在"));
    }

    /**
     * 通道原始对账单下载链接
     */
    public ReconcileDownResult getChannelDownUrl(String channel, LocalDate date){
        // 首先查询今天所有的对账单, 然后查询已经下载和比对完成的对账单, 获取他的通道对账文件链接
        var url = statementManager.findByChannelAndData(channel,date).stream()
                .filter(o->o.isDownOrUpload()&&o.isCompare())
                .map(ReconcileStatement::getChannelFileUrl)
                .findFirst()
                .orElseThrow(()->new DataNotExistException("未找到通道对账单下载链接"));
        return new ReconcileDownResult().setFileUrl(url);
    }

    /**
     * 平台对账单下载链接
     */
    public ReconcileDownResult getPlatformDownUrl(String channel, LocalDate date){
        // 首先查询今天所有的对账单, 然后查询已经下载和比对完成的对账单, 获取他的平台对账文件链接
        var url = statementManager.findByChannelAndData(channel,date).stream()
                .filter(o->o.isDownOrUpload()&&o.isCompare())
                .map(ReconcileStatement::getPlatformFileUrl)
                .findFirst()
                .orElseThrow(()->new DataNotExistException("未找到平台对账单下载链接"));
        return new ReconcileDownResult().setFileUrl(url);
    }
}

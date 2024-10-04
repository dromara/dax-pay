package org.dromara.daxpay.service.service.record.close;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.dao.record.close.PayCloseRecordManager;
import org.dromara.daxpay.service.entity.record.close.PayCloseRecord;
import org.dromara.daxpay.service.param.record.PayCloseRecordQuery;
import org.dromara.daxpay.service.result.record.close.PayCloseRecordResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseRecordService {
    private final PayCloseRecordManager manager;


    /**
     * 根据id查询
     */
    public PayCloseRecordResult findById(Long id) {
        return manager.findById(id).map(PayCloseRecord::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页查询
     */
    public PageResult<PayCloseRecordResult> page(PageParam pageParam, PayCloseRecordQuery param){
        return MpUtil.toPageResult(manager.page(pageParam,param));
    }

    /**
     * 新开事务进行记录保存
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(PayCloseRecord record){
        manager.save(record);
    }
}

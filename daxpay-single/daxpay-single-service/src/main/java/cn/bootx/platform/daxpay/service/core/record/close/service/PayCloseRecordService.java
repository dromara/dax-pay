package cn.bootx.platform.daxpay.service.core.record.close.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.record.close.dao.PayCloseRecordManager;
import cn.bootx.platform.daxpay.service.core.record.close.entity.PayCloseRecord;
import cn.bootx.platform.daxpay.service.dto.record.close.PayCloseRecordDto;
import cn.bootx.platform.daxpay.service.param.record.PayCloseRecordQuery;
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
    public PayCloseRecordDto findById(Long id) {
        return manager.findById(id).map(PayCloseRecord::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页查询
     */
    public PageResult<PayCloseRecordDto> page(PageParam pageParam, PayCloseRecordQuery param){
        return MpUtil.convert2DtoPageResult(manager.page(pageParam,param));
    }

    /**
     * 新开事务进行记录保存
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(PayCloseRecord record){
        manager.save(record);
    }
}

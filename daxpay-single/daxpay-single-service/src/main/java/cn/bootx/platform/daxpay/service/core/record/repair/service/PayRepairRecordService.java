package cn.bootx.platform.daxpay.service.core.record.repair.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.record.repair.dao.PayRepairRecordManager;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import cn.bootx.platform.daxpay.service.dto.order.repair.PayRepairRecordDto;
import cn.bootx.platform.daxpay.service.param.record.PayRepairRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xxm
 * @since 2024/1/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRepairRecordService {
    private final PayRepairRecordManager repairRecordManager;

    /**
     * 根据id查询
     */
    public PayRepairRecordDto findById(Long id) {
        return repairRecordManager.findById(id).map(PayRepairRecord::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页查询
     */
    public PageResult<PayRepairRecordDto> page(PageParam pageParam, PayRepairRecordQuery param){
        return MpUtil.convert2DtoPageResult(repairRecordManager.page(pageParam,param));
    }

    /**
     * 保存记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRecord(PayRepairRecord record){
        repairRecordManager.save(record);
    }
}

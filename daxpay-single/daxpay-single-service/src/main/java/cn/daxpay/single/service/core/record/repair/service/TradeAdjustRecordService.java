package cn.daxpay.single.service.core.record.repair.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.service.core.record.repair.dao.TradeAdjustRecordManager;
import cn.daxpay.single.service.core.record.repair.entity.TradeAdjustRecord;
import cn.daxpay.single.service.dto.record.repair.TradeAdjustRecordDto;
import cn.daxpay.single.service.param.report.TradeAdjustRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 交易调整记录
 * @author xxm
 * @since 2024/7/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeAdjustRecordService {
    private final TradeAdjustRecordManager tradeAdjustRecordManager;

    /**
     * 根据id查询
     */
    public TradeAdjustRecordDto findById(Long id) {
        return tradeAdjustRecordManager.findById(id).map(TradeAdjustRecord::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页查询
     */
    public PageResult<TradeAdjustRecordDto> page(PageParam pageParam, TradeAdjustRecordQuery param){
        return MpUtil.convert2DtoPageResult(tradeAdjustRecordManager.page(pageParam,param));
    }

    /**
     * 保存记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRecord(TradeAdjustRecord record){
        tradeAdjustRecordManager.save(record);
    }
    /**
     * 保存记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveAllRecord(List<TradeAdjustRecord> records){
        tradeAdjustRecordManager.saveAll(records);
    }
}

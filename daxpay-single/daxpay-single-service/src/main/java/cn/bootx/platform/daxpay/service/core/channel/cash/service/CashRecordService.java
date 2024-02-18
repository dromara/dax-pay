package cn.bootx.platform.daxpay.service.core.channel.cash.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.channel.cash.dao.CashRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.cash.entity.CashRecord;
import cn.bootx.platform.daxpay.service.dto.channel.cash.CashRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.cash.CashRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 现金记录
 *
 * @author xxm
 * @since 2021/6/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashRecordService {
    private final CashRecordManager manager;

    /**
     * 分页查询
     */
    public PageResult<CashRecordDto> page(PageParam pageParam, CashRecordQuery param) {
        return MpUtil.convert2DtoPageResult(manager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public CashRecordDto findById(Long id){
        return manager.findById(id).map(CashRecord::toDto).orElseGet(CashRecordDto::new);
    }
}

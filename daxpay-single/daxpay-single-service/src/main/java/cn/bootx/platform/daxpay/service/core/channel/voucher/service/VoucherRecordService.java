package cn.bootx.platform.daxpay.service.core.channel.voucher.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.channel.voucher.dao.VoucherRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.VoucherRecord;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 储值卡记录
 * @author xxm
 * @since 2024/2/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherRecordService {
    private final VoucherRecordManager manager;


    /**
     * 分页查询
     */
    public PageResult<VoucherRecordDto> page(PageParam pageParam, VoucherRecordQuery param) {
        return MpUtil.convert2DtoPageResult(manager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public VoucherRecordDto findById(Long id){
        return manager.findById(id).map(VoucherRecord::toDto).orElseGet(VoucherRecordDto::new);
    }
}

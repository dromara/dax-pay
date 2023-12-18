package cn.bootx.platform.daxpay.core.channel.voucher.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherLogManager;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 储值卡日志
 *
 * @author xxm
 * @since 2022/3/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherLogService {

    private final VoucherLogManager voucherLogManager;

    /**
     * 储值卡日志分页
     */
    public PageResult<VoucherLogDto> pageByVoucherId(PageParam pageParam, Long voucherId){
        return MpUtil.convert2DtoPageResult(voucherLogManager.pageByVoucherId(pageParam,voucherId));
    }

}

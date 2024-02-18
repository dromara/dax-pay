package cn.bootx.platform.daxpay.service.core.channel.voucher.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.VoucherRecord;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/2/18
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherRecordManager extends BaseManager<VoucherRecordMapper, VoucherRecord> {

    /**
     * 分页
     */
    public Page<VoucherRecord> page(PageParam pageParam, VoucherRecordQuery param){
        Page<VoucherRecord> mpPage = MpUtil.getMpPage(pageParam, VoucherRecord.class);
        QueryWrapper<VoucherRecord> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }
}

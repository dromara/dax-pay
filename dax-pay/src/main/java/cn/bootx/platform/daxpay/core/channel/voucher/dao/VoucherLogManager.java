package cn.bootx.platform.daxpay.core.channel.voucher.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xxm
 * @since 2022/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherLogManager extends BaseManager<VoucherLogMapper, VoucherLog> {

    /**
     * 根据储值卡id进行分页
     */
    public Page<VoucherLog> pageByVoucherId(PageParam pageParam, Long voucherId) {
        Page<VoucherLog> mpPage = MpUtil.getMpPage(pageParam,VoucherLog.class);
        return lambdaQuery()
                .eq(VoucherLog::getVoucherId, voucherId)
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);

    }
}

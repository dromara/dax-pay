package cn.bootx.platform.daxpay.core.paymodel.voucher.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.paymodel.voucher.entity.VoucherLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xxm
 * @date 2022/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherLogManager extends BaseManager<VoucherLogMapper, VoucherLog> {

    /**
     * 根据支付id和类型进行查询
     */
    public List<VoucherLog> findByPaymentIdAndType(Long paymentId, int type) {
        return lambdaQuery().eq(VoucherLog::getPaymentId, paymentId).eq(VoucherLog::getType, type).list();
    }

}

package org.dromara.daxpay.payment.device.dao.qrcode.info;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.device.entity.qrcode.info.CashierCode;
import org.dromara.daxpay.payment.device.param.qrcode.info.CashierCodeQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2025/7/1
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashierCodeManager extends BaseManager<CashierCodeMapper, CashierCode> {

    /**
     * 分页
     */
    public Page<CashierCode> page(PageParam pageParam, CashierCodeQuery query) {
        Page<CashierCode> mpPage = MpUtil.getMpPage(pageParam, CashierCode.class);
        QueryWrapper<CashierCode> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /**
     * 判断批次号是否存在
     */
    @IgnoreTenant
    public boolean existedByBatchNo(String batchNo) {
        return existedByField(CashierCode::getBatchNo, batchNo);
    }

    /**
     * 根据编号查询
     */
    public Optional<CashierCode> findByCode(String code) {
        return findByField(CashierCode::getCode, code);
    }
    /**
     * 根据编号查询, 忽略租户
     */
    @IgnoreTenant
    public Optional<CashierCode> findByCodeNotTenant(String code) {
        return findByField(CashierCode::getCode, code);
    }

    /**
     * 更新数据, 忽略租户
     */
    @IgnoreTenant
    public int updateByIdNotTenant(CashierCode code){
        return this.updateById(code);
    }
}

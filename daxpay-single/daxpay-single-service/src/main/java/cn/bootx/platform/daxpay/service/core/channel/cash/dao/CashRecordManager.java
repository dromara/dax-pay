package cn.bootx.platform.daxpay.service.core.channel.cash.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.channel.cash.entity.CashRecord;
import cn.bootx.platform.daxpay.service.param.channel.cash.CashRecordQuery;
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
public class CashRecordManager extends BaseManager<CashRecordMapper, CashRecord> {

    /**
     * 分页
     */
    public Page<CashRecord> page(PageParam pageParam, CashRecordQuery param){
        Page<CashRecord> mpPage = MpUtil.getMpPage(pageParam, CashRecord.class);
        QueryWrapper<CashRecord> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }
}

package cn.daxpay.single.service.core.channel.wallet.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.channel.wallet.entity.WalletRecord;
import cn.daxpay.single.service.param.channel.wallet.WalletRecordQuery;
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
public class WalletRecordManager extends BaseManager<WalletRecordMapper, WalletRecord> {

    /**
     * 分页
     */
    public Page<WalletRecord> page(PageParam pageParam, WalletRecordQuery param){
        Page<WalletRecord> mpPage = MpUtil.getMpPage(pageParam, WalletRecord.class);
        QueryWrapper<WalletRecord> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }
}

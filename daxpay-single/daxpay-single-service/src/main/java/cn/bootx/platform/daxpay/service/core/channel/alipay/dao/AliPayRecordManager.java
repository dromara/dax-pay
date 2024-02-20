package cn.bootx.platform.daxpay.service.core.channel.alipay.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayRecord;
import cn.bootx.platform.daxpay.service.param.channel.alipay.AliPayRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/2/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AliPayRecordManager extends BaseManager<AliPayRecordMapper, AliPayRecord> {


    /**
     * 分页
     */
    public Page<AliPayRecord> page(PageParam pageParam, AliPayRecordQuery param){
        Page<AliPayRecord> mpPage = MpUtil.getMpPage(pageParam, AliPayRecord.class);
        QueryWrapper<AliPayRecord> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }
}

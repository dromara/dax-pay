package cn.bootx.platform.daxpay.service.core.channel.union.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayRecord;
import cn.bootx.platform.daxpay.service.param.channel.union.UnionPayRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xxm
 * @since 2022/3/11
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UnionPayRecordManager extends BaseManager<UnionPayRecordMapper, UnionPayRecord> {

    /**
     * 分页
     */
    public Page<UnionPayRecord> page(PageParam pageParam, UnionPayRecordQuery param){
        Page<UnionPayRecord> mpPage = MpUtil.getMpPage(pageParam, UnionPayRecord.class);
        QueryWrapper<UnionPayRecord> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }

    /**
     * 按时间范围查询
     */
    public List<UnionPayRecord> findByDate(LocalDateTime startDate, LocalDateTime endDate){
        return this.lambdaQuery()
                .between(UnionPayRecord::getGatewayTime, startDate, endDate)
                .list();
    }
}

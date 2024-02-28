package cn.bootx.platform.daxpay.service.core.channel.wechat.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayRecord;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WeChatPayRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/2/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WeChatPayRecordManager extends BaseManager<WeChatPayRecordMapper, WeChatPayRecord> {

    /**
     * 分页
     */
    public Page<WeChatPayRecord> page(PageParam pageParam, WeChatPayRecordQuery param){
        Page<WeChatPayRecord> mpPage = MpUtil.getMpPage(pageParam, WeChatPayRecord.class);
        QueryWrapper<WeChatPayRecord> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }

    /**
     * 按时间范围查询
     */
    public List<WeChatPayRecord> findByDate(LocalDateTime start, LocalDateTime end) {
        return this.lambdaQuery()
                .between(WeChatPayRecord::getGatewayTime, start, end)
                .list();
    }
}

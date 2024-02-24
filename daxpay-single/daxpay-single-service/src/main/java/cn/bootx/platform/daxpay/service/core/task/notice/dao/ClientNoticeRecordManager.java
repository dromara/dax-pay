package cn.bootx.platform.daxpay.service.core.task.notice.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.task.notice.entity.ClientNoticeRecord;
import cn.bootx.platform.daxpay.service.param.record.ClientNoticeRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/2/23
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ClientNoticeRecordManager extends BaseManager<ClientNoticeRecordMapper, ClientNoticeRecord> {

    /**
     * 分页
     */
    public Page<ClientNoticeRecord> page(PageParam pageParam, ClientNoticeRecordQuery query){
        QueryWrapper<ClientNoticeRecord> generator = QueryGenerator.generator(query);
        Page<ClientNoticeRecord> mpPage = MpUtil.getMpPage(pageParam, ClientNoticeRecord.class);

        return this.page(mpPage, generator);
    }

}

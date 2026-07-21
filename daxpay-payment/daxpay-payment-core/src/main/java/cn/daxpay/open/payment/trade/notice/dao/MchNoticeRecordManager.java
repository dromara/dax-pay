package cn.daxpay.open.payment.trade.notice.dao;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeRecord;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeRecordQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 商户出站通知发送记录管理
///
@Repository
public class MchNoticeRecordManager extends BaseManager<MchNoticeRecordMapper, MchNoticeRecord> {

    /// 分页查询
    public Page<MchNoticeRecord> page(PageParam pageParam, MchNoticeRecordQuery query) {
        Page<MchNoticeRecord> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<MchNoticeRecord> wrapper = QueryGenerator.generator(query);
        wrapper.lambda().orderByDesc(MchNoticeRecord::getId);
        return this.page(mpPage, wrapper);
    }
}

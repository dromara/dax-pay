package org.dromara.daxpay.platform.system.dao.protocol;

import org.dromara.daxpay.platform.system.entity.protocol.UserProtocolItem;
import org.dromara.daxpay.platform.system.param.protocol.UserProtocolItemQuery;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 用户协议项管理
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProtocolItemManager extends BaseManager<UserProtocolItemMapper, UserProtocolItem> {

    /// 分页
    public Page<UserProtocolItem> page(PageParam pageParam, UserProtocolItemQuery query){
        Page<UserProtocolItem> mpPage = MpUtil.getMpPage(pageParam, UserProtocolItem.class);
        QueryWrapper<UserProtocolItem> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }

    /// 根据协议ID查询明细，根据排序字段进行排序
    public List<UserProtocolItem> findAllByProtocolIdOrderBySortNo(Long protocolId) {
        return this.lambdaQuery()
                .eq(UserProtocolItem::getProtocolId, protocolId)
                .orderByAsc(UserProtocolItem::getSortNo)
                .list();
    }
}

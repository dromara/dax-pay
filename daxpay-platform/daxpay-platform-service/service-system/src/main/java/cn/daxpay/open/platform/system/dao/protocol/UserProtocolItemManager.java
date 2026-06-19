package cn.daxpay.open.platform.system.dao.protocol;

import cn.daxpay.open.platform.system.entity.protocol.UserProtocolItem;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolItemQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
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

package org.dromara.daxpay.payment.isv.dao.isv;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import org.dromara.daxpay.payment.isv.dao.info.IsvInfoMapper;
import org.dromara.daxpay.payment.isv.entity.info.IsvInfo;
import org.dromara.daxpay.payment.isv.param.isv.IsvInfoQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 服务商信息
 * @author xxm
 * @since 2024/10/29
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class IsvInfoManager extends BaseManager<IsvInfoMapper, IsvInfo> {


    /**
     * 根据服务商号查询
     */
    public Optional<IsvInfo> findByIsvNo(String isvNo) {
        return this.findByField(IsvInfo::getIsvNo, isvNo);
    }

    /**
     * 分页
     */
    public Page<IsvInfo> page(PageParam pageParam, IsvInfoQuery query) {
        Page<IsvInfo> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<IsvInfo> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /**
     * 根据状态查询所有
     */
    public List<IsvInfo> findAllByStatus(IsvStatusEnum status) {
        return findAllByField(IsvInfo::getStatus, status.getCode());

    }

    public boolean existedByIsvNo(String isvNo) {
        return existedByField(IsvInfo::getIsvNo, isvNo);
    }
}

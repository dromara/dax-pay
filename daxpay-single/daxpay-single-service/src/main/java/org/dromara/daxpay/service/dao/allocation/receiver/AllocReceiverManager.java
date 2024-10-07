package org.dromara.daxpay.service.dao.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.dromara.daxpay.service.param.allocation.receiver.AllocReceiverQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/10/6
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocReceiverManager extends BaseManager<AllocReceiverMapper, AllocReceiver> {

    /**
     * 分页
     */
    public Page<AllocReceiver> page(PageParam pageParam, AllocReceiverQuery param){
        Page<AllocReceiver> mpPage = MpUtil.getMpPage(pageParam, AllocReceiver.class);
        QueryWrapper<AllocReceiver> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }

    /**
     * 根据接收方编号查询
     */
    public Optional<AllocReceiver> findByReceiverNo(String receiverNo, String appId) {
        return lambdaQuery()
                .eq(AllocReceiver::getReceiverNo, receiverNo)
                .eq(MchAppBaseEntity::getAppId, appId)
                .oneOpt();
    }

    /**
     * 根据分账方编号查询列表
     */
    public List<AllocReceiver> findAllByReceiverNos(List<String> receiverNos, String appId) {
        return lambdaQuery()
                .eq(AllocReceiver::getReceiverNo, receiverNos)
                .eq(MchAppBaseEntity::getAppId, appId)
                .list();
    }

    /**
     * 根据所属通道查询
     */
    public List<AllocReceiver> findAllByChannel(String channel, String appId) {
        return lambdaQuery()
                .eq(AllocReceiver::getChannel, channel)
                .eq(MchAppBaseEntity::getAppId, appId)
                .list();
    }


    /**
     * 判断是否存在
     */
    public boolean existedByReceiverNo(String receiverNo, String appId) {
        return lambdaQuery()
                .eq(AllocReceiver::getReceiverNo, receiverNo)
                .eq(MchAppBaseEntity::getAppId, appId)
                .exists();
    }
}

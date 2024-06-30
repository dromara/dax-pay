package cn.daxpay.multi.service.dao.constant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.constant.ChannelConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelConstManager extends BaseManager<ChannelConstMapper, ChannelConst> {

    /**
     * 查询全部启用的通道
     */
    public List<ChannelConst> findAllByEnable() {
        return findAllByField(ChannelConst::isEnable, true);
    }
}

package cn.bootx.platform.daxpay.core.channel.union.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.channel.union.entity.UnionPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xxm
 * @since 2022/3/11
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UnionPayConfigManager extends BaseManager<UnionPayConfigMapper, UnionPayConfig> {

}

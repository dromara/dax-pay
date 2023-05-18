package cn.bootx.platform.daxpay.core.channel.union.service;

import cn.bootx.platform.daxpay.core.channel.union.dao.UnionPayConfigManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xxm
 * @date 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayConfigService {

    private final UnionPayConfigManager unionPayConfigManager;

}

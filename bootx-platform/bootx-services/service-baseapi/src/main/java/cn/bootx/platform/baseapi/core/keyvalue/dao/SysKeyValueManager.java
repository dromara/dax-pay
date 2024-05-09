package cn.bootx.platform.baseapi.core.keyvalue.dao;

import cn.bootx.platform.baseapi.core.keyvalue.entity.SysKeyValue;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xxm
 * @since 2022/3/30
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysKeyValueManager extends BaseManager<SysKeyValueMapper, SysKeyValue> {

}

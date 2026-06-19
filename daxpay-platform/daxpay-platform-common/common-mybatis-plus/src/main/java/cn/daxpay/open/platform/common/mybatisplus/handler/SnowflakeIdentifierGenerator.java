package cn.daxpay.open.platform.common.mybatisplus.handler;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;

/// # 雪花id生成器
///
@RequiredArgsConstructor
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        return IdUtil.getSnowflakeNextId();
    }

}

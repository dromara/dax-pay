package cn.bootx.platform.common.mybatisplus.handler;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;

/**
 * 雪花id生成器
 *
 * @author xxm
 * @since 2021/8/18
 */
@RequiredArgsConstructor
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        return IdUtil.getSnowflake().nextId();
    }

}

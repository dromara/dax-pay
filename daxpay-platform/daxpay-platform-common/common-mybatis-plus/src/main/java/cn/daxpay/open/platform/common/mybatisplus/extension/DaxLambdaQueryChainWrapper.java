package cn.daxpay.open.platform.common.mybatisplus.extension;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/// # 单条数据读取安全
///
/// 重写 [#one]：查询结果不唯一时记录告警(查全部，方言无关)，适用于期望唯一的场景。
/// 取"排序后第一条"的方言无关写法(只查 1 条、不校验唯一)见 [cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager#firstOpt]。
///
@Slf4j
public class DaxLambdaQueryChainWrapper<T> extends LambdaQueryChainWrapper<T> {
    private final Class<T> entityClass;
    public DaxLambdaQueryChainWrapper(BaseMapper<T> baseMapper, Class<T> entityClass) {
        super(baseMapper);
        this.entityClass = entityClass;
    }

    /// 获取单个, 如果获取到了多个, 记录告警
    ///
    /// @return 单个
    @Override
    public T one() {
        return execute(mapper -> {
            var wrapper = getWrapper();
            List<T> list = mapper.selectList(wrapper);
            if (list.isEmpty()) {
                return null;
            }
            if (list.size() > 1){
                log.warn("实体类:{}, 查询条件:{}, 查询值:{}的查询结果不是唯一值，需要进行排查", entityClass.getName(),  wrapper.getTargetSql(), wrapper.getParamNameValuePairs());
            }
            return list.getFirst();
        });
    }
}

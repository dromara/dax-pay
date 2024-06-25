package cn.bootx.platform.common.mybatisplus.query.generator;

import cn.bootx.platform.common.mybatisplus.query.entity.QueryParams;
import cn.bootx.platform.common.mybatisplus.query.entity.SortParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.experimental.UtilityClass;

/**
 * 简单查询生成器
 *
 * @author xxm
 * @since 2021/11/17
 */
@UtilityClass
public class QueryGenerator {

    /**
     * 根据接收的前端超级查询参数对象进行生成
     * @param queryParams 参数
     * @param <T> 泛型
     * @return 查询器
     */
    public static <T> QueryWrapper<T> generator(QueryParams queryParams) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 查询条件
        SuperQueryGenerator.initQueryParam(queryWrapper, queryParams.getQueryParams());
        // 排序条件
        SuperQueryGenerator.initQueryOrder(queryWrapper, queryParams.getQueryOrders());

        return queryWrapper;
    }

    /**
     * 根据查询对象数据和该类上标注的注解进行生成, 生成的多个查询条件之间用And连接
     * @param queryParams 参数对象
     * @param <T> 泛型
     * @return 查询器
     */
    public static <T> QueryWrapper<T> generator(Object queryParams, SortParam...queryOrder) {
        return AnnotationQueryGenerator.generator(queryParams,queryOrder);
    }

    /**
     * 根据查询对象数据，以及参数对象类和数据库Entity类上标注的注解进行生成, 生成的多个查询条件之间用And连接
     * @param queryParams 参数对象
     * @param clazz 数据库Entity类
     * @param <T> 泛型
     * @return 查询器
     */
    public static <T> QueryWrapper<T> generator(Object queryParams, Class<T> clazz) {
        return AnnotationQueryGenerator.generator(queryParams, clazz);
    }

}

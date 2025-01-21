package cn.bootx.platform.common.mybatisplus.query.generator;

import cn.bootx.platform.common.mybatisplus.query.code.ParamTypeEnum;
import cn.bootx.platform.common.mybatisplus.query.entity.QueryParam;
import cn.bootx.platform.core.exception.BizException;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * 参数值转换
 *
 * @author xxm
 * @since 2021/11/18
 */
public class ParamValueTypeConvert {

    /**
     * 查询条件值处理
     * @param queryParam 查询参数
     * @return 处理完的查询条件值
     */
    public static Object initQueryParamValue(QueryParam queryParam) {
        Object paramValue = queryParam.getParamValue();
        // 空值不进行处理
        if (Objects.isNull(paramValue)) {
            return null;
        }
        // 未传入参数类型原样返回
        if (StrUtil.isBlank(queryParam.getParamType())) {
            return paramValue;
        }
        ParamTypeEnum paramTypeEnum = Optional.ofNullable(ParamTypeEnum.getByCode(queryParam.getParamType()))
            .orElseThrow(() -> new BizException("不支持的数据类型"));
        return switch (paramTypeEnum) {
            // 原样返回
            case NUMBER, STRING, BOOLEAN, DATE, TIME, DATE_TIME -> convertType(paramValue, paramTypeEnum);
            case LIST -> {
                Collection<?> collection = (Collection<?>) paramValue;
                yield collection.stream()
                        .map(o -> convertType(o, paramTypeEnum))
                        .collect(toList());
            }
        };
    }

    /**
     * 数据类型解析
     * @param paramValue 参数
     * @param paramTypeEnum 参数类型
     * @return 解析完的数据
     */
    private static Object convertType(Object paramValue, ParamTypeEnum paramTypeEnum) {

        ParamTypeEnum typeEnum = Optional.ofNullable(ParamTypeEnum.getByCode(paramTypeEnum.getCode()))
            .orElseThrow(() -> new BizException("不支持的数据类型"));
        return switch (typeEnum) {
            // 原样返回
            case NUMBER, STRING, BOOLEAN -> paramValue;
            case DATE -> LocalDateTimeUtil.parseDate((String) paramValue, DatePattern.NORM_DATE_PATTERN);
            case TIME -> LocalDateTimeUtil.parse((String) paramValue, DatePattern.NORM_TIME_PATTERN)
                    .toLocalTime();
            case DATE_TIME -> LocalDateTimeUtil.parse((String) paramValue, DatePattern.NORM_DATETIME_PATTERN);
            case LIST -> paramValue;
        };
    }

}

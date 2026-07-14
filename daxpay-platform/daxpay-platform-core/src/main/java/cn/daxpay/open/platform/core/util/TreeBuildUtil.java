package cn.daxpay.open.platform.core.util;

import cn.hutool.core.collection.CollectionUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 构建数据树工具类
///
@UtilityClass
public class TreeBuildUtil {

    /// 构建成树
    /// @param list 要进行转换的列表
    /// @param pid 一级节点的父级id，通常为null
    /// @param getId 获取主键的方法方法引用
    /// @param getPid 获取关联父级节点主键的方法引用
    /// @param setChildren 设置子节点列表的方法引用
    public <T> List<T> build(List<T> list, Object pid, Function<T, Object> getId, Function<T, Object> getPid,
                             BiConsumer<T, List<T>> setChildren) {
        return build(list, pid, getId, getPid, setChildren, null);
    }

    /// 构建成树 (带排序)
    /// @param list 要进行转换的列表
    /// @param pid 一级节点的父级id，通常为null
    /// @param getId 获取主键的方法方法引用
    /// @param getPid 获取关联父级节点主键的方法引用
    /// @param setChildren 设置子节点列表的方法引用
    /// @param comparator 节点顺序的排序规则
    public <T> List<T> build(List<T> list, Object pid, Function<T, Object> getId, Function<T, Object> getPid,
                             BiConsumer<T, List<T>> setChildren, Comparator<? super T> comparator) {
        return build(list, pid, getId, getPid, setChildren, comparator, new HashSet<>());
    }

    /// 构建成树(带循环检测)
    private <T> List<T> build(List<T> list, Object pid, Function<T, Object> getId, Function<T, Object> getPid,
                              BiConsumer<T, List<T>> setChildren, Comparator<? super T> comparator,
                              Set<Object> visited) {
        List<T> children = list.stream().filter(m -> Objects.equals(getPid.apply(m), pid)).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(children)) {
            return new ArrayList<>(0);
        }
        for (T region : children) {
            Object id = getId.apply(region);
            if (!visited.add(id)) {
                continue;
            }
            List<T> childrenList = build(list, id, getId, getPid, setChildren, comparator, visited);
            setChildren.accept(region, childrenList);
        }
        // 排序
        if (Objects.nonNull(comparator)) {
            children.sort(comparator);
        }
        return children;
    }
}


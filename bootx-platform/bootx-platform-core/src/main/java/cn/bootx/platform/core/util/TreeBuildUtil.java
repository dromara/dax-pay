package cn.bootx.platform.core.util;

import cn.hutool.core.collection.CollectionUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 构建数据树工具类
 *
 * @author xxm
 * @since 2022/12/24
 */
@UtilityClass
public class TreeBuildUtil {

    /**
     * 构建成树
     * @param list 要进行转换的列表
     * @param pid 一级节点的父级id，通常为null
     * @param getId 获取主键的方法方法引用
     * @param getPid 获取关联父级节点主键的方法引用
     * @param setChildren 设置子节点列表的方法引用
     */
    public <T> List<T> build(List<T> list, Object pid, Function<T, Object> getId, Function<T, Object> getPid,
                             BiConsumer<T, List<T>> setChildren) {
        return build(list, pid, getId, getPid, setChildren, null);
    }

    /**
     * 构建成树 (带排序)
     * @param list 要进行转换的列表
     * @param pid 一级节点的父级id，通常为null
     * @param getId 获取主键的方法方法引用
     * @param getPid 获取关联父级节点主键的方法引用
     * @param setChildren 设置子节点列表的方法引用
     * @param comparator 节点顺序的排序规则
     */
    public <T> List<T> build(List<T> list, Object pid, Function<T, Object> getId, Function<T, Object> getPid,
                             BiConsumer<T, List<T>> setChildren, Comparator<? super T> comparator) {
        List<T> children = list.stream().filter(m -> Objects.equals(getPid.apply(m), pid)).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(children)) {
            return new ArrayList<>(0);
        }
        for (T region : children) {
            List<T> childrenList = build(list, getId.apply(region), getId, getPid, setChildren,comparator);
            setChildren.accept(region, childrenList);
        }
        // 排序
        if (Objects.nonNull(comparator)) {
            children.sort(comparator);
        }
        return children;
    }

    /**
     * 展开树为list列表
     *
     * @param list        要进行展开的列表
     * @param getChildren 获取子节点列表的方法引用
     * @return 展开后的结果列表
     */
    public <T> List<T> unfold(List<T> list, Function<T, List<T>> getChildren){
        return unfold(list,getChildren, new ArrayList<>());
    }

    /**
     * 展开树为list列表
     *
     * @param list        要进行展开的列表
     * @param getChildren 获取子节点列表的方法引用
     * @param result      用于存储展开后的列表对象
     * @return 展开后的结果列表
     */
    private  <T> List<T> unfold(List<T> list, Function<T, List<T>> getChildren, List<T> result){
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        for (T region : list) {
            unfold(getChildren.apply(region), getChildren, result);
            result.add(region);
        }
        return result;
    }
}

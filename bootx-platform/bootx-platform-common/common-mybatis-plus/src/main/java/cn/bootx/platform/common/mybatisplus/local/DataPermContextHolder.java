package cn.bootx.platform.common.mybatisplus.local;

import cn.bootx.platform.core.annotation.DataPermScope;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;
import java.util.Stack;

/**
 * 忽略鉴权数据上下文
 *
 * @author xxm
 * @since 2021/12/22
 */
public class DataPermContextHolder {

    private static final ThreadLocal<Stack<DataPermScope>> PERMISSION_LOCAL = new TransmittableThreadLocal<>();


    /**
     * 设置 数据权限控制注解
     */
    public static void push(DataPermScope permission) {
        Stack<DataPermScope> stack = Optional.ofNullable(PERMISSION_LOCAL.get())
                .orElse(new Stack<>());
        stack.push(permission);
        PERMISSION_LOCAL.set(stack);
    }

    /**
     * 获取 数据权限控制注解
     */
    public static DataPermScope peek() {
        return PERMISSION_LOCAL.get().peek();
    }

    /**
     * 弹出
     */
    public static void pop() {
        Stack<DataPermScope> stack = PERMISSION_LOCAL.get();
        if (CollUtil.isNotEmpty(stack)) {
            stack.pop();
        }
    }

    /**
     * 清除线程变量(数据权限控制和用户信息)
     */
    public static void clear() {
        PERMISSION_LOCAL.remove();
    }

}

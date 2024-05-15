package cn.bootx.platform.starter.auth.service;

/**
 * 路由拦截检查
 *
 * @author xxm
 * @since 2021/12/21
 */
public interface RouterCheck {

    /**
     * 排序
     */
    default int sortNo() {
        return 0;
    }

    /**
     * 路由
     */
    boolean check(Object handler);

}

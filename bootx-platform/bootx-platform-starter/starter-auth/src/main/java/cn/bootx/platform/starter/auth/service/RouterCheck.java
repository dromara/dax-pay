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
     * 检查是否通过鉴权
     */
    boolean check(Object handler);

}

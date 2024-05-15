package org.slf4j.impl;

import org.slf4j.spi.MDCAdapter;

/**
 * 静态 MDC 粘合剂 覆盖原始类
 *
 * @author xxm
 * @since 2021/8/4
 */
public class StaticMDCBinder {

    /**
     * The unique instance of this class.
     */
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    /**
     * Currently this method always returns an instance of {@link StaticMDCBinder}.
     */
    public MDCAdapter getMDCA() {
        return new TtlMDCAdapter();
    }

    public String getMDCAdapterClassStr() {
        return TtlMDCAdapter.class.getName();
    }

}

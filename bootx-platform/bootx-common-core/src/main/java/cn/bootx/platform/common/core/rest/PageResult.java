package cn.bootx.platform.common.core.rest;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页包装类
 *
 * @author xxm
 * @since 2020/4/21 14:37
 */
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -739437195275623607L;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    private long total = 0;

    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;

    /**
     * 当前页
     */
    private long current = 1;

    public List<T> getRecords() {
        return records;
    }

    public PageResult<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageResult<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getSize() {
        return size;
    }

    public PageResult<T> setSize(long size) {
        this.size = size;
        return this;
    }

    public long getCurrent() {
        return current;
    }

    public PageResult<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

}

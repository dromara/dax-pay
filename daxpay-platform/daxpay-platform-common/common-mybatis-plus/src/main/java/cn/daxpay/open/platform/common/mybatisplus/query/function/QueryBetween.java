package cn.daxpay.open.platform.common.mybatisplus.query.function;

/// # 查询生成器范围查询标识接口
///
public interface QueryBetween {

    /// 获取开始值
    Object getStart();

    /// 获取结束值
    Object getEnd();
}

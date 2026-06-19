package cn.daxpay.open.platform.capability.audit.log.entity;

import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.type.JsonbStringTypeHandler;
import cn.daxpay.open.platform.capability.audit.log.convert.LogConvert;
import cn.daxpay.open.platform.capability.audit.log.result.OperateLogResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 操作日志
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "starter_audit_operate_log", autoResultMap = true)
public class OperateLogDb extends MpIdEntity implements ToResult<OperateLogResult> {

    /// 操作模块
    private String title;

    /// 操作人员id
    private Long operateId;

    /// 操作人员账号
    private String account;

    /// 终端编码
    private String client;

    /// 浏览器类型
    private String browser;

    /// 操作系统
    private String os;

    /// 业务类型
    private String businessType;

    /// 请求方法
    private String method;

    /// 请求方式
    private String requestMethod;

    /// 请求url
    private String operateUrl;

    /// 操作ip
    private String operateIp;

    /// 操作地点
    private String operateLocation;

    /// 请求参数
    @TableField(typeHandler = JsonbStringTypeHandler.class)
    private String operateParam;

    /// 返回参数
    @TableField(typeHandler = JsonbStringTypeHandler.class)
    private String operateReturn;

    /// 操作状态（0正常 1异常）
    private Boolean success;

    /// 错误消息
    private String errorMsg;

    /// 操作时间 (UTC)
    private OffsetDateTime operateTime;

    @Override
    public OperateLogResult toResult() {
        return LogConvert.CONVERT.convert(this);
    }

}

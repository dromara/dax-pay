package cn.bootx.platform.starter.audit.log.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.starter.audit.log.convert.LogConvert;
import cn.bootx.platform.starter.audit.log.result.OperateLogResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author xxm
 * @since 2021/8/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("starter_audit_operate_log")
public class OperateLogDb extends MpIdEntity implements ToResult<OperateLogResult> {

    /** 操作模块 */
    private String title;

    /** 操作人员id */
    private Long operateId;

    /** 操作人员账号 */
    private String account;

    /** 业务类型 */
    private String businessType;

    /** 请求方法 */
    private String method;

    /** 请求方式 */
    private String requestMethod;

    /** 请求url */
    private String operateUrl;

    /** 操作ip */
    private String operateIp;

    /** 操作地点 */
    private String operateLocation;

    /** 请求参数 */
    private String operateParam;

    /** 返回参数 */
    private String operateReturn;

    /** 操作状态（0正常 1异常） */
    private Boolean success;

    /** 错误消息 */
    private String errorMsg;

    /** 操作时间 */
    private LocalDateTime operateTime;

    @Override
    public OperateLogResult toResult() {
        return LogConvert.CONVERT.convert(this);
    }

}

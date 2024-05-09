package cn.bootx.platform.starter.audit.log.core.mongo.entity;

import cn.bootx.platform.starter.audit.log.core.mongo.convert.LogConvert;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.starter.audit.log.dto.OperateLogDto;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author xxm
 * @since 2021/12/2
 */
@Data
@Accessors(chain = true)
@Document(collection = "starter_audit_operate_log")
public class OperateLogMongo implements EntityBaseFunction<OperateLogDto> {

    @Id
    private Long id;

    /** 操作模块 */
    private String title;

    /** 操作人员id */
    private Long operateId;

    /** 操作人员账号 */
    private String username;

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
    public OperateLogDto toDto() {
        return LogConvert.CONVERT.convert(this);
    }

}

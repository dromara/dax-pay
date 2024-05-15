package cn.bootx.platform.common.core.rest.param;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 时间日期范围
 * @author xxm
 * @since 2024/1/14
 */
@Getter
@Setter
@QueryParam(ignore = true)
public class DateRangeParam {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "开始时间")
    private LocalDate startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "结束时间")
    private LocalDate endTime;

    /**
     * 获取开始时间
     */
    public String startTime(){
       return startTime("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取开始时间
     */
    public String startTime(String format){
        return LocalDateTimeUtil.format(getStartTime().atStartOfDay(),format);
    }

    /**
     * 获取结束时间
     */
    public String endTime(){
        return endTime("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取结束日期
     */
    public String endTime(String format){
        return LocalDateTimeUtil.format(getEndTime().plusDays(1).atStartOfDay(),"format");
    }


    /**
     * 获取开始日期
     */
    public String startDate(){
       return startDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取开始日期
     */
    public String startDate(String format){
        return LocalDateTimeUtil.format(getStartTime(),format);
    }


    /**
     * 获取开始日期
     */
    public String endDate(){
        return startDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取结束日期
     */
    public String endDate(String format){
        return LocalDateTimeUtil.format(getEndTime(),format);
    }

}

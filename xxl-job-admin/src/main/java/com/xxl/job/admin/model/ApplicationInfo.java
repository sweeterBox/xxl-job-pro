package com.xxl.job.admin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xxl.job.admin.entity.Application;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

/**
 * @author sweeter
 * @date 2022/12/24
 */
@Data
public class ApplicationInfo implements Function<Application,ApplicationInfo> {

    private Long id;

    private String name;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    private List<String> instanceUrls;

    private Integer instanceAllSize;

    private Integer instanceHealthySize;

    /**
     * Applies this function to the given argument.
     *
     * @param application the function argument
     * @return the function result
     */
    @Override
    public ApplicationInfo apply(Application application) {
        ApplicationInfo info = new ApplicationInfo();
        BeanUtils.copyProperties(application, info);
        return info;
    }

}

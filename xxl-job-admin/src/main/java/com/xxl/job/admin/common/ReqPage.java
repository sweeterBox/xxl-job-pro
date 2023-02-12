package com.xxl.job.admin.common;

/**
 * @author sweeter
 * @date 2022/9/4
 */
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class ReqPage implements Serializable {

    @ApiModelProperty(value = "页码")
    @Min(value = 0, message = "表示第一页")
    private int page = 0;

    @ApiModelProperty(value = "每页大小")
    @Min(value = 5,message = "最小每页行数为5")
    private int size = 20;

    private String sort;

    private String order;

    public PageRequest createPageRequest() {
        this.page = Math.max(this.page, 0);
        if (StringUtils.isNotBlank(this.sort)) {
            if (StringUtils.isNotBlank(this.order)) {
                return PageRequest.of(this.page, this.size, Sort.Direction.fromString(this.order),this.sort);
            }else {
                return PageRequest.of(this.page, this.size, Sort.Direction.ASC,this.sort);
            }
        }
        return PageRequest.of(this.page, this.size);
    }

    public PageRequest createPageRequest(Sort sort) {
        this.page = Math.max(this.page, 0);
        return PageRequest.of(this.page, this.size, sort);
    }

}

package com.xxl.job.admin.common;

/**
 * @author sweeter
 * @description
 * @date 2022/9/4
 */
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
//import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class ReqPage implements Serializable {

    @ApiModelProperty(value = "页码")
   // @Min(value = 0, message = "表示第一页")
    private int page = 0;

    @ApiModelProperty(value = "每页大小")
  //  @Min(value = 10,message = "最小每页行数为10")
    private int size = 20;

    private String sort;

    private String order;

    public PageRequest createPageRequest() {
        page = Math.max(page, 0);
        if (StringUtils.isNotBlank(sort)) {
            if (StringUtils.isNotBlank(order)) {
                return PageRequest.of(page, size, Sort.Direction.fromString(order),sort);
            }else {
                return PageRequest.of(page, size, Sort.Direction.ASC,sort);
            }
        }
        return PageRequest.of(page, size);
    }

    public PageRequest createPageRequest(Sort sort) {
        page = Math.max(page, 0);
        return PageRequest.of(page, size, sort);
    }

}

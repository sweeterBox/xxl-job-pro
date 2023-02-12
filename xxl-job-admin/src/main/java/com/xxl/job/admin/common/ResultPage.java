package com.xxl.job.admin.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author sweeter
 * @date 2022/9/4
 */
@NoArgsConstructor
@Data
public class ResultPage<T> implements Serializable {

    @ApiModelProperty("当前页,初始值1")
    private Integer page = 0;

    @ApiModelProperty("当前页数据行数")
    private Integer size = 0;

    @ApiModelProperty("数据总行数")
    private Long total = 0L;

    private List<T> content = Collections.emptyList();


    /**
     * 将spring springframework.data的分页格式化
     * @param page @ org.springframework.data.domain.Page
     * @param content
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> ResultPage<T> of(Page<S> page, List<T> content) {
        Objects.requireNonNull(page);
        ResultPage<T> result = new ResultPage<>();
        if (Objects.nonNull(content)) {
            result.setContent(content);
        }
        result.setPage(page.getPageable().getPageNumber());
        result.setSize(content.size());
        result.setTotal(page.getTotalElements());
        return result;
    }

    public static <T> ResultPage<T> of(Page<T> page) {
        return ResultPage.of(page,page.getContent());
    }

    public ResultPage<T> peek(Consumer<T> action) {
        this.content = this.content.stream().peek(action).collect(Collectors.toList());
        return this;
    }

}

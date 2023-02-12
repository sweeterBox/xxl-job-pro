package com.xxl.job.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sweeter
 * @description
 * @date 2022/9/4
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthInfo {

    private String token;

    private Boolean success;

}

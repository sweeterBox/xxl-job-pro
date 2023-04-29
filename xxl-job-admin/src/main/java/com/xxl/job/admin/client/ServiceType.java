package com.xxl.job.admin.client;

import lombok.Getter;

/**
 * @author sweeter
 * @date 2022/9/11
 */
@Getter
public enum ServiceType {
    /**
     * 类型
     */
    CALLBACK("callback"),
    REGISTRY("registry"),
    ADDTASK("registry"),
    DEREGISTER("deregister");

    private String value;

    ServiceType(String value) {
        this.value = value;
    }

    public static ServiceType of(String value) {
        for (ServiceType serviceType : values()) {
            if (serviceType.value.equals(value)) {
                return serviceType;
            }
        }
        throw new IllegalArgumentException("invalid type, uri-mapping(" + value + ") not found.");
    }

}

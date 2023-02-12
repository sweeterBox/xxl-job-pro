package com.xxl.job.client.executor.model;

import com.xxl.job.client.executor.ClientHostType;
import com.xxl.job.utils.JsonUtils;
import lombok.Data;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


@Data
public class InstanceRegistry implements Serializable {

    private static final long serialVersionUID = 1000L;

    private String name = "spring-boot-application";

    /**
     * 中文名称
     */
    private String title = "UNKNOWN";

    /**
     * Should the registered urls be built with server.address or with hostname.
     */
    private ClientHostType clientHostType = ClientHostType.CANONICAL_HOST_NAME;

    /**
     * Path for computing the service-url to register with. If not specified, defaults to
     * "/"
     */
    private String contextPath;

    /**
     * Client service URL.
     */
    private String url;

    private boolean healthy = true;

    private boolean ephemeral = true;

    /**
     * 权重
     */
    private Double weight = 1.0;


    /**
     * Metadata that should be associated with this application
     */
    private Map<String, String> metadata = new LinkedHashMap<>();

    public InstanceRegistry(){

    }

    public InstanceRegistry(String name, String url, String title) {
        this.name = name;
        this.url = url;
        this.title = title;
    }

    @Override
    public String toString() {
        return JsonUtils.obj2Json(this);
    }
}

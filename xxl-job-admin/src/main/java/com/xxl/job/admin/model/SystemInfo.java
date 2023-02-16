package com.xxl.job.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sweeter
 * @date 2023/2/16
 */
@Data
public class SystemInfo {

    private String pid;

    private String osName;

    private Integer cpuCoreSize;

    private Integer totalThread;

    private String startTime;

    private MemoryUsage heapMemory;

    private MemoryUsage nonHeapMemory;

    private PhysicalMemory physicalMemory;

    private List<Space> spaces;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class MemoryUsage {

        private Long initMemorySize;

        private Long maxMemorySize;

        private Long usedMemorySize;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PhysicalMemory {

        private Long totalPhysicalMemorySize;

        private Long freePhysicalMemorySize;

        private Long usedPhysicalMemorySize;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Space {

        private String name;

        private Long totalSpace;

        private Long freeSpace;

        private Long usableSpace;
    }

}

package lyr.testbot.util;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Usage {

    private static final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final Runtime runtime = Runtime.getRuntime();

    public static double getSystemCpuUsage(){
        return osBean.getSystemCpuLoad();
    }

    public static double getProcessCpuUsage(){
        return osBean.getProcessCpuLoad();
    }

    public static long getProcessAvailableMemory(){
        return osBean.getCommittedVirtualMemorySize();
    }

    public static long getFreeMemory(){
        return osBean.getFreePhysicalMemorySize();
    }

    public static long getFreeSwapSpace(){
        return osBean.getFreeSwapSpaceSize();
    }

    public static long getTotalMemory(){
        return osBean.getFreePhysicalMemorySize();
    }

    public static long getTotalSwapSpace(){
        return osBean.getFreePhysicalMemorySize();
    }

    public static long getRuntimeTotalMemory(){
        return runtime.totalMemory();
    }

    public static long getRuntimeMaxMemory(){
        return runtime.maxMemory();
    }

    public static long getRuntimeFreeMemory(){
        return runtime.freeMemory();
    }
}

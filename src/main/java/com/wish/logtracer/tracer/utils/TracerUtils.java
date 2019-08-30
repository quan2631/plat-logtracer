package com.wish.logtracer.tracer.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class TracerUtils {

    private static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
    public static final String   EMPTY_STRING             = "";
    public static String       P_ID_CACHE                 = null;
    /**
     * 获取traceId
     */
    public static String get() {
        String traceId = inheritableThreadLocal.get();
        return traceId;
    }

    public static void set(String trace_id) {
        inheritableThreadLocal.set(trace_id);
    }

    public static void remove() {
        inheritableThreadLocal.remove();
    }
    public static String getInetAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * This method can be a better way under JDK9, but in the current JDK version, it can only be implemented in this way.
     *
     * In Mac OS , JDK6，JDK7，JDK8 ,it's OK
     * In Linux OS,JDK6，JDK7，JDK8 ,it's OK
     *
     * @return Process ID
     */
    public static String getPID() {
        //check pid is cached
        if (P_ID_CACHE != null) {
            return P_ID_CACHE;
        }
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();

        if (StringUtils.isBlank(processName)) {
            return StringUtils.EMPTY_STRING;
        }

        String[] processSplitName = processName.split("@");

        if (processSplitName.length == 0) {
            return StringUtils.EMPTY_STRING;
        }

        String pid = processSplitName[0];

        if (StringUtils.isBlank(pid)) {
            return StringUtils.EMPTY_STRING;
        }
        P_ID_CACHE = pid;
        return pid;
    }
}


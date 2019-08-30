package com.wish.logtracer.tracer.event;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: QUAN
 * @date: Created in 2019/8/30 16:33
 * @description: 利用log4j2的插件，将信息追加到日志中
 * @modified By:
 */
@Plugin(name = "commonMsgPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "commonMsg"})
public class CommonMsgPatternConverter extends LogEventPatternConverter {

    /**
     * 并发线程的map
     * key为当前线程，value为tokenId
     */
    private volatile static ConcurrentHashMap<Long, String> commonMsg = new ConcurrentHashMap<>();
    private static final CommonMsgPatternConverter INSTANCE =
            new CommonMsgPatternConverter("commonMsg", "commonMsg");

    public static CommonMsgPatternConverter newInstance(
            final String[] options) {
        return INSTANCE;
    }

    protected CommonMsgPatternConverter(String name, String style) {
        super(name, style);
    }

    @Override
    public void format(LogEvent logEvent, StringBuilder stringBuilder) {
        stringBuilder.append(CommonMsgEventFactory.getCommonMsg(logEvent));
    }

    public static void setCommonMsg(String msg) {
        commonMsg.put(Thread.currentThread().getId(), msg);
    }
    public static String getCommonMsg() { return commonMsg.get(Thread.currentThread().getId()); }
    public static void removeCommonMsg() {
        commonMsg.remove(Thread.currentThread().getId());
    }
}

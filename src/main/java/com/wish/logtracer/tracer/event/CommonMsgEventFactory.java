package com.wish.logtracer.tracer.event;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.common.utils.StringUtil;
import com.wish.logtracer.tracer.vo.CommonMsg;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringMap;

import java.util.List;
/**
 * @author: QUAN
 * @date: Created in 2019/8/30 16:33
 * @description: 确保本次请求和日志打印的线程一致
 * @modified By:
 */
public class CommonMsgEventFactory extends ReusableLogEventFactory implements LogEventFactory {

    @Override
    public LogEvent createEvent(String loggerName, Marker marker, String fqcn, Level level, Message message, List<Property> properties, Throwable t) {
        LogEvent event = super.createEvent(loggerName, marker, fqcn, level, message, properties, t);
        if (event instanceof MutableLogEvent) {
            StringMap contextData = ContextDataFactory.createContextData();
            contextData.putAll(event.getContextData());
            contextData.putValue("commonMsg", CommonMsgPatternConverter.getCommonMsg());
            ((MutableLogEvent) event).setContextData(contextData);
        }
        return event;
    }

    public static String getCommonMsg(LogEvent event) {
        String commonMsg = event.getContextData().getValue("commonMsg");
        if(StringUtil.isNotBlank(commonMsg)){
            return changeStr(commonMsg);
        }else{
            return "";
        }
    }

    private static String changeStr(String commonMsg){
        CommonMsg msg = new CommonMsg();
        msg.setCityId(commonMsg);
        msg.setChannel("WeiXin");
        msg.setSystemTime("2019-07-27:08:56:00");
        msg.setBusinessDate("2019-07-27");
        msg.setLoginId("123");
        msg.setThreadId(Thread.currentThread().getId()+ "");
        return JSON.toJSON(msg).toString();
    }

}

# A：日志追加公共部分

## 1.说明
```
1.实现功能为日志追加
2.此部分为公共部分
3.需放在项目中使用，可参考plat-base下的readme中C部分
```  
## 2.利用log4j2的插件，将信息追加到日志中
```
CommonMsgPatternConverter类实现。
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
``` 
## 3.确保本次请求和日志打印的线程一致
```
CommonMsgEventFactory类实现。
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
``` 
## 4.sofa全局过滤器，将当前线程和追加信息放到map，请求结束后移除
```
GlobalSofaFilter类实现。
@Override
public SofaResponse invoke(FilterInvoker invoker, SofaRequest request) throws SofaRpcException {
    System.out.println("start thread Id:" + Thread.currentThread().getId());
    Object oo = request.getMethodArgs()[0];
    String msg = oo.toString();
    CommonMsgPatternConverter.setCommonMsg(msg);
    SofaResponse response = invoker.invoke(request);
    CommonMsgPatternConverter.removeCommonMsg();
    System.out.println("end thread Id:" + Thread.currentThread().getId());
    return response;
}
``` 
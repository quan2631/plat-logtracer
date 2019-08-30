package com.wish.logtracer.tracer.filter;

import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.filter.AutoActive;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import com.wish.logtracer.tracer.event.CommonMsgPatternConverter;

/**

 */
/**
 * @author: QUAN
 * @date: Created in 2019/8/30 16:33
 * @description: sofa全局过滤器
 * 将当前线程和追加信息放到map，请求结束后移除
 * @modified By:
 */
@Extension("globalSofaFilter")
@AutoActive(providerSide = true)
public class GlobalSofaFilter extends Filter{

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
}

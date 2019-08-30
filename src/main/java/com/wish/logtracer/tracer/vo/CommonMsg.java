package com.wish.logtracer.tracer.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * @author: QUAN
 * @date: Created in 2019/8/30 16:33
 * @description: 公共信息对象
 * @modified By:
 */
@Getter
@Setter
public class CommonMsg {
    /**
     * 城市码
     */
    private String cityId;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 系统时间
     */
    private String systemTime;
    /**
     * 业务日期
     */
    private String businessDate;
    /**
     * 登录人ID
     */
    private String loginId;
    /**
     * 当前线程ID
     */
    private String threadId;
}

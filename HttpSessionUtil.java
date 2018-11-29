/*
 * @(#) HttpSessionUtil.java 
 * 
 * Copyright (c) 2015, SIMPO Technology. All Rights Reserved. SIMPO Technology. CONFIDENTIAL
 */
package com.aia.eservice.common.utility;

import javax.servlet.http.HttpServletRequest;

import com.aia.eservice.common.entity.frontend.wechat.WechatUserInfo;




/**
 * session工具类
 * 
 * @Description
 * @version 1.0
 * @since 2016年1月26日
 */
public class HttpSessionUtil {

    private static final String WECHAT_USER = "WECHAT_USER";


    /**
     * 获取当前登录openId
     * 
     * @since 2016年1月26日
     * @param request
     * @return
     */
    public static String getOpenId(HttpServletRequest request) {
    	WechatUserInfo wechatUser = (WechatUserInfo) request.getSession().getAttribute(WECHAT_USER);

        if (null != wechatUser && null != wechatUser.getOpenId()) {
            return wechatUser.getOpenId();
        } else {
            return "";
        }
    }

    /**
     * 设置当前登录用户对象
     * 
     * @since 2016年1月26日
     * @param request
     * @param userInfo
     */
    public static void setWechatUser(HttpServletRequest request, WechatUserInfo userInfo) {
        request.getSession().setAttribute(WECHAT_USER, userInfo);
    }
}

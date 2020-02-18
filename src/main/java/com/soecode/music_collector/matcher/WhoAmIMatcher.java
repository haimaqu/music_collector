package com.soecode.music_collector.matcher;

import com.soecode.wxtools.api.WxMessageMatcher;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.util.StringUtils;

/**
 * 其中，以下三个是接口，开发者可实现并构建自己的匹配器，拦截器，处理器。
 *
 * WxMessageMatcher （可以通过Matcher进行简单拦截）
 * WxMessageInterceptor（可以通过Interceptor进行高级拦截）
 * WxMessageHandler（核心-负责处理消息，并返回）
 * 此外，还有几个类需要注意一下
 *
 * WxConsts 类
 * 封装所有微信公众平台API的常量类型，包括接口请求路径，事件等。
 * WxConfig 类
 * 基本配置库。里面包含了AppId，AppSecret等信息。wx-tools已经提供了个基于内存管理的配置库。暂不支持自行拓展，如有需要持久化到数据库，需要自己实现。注意：配置库对于整个程序是单例的。
 * WxService 类
 * 微信统一的API Service入口，继承IService接口，所有接口都从这里调用。
 * WxErrorException 类
 * 微信异常
 * WxErrorExceptionHandler 接口
 * 开发者可自行实现该接口，处理微信异常。
 */
@SuppressWarnings("ALL")
public class WhoAmIMatcher implements WxMessageMatcher{

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @Override
    public boolean match(WxXmlMessage message) {
        if(StringUtils.isNotEmpty(message.getContent())){
            //noinspection AlibabaUndefineMagicConstant
            if("我是谁".equals(message.getContent())){
                return true;
            }
        }
        return false;
    }

}

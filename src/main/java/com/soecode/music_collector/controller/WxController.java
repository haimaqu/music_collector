package com.soecode.music_collector.controller;
import java.io.UnsupportedEncodingException;
import	java.net.URLEncoder;

import com.soecode.music_collector.constants.MenuKey;
import com.soecode.music_collector.handler.*;
import com.soecode.music_collector.http.HttpResult;
import com.soecode.music_collector.matcher.WhoAmIMatcher;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxMessageRouter;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;
import com.soecode.wxtools.util.xml.XStreamTransformer;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 微信开发中网页授权access_token与基础支持的access_token异同
 * https://blog.csdn.net/wlphyl/article/details/55214984
 */
@RestController
@RequestMapping("/wx")
public class WxController {

    private IService iService = new WxService();

    /**
     * 开发者通过检验signature对请求进行校验（下面有校验方式）。若确认此次GET请求来自微信服务器，
     * 请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。加密/校验流程如下：
     * 1）将token、timestamp、nonce三个参数进行字典序排序 2）将三个参数字符串拼接成一个字符串进行sha1加密
     * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    @GetMapping
    public String check(String signature, String timestamp, String nonce, String echostr) {
        if (iService.checkSignature(signature, timestamp, nonce, echostr)) {
            return echostr;
        }
        return null;
    }

    @PostMapping
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 创建一个路由器     实例化消息路由器，作用是将消息路由去匹配器，拦截器，处理器。
        WxMessageRouter router = new WxMessageRouter(iService);
        try {
            // 微信服务器推送过来的是XML格式(来自微信服务器的消息)
            WxXmlMessage wx = XStreamTransformer.fromXml(WxXmlMessage.class, request.getInputStream());
            System.out.println("消息：\n " + wx.toString());
            // 添加路由规则，只处理满足规则的消息，可以自定义匹配器，拦截器，处理器。
            //每条路由规则必须以next()或者end()结束。否则不生效。这个后续会讲到。
            //这里（58行）意思是，只接收TEXT类型的消息，交给DemoMatcher匹配器、DemoInterceptor拦截器、DemoHandler处理器处理。
            router.rule().msgType(WxConsts.XML_MSG_TEXT).matcher(new WhoAmIMatcher()).handler(new WhoAmIHandler()).end()
                    .rule().msgType(WxConsts.XML_MSG_TEXT).handler(ConfigHander.getInstance()).end()
                    .rule().event(WxConsts.EVT_CLICK).eventKey(MenuKey.HELP).handler(HelpDocHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.CHANGE_NEWS).handler(ChangeNewsHandler.getInstance()).next()

                    .rule().eventKey(MenuKey.HOT_SONG).handler(RankHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.TOP_500).handler(RankHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.NET_HOT_SONG).handler(RankHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.HUAYU_SONG).handler(RankHandler.getInstance()).next()
                    .rule().eventKey(MenuKey.XINAO_SONG).handler(RankHandler.getInstance()).end();
            // 把消息传递给路由器进行处理
            WxXmlOutMessage xmlOutMsg = router.route(wx);
            if (xmlOutMsg != null)
                out.print(xmlOutMsg.toXml());// 因为是明文，所以不用加密，直接返回给用户。

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    @PostMapping("/getuserinfo")
    public void getUserInfo(@RequestParam(value = "test",required = true) String test) throws UnsupportedEncodingException {

        System.out.println(test);
        System.out.println("ggggg");
        String url = "http://haimaqu.free.idcfengye.com/wx/getcallback";
//        url = URLEncoder.encode(url, "UTF-8");
        try {
            String oauthUrl = iService.oauth2buildAuthorizationUrl(url,WxConsts.OAUTH2_SCOPE_USER_INFO, "state");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

    // HttpResult
    @RequestMapping("getcallback")
    public void geCallBack(){

        try {
            WxOAuth2AccessTokenResult result = iService.oauth2ToGetAccessToken("code");
            System.out.println(result.getAccess_token());
            System.out.println(result.getOpenid());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }


}

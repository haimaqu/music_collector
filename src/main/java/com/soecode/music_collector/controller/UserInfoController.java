package com.soecode.music_collector.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soecode.music_collector.http.HttpResult;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxJsapiConfig;
import com.soecode.wxtools.bean.WxUserList;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserInfoController {

    private IService iService = new WxService();

    @RequestMapping("/getuserinfo")
    @ResponseBody
    public void getUserInfo(HttpServletResponse response, @RequestParam(value = "test", required = false) String test) throws IOException {

        System.out.println(test);
        String url = "http://haimaqu.free.idcfengye.com/user/getcallback";
//        url = URLEncoder.encode(url, "UTF-8");
        try {
            // 获取 URL_OAUTH2_GET_CODE ，即获取授权码，
            String oauthUrl = iService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, "javatest");
            System.out.println(oauthUrl);
            // 重定向到 oauthUrl ，里面REDIRECT_URI 是我的 "http://haimaqu.free.idcfengye.com/user/getcallback"地址
            //  这样可以在"http://haimaqu.free.idcfengye.com/user/getcallback" 拿到授权码
            response.sendRedirect(oauthUrl);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这里我本人手动受用session设置了，当第一次获取到 个人信息后，直接缓存，有效期为2小时，
     *
     * @param request
     * @param modelAndView
     * @return
     * @throws WxErrorException
     */
    @RequestMapping("/getcallback")
//    @ResponseBody
    public ModelAndView geCallBack(HttpServletRequest request, HttpSession  session, ModelAndView modelAndView) throws WxErrorException {

//        HttpSession session = request.getSession();
        String code = request.getParameter("code");
        WxUserList.WxUser user1 = (WxUserList.WxUser) session.getAttribute("user");
        if (user1 == null) {
            //  根据code获取返回消息集，之后通过特定方法获得  accessToken  和 openid
            //  获取授权码，记住，code只能使用一次，第二次使用的时候会报 （被使用过，则只能重新获取）
            // 通过code获取access_token接口，没有使用次数限制
            // https://blog.csdn.net/u010882234/article/details/66972327
            WxOAuth2AccessTokenResult result = iService.oauth2ToGetAccessToken(code);
            String accessToken = result.getAccess_token();
            String openId = result.getOpenid();

//            if (result.getAccess_token() == null || result.getAccess_token() == "") {
//                WxOAuth2AccessTokenResult result1 = iService.oauth2ToGetRefreshAccessToken(result.getRefresh_token());
//                accessToken = result1.getAccess_token();
//            }
            // 获取网页授权access_token　　 无每日次数限制
            // 刷新网页授权access_token　　 无
            // 网页授权获取用户信息　　 无
            // 通过accessToken 和 openid获取 用户信息
            WxUserList.WxUser user = iService.oauth2ToGetUserInfo(accessToken, new WxUserList.WxUser.WxUserGet(openId, WxConsts.LANG_CHINA));
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(3600 * 2);
            user1 = (WxUserList.WxUser) session.getAttribute("user");
        }
        modelAndView.addObject("user", user1);
        modelAndView.setViewName("user");
        return modelAndView;

    }

    @RequestMapping("/tests")
    public String testsimples() {
        return "tests";
    }

    @PostMapping("/test")
    @ResponseBody
    public HttpResult checkSignature(@RequestParam(value = "signUrl", required = true) String signUrl) throws Exception {
        List<String> jsApiList = new ArrayList<>();
        //需要用到哪些JS SDK API 就设置哪些
        jsApiList.add("chooseImage");//拍照或从手机相册中选图接口
        jsApiList.add("onMenuShareQZone");//获取“分享到QQ空间”按钮点击状态及自定义分享内容接口
        jsApiList.add("scanQRCode");// 扫描条形码
        //把config返回到前端进行js调用即可。     签名用的url必须是调用JS接口页面的完整URL。
        WxJsapiConfig config = iService.createJsapiConfig(signUrl, jsApiList);
//        String appid = WxConfig.getInstance().getAppId();
//        WxConfig.getInstance().getJsapiTicket();
        String date = JSON.toJSONString(config);

        return HttpResult.ok(date);
    }


    @ResponseBody
    @PostMapping("/api/searchs")
    public String tests(HttpServletResponse response) throws Exception {

//        List<User> list = new ArrayList<>();
//        User user = userService.findTopByOrderByIdDesc();
//        list.add(user);
//        String date = JSON.toJSONString(list);
//        sendJsonData(response, date);
        return null;
    }

    /**
     * 返回JOSN数据格式
     */
    protected void sendJsonData(HttpServletResponse response, String data)
            throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(data);
        out.flush();
        out.close();
    }

    /**
     * 返回JOSN数据格式
     */
    public static void responseJSON(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        if (object == null)
            return;
        String jsonStr = mapper.writeValueAsString(object);
        OutputStream out = response.getOutputStream();
        out.write(jsonStr.getBytes("UTF-8"));
        out.flush();
    }

    /**
     * 生成随机字符串
     */
    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成时间戳
     */
    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}

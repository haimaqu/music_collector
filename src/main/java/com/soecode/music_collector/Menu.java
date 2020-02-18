package com.soecode.music_collector;

import com.soecode.music_collector.constants.MenuKey;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxMenu;
import com.soecode.wxtools.exception.WxErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {

    public static void main(String[] args) {
        IService iService = new WxService();
        WxMenu menu = new WxMenu();
        List<WxMenu.WxMenuButton> btnList = new ArrayList<>();

        //飙升功能
        WxMenu.WxMenuButton btn1 = new WxMenu.WxMenuButton();
        btn1.setName("分类");
        // subList.addAll(Arrays.asList(btn1_1, btn1_2, btn1_3, btn1_4, btn1_5));
        // btn1.setSub_button(subList);
        List<WxMenu.WxMenuButton> subList = new ArrayList<>();
        WxMenu.WxMenuButton btn1_1 = new WxMenu.WxMenuButton();
        btn1_1.setType(WxConsts.MENU_BUTTON_CLICK);
        btn1_1.setKey(MenuKey.HOT_SONG);
        btn1_1.setName("飙升榜");
        WxMenu.WxMenuButton btn1_2 = new WxMenu.WxMenuButton();
        btn1_2.setType(WxConsts.MENU_BUTTON_CLICK);
        btn1_2.setKey(MenuKey.TOP_500);
        btn1_2.setName("TOP500");
        WxMenu.WxMenuButton btn1_3 = new WxMenu.WxMenuButton();
        btn1_3.setType(WxConsts.MENU_BUTTON_CLICK);
        btn1_3.setKey(MenuKey.NET_HOT_SONG);
        btn1_3.setName("网络红歌");
        WxMenu.WxMenuButton btn1_4 = new WxMenu.WxMenuButton();
        btn1_4.setType(WxConsts.MENU_BUTTON_CLICK);
        btn1_4.setKey(MenuKey.HUAYU_SONG);
        btn1_4.setName("华语新歌");
        WxMenu.WxMenuButton btn1_5 = new WxMenu.WxMenuButton();
        btn1_5.setType(WxConsts.MENU_BUTTON_CLICK);
        btn1_5.setKey(MenuKey.XINAO_SONG);
        btn1_5.setName("洗脑神曲");


        ///////////////////////////////
        //飙升功能
        WxMenu.WxMenuButton btn2 = new WxMenu.WxMenuButton();
        btn2.setName("多功能");

        List<WxMenu.WxMenuButton> subList2 = new ArrayList<>();
        WxMenu.WxMenuButton btn2_1 = new WxMenu.WxMenuButton();
        btn2_1.setType(WxConsts.MENU_SCANCODE_PUSH);
        btn2_1.setKey(MenuKey.SAO_MA);
        btn2_1.setName("微信扫码");

        WxMenu.WxMenuButton btn2_2 = new WxMenu.WxMenuButton();
        btn2_2.setType(WxConsts.MENU_LOCATION_SELECT);
        btn2_2.setKey(MenuKey.DILI_WEIZHI);
        btn2_2.setName("地理位置");

        WxMenu.WxMenuButton btn2_3 = new WxMenu.WxMenuButton();
        // MENU_BUTTON_VIEW       MENU_SCANCODE_WAITMSG
        btn2_3.setType(WxConsts.MENU_SCANCODE_WAITMSG);
        btn2_3.setKey(MenuKey.TIAO_XING_MA);
        btn2_3.setName("扫条形码");

        WxMenu.WxMenuButton btn2_4 = new WxMenu.WxMenuButton();
        btn2_4.setType(WxConsts.MENU_PIC_PHOTO_OR_ALBUM);
        btn2_4.setKey(MenuKey.PIC_PHOTO_OR_ALBUM);
        btn2_4.setName("选择照片");

        WxMenu.WxMenuButton btn2_5 = new WxMenu.WxMenuButton();
        btn2_5.setType(WxConsts.MENU_BUTTON_CLICK);
        btn2_5.setKey(MenuKey.CHANGE_NEWS);
        btn2_5.setName("换一组");

        WxMenu.WxMenuButton btn3 = new WxMenu.WxMenuButton();
        btn3.setName("帮助");
        List<WxMenu.WxMenuButton> subList3 = new ArrayList<>();
        WxMenu.WxMenuButton btn3_1 = new WxMenu.WxMenuButton();
        btn3_1.setType(WxConsts.MENU_BUTTON_VIEW);
        btn3_1.setKey(MenuKey.DENG_LU);
        btn3_1.setUrl("http://haimaqu.free.idcfengye.com/user/getuserinfo");
//        btn3_1.setUrl("http://www.baidu.com");
        btn3_1.setName("登录授权");

        WxMenu.WxMenuButton btn3_2 = new WxMenu.WxMenuButton();
        btn3_2.setType(WxConsts.MENU_BUTTON_VIEW);
        btn3_2.setKey(MenuKey.FU_JIA_GONG_NENG);
        btn3_2.setUrl("http://haimaqu.free.idcfengye.com/user/tests");
        btn3_2.setName("附加功能");

        WxMenu.WxMenuButton btn3_3 = new WxMenu.WxMenuButton();
        btn3_3.setType(WxConsts.MENU_BUTTON_CLICK);
        btn3_3.setKey(MenuKey.HELP);
        btn3_3.setName("帮助");


        subList.addAll(Arrays.asList(btn1_1, btn1_2, btn1_3, btn1_4, btn1_5));
        subList2.addAll(Arrays.asList(btn2_1, btn2_2,btn2_3, btn2_4, btn2_5));
        subList3.addAll(Arrays.asList(btn3_1,btn3_2,btn3_3));
        btn1.setSub_button(subList);
        btn2.setSub_button(subList2);
        btn3.setSub_button(subList3);

        //将三个按钮设置进btnList
        btnList.add(btn1);
        btnList.add(btn2);
        btnList.add(btn3);
        //设置进菜单类
        menu.setButton(btnList);
        //调用API即可
        try {
            //参数1--menu  ，参数2--是否是个性化定制。如果是个性化菜单栏，需要设置MenuRule
            iService.createMenu(menu, false);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}

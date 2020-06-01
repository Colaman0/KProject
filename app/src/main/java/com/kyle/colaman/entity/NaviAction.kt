package com.kyle.colaman.entity

/**
 * Author   : kyle
 * Date     : 2020/5/4
 * Function : 首页左侧导航栏内容
 */
sealed class NaviAction(val text: String) {

}

object ActionGuangchang : NaviAction("广场")
object ActionMain : NaviAction("首页")
object ActionWenda : NaviAction("问答")
object ActionXiangmu : NaviAction("项目")
object ActionGongzhonghao : NaviAction("公众号")
object ActionTixi : NaviAction("体系")
object ActionDaohang : NaviAction("导航")
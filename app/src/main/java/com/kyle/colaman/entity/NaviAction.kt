package com.kyle.colaman.entity

import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/5/4
 * Function : 首页左侧导航栏内容
 */
sealed class NaviAction(val text: String) : Serializable {

}

object ActionGuangchang : NaviAction("广场"), Serializable
object ActionMain : NaviAction("首页"), Serializable
object ActionWenda : NaviAction("问答"), Serializable
object ActionXiangmu : NaviAction("项目"), Serializable
object ActionGongzhonghao : NaviAction("公众号"), Serializable
object ActionTixi : NaviAction("体系"), Serializable
object ActionDaohang : NaviAction("导航"), Serializable
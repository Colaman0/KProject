package com.colaman.common.base.recyclerview

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/7/31
 *     desc   : 分页配置信息
 * </pre>
 */
data class PageConfig(val pageSize: Int = 20,
                      val preLoadSize: Int = 0)
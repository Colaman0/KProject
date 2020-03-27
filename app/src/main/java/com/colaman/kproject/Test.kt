package com.colaman.kproject

import com.colaman.kyle.common.expand.no
import com.colaman.kyle.common.expand.yes

/**
 *
 *     author : kyle
 *     time   : 2019/10/23
 *     desc   : d
 *
 */

fun main(args: Array<String>) {
    (null).yes {
        print("yes")
    }

    (null).no {
        print("yes")
    }
}


package com.kyle.colman.view.recyclerview;


import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;

import com.kyle.colman.impl.IDiffComparator;

import java.util.List;
import java.util.Objects;

/**
 * Create by kyle on 2018/9/3
 * Function : 通用的diffutil
 */
public class CommonDiffCallBack<T extends IDiffComparator> extends DiffUtil.Callback {
    List<T> oldDatas;
    List<T> newDatas;

    public CommonDiffCallBack(List<T> oldDatas, List<T> newDatas) {
        this.oldDatas = oldDatas;
        this.newDatas = newDatas;
    }

    @Override
    public int getOldListSize() {
        return oldDatas == null ? 0 : oldDatas.size();
    }

    @Override
    public int getNewListSize() {
        return newDatas == null ? 0 : newDatas.size();
    }

    /**
     * 先判断是不是同一个class
     * 再判断是不是同一个type 如果是多类型布局可以instance of MultiItemEntity，再去判断type，
     * 如果是普通布局，就直接通过key去判断，一般来说根据id判断就可以
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDatas.get(oldItemPosition).isItemSame(newDatas.get(newItemPosition));
    }

    /**
     * 这里直接用toString()来判断两个数据里的值是否相同，记得要重写toString()
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDatas.get(oldItemPosition).isUISame(newDatas.get(newItemPosition));
    }
}

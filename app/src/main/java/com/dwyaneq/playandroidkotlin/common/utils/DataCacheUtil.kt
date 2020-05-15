package com.dwyaneq.playandroidkotlin.common.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.lang.Exception
import java.math.BigDecimal

/**
 * Created by DWQ on 2020/5/15.
 * E-Mail:lomapa@163.com
 */
object DataCacheUtil {
    fun getTotalCacheSize(context: Context): String {
        var cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += context.externalCacheDir?.let { getFolderSize(it) }!!
        }
        return getFormatSize(cacheSize)
    }

    fun clearAllCache(context: Context): Boolean {
        var b = false
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            b = context.externalCacheDir?.let { deleteDir(it) }!!
        }
        return b
    }

    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children: Array<String>? = dir.list()
            children?.forEach {
                val success = deleteDir(File(dir, it))
                if (!success)
                    return false
            }
        }
        return dir.delete()
    }


    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList: Array<File>? = file.listFiles()
            fileList?.forEach {
                // 如果下面还有文件
                size += if (it.isDirectory) {
                    getFolderSize(it)
                } else {
                    it.length();
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }


    fun getFormatSize(size: Long): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return "0.00M";
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            var result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB";
        }

        val gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            var result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "M";
        }

        val teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB";
        }
        val result4 = BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }


}
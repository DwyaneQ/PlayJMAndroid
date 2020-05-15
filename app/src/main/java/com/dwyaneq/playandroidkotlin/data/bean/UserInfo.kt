package com.dwyaneq.playandroidkotlin.data.bean
import java.io.Serializable

/**
 */
data class UserInfo(var admin: Boolean = false,
                    var chapterTops: List<String> = listOf(),
                    var collectIds: MutableList<String> = mutableListOf(),
                    var email: String="",
                    var icon: String="",
                    var id: String="",
                    var nickname: String="",
                    var password: String="",
                    var token: String="",
                    var type: Int =0,
                    var username: String="") : Serializable{
    override fun toString(): String {
        return "UserInfo(admin=$admin, chapterTops=$chapterTops, collectIds=$collectIds, email='$email', icon='$icon', id='$id', nickname='$nickname', password='$password', token='$token', type=$type, username='$username')"
    }
}

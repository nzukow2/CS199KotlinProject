package com.example.thechallen_ge

import com.google.gson.annotations.SerializedName

enum class Role {
    @SerializedName("head")
    HEAD,

    @SerializedName("captain")
    CAPTAIN,

    @SerializedName("associate")
    ASSOCIATE,

    @SerializedName("assistant")
    ASSISTANT,

    TA,

    @SerializedName("developer")
    DEVELOPER
}

data class Staff(
        var name: String = "",
        var emai: String = "",
        var role: Role = Role.HEAD
)
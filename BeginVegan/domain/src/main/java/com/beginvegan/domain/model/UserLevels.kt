package com.beginvegan.domain.model

enum class UserLevels(val kr: String, val eng: String) {
    SEED("씨앗", "SEED"),
    ROOT("떡잎", "ROOT"),
    SPROUT("새싹", "SPROUT"),
    STEM("줄기", "STEM"),
    LEAF("잎사귀", "LEAF"),
    TREE("나무", "TREE"),
    FLOWER("꽃", "FLOWER"),
    FRUIT("열매", "FRUIT");

    companion object {
        fun getValues(): List<UserLevels> = values().toList()

        fun fromKr(value: String): UserLevels? {
            return values().find { it.kr == value }
        }

        fun fromEng(value: String): UserLevels? {
            return values().find { it.eng == value }
        }
    }
}
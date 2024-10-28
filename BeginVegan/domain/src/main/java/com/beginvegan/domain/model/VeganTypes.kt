package com.beginvegan.domain.model


enum class VeganTypes(val kr: String, val eng: String) {
    UNKNOWN("알고 있지 않아요","UNKNOWN"),
    VEGAN("비건","VEGAN"),
    LACTO_VEGETARIAN("락토 베지테리언","LACTO_VEGETARIAN"),
    OVO_VEGETARIAN("오보 베지테리언","OVO_VEGETARIAN"),
    LACTO_OVO_VEGETARIAN("락토 오보 베지테리언","LACTO_OVO_VEGETARIAN"),
    PASCATARIAN("페스코 베지테리언","PASCATARIAN"),
    POLLOTARIAN("폴로 베지테리언","POLLOTARIAN"),
    FLEXITARIAN("플렉시테리언","FLEXITARIAN");

    companion object {
        fun getValues(): MutableList<VeganTypes> = values().toMutableList()
        fun fromKr(value: String): VeganTypes? {
            return VeganTypes.values().find { it.kr == value }
        }

        fun fromEng(value: String): VeganTypes? {
            return VeganTypes.values().find { it.eng == value }
        }
    }

}

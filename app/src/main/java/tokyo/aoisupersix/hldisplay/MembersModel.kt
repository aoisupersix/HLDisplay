package tokyo.aoisupersix.hldisplay

/**
 * DBのモデルクラス
 */
data class DbModel(val members: MutableList<MemberModel>, val states: MutableList<StatusModel>)

/**
 * メンバー情報のモデルクラス
 */
data class MemberModel(val id: Int, val last_name: String, val first_name: String, val status: Int)

/**
 * ステータス情報のモデルクラス
 */
data class StatusModel(val name: String, val color: String)
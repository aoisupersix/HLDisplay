package tokyo.aoisupersix.hldisplay
/**
 * DBのモデル
 */
data class DbModel(val members: MutableList<MemberModel>, val states: MutableList<StatusModel>)

data class MemberModel(val name: String, val status: Int)

data class StatusModel(val name: String, val color: String)
package tokyo.aoisupersix.hldisplay
/**
 * DBのメンバーモデル
 */
data class MembersModel(val members: MutableList<MemberModel>)

data class MemberModel(val name: String, val status: Int)

data class StatesModel(val states: MutableList<StatusModel>)

data class StatusModel(val name: String, val color: String)
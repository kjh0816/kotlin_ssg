class MemberRepository {

    @JvmName("getMemberLastId1")
    private fun getMemberLastId(): Int{
        val memberLastId = readIntFromFile("data/member/memberLastId.txt", 0)

        return memberLastId
    }

    @JvmName("setMemberLastId1")
    private fun setMemberLastId(newId: Int){
        writeIntToFile("data/member/memberLastId.txt", newId)
    }


    // filePath로 객체가 구분되기 때문에,  getMemberFromFile이 호출될 때 filePath를 줘야한다.
    // 불러온 값은 json 데이터이므로, 변환해주어야한다.
    private fun getMemberFromFile(filePath: String): Member?{
        var jsonStr = readStrFromFile(filePath)

        if(jsonStr.isEmpty()){
            return null
        }


        val map = mapFromJson(jsonStr)

        // mapToJson 함수에서 이미 trim 과정을 거쳤다.
        val id = map["id"].toString().toInt()
        val loginId = map["loginId"].toString()
        val loginPw = map["loginPw"].toString()
        val regDate = map["regDate"].toString()
        val updateDate = map["updateDate"].toString()
        val name = map["name"].toString()
        val nickname = map["nickname"].toString()
        val cellphoneNo = map["cellphoneNo"].toString()
        val email = map["email"].toString()

        return Member(id, loginId, loginPw, regDate, updateDate, name, nickname, cellphoneNo, email)

    }

    @JvmName("getMembers1")
    private fun getMembers(): List<Member>{

        val members = mutableListOf<Member>()
        val memberLastId = getMemberLastId()
        for(id in 1 .. memberLastId){

            val member = getMemberFromFile("data/member/${id}.json")

            if(member != null){
                members.add(member)
            }

        }
        return members

    }






    fun join(loginId: String, loginPw: String, name: String, nickname: String, cellphoneNo: String, email: String) {

        val id = getMemberLastId() + 1
        setMemberLastId(id)
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val member = Member(id, loginId, loginPw, regDate, updateDate, name, nickname, cellphoneNo, email)

        writeStrInFile("data/member/${id}.json", member.toJson())

    }

    fun isUsableLoginId(loginId: String): Boolean {
        for(member in getMembers()){
            if(member.loginId == loginId){
                return false
            }
        }
        return true
    }

    fun getMemberByLoginId(loginId: String): Member? {
        for(member in getMembers()){
            if(member.loginId == loginId){
                return member
            }
        }
        return null

    }

    fun getMemberById(memberId: Int): Member? {
        for(member in getMembers()){
            if(member.id == memberId){
                return member
            }
        }
        return null

    }



}
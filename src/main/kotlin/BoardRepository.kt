class BoardRepository {


    @JvmName("getBoardLastId1")
    private fun getBoardLastId(): Int{
        val boardLastId = readIntFromFile("data/board/boardLastId.txt", 0)

        return boardLastId
    }


    @JvmName("setBoardLastId1")
    private fun setBoardLastId(newId: Int){
        writeIntToFile("data/board/boardLastId.txt", newId)
    }


    // filePath로 객체가 구분되기 때문에,  getMemberFromFile이 호출될 때 filePath를 줘야한다.
    // 불러온 값은 json 데이터이므로, 변환해주어야한다.
    private fun getBoardFromFile(filePath: String): Board?{
        var jsonStr = readStrFromFile(filePath)

        if(jsonStr.isEmpty()){
            return null
        }


        val map = mapFromJson(jsonStr)

        // mapToJson 함수에서 이미 trim 과정을 거쳤다.
        val id = map["id"].toString().toInt()
        val regDate = map["regDate"].toString()
        val updateDate = map["updateDate"].toString()
        val memberId = map["memberId"].toString().toInt()
        val name = map["name"].toString()
        val code = map["code"].toString()


        return Board(id, regDate, updateDate, memberId, name, code)

    }


    @JvmName("getBoards1")
    fun getBoards(): List<Board>{

        val boards = mutableListOf<Board>()
        val memberLastId = getBoardLastId()
        for(id in 1 .. memberLastId){

            val board = getBoardFromFile("data/board/${id}.json")

            if(board != null){
                boards.add(board)
            }

        }
        return boards

    }



    fun isUsableName(name: String): Boolean {
        for(board in getBoards()){
            if(board.name == name){
                return false
            }
        }
        return true

    }

    fun isUsableCode(code: String): Boolean {
        for(board in getBoards()){
            if(board.code == code){
                return false
            }
        }
        return true

    }

    fun add(memberId: Int, name: String, code: String) {
        val id = getBoardLastId() + 1
        setBoardLastId(id)
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val board = Board(id, regDate, updateDate, memberId, name, code)

        writeStrInFile("data/board/${id}.json", board.toJson())
    }

    fun getBoardById(id: Int): Board? {
        for(board in getBoards()){
            if(board.id == id){
                return board
            }
        }
        return null

    }

    fun deleteBoard(board: Board) {
        deleteFile("data/board/${board.id}.json")

    }

    fun modify(board: Board, name: String, code: String) {
        board.updateDate = Util.getNowDateStr()
        board.name = name
        board.code = code

        writeStrInFile("data/board/${board.id}.json", board.toJson())

    }

    fun getList() {
        for(board in getBoards()){
            val member = memberRepository.getMemberById(board.memberId)
            if(member == null){
                member
            }

            println("${board.id} / ${board.name} / ${board.code} / ${member?.nickname} / ${board.updateDate}")

        }
    }



    fun getBoardIdByCode(boardCode: String): Int {
        for(board in getBoards()){
            if(board.code == boardCode){
                return board.id
            }
        }
        return 0

    }



}
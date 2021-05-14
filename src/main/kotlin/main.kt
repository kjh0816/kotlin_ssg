
    val articleRepository = ArticleRepository()
    val boardRepository = BoardRepository()
    val memberRepository = MemberRepository()

    var loginedMember: Member? = null


fun main(){

    val systemController  = SystemController()
    val articleController = ArticleController()
    val boardController = BoardController()
    val memberController = MemberController()







    while(true){

        val prompt = if(loginedMember == null){
            "명령어)"
        }else{
            "${loginedMember!!.nickname})"
        }


        print(prompt)

        val command = readLineTrim()
        val req = Req(command)


        when(req.actionPath){

            "/system/exit" -> {
                systemController.exit(req)
                break
            }
            "/member/join" -> {
                memberController.join(req)
            }
            "/member/login" -> {
                memberController.login(req)
            }
            "/member/logout" -> {
                memberController.logout(req)
            }
            "/board/add" -> {
                boardController.add(req)
            }
            "/board/delete" -> {
                boardController.delete(req)
            }
            "/board/modify" -> {
                boardController.modify(req)
            }
            "/board/detail" -> {
                boardController.detail(req)
            }
            "/board/list" -> {
                boardController.list(req)
            }
            "/article/write" -> {
                articleController.write(req)
            }
            "/article/delete" -> {
                articleController.delete(req)
            }
            "/article/modify" -> {
                articleController.modify(req)
            }
            "/article/detail" -> {
                articleController.detail(req)
            }
            "/article/list" -> {
                articleController.list(req)
            }
            "/ssg/build" -> {

            }
            else -> {
                println("${command}는(은) 존재하지 않는 명령어입니다.")
            }





        }



    }
}
import java.lang.NumberFormatException

class ArticleController {
    fun write(req: Req) {
        if(loginedMember == null){
            println("로그인 후 이용해주세요.")
            return
        }
        println("< 게시물 쓰기 >")
        boardRepository.getList()
        println("게시판을 번호를 입력해주세요.")
        var boardId: Int
        try {
            boardId = readLineTrim().trim().toInt()
        }
        catch(e: NumberFormatException){
            println("숫자만 입력해주세요.")
            return
        }

        val board = boardRepository.getBoardById(boardId)
        if(board == null){
            println("`${boardId}`은(는) 존재하지 않는 게시판입니다.")
            return
        }


        println("제목을 입력해주세요.")
        val title = readLineTrim()
        println("내용을 입력해주세요.")
        val body = readLineTrim()




        val id = articleRepository.addArticle(loginedMember!!.id,title, body, boardId)

        println("${id}번 게시물이 추가되었습니다.")


    }

    fun delete(req: Req) {
        if(loginedMember == null){
            println("로그인 후 이용해주세요.")
            return
        }

        val id = req.getIntParam("id", 0)
        if(id == 0){
            println("id를 입력해주세요.")
            return
        }


        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("${id}번 게시물이 존재하지 않습니다.")
            return
        }

        if(loginedMember!!.id != article.memberId){
            println("권한이 없습니다.")
            return
        }

        articleRepository.removeArticle(article)
        println("${id}번 게시물이 삭제되었습니다.")

    }

    fun modify(req: Req) {
        if(loginedMember == null){
            println("로그인 후 이용해주세요.")
            return
        }
        val id = req.getIntParam("id", 0)
        if(id == 0){
            println("id를 입력해주세요.")
            return
        }

        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("${id}번 게시물이 존재하지 않습니다.")
            return
        }

        if(loginedMember!!.id != article.memberId){
            println("권한이 없습니다.")
            return
        }

        println("새 제목을 입력해주세요.")
        val title = readLineTrim()
        println("새 내용을 입력해주세요.")
        val body = readLineTrim()

        articleRepository.modifyArticle(article, title, body)

        println("${id}번 게시물이 수정되었습니다.")


    }

    fun detail(req: Req) {
        val id = req.getIntParam("id", 0)
        if(id == 0){
            println("id를 입력해주세요.")
            return
        }

        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("${id}번 게시물이 존재하지 않습니다.")
            return
        }

        articleRepository.articleDetail(article)


    }

    fun list(req: Req) {
        val page = req.getIntParam("page", 1)
        val searchKeyword = req.getStrParam("searchKeyword", "")
        val boardCode = req.getStrParam("boardCode","")

        val itemCountInAPage = 10




        println("번호 / 게시판 / 작성자 / 제목 / 내용 / 갱신날짜")
        val filteredArticles = articleRepository.getFilteredArticle(searchKeyword, page, boardCode, itemCountInAPage)
        for(article in filteredArticles){
            val member = memberRepository.getMemberById(article.memberId)
            println("${article.id} / ${member!!.nickname} / ${article.title} / ${article.body} / ${article.updateDate}")

        }

    }
}
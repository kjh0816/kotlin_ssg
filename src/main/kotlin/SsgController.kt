class SsgController {
    fun build(req: Req) {


        // ext/article_list_board.code > board.code에 해당되는 articles를 저장 ( = 게시판 별 게시물 리스트 )
        println("게시물 상세 페이지 생성 시작")
        makeArticleDetailPages()
        println("게시물 상세 페이지 생성 끝")


        // ext/article_detail_article.id > 모든 article의 detail ( = 모든 게시물 별 detail )
        // 뒤로가기 구현
        println("게시물 리스트 페이지 생성 시작")
        makeArticleListPages()
        println("게시물 리스트 페이지 생성 끝")

    }

    private fun makeArticleListPages() {
        val boards = boardRepository.getBoards()

        for(board in boards){
            makeArticleListPage(board)
        }
    }

    private fun makeArticleListPage(board: Board) {
        val articles = articleRepository.getArticles()

        var fileContent ="<mata charset=\"UTF-8\">\n"

        fileContent += "<h1>${board.name} 게시물 리스트</h1>\n"

        // 매개인자 boardId와 일치하는 게시물들을 찾아서 boardId 마다의 게시물 리스트의 URI를 fileContent에 기록해서
        // fileContent를 파일에 담는 과정
        for(article in articles){

            if(article.boardId == board.id){

                val writer = memberRepository.getMemberById(article.id)!!
                val writerName = writer.nickname

                // ArticleDetail page마다 URI를 지정해주고, 파일을 생성해주는 작업
                // 형식 : <div><a href="URI"></a>
                fileContent += "<div>"

                val detailPageUri ="article_detail_${article.id}.html"

                fileContent += "<a href=\"$detailPageUri\">"
                fileContent += "번호: ${article.id}"
                fileContent += " / 작성날짜: ${article.regDate}"
                fileContent += " / 작성자: $writerName"
                fileContent += " / 제목: ${article.title}"
                fileContent += "</a>"



                fileContent += "<div>\n"  // 하나의 div가 끝나면 다음 줄로 내려준다.


            }
        }

        val fileName = "article_list_${board.code}.html"
        writeStrInFile("ext/${fileName}", fileContent)
        println("fileName + 파일이 생성되었습니다.")


    }

    private fun makeArticleDetailPages() {

        val articles = articleRepository.getArticles()

        for(article in articles){
            makeArticleDetailPage(article)
        }

    }

    private fun makeArticleDetailPage(article: Article) {
        val board = boardRepository.getBoardById(article.boardId)!!
        val writer = memberRepository.getMemberById(article.memberId)!!

        val boardName = board.name
        val writerName = writer.nickname

        var fileContent = "<meta charset=\"UTF-8\"\n"

        fileContent += "<h1>${article.id}번 게시물</h1>\n"

        fileContent += "<div>번호: ${article.id}</div>\n"
        fileContent += "<div>작성날짜: ${article.regDate}</div>\n"
        fileContent += "<div>수정날짜: ${article.updateDate}</div>\n"
        fileContent += "<div>게시물 종류: ${boardName}</div>\n"
        fileContent += "<div>작성자: ${writerName}</div>\n"
        fileContent += "<div>제목: ${article.title}</div>\n"
        fileContent += "<div>내용: ${article.body}</div>\n"
        fileContent += "<div><a href=\"#\" onclick=\"history.back();\"></a></div>\n"

        val fileName ="article_detail_${article.id}.html"
        writeStrInFile("ext/${fileName}", fileContent)
        println(fileName + "파일이 생성되었습니다.")




    }
}
class ArticleRepository {



    @JvmName("getArticleLastId1")
    private fun getArticleLastId(): Int{
        val articleLastId = readIntFromFile("data/article/articleLastId.txt", 0)

        return articleLastId
    }



    @JvmName("setArticleLastId1")
    private fun setArticleLastId(newId: Int){
        writeIntToFile("data/article/articleLastId.txt", newId)
    }


    // filePath로 객체가 구분되기 때문에,  getMemberFromFile이 호출될 때 filePath를 줘야한다.
    // 불러온 값은 json 데이터이므로, 변환해주어야한다.
    private fun getArticleFromFile(filePath: String): Article?{
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
        val boardId = map["boardId"].toString().toInt()
        val title = map["title"].toString()
        val body = map["body"].toString()


        return Article(id, regDate, updateDate, memberId, boardId, title, body)

    }



    @JvmName("getArticles1")
    fun getArticles(): List<Article>{

        val articles = mutableListOf<Article>()
        val articleLastId = getArticleLastId()
        for(id in 1 .. articleLastId){

            val article = getArticleFromFile("data/article/${id}.json")

            if(article != null){
                articles.add(article)
            }

        }
        return articles

    }




    fun addArticle(memberId: Int, title: String, body: String, boardId: Int):Int {

        val id = getArticleLastId() + 1
        setArticleLastId(id)
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val article = Article(id,regDate, updateDate, memberId, boardId, title, body)

        writeStrInFile("data/article/${id}.json", article.toJson())

        return id



    }

    fun getArticleById(id: Int): Article? {
        for(article in getArticles()){
            if(article.id == id){
                return article
            }
        }
        return null


    }

    fun removeArticle(article: Article) {
        deleteFile("data/article/${article.id}.json")


    }

    fun modifyArticle(article: Article, title: String, body: String) {

        article.title = title
        article.body = body
        article.updateDate = Util.getNowDateStr()

        writeStrInFile("data/article/${article.id}.json", article.toJson())


    }

    fun articleDetail(article: Article?) {

        val board = boardRepository.getBoardById(article!!.boardId)
        val member = memberRepository.getMemberById(article.memberId)

        println("번호      : ${article.id}")
        println("게시판    : ${board!!.name}")
        println("제목      : ${article.title}")
        println("내용      : ${article.body}")
        println("작성자    : ${member!!.nickname}")
        println("최초 작성일: ${article.regDate}")
        println("수정일자   : ${article.updateDate}")
    }

    fun getFilteredArticle(searchKeyword: String, page: Int, boardCode: String, itemCountInAPage: Int):List<Article> {
        val filtered1Articles = getSearchKeywordFilteredArticles(getArticles(), searchKeyword, boardCode)
        val filtered2Articles = getPageFilteredArticles(filtered1Articles, page, itemCountInAPage)
        return filtered2Articles
    }



    private fun getSearchKeywordFilteredArticles(articles: List<Article>, searchKeyword: String, boardCode: String): List<Article> {
        val filteredArticles = mutableListOf<Article>()


        val boardId = boardRepository.getBoardIdByCode(boardCode)

        if(searchKeyword.isNotEmpty() && boardId != 0){

            for(article in articles){
                if(article.title.contains(searchKeyword) && article.boardId == boardId){
                    filteredArticles.add(article)
                }
            }
            return filteredArticles
        }
        else if(searchKeyword.isNotEmpty() && boardId == 0) {


            for (article in articles) {
                if (article.title.contains(searchKeyword)) {
                    filteredArticles.add(article)

                }
            }

            return filteredArticles
        }
        else if(searchKeyword.isEmpty() && boardId != 0){


            for(article in articles){
                if(article.boardId == boardId){
                    filteredArticles.add(article)
                }
            }
            return filteredArticles
        }


        return articles
    }
    fun getPageFilteredArticles(
        filtered1Articles: List<Article>,
        page: Int,
        itemCountInAPage: Int
    ): List<Article> {

        val filteredArticles = mutableListOf<Article>()

        val offsetCount = (page - 1) * itemCountInAPage

        val startIndex = filtered1Articles.lastIndex - offsetCount
        var endIndex = startIndex - (itemCountInAPage - 1)

        if(endIndex < 0){
            endIndex = 0
        }

        for(i in startIndex downTo endIndex){
            filteredArticles.add(filtered1Articles[i])
        }




        return filteredArticles

    }





}
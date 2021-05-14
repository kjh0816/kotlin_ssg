import java.io.File
import java.text.SimpleDateFormat

fun readLineTrim() = readLine()!!.trim()

///ssg/build 명령이 입력되면 실행될 일
//- 가정 : 게시판이 2개, 회원이 2매여, 글이 3개 있다고
//- 모든 게시물 불러오기, 반복문
//- build/article_detail_1.html
//- build/article_detail_2.html
//- build/article_detail_3.html
//- 모든 게시판 불러오기, 반복문
//- build/article_list_notice.html 파일 생성
//- build/article_list_free.html 파일 생성




fun mapFromJson(jsonStr: String):Map<String, Any>{

    val map = mutableMapOf<String, Any>()

    var jsonStr = jsonStr.drop(1)
    jsonStr = jsonStr.dropLast(1)


    val jsonStrBits = jsonStr.split(",\r\n")


    // String , Double, Boolean, email
    for(jsonStrBit in jsonStrBits){

        val keyValueBit = jsonStrBit.split(":", limit=2)
        val key = keyValueBit[0].trim().drop(1).dropLast(1)
        var value = keyValueBit[1].trim()
        when{


            value == "true" -> {
                map[key] = true
            }
            value == "false" -> {
                map[key] = false
            }
            value.startsWith("\"") -> {
                map[key] = value.toString().drop(1).dropLast(1)
            }
            value.contains(".") && !value.contains("@") -> {
                map[key] = value.toDouble()
            }
            value.contains(".") && value.contains("@") -> {
                map[key] = value.drop(1).dropLast(1)
            }
            else -> {
                map[key] = value.toInt()
            }

        }



    }

    return map

}


// 읽어오려면 path만 알면 되고, 쓰려면 path와 쓸 내용을 알아야한다.
// 읽을 때는 모두 파일이 존재하는지 검사해야한다.
// toInt()는 신중하게.

fun readStrFromFile(filePath: String): String{

    if(!File(filePath).isFile){
        return ""
    }
    return File(filePath).readText(Charsets.UTF_8)
}
fun writeStrInFile(filePath: String, content: String){
    File(filePath).parentFile.mkdirs()
    File(filePath).writeText(content)
}

fun readIntFromFile(filePath: String, default: Int): Int{


    val content = readStrFromFile(filePath)

    if(content.isEmpty()){
        return default
    }
    return content.toInt()
}

fun writeIntToFile(filePath: String, content: Int){

    writeStrInFile(filePath, content.toString())

}
fun deleteFile(filePath: String){
    File(filePath).delete()
}





object Util {

    fun getNowDateStr(): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return format.format(System.currentTimeMillis())
    }
}
class Req(command: String) {


    val actionPath: String
    val paramMap: Map<String, String>


    init {
        val commandBits = command.split("?", limit = 2)

        actionPath = commandBits[0].trim()

        val queryStr = if (commandBits.lastIndex == 1 && commandBits[1].isNotBlank()) {
            commandBits[1].trim()
        } else {
            ""
        }

        paramMap = if (queryStr.isEmpty()) {
            mapOf()
        } else {

            val queryStrBits = queryStr.split("&")
            val paramMapTemp = mutableMapOf<String, String>()

            for (queryStrBit in queryStrBits) {
                val eachQueryBits = queryStrBit.split("=", limit = 2)
                val paramName = eachQueryBits[0].trim()
                val paramValue = if (eachQueryBits.lastIndex == 1 && eachQueryBits[1].isNotEmpty()) {
                    eachQueryBits[1].trim()
                } else {
                    ""
                }
                if (paramValue.isNotEmpty()) {
                    paramMapTemp[paramName] = paramValue
                }
            }
            paramMapTemp


        }
    }


    fun getStrParam(name: String, default: String): String {
        return paramMap[name] ?: default
    }

    fun getIntParam(name: String, default: Int): Int {
        return if (paramMap[name] != null) {
            try {
                paramMap[name]!!.toInt()
            } catch (e: NumberFormatException) {
                default
            }

        } else {
            default
        }
    }
}

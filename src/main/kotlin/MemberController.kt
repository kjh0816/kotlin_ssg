class MemberController {
    fun join(req: Req) {
        println("< 회원가입 >")
        println("로그인 아이디를 입력해주세요.")
        val loginId = readLineTrim()
        val isUsableLoginId = memberRepository.isUsableLoginId(loginId)
        if(!isUsableLoginId){
            println("${loginId}는(은) 이미 존재하는 로그인 아이디입니다.")
            return
        }
        println("로그인 비밀번호를 입력해주세요.")
        val loginPw = readLineTrim()

        println("이름을 입력해주세요.")
        val name = readLineTrim()
        println("별명을 입력해주세요.")
        val nickname = readLineTrim()
        println("핸드폰 번호를 입력해주세요.")
        val cellphoneNo = readLineTrim()
        println("이메일을 입력해주세요.")
        val email = readLineTrim()

        memberRepository.join(loginId, loginPw, name, nickname, cellphoneNo, email)

        println("${nickname}님의 회원가입이 완료되었습니다.")

    }

    fun login(req: Req) {
        println("로그인 아이디를 입력해주세요.")
        val loginId = readLineTrim()
        val member = memberRepository.getMemberByLoginId(loginId)
        if(member == null){
            println("${loginId}는(은) 존재하지 않는 로그인 아이디입니다.")
            return
        }
        println("로그인 비밀번호를 입력해주세요.")
        val loginPw = readLineTrim()
        if(member.loginPw != loginPw){
            println("잘못된 비밀번호입니다.")
            return
        }

        loginedMember = member
        println("${member.nickname}님, 환영합니다.")
    }

    fun logout(req: Req) {

        loginedMember = null
        println("로그아웃되었습니다.")


    }
}
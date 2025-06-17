package taskweaver.taskweaver_backend.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskweaver.taskweaver_backend.common.code.ErrorCode;
import taskweaver.taskweaver_backend.common.exception.handler.BusinessExceptionHandler;
import taskweaver.taskweaver_backend.domain.member.model.Member;
import taskweaver.taskweaver_backend.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member updateNickname(Long memberId, String newNickname) {
        // 1. memberId로 회원을 찾습니다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));

         if (memberRepository.findByNickname(newNickname).isPresent()) {
             throw new BusinessExceptionHandler(ErrorCode.DUPLICATED_NICKNAME);
         }

        member.updateNickname(newNickname);
        return member;
    }

}
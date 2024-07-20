package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.entities.Member;
import roomescape.exceptions.ErrorCode;
import roomescape.exceptions.RoomEscapeException;
import roomescape.repositories.MemberRepository;
import roomescape.ui.data.SignupRequest;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  public void save(SignupRequest signupRequest){
    memberRepository.save(
      new Member(signupRequest.getName(), signupRequest.getEmail(), signupRequest.getEmail()));
  }

  public Member findMemberByEmail(String email){
    return memberRepository.findByEmail(email).orElseThrow(
      () -> new RoomEscapeException(ErrorCode.INVALID_INPUT_VALUE, ErrorCode.INVALID_INPUT_VALUE.name()));

  }
}

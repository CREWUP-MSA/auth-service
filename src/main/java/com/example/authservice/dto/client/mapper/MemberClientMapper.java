package com.example.authservice.dto.client.mapper;

import org.springframework.stereotype.Component;

import com.example.authservice.dto.client.AuthenticateRequest;
import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.exception.CustomException;
import com.example.authservice.exception.ErrorCode;
import com.example.authservice.service.client.MemberServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class MemberClientMapper {

	private final MemberServiceClient memberServiceClient;

	/**
	 * 멤버 조회 - By ID Mapper
	 * @param id 멤버 ID
	 * @return MemberResponse
	 * @throws CustomException 멤버를 찾을 수 없는 경우
	 * @throws CustomException 멤버 서비스 에러
	 * @see ErrorCode
	 */
	public MemberResponse getMemberById(Long id) {
		try{
			MemberResponse memberResponse =  memberServiceClient.getMemberById(id).data();
			validateMemberDeleted(memberResponse);
			return memberResponse;

		} catch (FeignException e){
			throw handleFeignException(e);
		}
	}

	/**
	 * 멤버 조회 - By Email Mapper
	 * @param email 멤버 Email
	 * @return MemberResponse
	 * @throws CustomException 멤버를 찾을 수 없는 경우
	 * @throws CustomException 멤버 서비스 에러
	 * @see ErrorCode
	 */
	public MemberResponse getMemberByEmail(String email) {
		try{
			MemberResponse memberResponse = memberServiceClient.getMemberByEmail(email).data();
			validateMemberDeleted(memberResponse);
			return memberResponse;

		} catch (FeignException e){
			throw handleFeignException(e);
		}
	}

	/**
	 * 사용자 인증 처리 (비밀번호 확인)
	 *
	 * @param request 인증 요청 정보 (이메일, 비밀번호)
	 * @throws CustomException 비밀번호가 일치하지 않는 경우
	 */
	public void authenticateMember(AuthenticateRequest request) {
		try {
			memberServiceClient.authenticateMember(request);
		} catch (FeignException e) {
			throw handleFeignException(e);
		}
	}

	/**
	 * 회원 삭제 여부 확인
	 * @param memberResponse 회원 정보
	 * @throws CustomException 회원이 삭제된 경우
	 */
	private void validateMemberDeleted(MemberResponse memberResponse) {
		if (memberResponse.isDeleted())
			throw new CustomException(ErrorCode.MEMBER_IS_DELETED);
	}

	/**
	 * FeignException 처리
	 *
	 * @param e FeignException
	 * @return CustomException (MEMBER_NOT_FOUND, INTERNAL_SERVER_ERROR)
	 */
	private CustomException handleFeignException(FeignException e) {
		if (e instanceof FeignException.NotFound) {
			log.error("FROM Member-Service : Member not found");
			return new CustomException(ErrorCode.MEMBER_NOT_FOUND);

		}else {
			log.error("FROM Member-Service : Member service error");
			return new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}


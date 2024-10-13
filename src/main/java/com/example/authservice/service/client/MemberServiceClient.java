package com.example.authservice.service.client;

import com.example.authservice.dto.client.ClientResponse;
import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.request.MemberRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    @PostMapping("/member-service/api/member")
    ClientResponse<MemberResponse> createMember(@RequestBody MemberRequest request);

    @GetMapping("/member-service/api/member/by-email")
    ClientResponse<MemberResponse> getMemberByEmail(@RequestParam("email") String email);
}

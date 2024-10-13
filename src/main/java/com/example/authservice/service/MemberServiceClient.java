package com.example.authservice.service;

import com.example.authservice.dto.client.ClientResponse;
import com.example.authservice.dto.client.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    @GetMapping(value = "/member-service/api/member")
    ClientResponse<MemberResponse> getMemberByEmail(@RequestParam("email") String email);
}

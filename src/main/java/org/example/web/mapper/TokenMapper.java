package org.example.web.mapper;

import org.example.model.user.Token;
import org.example.web.dto.auth.AuthenticationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.util.Pair;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TokenMapper {

  @Mapping(target = "accessToken", expression = "java(tokenPair.getFirst().getValue())")
  @Mapping(target = "refreshToken", expression = "java(tokenPair.getSecond().getValue())")
  AuthenticationResponse toResponse(Pair<Token, Token> tokenPair);
}

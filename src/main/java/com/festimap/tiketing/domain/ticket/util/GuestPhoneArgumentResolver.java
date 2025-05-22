package com.festimap.tiketing.domain.ticket.util;

import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import com.festimap.tiketing.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GuestPhoneArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwt;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GuestPhone.class)
                && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter param,
                                  ModelAndViewContainer mav,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        String token = jwt.resolveGuestToken(req);
        if (token == null || !jwt.validateToken(token)) {
            throw new BaseException(ErrorCode.INVALID_GUEST_TOKEN);
        }

        List<GrantedAuthority> auths = jwt.getAuthoritiesFromToken(token);
        if (auths.stream().noneMatch(a -> a.getAuthority().equals("GUEST"))) {
            throw new BaseException(ErrorCode.FORBIDDEN_GUEST_TOKEN);
        }

        return jwt.getAccount(token);
    }
}

package eu.unicredit.xframe.qrh.admin.rs.api.converter;

import eu.unicredit.xframe.qrh.admin.rs.security.ContextTokenProvider;
import eu.unicredit.xframe.qrh.admin.rs.security.UserContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component(JwtToUserContextConverter.SERVICE_NAME)
public class JwtToUserContextConverter implements Converter<String, UserContext> {

    public static final String SERVICE_NAME = "jwt-to-user-context-converter";

    private final ContextTokenProvider jwtTokenProvider;

    public JwtToUserContextConverter(@Qualifier(ContextTokenProvider.SERVICE_NAME) ContextTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserContext convert(String tokenHeader) {
        return jwtTokenProvider.extractUserContext(ContextTokenProvider.extractJwtFromHeaderParameter(tokenHeader));
    }
}

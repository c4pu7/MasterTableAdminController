

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

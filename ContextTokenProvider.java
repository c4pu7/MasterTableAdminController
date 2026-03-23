

@Component(ContextTokenProvider.SERVICE_NAME)
@Slf4j
public class ContextTokenProvider {
    public static final String SERVICE_NAME = "jwt-token-provider";

    public static final String NDG_CLAIM = "ndg";
    public static final String LOCALE_CLAIM = "user-locale";
    public static final String AUTHORITIES_CLAIM = "app-roles";
    public static final String FLAT_ROLES_DELIMITER = ",";
    public static final String PROPOSAL_ID_CLAIM = "proposal-id";
    public static final String PROPOSAL_NUMBER_CLAIM = "proposal-number";
    public static final String IS_READ_ONLY = "is-read-only";
    public static final String LAST_RELEASED_PROPOSAL_ID_CLAIM = "last-released-proposal-id";
    public static final String LAST_RELEASED_PROPOSAL_NUMBER_CLAIM = "last-released-proposal-number";

    private final JWTConfiguration jwtConfig;

    @Autowired
    public ContextTokenProvider(JWTConfiguration jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String userName, String ndg, String locale, Set<String> roles) {
        return generateToken(userName, ndg, locale, roles, null, null, true);
    }

    public String generateToken(String userName, String ndg, String locale, Set<String> roles, Integer proposalNumber, boolean isReadOnly) {
        return generateToken(userName, ndg, locale, roles, null, proposalNumber, isReadOnly);
    }

    public String generateToken(String userName, String ndg, String locale, Set<String> roles, UUID proposalId, Integer proposalNumber, boolean isReadOnly) {
        return generateToken(userName, ndg, locale, roles, proposalId, proposalNumber, isReadOnly, null);
    }

    public String generateToken(String userName, String ndg, String locale, Set<String> roles, UUID proposalId, Integer proposalNumber, boolean isReadOnly, Long expirationEpochMilli) {
        Long expirationMillis = expirationEpochMilli;
        if (null == expirationMillis) {
            LocalDateTime expiryDate = calculateExpiration(jwtConfig.getLifetime());
            expirationMillis = expiryDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        }

        Date expiration = new Date(expirationMillis);

        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(expiration)
                .subject(userName)
                .claim(NDG_CLAIM, ndg)
                .claim(LOCALE_CLAIM, locale)
                .claim(AUTHORITIES_CLAIM, CollectionUtils.flat(roles, FLAT_ROLES_DELIMITER))
                .claim(PROPOSAL_ID_CLAIM, proposalId)
                .claim(PROPOSAL_NUMBER_CLAIM, proposalNumber)
                .claim(IS_READ_ONLY, isReadOnly ? "1" : "0")
                .signWith(jwtConfig.getSecret())
                .compact();
    }

    public static LocalDateTime calculateExpiration(Long lifetime) {
        return DateUtils.utcNow().plusSeconds(lifetime);
    }

    public UserContext extractUserContext(String token) {
        Claims claims = Jwts.parser().verifyWith(jwtConfig.getSecret()).build().parseSignedClaims(token).getPayload();

        return UserContext.builder()
                .username(claims.getSubject())
                .ndg((String) claims.get(NDG_CLAIM))
                .locale((String) claims.get(LOCALE_CLAIM))
                .roles(CollectionUtils.expandAsSet((String) claims.get(AUTHORITIES_CLAIM), ","))
                .proposalId(Optional.ofNullable(claims.get(PROPOSAL_ID_CLAIM)).map(String.class::cast).map(UUID::fromString).orElse(null))
                .proposalNumber(Optional.ofNullable(claims.get(PROPOSAL_NUMBER_CLAIM)).map(Integer.class::cast).orElse(null))
                .lastReleasedProposalId(Optional.ofNullable(claims.get(LAST_RELEASED_PROPOSAL_ID_CLAIM)).map(String.class::cast).map(UUID::fromString).orElse(null))
                .lastReleasedProposalNumber(Optional.ofNullable(claims.get(LAST_RELEASED_PROPOSAL_NUMBER_CLAIM)).map(Integer.class::cast).orElse(null))
                .isReadOnly("1".equals(claims.get(IS_READ_ONLY)))

                .build();
    }

    public long extractExpirationTime(String jwt) {
        Claims claims = Jwts.parser().verifyWith(jwtConfig.getSecret()).build().parseSignedClaims(jwt).getPayload();
        return claims.getExpiration().getTime();

    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(jwtConfig.getSecret()).build().parseSignedClaims(authToken).getPayload();
            return true;
        } catch (SignatureException ex) { //NOSONAR logging just a message
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) { //NOSONAR logging just a message
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) { //NOSONAR logging just a message
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) { //NOSONAR logging just a message
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) { //NOSONAR logging just a message
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public static String extractJwtFromHeaderParameter(String authorizationToken) {
        if (StringUtils.hasText(authorizationToken)) {
            // TODO
            return authorizationToken;
        }
        return null;
    }

    public long calculateTimeToExpiration(String jwt) {
        return extractExpirationTime(jwt) - new Date().getTime();
    }
}

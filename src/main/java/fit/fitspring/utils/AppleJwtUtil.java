package fit.fitspring.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import fit.fitspring.controller.dto.apple.AppleClient;
import fit.fitspring.controller.dto.apple.ApplePublicKeys;
import fit.fitspring.exception.common.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static  fit.fitspring.exception.common.ErrorCode.*;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleJwtUtil {

    private final AppleClient appleClient;

    //id_token 검증
    public Claims getClaimsBy(String identityToken){
        try{
            ApplePublicKeys applePublicKeys = appleClient.getAppleAuthPublicKey();
            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
            ApplePublicKeys.Key key = applePublicKeys.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new NullPointerException("공개키를 얻을 수 없습니다."));

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            //identity token 정보 return
            return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).getBody();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(FAIL_GET_PUB_KEY);
        } catch (InvalidKeySpecException e) {
            throw new BusinessException(FAIL_GET_PUB_KEY);
        } catch (MalformedJwtException e) {
            throw new BusinessException(FAIL_GET_PUB_KEY);
        } catch (ExpiredJwtException e) {
            //토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함.
            throw new BusinessException(EXPIRED_JWT);
        } catch (Exception e) {
            throw new BusinessException(FAIL_GET_PUB_KEY);
        }
    }

}

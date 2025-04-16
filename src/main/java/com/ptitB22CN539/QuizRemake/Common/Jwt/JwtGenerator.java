package com.ptitB22CN539.QuizRemake.Common.Jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ptitB22CN539.QuizRemake.DTO.DTO.JwtDTO;
import com.ptitB22CN539.QuizRemake.Model.Entity.UserEntity;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtGenerator {
    @Value(value = "${signerKey}")
    private String signerKey;
    @Value(value = "${accessTokenDuration}")
    private Long accessTokenDuration;

    public JwtDTO generateJwtEntity(UserEntity user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            String jti = UUID.randomUUID().toString();
            Date exp = new Date(System.currentTimeMillis() + accessTokenDuration * 1000);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .expirationTime(exp)
                    .notBeforeTime(new Date(System.currentTimeMillis()))
                    .jwtID(jti)
                    .claim("scope", user.getRole().getCode())
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(header, payload);
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return new JwtDTO(jti, jwsObject.serialize(), exp);
        } catch (Exception exception) {
            throw new DataInvalidException(ExceptionVariable.SERVER_ERROR);
        }
    }

    public SignedJWT getSignedJWT(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(verifier)) {
                // verify token
                throw new DataInvalidException(ExceptionVariable.TOKEN_INVALID);
            }
            return signedJWT;
        } catch (JOSEException | ParseException exception) {
            throw new DataInvalidException(ExceptionVariable.TOKEN_INVALID);
        }
    }
}

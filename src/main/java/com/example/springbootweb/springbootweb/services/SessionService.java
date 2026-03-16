package com.example.springbootweb.springbootweb.services;

import com.example.springbootweb.springbootweb.entities.SessionEntity;
import com.example.springbootweb.springbootweb.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final Integer SESSION_LIMIT=2;

    public void addSession(Long userId, String refreshToken){
        List<SessionEntity> sessionEntityList = sessionRepository.findByUserId(userId);
        if(sessionEntityList.size()==SESSION_LIMIT){
             sessionEntityList.sort((a,b)->a.getLastUsedAt().compareTo(b.getLastUsedAt())); //Oldest First
            SessionEntity oldestSession = sessionEntityList.get(0);
            sessionRepository.delete(oldestSession);
        }
        SessionEntity newSession = SessionEntity.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);


    }
    //NOTE : har /login api  pe check karo agar 2 ya 2 se jydada sessions ahai toh oldest wala uda session and insert a new one with that same userId and new Refresh Token in DB. (so max at 2 sessions of a user(userId) can exists in table)

    public void validateRefreshTokenWithSession(String refreshToken){
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new SessionAuthenticationException("Session not exists with Refresh Token : Invalid Refresh Token"));

    }
    //NOTE : ab har /refresh api mein check hoga pehely toh jwt valid Refresh token hona chaayie  + db mein us Refresh token se koi session pda hona chaayie else wo bhi invalid Refresh token maana jaayega (though wo 15 min ka gap jsiesy one time access token le chuka 1st login pe w/.r.t to 3rd login,15 min active rhega bss only 1st acc pe next time refreshTOken se refrsh kreney jaaygea invalid btayega coz uska session milega hi ni)
}

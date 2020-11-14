package guru.sfg.brewery.services;

import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnlockUsers {


    private final UserRepository userRepository;

    @Scheduled(fixedRate = 5000)
    public void unlockUsers() {

        List<User> lockedUsers = userRepository.findAllByAccountNonLockedAndModifyDateBefore(false,
                Timestamp.valueOf(LocalDateTime.now().minusSeconds(10)));

        if (lockedUsers.size() > 0) {
            log.debug("Unlocking user accounts");
            lockedUsers.forEach(user -> user.setAccountNonLocked(true));
            userRepository.saveAll(lockedUsers);
        }

    }


}

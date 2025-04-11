package com.festimap.tiketing.domain.verification.repository;

import com.festimap.tiketing.domain.verification.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByPhoneNumber(String phoneNumber);
}

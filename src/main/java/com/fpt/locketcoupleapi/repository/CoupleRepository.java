package com.fpt.locketcoupleapi.repository;

import com.fpt.locketcoupleapi.entity.Couple;
import com.fpt.locketcoupleapi.payload.DTO.CoupleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoupleRepository extends JpaRepository<Couple,Integer> {
    List<Couple> findCouplesByUserBoyfriend_UserId(int userId);
    List<Couple> findCouplesByUserGirlfriend_UserId(int userId);
}

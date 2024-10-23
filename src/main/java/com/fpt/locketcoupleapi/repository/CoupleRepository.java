package com.fpt.locketcoupleapi.repository;

import com.fpt.locketcoupleapi.entity.Couple;
import com.fpt.locketcoupleapi.entity.EStatus;
import com.fpt.locketcoupleapi.payload.DTO.CoupleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoupleRepository extends JpaRepository<Couple,Integer> {
    Couple findCoupleByUserBoyfriend_UserIdAndStatus(int userId, EStatus status);
    Couple findCoupleByUserGirlfriend_UserIdAndStatus(int userId, EStatus status);

    // Đếm cặp đôi của bạn trai có trạng thái PENDING
    long countCouplesByUserBoyfriend_UserIdAndStatus(int userId, EStatus status);

    // Đếm cặp đôi của bạn gái có trạng thái PENDING
    long countCouplesByUserGirlfriend_UserIdAndStatus(int userId, EStatus status);


}

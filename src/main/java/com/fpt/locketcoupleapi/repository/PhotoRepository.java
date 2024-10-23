package com.fpt.locketcoupleapi.repository;

import com.fpt.locketcoupleapi.entity.Couple;
import com.fpt.locketcoupleapi.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Integer> {
    List<Photo> findPhotosByCouple_CoupleId(int coupleId);
}

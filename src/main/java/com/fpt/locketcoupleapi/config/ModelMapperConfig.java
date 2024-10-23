package com.fpt.locketcoupleapi.config;

import com.fpt.locketcoupleapi.entity.Couple;
import com.fpt.locketcoupleapi.entity.Photo;
import com.fpt.locketcoupleapi.payload.DTO.CoupleDTO;
import com.fpt.locketcoupleapi.payload.DTO.PhotoDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        // Create ModelMapper instance and configure matching strategy
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(Couple.class, CoupleDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getUserBoyfriend().getUserId(), CoupleDTO::setUserBoyfriendId);
            mapper.map(src -> src.getUserGirlfriend().getUserId(), CoupleDTO::setUserGirlfriendId);
        });

        modelMapper.typeMap(Photo.class, PhotoDTO.class).addMappings(mapper ->{
            mapper.map(src -> src.getCouple().getCoupleId(), PhotoDTO::setCoupleId);
            mapper.map(src -> src.getSender().getUserId(), PhotoDTO::setSenderId);
        });


        return modelMapper;


    }


}

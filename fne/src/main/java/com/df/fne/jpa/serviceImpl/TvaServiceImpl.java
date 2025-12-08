package com.df.fne.jpa.serviceImpl;

import com.df.fne.core.domaines.TvaDto;
import com.df.fne.core.mappers.TvaMapper;
import com.df.fne.core.services.TvaService;
import com.df.fne.jpa.entities.Tva;
import com.df.fne.jpa.repositories.TvaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TvaServiceImpl implements TvaService {

    private final TvaRepository tvaRepository;
    private final TvaMapper tvaMapper;

    public TvaServiceImpl(TvaRepository tvaRepository , TvaMapper tvaMapper){
        this.tvaRepository = tvaRepository;
        this.tvaMapper = tvaMapper;
    }

    @Override
    public List<TvaDto> getAll() {
        List<Tva> tvaList = tvaRepository.findAll();
        return tvaMapper.toDtoList(tvaList);
    }
}

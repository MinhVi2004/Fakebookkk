package com.example.backend.Service;

import java.util.List;

import com.example.backend.DTO.LuotThichDTO;

public interface LuotThichService {
    List<LuotThichDTO> findByMaBV(int maBV);
    boolean toggleLuotThich(LuotThichDTO luotThichDTO);
}
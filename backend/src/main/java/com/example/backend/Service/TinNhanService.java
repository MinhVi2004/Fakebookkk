package com.example.backend.Service;

import java.util.List;

import com.example.backend.Entity.TinNhanEntity;

public interface TinNhanService {
    TinNhanEntity guiTinNhan(Integer nguoiGuiId, Integer nguoiNhanId, String noiDung);
    List<TinNhanEntity> layTinNhanGiuaHaiNguoi(Integer id1, Integer id2);
}

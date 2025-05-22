package com.example.backend.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Entity.TaiKhoanEntity;
import com.example.backend.Entity.TinNhanEntity;
import com.example.backend.Repository.TaiKhoanRepository;
import com.example.backend.Repository.TinNhanRepository;

@Service
public class TinNhanServiceImpl implements TinNhanService {

    @Autowired
    private TinNhanRepository tinNhanRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

      @Override
      public TinNhanEntity guiTinNhan(Integer nguoiGuiId, Integer nguoiNhanId, String noiDung) {
            TaiKhoanEntity nguoiGui = taiKhoanRepository.findById(nguoiGuiId)
                  .orElseThrow(() -> new RuntimeException("Người gửi không tồn tại"));
            TaiKhoanEntity nguoiNhan = taiKhoanRepository.findById(nguoiNhanId)
                  .orElseThrow(() -> new RuntimeException("Người nhận không tồn tại"));

            TinNhanEntity tinNhan = new TinNhanEntity();
            tinNhan.setNguoiGui(nguoiGui);
            tinNhan.setNguoiNhan(nguoiNhan);
            tinNhan.setNoiDung(noiDung);
            tinNhan.setThoiGianGui(LocalDateTime.now());
            tinNhan.setDaDoc(false);

            return tinNhanRepository.save(tinNhan);
      }

      @Override
      public List<TinNhanEntity> layTinNhanGiuaHaiNguoi(Integer id1, Integer id2) {
            return tinNhanRepository.findTinNhanGiuaHaiNguoi(id1, id2);
      }
}

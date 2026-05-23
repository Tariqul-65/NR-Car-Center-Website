package com.example.nrcarcenter.service;

import com.example.nrcarcenter.entity.*;
import com.example.nrcarcenter.repository.StockCarRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockCarRepository stockRepo;
    private final FileStorageService fileStorage;

    public List<StockCar> list(String q, StockStatus status) {
        String s = clean(q);

        if (status != null && (s == null)) return stockRepo.findByStatus(status);

        if (!StringUtils.hasText(s)) {
            if (status == null) return stockRepo.findAll();
            return stockRepo.findByStatus(status);
        }

        Map<Long, StockCar> uniq = new LinkedHashMap<>();
        stockRepo.findByChassisNoContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));
        stockRepo.findByMakeContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));
        stockRepo.findByModelContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));

        List<StockCar> out = new ArrayList<>(uniq.values());
        if (status != null) out.removeIf(x -> x.getStatus() != status);
        return out;
    }

    @Transactional
    public StockCar create(StockCar payload, List<MultipartFile> images) {
        if (payload == null) throw new IllegalArgumentException("Invalid payload");

        String stockId = req(clean(payload.getStockId()), "Stock ID required");
        String chassisNo = req(clean(payload.getChassisNo()), "Chassis No required");

        if (stockRepo.existsByStockId(stockId)) throw new IllegalArgumentException("Stock ID already exists");
        if (stockRepo.existsByChassisNo(chassisNo)) throw new IllegalArgumentException("Chassis No already exists");

        StockCar car = StockCar.builder()
                .stockId(stockId)
                .chassisNo(chassisNo)
                .make(req(clean(payload.getMake()), "Make required"))
                .model(req(clean(payload.getModel()), "Model required"))
                .grade(clean(payload.getGrade()))
                .chassisCode(clean(payload.getChassisCode()))
                .year(req(payload.getYear(), "Year required"))
                .color(req(clean(payload.getColor()), "Color required"))
                .engineCc(payload.getEngineCc())
                .fuel(payload.getFuel())
                .mileage(payload.getMileage())
                .auctionPoint(payload.getAuctionPoint())
                .status(req(payload.getStatus(), "Status required"))
                .price(req(payload.getPrice(), "Price required"))
                .transmission(payload.getTransmission())
                .seats(payload.getSeats())
                .doors(payload.getDoors())
                .build();

        attachImages(car, images);
        return stockRepo.save(car);
    }

    @Transactional
    public StockCar update(Long id, StockCar payload, List<MultipartFile> images) {
        if (id == null) throw new IllegalArgumentException("Invalid id");
        StockCar car = stockRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));

        if (payload != null) {
            if (StringUtils.hasText(payload.getStockId()) && !payload.getStockId().equals(car.getStockId())) {
                String newStockId = clean(payload.getStockId());
                if (stockRepo.existsByStockId(newStockId)) throw new IllegalArgumentException("Stock ID already exists");
                car.setStockId(newStockId);
            }

            if (StringUtils.hasText(payload.getChassisNo()) && !payload.getChassisNo().equals(car.getChassisNo())) {
                String newChassis = clean(payload.getChassisNo());
                if (stockRepo.existsByChassisNo(newChassis)) throw new IllegalArgumentException("Chassis already exists");
                car.setChassisNo(newChassis);
            }

            setIfText(payload.getMake(), car::setMake);
            setIfText(payload.getModel(), car::setModel);
            car.setGrade(clean(payload.getGrade()));
            car.setChassisCode(clean(payload.getChassisCode()));
            if (payload.getYear() != null) car.setYear(payload.getYear());
            setIfText(payload.getColor(), car::setColor);
            if (payload.getEngineCc() != null) car.setEngineCc(payload.getEngineCc());
            if (payload.getFuel() != null) car.setFuel(payload.getFuel());
            if (payload.getMileage() != null) car.setMileage(payload.getMileage());
            if (payload.getAuctionPoint() != null) car.setAuctionPoint(payload.getAuctionPoint());
            if (payload.getStatus() != null) car.setStatus(payload.getStatus());
            if (payload.getPrice() != null) car.setPrice(payload.getPrice());
            if (payload.getTransmission() != null) car.setTransmission(payload.getTransmission());
            if (payload.getSeats() != null) car.setSeats(payload.getSeats());
            if (payload.getDoors() != null) car.setDoors(payload.getDoors());
        }

        if (images != null && !images.isEmpty()) attachImages(car, images);

        return stockRepo.save(car);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Invalid id");
        stockRepo.deleteById(id);
    }

    private void attachImages(StockCar car, List<MultipartFile> images) {
        if (images == null) return;
        for (MultipartFile f : images) {
            if (f == null || f.isEmpty()) continue;
            String saved = fileStorage.saveImage(f, "stock");
            if (saved == null) continue;
            car.getImages().add(StockImage.builder().stockCar(car).imagePath(saved).build());
        }
    }

    private static void putUniq(Map<Long, StockCar> uniq, StockCar x) {
        if (x != null && x.getId() != null) uniq.put(x.getId(), x);
    }

    private static String req(String v, String msg) {
        if (!StringUtils.hasText(v)) throw new IllegalArgumentException(msg);
        return v;
    }

    private static <T> T req(T v, String msg) {
        if (v == null) throw new IllegalArgumentException(msg);
        return v;
    }

    private static void setIfText(String v, java.util.function.Consumer<String> setter) {
        String t = clean(v);
        if (StringUtils.hasText(t)) setter.accept(t);
    }

    private static String clean(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isBlank() ? null : t;
    }
    public StockCar getById(Long id){
        if(id == null) throw new IllegalArgumentException("Invalid id");
        return stockRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

}

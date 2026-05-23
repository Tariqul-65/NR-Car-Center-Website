package com.example.nrcarcenter.service;

import com.example.nrcarcenter.entity.DeliveredCar;
import com.example.nrcarcenter.repository.DeliveredCarRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DeliveredService {

    private final DeliveredCarRepository repo;
    private final FileStorageService fileStorage;

    public List<DeliveredCar> list(String q) {
        String s = clean(q);
        if (!StringUtils.hasText(s)) return repo.findAll();

        Map<Long, DeliveredCar> uniq = new LinkedHashMap<>();
        repo.findByChassisNoContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));
        repo.findByCustomerNameContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));
        repo.findByMakeContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));
        repo.findByModelContainingIgnoreCase(s).forEach(x -> putUniq(uniq, x));

        return new ArrayList<>(uniq.values());
    }

    @Transactional
    public DeliveredCar create(DeliveredCar payload, MultipartFile image) {
        if (payload == null) throw new IllegalArgumentException("Invalid payload");

        DeliveredCar car = DeliveredCar.builder()
                .chassisNo(req(clean(payload.getChassisNo()), "Chassis No required"))
                .make(req(clean(payload.getMake()), "Make required"))
                .model(req(clean(payload.getModel()), "Model required"))
                .customerName(req(clean(payload.getCustomerName()), "Customer required"))
                .deliveryDate(req(payload.getDeliveryDate(), "Delivery date required"))
                .deliveryCost(req(payload.getDeliveryCost(), "Delivery cost required"))
                .build();

        String saved = fileStorage.saveImage(image, "delivered");
        car.setImagePath(saved);

        return repo.save(car);
    }

    @Transactional
    public DeliveredCar update(Long id, DeliveredCar payload, MultipartFile image) {
        if (id == null) throw new IllegalArgumentException("Invalid id");
        DeliveredCar car = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));

        if (payload != null) {
            setIfText(payload.getChassisNo(), car::setChassisNo);
            setIfText(payload.getMake(), car::setMake);
            setIfText(payload.getModel(), car::setModel);
            setIfText(payload.getCustomerName(), car::setCustomerName);
            if (payload.getDeliveryDate() != null) car.setDeliveryDate(payload.getDeliveryDate());
            if (payload.getDeliveryCost() != null) car.setDeliveryCost(payload.getDeliveryCost());
        }

        if (image != null && !image.isEmpty()) {
            String saved = fileStorage.saveImage(image, "delivered");
            car.setImagePath(saved);
        }

        return repo.save(car);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Invalid id");
        repo.deleteById(id);
    }

    private static void putUniq(Map<Long, DeliveredCar> uniq, DeliveredCar x) {
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
}

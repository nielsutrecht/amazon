package com.nibado.amazon.service.browser.buckets;

import com.nibado.amazon.service.browser.buckets.dto.BucketDTO;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/bucket")
public class BucketController {
    private static final BucketListResponse ALL_BUCKETS = new BucketListResponse(Collections.singletonList(BucketDTO.of("nibado-backup")));

    @GetMapping
    public BucketListResponse list() {
        return ALL_BUCKETS;
    }

    @Data
    public static class BucketListResponse {
        private final List<BucketDTO> buckets;
    }
}

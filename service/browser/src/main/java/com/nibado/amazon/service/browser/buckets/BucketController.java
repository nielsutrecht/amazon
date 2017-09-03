package com.nibado.amazon.service.browser.buckets;

import com.amazonaws.services.s3.model.ObjectListing;
import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.service.browser.buckets.dto.BucketDTO;
import com.nibado.amazon.service.browser.buckets.dto.ObjectDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bucket")
public class BucketController {
    private static final BucketListResponse ALL_BUCKETS = new BucketListResponse(Collections.singletonList(BucketDTO.of("nibado-backup")));

    private final S3 s3;

    @Autowired
    public BucketController(final S3 s3) {
        this.s3 = s3;
    }

    @GetMapping
    public BucketListResponse list() {
        return ALL_BUCKETS;
    }

    @GetMapping("/{bucket}")
    public ObjectListResponse list(@PathVariable("bucket") final String bucket, @RequestParam(value = "prefix", required = false) final String prefix) {
        ObjectListing list = prefix == null ? s3.listObjects(bucket) : s3.listObjects(bucket, prefix);

        List<ObjectDTO> objects = list.getObjectSummaries().stream().map(ObjectDTO::from).collect(Collectors.toList());

        return new ObjectListResponse(objects);
    }

    @Data
    public static class ObjectListResponse {
        private final List<ObjectDTO> objects;
    }

    @Data
    public static class BucketListResponse {
        private final List<BucketDTO> buckets;
    }
}

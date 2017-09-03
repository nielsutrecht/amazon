package com.nibado.amazon.service.browser.objects;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectListing;
import com.nibado.amazon.lib.s3wrapper.S3;
import com.nibado.amazon.service.browser.objects.dto.ObjectDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/bucket/{bucket}")
public class ObjectController {
    private final S3 s3;

    @Autowired
    public ObjectController(final S3 s3) {
        this.s3 = s3;
    }

    @GetMapping
    public ObjectListResponse list(@PathVariable("bucket") final String bucket, @RequestParam(value = "prefix", required = false) final String prefix) {
        ObjectListing list = prefix == null ? s3.listObjects(bucket) : s3.listObjects(bucket, prefix);

        List<ObjectDTO> objects = list.getObjectSummaries().stream().map(ObjectDTO::from).collect(Collectors.toList());

        return new ObjectListResponse(objects   );
    }

    @GetMapping(value = "/dl")
    public ResponseEntity<ObjectResponse> get(@PathVariable("bucket") final String bucket, @RequestParam("key") final String key) {
        log.info("Test {}", key);
        try {
            return ResponseEntity.ok(new ObjectResponse(s3.get(bucket, key)));
        } catch (AmazonS3Exception e) {
            if(e.getStatusCode() == 404) {
                log.info("Key {} not found", key);
                return ResponseEntity.notFound().build();
            } else {
                throw e;
            }
        }
    }

    @Data
    public static class ObjectListResponse {
        private final List<ObjectDTO> objects;
    }

    @Data
    public static class ObjectResponse {
        private final String redirect;
    }
}

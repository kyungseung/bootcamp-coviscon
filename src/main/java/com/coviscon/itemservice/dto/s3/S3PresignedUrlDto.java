package com.coviscon.itemservice.dto.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class S3PresignedUrlDto {
    private String preSignedUrl;
}

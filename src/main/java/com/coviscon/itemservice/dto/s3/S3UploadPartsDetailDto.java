package com.coviscon.itemservice.dto.s3;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class S3UploadPartsDetailDto {
    private String awsETag;
    private int partNumber;
}

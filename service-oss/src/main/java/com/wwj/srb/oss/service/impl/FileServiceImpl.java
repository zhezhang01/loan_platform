package com.wwj.srb.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.wwj.srb.oss.service.FileService;
import com.wwj.srb.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    /**
     * Upload file to alibaba cloud
     *
     * @param inputStream file input stream
     * @param module      file type
     * @param fileName    file name
     * @return url address of the file
     */
    @Override
    public String upload(InputStream inputStream, String module, String fileName) {
        // create OSSClient object
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
        // Check if BUCKET_NAME exist
        if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)) {
            // Create a new one if not exist
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            // Set access permissions to read / write
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }
        // upload file stream
        // Create file directory structure
        String timeFolder = new DateTime().toString("/yyyy/MM/dd/");
        // Generate file name
        fileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        String key = module + timeFolder + fileName;
        ossClient.putObject(OssProperties.BUCKET_NAME, key, inputStream);
        // close OSSClient
        ossClient.shutdown();
        // return url address of the file
        return "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + key;
    }

    /**
     * delete oss file
     *
     * @param url url of the address
     */
    @Override
    public void removeFile(String url) {
        // create OSSClient object
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
        // original url：https://srb-file-wwj.oss-cn-beijing.aliyuncs.com/test/2021/05/05c8c9f2ed-3f01-4a31-a38b-6ceeda2e97a5.png
        // objectName：test/2021/05/05c8c9f2ed-3f01-4a31-a38b-6ceeda2e97a5.png
        String host = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/";
        // pre-processing the url string
        String objectName = url.substring(host.length());
        // delete file
        ossClient.deleteObject(OssProperties.BUCKET_NAME, objectName);
        // close OSSClient
        ossClient.shutdown();
    }
}

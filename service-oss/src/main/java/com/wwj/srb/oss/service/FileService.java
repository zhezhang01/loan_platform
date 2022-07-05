package com.wwj.srb.oss.service;

import java.io.InputStream;

public interface FileService {

    /**
     * Upload the file to Alibaba Cloud
     *
     * @param inputStream file input stream
     * @param module      file type
     * @param fileName    file name
     * @return url of the file
     */
    String upload(InputStream inputStream, String module, String fileName);

    /**
     * delete oss file
     *
     * @param url url of the file
     */
    void removeFile(String url);
}

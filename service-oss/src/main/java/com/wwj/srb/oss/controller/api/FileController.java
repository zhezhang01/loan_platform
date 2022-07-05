package com.wwj.srb.oss.controller.api;

import com.wwj.common.result.R;
import com.wwj.common.result.ResponseEnum;
import com.wwj.common.result.exception.BusinessException;
import com.wwj.srb.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Api(tags = "Alibaba cloud file management")
@RestController
//@CrossOrigin
@RequestMapping("/api/oss/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("file upload")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(value = "file", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "module", required = true)
            @RequestParam("module") String module) {
        try {
            // Get file stream
            InputStream inputStream = file.getInputStream();
            // Get file extension
            String originalFilename = file.getOriginalFilename();
            String url = fileService.upload(inputStream, module, originalFilename);
            return R.ok().message("File uploaded successfully").data("url", url);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }

    @ApiOperation("delete oss file")
    @DeleteMapping("/remove")
    public R remove(
            @ApiParam(value = "Files to delete", required = true)
            @RequestParam("url") String url) {
        fileService.removeFile(url);
        return R.ok().message("successfully removed");
    }
}

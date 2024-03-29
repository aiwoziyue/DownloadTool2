package team.redrock.downloadtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import team.redrock.downloadtool.service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadController {

    @Autowired
    FileService fileService;

    @PostMapping(value = "/upload")
    @ResponseBody
    public String  upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty())
            return "文件为空";

        String username = request.getSession().getAttribute("user").toString();
        String remark = request.getParameter("filename");// 备注
        String fileName = file.getOriginalFilename();// 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));// 后缀名

        if (fileService.isFileExist(fileName, username))
            return "已存在同名文件！";

        String filePath =  "D://temp/" +  UUID.randomUUID() + suffixName;
        File dest = new File(filePath);

        if (!dest.getParentFile().exists()) // 目录不存在则创建
            dest.getParentFile().mkdirs();

        try {
            file.transferTo(dest);
            fileService.fileUpload(fileName, filePath, suffixName, remark, username);
            return "上传成功";
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

}

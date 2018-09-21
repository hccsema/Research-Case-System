package oil.controller;

import oil.model.Case;
import oil.model.Doc;
import oil.service.CaseService;
import oil.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by  waiter on 18-9-20  下午2:44.
 *
 * @author waiter
 */
@Controller
@RequestMapping(value = "/file")
public class FileController {
    @Autowired
    private DocService docService;
    @Autowired
    private CaseService caseService;

    @Value("${doc.path}")
    private String basePath;

    /**
     * 上传文件
     * @param files 文件
     * @param caseId 案例id
     * @param type 类型： 1为案例内容，2为解决方案
     * @return
     * @throws IOException
     */
    @ResponseBody
    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/uploads/{caseId}/{type}")
    public Object uploads(@RequestParam("files")MultipartFile[] files, @PathVariable() Long caseId, @PathVariable() Integer type) throws IOException {

        Case byId = caseService.findById(caseId);
        for (MultipartFile multipartFile:files){
            Doc doc = new Doc();
            doc.setPath("/"+byId.getType().getName()+"/"+byId.getName()+"/"+multipartFile.getOriginalFilename());
            doc.setName(multipartFile.getOriginalFilename());
            doc.setDownCount(0L);
            File file = new File(basePath+doc.getPath());
            /**
             * 创建文件目录
             */
            creatDir(file.getParentFile());
            multipartFile.transferTo(file);
            if (type==1){
                List<Doc> contents = byId.getContents();
                contents.add(doc);
            }else {
                List<Doc> solves = byId.getSolves();
                solves.add(doc);
            }
            docService.save(doc);
        }
        caseService.save(byId);
        return null;
    }


    @RequestMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downLoad(@PathVariable(value = "id") Long id) throws IOException {

        Doc byId = docService.findById(id);


        FileSystemResource file = new FileSystemResource(basePath+byId.getPath());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        byId.setDownCount(byId.getDownCount()+1);
        docService.save(byId);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }


    private void creatDir(File file){
         while (!file.exists()){
             creatDir(file.getParentFile());
             file.mkdir();
         }
    }
}

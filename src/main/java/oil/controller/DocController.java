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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Created by  waiter on 18-9-20  下午2:44.
 *
 * @author waiter
 */
@Controller
@RequestMapping(value = "/doc")
public class DocController {
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
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/uploads/{caseId}/{type}")
    public String uploads(@RequestParam("files")MultipartFile[] files,
                        @PathVariable() Case caseId,
                        @PathVariable() Integer type) throws IOException {
        Assert.notEmpty(files,"没有要上传的文件");
        Assert.notNull(type,"没有选择上传的文件类别");

        for (MultipartFile multipartFile:files){
            Doc doc = new Doc();
            doc.setPath("/"+caseId.getType().getName()+"/"+caseId.getName()+"/"+multipartFile.getOriginalFilename());
            doc.setName(multipartFile.getOriginalFilename());
            doc.setUploadDate(new Date());
            doc.setDownCount(0L);
            File file = new File(basePath+doc.getPath());
            /**
             * 创建文件目录
             */
            creatDir(file.getParentFile());
            multipartFile.transferTo(file);
            if (type==1){
                List<Doc> contents = caseId.getContents();
                contents.add(doc);
            }else {
                List<Doc> solves = caseId.getSolves();
                solves.add(doc);
            }
            doc.setACase(caseId);
            docService.save(doc);
        }
        caseService.save(caseId);
        return "redirect:"+"/case/get/"+caseId.getId()+"/case_info.html";
    }


    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downLoad(@PathVariable(value = "id") Doc doc) throws Exception {

        Assert.notNull(doc,"文件不存在");

        FileSystemResource file = new FileSystemResource(basePath+doc.getPath());
        String s = new String(file.getFilename().getBytes(), StandardCharsets.UTF_8);
        System.out.println(s);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; fileName="+  s +";filename*=utf-8''"+ URLEncoder.encode(s,"UTF-8"));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        doc.setDownCount(doc.getDownCount()+1);
        docService.save(doc);
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


    @GetMapping(value = "/remove/{id}")
    public String remove(@PathVariable(value = "id")Doc doc){
        Assert.notNull(doc,"文件不存在");
        Case aCase = doc.getACase();
        File file = new File(basePath + doc.getPath());
        file.delete();
        List<Doc> solves = aCase.getSolves();
        List<Doc> contents = aCase.getContents();
        solves.remove(doc);
        contents.remove(doc);
        aCase.setContents(contents);
        aCase.setSolves(solves);
        caseService.save(aCase);
        docService.remove(doc);
        return "redirect:"+"/case/get/"+aCase.getId()+"/case_info.html";
    }
}

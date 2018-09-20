package oil.controller;

import oil.model.Case;
import oil.model.Doc;
import oil.service.CaseService;
import oil.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by  waiter on 18-9-20  下午2:44.
 *
 * @author waiter
 */
@RestController
public class FileController {
    @Autowired
    private DocService docService;
    @Autowired
    private CaseService caseService;

    @Value("${doc.path}")
    private String basePath;

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/file/uploads/{caseId}/{type}")
    public Object uploads(@RequestParam("files")MultipartFile[] files, @PathVariable() Long caseId, @PathVariable() Integer type) throws IOException {
        Case byId = caseService.findById(caseId);
        for (MultipartFile multipartFile:files){
            Doc doc = new Doc();
            doc.setPath("/"+byId.getName()+"/"+multipartFile.getOriginalFilename());
            doc.setName(multipartFile.getOriginalFilename());
            doc.setDownCount(0L);
            File file = new File(basePath+doc.getPath());
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdir();
            }
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
}

package home.boottest1.controllers;

import home.boottest1.entities.Files;
import home.boottest1.entities.TaskStatus;
import home.boottest1.entities.Tasks;
import home.boottest1.entities.Users;
import home.boottest1.repos.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
@Controller
public class FilesController {
  //  @Value( "${filepath}" ) private static String userfilepath;
    @Autowired
    private Environment env;

    @Autowired
    FilesRepository repo;


    @RequestMapping(value = { "/files", "/files/list" }, method = RequestMethod.GET)
    public String userorder(Model model) {
        final String userfilepath = env.getProperty("filepath");
        List<Files> files= repo.findAll(); model.addAttribute("list", files);
        model.addAttribute("userfilepath", userfilepath);
        return "files/list";
    }

    @RequestMapping(value = {"/files/jlist" }, method = RequestMethod.GET)
    @ResponseBody
    public List<Files> jlist() throws LogicException {
        List<Files> files= repo.findAll(); return files;
    }
    @RequestMapping(value = "/files/list/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Files findById(Model model, @PathVariable("id") Long id) throws Exception {
        Files files = repo.findById(id).orElse(null); //  model.addAttribute("list", users);
        return files;
    }

    @RequestMapping(value = { "/files/edit" , "/files/edit/{uid}"}, method = RequestMethod.GET)
    public String edit(ModelMap model, @PathVariable(required = false, value = "uid") Long uid,
                       @RequestParam(required = false, value = "id") Long id) {
        if (uid!=null) {id=uid;}
        System.out.println("============================= edit files id " + id);
        Files files=null;
        if(id != null) {
            try {
                files = repo.findById(id).orElse(null);
                if (files==null) {throw new LogicException("no entity with this id");}
                System.out.println("]============================= edit files" + files);
            } catch (LogicException e) {e.printStackTrace(); files = new Files();}
        }
        else {files = new Files();}
        model.addAttribute("files", files);

        return "/files/edit";
    }

    @RequestMapping(value = "/files/save", method = RequestMethod.POST)
    @ResponseBody
    public String saveUser(ModelMap model, final @RequestParam(required = false, value = "filesid") Long filesid,
                           @RequestParam(required = false, value = "fileName") String fileName,
    @RequestParam MultipartFile file) throws LogicException {
        if (file==null) {return "error";}
        final String userfilepath = env.getProperty("filepath");
        if (filesid!=null) {
            Files files0 = repo.findById(filesid).orElse(null);
            fileName = files0.getUrl();
        }
         if (fileName==null || fileName.isBlank()) {fileName = userfilepath;}
        System.out.println("]============================= save files fileName " + fileName);
        try{
            final String sfile = file.getOriginalFilename();
            System.out.println("]============================= save sfile " + sfile);
            byte[] bytes = file.getBytes();
            System.out.println("]============================= save files sfile " + ( userfilepath+sfile));
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream( userfilepath+sfile));
            stream.write(bytes); stream.close();
            Files files = new Files(); files.setUrl( sfile);
            repo.save(files);
            return "file saved: " + sfile;
        } catch (Exception e) { return "saving failed: " + fileName; }
    }

    @RequestMapping(value= {"/files/delete" }, method = RequestMethod.POST)
    @ResponseBody
    public String deleteUser(ModelMap model, @RequestParam(required = false) Long id, @RequestParam(required = false) String realdelete) throws LogicException {
        String filedel="", dbfile="";
        if (id==null) {dbfile= "select the file";}
       else {
            Files files = repo.findById(id).orElse(null);
            if (files == null) { dbfile= "there is no such a file on the disc"; }
            else {
               if (realdelete!=null && !realdelete.isBlank()) {
                   try {
                       File file = new File(files.getUrl());
                       if (file!=null) {file.delete();filedel = "the real file deleted";}
                       else {filedel = "the real file is not exist";}
                   } catch (Exception e) {filedel="cannot delete the real file";}
               } else {filedel="no need to delete the real file";}
                repo.deleteById(id);
                dbfile = "attempted to delete the file from the base";
            }
        }


        return "<p>" + dbfile + "</p>" + "<p>" + filedel + "</p>";
    }
/*
    @RequestMapping(value = "/files/{file_name:.+}", method = RequestMethod.GET)
    public void getFile0(@PathVariable("file_name") String fileName, HttpServletResponse response) {
        Path file = Paths.get(PathUtil.getUploadedFolder(), fileName);
        if (Files.exists(file)){
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setContentType("application/vnd.ms-excel");

            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException e) {
                LOG.info("Error writing file to output stream. Filename was '{}'" + fileName, e);
                throw new RuntimeException("IOError writing file to output stream");
            }
        }
    } */
    @RequestMapping(value = { "/files/download" }, method = RequestMethod.GET)
    public void getFile(ModelMap model, @RequestParam String filename,  HttpServletResponse response) {
        try {

            final String userfilepath = env.getProperty("filepath");
            System.out.println("]=============================download fileName " + ( filename));
            File initialFile = new File(userfilepath + filename);
            InputStream targetStream = new FileInputStream(initialFile);
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(targetStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
           System.out.println("Error writing file to output stream. Filename was '{}'" + filename);
            throw new RuntimeException("IOError writing file to output stream");
        }

    }

}

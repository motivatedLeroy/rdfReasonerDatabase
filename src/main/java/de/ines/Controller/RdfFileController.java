package de.ines.Controller;

import de.ines.domain.RdfFile;
import de.ines.services.RdfFileService;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import org.apache.jena.util.FileManager;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;


@RestController
public class RdfFileController {

    @Autowired
    public RdfFileService rdfFileService;

    @RequestMapping(value = "/loadAll", method = RequestMethod.GET)
    public ArrayList<RdfFile> loadAll() {
        return rdfFileService.loadRdfFiles();
    }

    @RequestMapping(value = "/loadSingle", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] loadSingle(@RequestParam("fileName") String fileName){
        Path path = Paths.get("src/main/resources/uploads/"+fileName);
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @RequestMapping(value = "/saveRdfFile", method = RequestMethod.POST)
    public String saveRdfFile(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName, @RequestParam("tags") String tags, @RequestParam("rating") double rating, @RequestParam("dateOfUpload") String
            dateOfUpload, @RequestParam("entries") long numberOfEntries, @RequestParam("provider") String provider, @RequestParam("type") String type) {

        rdfFileService.saveRdfFile(new RdfFile(fileName, tags, rating, dateOfUpload, numberOfEntries, provider, type));
        String name = fileName + "." + type;
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/uploads/" + name)));
                stream.write(bytes);
                stream.close();
                return "File was successfully uploaded";
            } catch (Exception e) {
                return "There has been an exception: " + e;
            }
        } else {
            return "Your file was empty and could not be stored.";
        }
    }



    @RequestMapping(value = "/showSchema", method = RequestMethod.POST)
    public ArrayList<String[]> showSchema(@RequestParam("fileName") String fileName) {
        try {
            Reasoner reasoner = GenericRuleReasonerFactory.theInstance().create(null);
            Model model = FileManager.get().loadModel("src/main/resources/uploads/" + fileName);
            InfModel infmodel = ModelFactory.createInfModel(reasoner, model);

            StmtIterator iterator = model.listStatements();
            ArrayList<String[]> statements = new ArrayList<>();
            HashSet<String> tempSub0 = new HashSet<>();
            HashSet<String> tempPred = new HashSet<>();

            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();  // get next statement
                Resource subject = stmt.getSubject();     // get the subject
                Property predicate = stmt.getPredicate();   // get the predicate
                //RDFNode object = stmt.getObject();      // get the object

                String[] statement = new String[]{subject.toString(), predicate.toString()};

                String[] sub = statement[0].split("/");
                String tempSub1 = "";
                for (int i = 0; i < sub.length; i++) {
                    if (i < sub.length - 1) {
                        tempSub1 += sub[i];
                    }
                }
                String[] pred = statement[1].split("/");

                if (tempSub0.contains(tempSub1) && tempPred.contains(pred[pred.length - 1])) {
                    continue;
                }
                if (tempSub0.contains(tempSub1)) {
                    statement[0] = "";
                    statement[1] = " --> " + pred[pred.length - 1];
                } else {
                    statement[0] = tempSub1;
                    statement[1] = " --> " + pred[pred.length - 1];
                }
                tempSub0.add(tempSub1);
                tempPred.add(pred[pred.length - 1]);

                statements.add(statement);

            }

            return statements;
        }catch(Exception ex){
            return null;
        }
    }

    @RequestMapping(value = "/reason", method = RequestMethod.POST)
    public String reason(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file){
        return rdfFileService.reason(fileName, file);
    }

}
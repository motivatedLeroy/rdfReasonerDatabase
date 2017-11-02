package de.ines.services;

import de.ines.domain.RdfFile;
import de.ines.repositories.RdfFileRepository;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

@Service
public class RdfFileService {

    @Autowired
    RdfFileRepository rdfFileRepository;

    public ArrayList<RdfFile> loadRdfFiles(){
        //rdfFileRepository.save(new RdfFile("test","testTags",5.0,"10.05.17", 18,"HambrechtAG", "rdf"));
        ArrayList<RdfFile> result = new ArrayList<>();
        Iterator<RdfFile> files = rdfFileRepository.findAll().iterator();
        while(files.hasNext()){
            result.add(files.next());
        }
        return result;
    }

    public void saveRdfFile(RdfFile file){
        rdfFileRepository.save(file);
    }

    public File loadSingle(String name){
        return new File("src/main/resources/uploads/"+name);
    }

    public String reason(String fileName, MultipartFile file){
        if (!file.isEmpty()) {


            try {
                /*
                File convFile = new File( file.getOriginalFilename());
                file.transferTo(convFile);
                convFile.createNewFile();*/

                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/ruleSets/ruleSet.rules")));
                stream.write(bytes);
                stream.close();


                File rules = new File("src/main/resources/ruleSets/ruleSet.rules");
                Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL(rules.getAbsolutePath()));
                Model model = FileManager.get().loadModel("src/main/resources/uploads/"+fileName);

                reasoner.setDerivationLogging(true);
                InfModel inf = ModelFactory.createInfModel(reasoner, model);

                StmtIterator stmtIterator = inf.listStatements(null, null, (RDFNode)null);
                inf.getDeductionsModel().write(System.out);
                while (stmtIterator.hasNext()) {
                    //System.out.println(" - " + PrintUtil.print(stmtIterator.nextStatement()));
                }

                return "Reasoning Complete!";
            } catch (Exception e) {
                return "There has been an exception: " + e;
            }
        } else {
            return "Your file was empty and could not be stored.";
        }
    }
}

package de.ines.services;

import de.ines.domain.RdfFile;
import de.ines.repositories.RdfFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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
}

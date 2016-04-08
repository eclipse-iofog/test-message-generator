package com.iotracks.utils;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Utils class to open files and save data.
 *
 * Created by forte on 3/29/16.
 */
public class TMGFileUtils {

    private static final Logger log = Logger.getLogger(TMGFileUtils.class.getName());

    private static final String FILE_PATH = "./files/";

    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    /**
     * Method opens file based on its name.
     *
     * @param filename - name of the file to open
     *
     * @return FileReader
     */
    public static FileReader readFile(String filename){
        String fullname = FILE_PATH + filename;
        if (!Files.exists(Paths.get(fullname), LinkOption.NOFOLLOW_LINKS)) {
            log.warning("Can't find file by path: " + fullname);
            return null;
        }
        try {
            return new FileReader(new File(fullname));
        } catch (FileNotFoundException e) {
            log.warning("Can't find file by path: " + fullname + ". " + e.getMessage());
            return null;
        }
    }

    /**
     * Method opens XML document.
     *
     * @param filename - name of the file to open
     *
     * @return Document
     */
    public static Document getXMLDocument(String filename){
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(FILE_PATH + filename);
        } catch (ParserConfigurationException e) {
            log.warning("Can't parse XML file " + FILE_PATH + filename + ". " + e.getMessage());
        } catch (SAXException e) {
            log.warning("SAXException with " + FILE_PATH + filename + ". " + e.getMessage());
        } catch (IOException e) {
            log.warning("IOException with " + FILE_PATH + filename + ". " + e.getMessage());
        }
        return null;
    }

    /**
     * Method saves XML file data to specified file.
     *
     * @param document - XML data to be saved
     * @param filename - name of the file to save to data
     *
     */
    public static void saveFile(Document document, String filename){
        try {
            OutputFormat ouFormat = new OutputFormat(document);
            ouFormat.setIndenting(true);
            XMLSerializer xmlSerializer = new XMLSerializer(new FileOutputStream(FILE_PATH + filename), ouFormat);
            xmlSerializer.serialize(document);
        } catch (FileNotFoundException e) {
            log.warning("Can't find XML file " + FILE_PATH + filename + ". " + e.getMessage());
        } catch (IOException e) {
            log.warning("IOException: Couldn't save xml file " + FILE_PATH + filename + ". " + e.getMessage());
        }
    }
}

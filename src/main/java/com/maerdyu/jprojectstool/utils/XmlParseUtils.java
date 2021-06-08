package com.maerdyu.jprojectstool.utils;

import com.maerdyu.jprojectstool.handler.SaxHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * @author jinchun
 * @date 2021/03/26 14:04
 **/
public class XmlParseUtils {

    public static void parse(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        SaxHandler handler = new SaxHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(xmlPath, handler);
    }
}
package com.maerdyu.jprojectstool.handler;

import com.alibaba.fastjson.JSON;
import com.maerdyu.jprojectstool.dto.maven.MavenConf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author jinchun
 * @date 2021/03/26 15:29
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SaxHandler extends DefaultHandler {

    private MavenConf mavenConf;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println(uri + qName + localName + JSON.toJSONString(attributes));
    }
}
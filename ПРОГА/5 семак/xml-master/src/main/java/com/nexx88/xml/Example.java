package com.nexx88.xml;

import com.nexx88.xml.lexer.Lexem;
import com.nexx88.xml.lexer.TokenizeException;
import com.nexx88.xml.lexer.XmlLexer;
import com.nexx88.xml.model.Document;
import com.nexx88.xml.model.Element;
import com.nexx88.xml.parser.XmlParseException;
import com.nexx88.xml.parser.XmlParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class Example {
    public static void main(String[] args) throws IOException, TokenizeException, XmlParseException {
        try(InputStream input = Example.class.getResourceAsStream("example.xml")) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            IOUtils.copy(input, output);

            String xml = output.toString("UTF-8");
            XmlLexer lexer = new XmlLexer(xml);

            List<Lexem> lexems = lexer.tokenize();
            XmlParser parser = new XmlParser();
            Document document = parser.parse(lexems);
            Element e1 = document.getElement();
            int count = 0;
            System.out.println(e1.getName());
            count++;
            for(Element element : e1.getChildren()){
                    for(int i = 0; i < count; i++){
                        System.out.printf("\t");
                    }
                    System.out.println(element.getName());
                    if(element.getText() != null) System.out.printf(element.getText());
                    if(element.getChildren().size() != 0) printElement(element, count+1);
                }
            }

        }


    public static void printElement(Element element, int count){
            for(Element e1 : element.getChildren()){
                for(int i = 0; i < count; i++){
                    System.out.printf("\t");
                }
                System.out.print(e1.getName());
                if(e1.getText() != null) System.out.printf(" = "+e1.getText());
                System.out.println();
                if(e1.getChildren().size() != 0) printElement(e1, count+1);
            }
    }
}

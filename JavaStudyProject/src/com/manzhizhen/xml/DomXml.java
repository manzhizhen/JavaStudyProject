package com.manzhizhen.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * DOM生成和解析XML文档
 * 为 XML 文档的已解析版本定义了一组接口。解析器读入整个文档，然后构建一个驻留内存的树结构，
 * 然后代码就可以使用 DOM 接口来操作这个树结构。优点：整个文档树在内存中，便于操作；支持删除、
 * 修改、重新排列等多种功能；缺点：将整个文档调入内存（包括无用的节点），浪费时间和空间；
 * 使用场合：一旦解析了文档还需多次访问这些数据；硬件资源充足（内存、CPU）。 
 * @author Administrator
 *
 */
public class DomXml {
	public static void main(String[] args) {
		File file = new File("d://test.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document doc = documentBuilder.parse(file);		// 通过xml文件创建Docuemnt对象
//			Document doc2 = documentBuilder.newDocument();	// 创建一个空的Docuemnt对象
			
			Element rootElement = doc.getDocumentElement();	// 获取根元素
			NodeList nodeList = rootElement.getChildNodes();
			for(int index = nodeList.getLength(), i = 0; i < index; i++ ) {
				System.out.println(nodeList.item(i).getNodeName() + ":" + nodeList.item(i).getNodeValue());
			}
			
			// 在students和teachers节点下都添加以为成员
			Element stusE = (Element) doc.getElementsByTagName("students");
			Attr a1 = doc.createAttribute("name");
			a1.setTextContent("路飞");
			Attr a2 = doc.createAttribute("name");
			a2.setTextContent("24");
			stusE.appendChild(a1);
			stusE.appendChild(a2);
			Element descE = doc.createElement("desc");
			descE.setTextContent("我是要成为海贼王的男人！");
			stusE.appendChild(descE);
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			//创建Transformer，它能够将源树转换为结果树
			Transformer transformer = transFactory.newTransformer();
			//接下来设置输出属性
			transformer.setOutputProperty("indent", "yes");
			DOMSource source =new DOMSource();
			source.setNode(doc);
			StreamResult result = new StreamResult();
			result.setOutputStream(new FileOutputStream(file));
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

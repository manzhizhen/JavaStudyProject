package com.manzhizhen.xml;

/**
 * SAX生成和解析XML文档
 * 为解决DOM的问题，出现了SAX。SAX ，事件驱动。
 * 当解析器发现元素开始、元素结束、文本、文档的开始或结束等时，发送事件，
 * 程序员编写响应这些事件的代码，保存数据。优点：不用事先调入整个文档，占用资源少；
 * SAX解析器代码比DOM解析器代码小，适于Applet，下载。
 * 缺点：不是持久的；事件过后，若没保存数据，那么数据就丢了；无状态性；
 * 从事件中只能得到文本，但不知该文本属于哪个元素；使用场合：Applet;
 * 只需XML文档的少量内容，很少回头访问；机器内存少；
 */
public class SaxXml {

}

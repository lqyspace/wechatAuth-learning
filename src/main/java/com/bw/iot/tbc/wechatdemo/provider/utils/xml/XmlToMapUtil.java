package com.bw.iot.tbc.wechatdemo.provider.utils.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: XmlToMapUtils
 * @Description: xml转map工具类
 * @Author: lengqy
 * @Date: 2024/12/5 10:22
 **/
public class XmlToMapUtil {
    private static Map<String, Object> parseXmlNodeToMap(Node node) {
        Map<String, Object> nodeMap = new HashMap<>();

        // 获取当前节点的所有子节点
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            // 只处理元素节点
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = childNode.getNodeName();
                String nodeText = childNode.getTextContent().trim();

                // 如果当前节点有子节点（递归），则继续解析
                if (childNode.getChildNodes().getLength() > 1) {
                    // 递归调用，解析嵌套的节点
                    Map<String, Object> nestedMap = parseXmlNodeToMap(childNode);
                    nodeMap.put(nodeName, nestedMap);
                } else {
                    // 没有子节点时，直接存储文本值
                    nodeMap.put(nodeName, nodeText.isEmpty() ? null : nodeText);
                }
            }
        }

        return nodeMap;
    }

    public static Map<String, Object> parseXmlToMap(String xmlString) throws Exception {
        // 创建一个 DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 将 XML 字符串解析为 Document 对象
        ByteArrayInputStream input = new ByteArrayInputStream(xmlString.getBytes());
        Document document = builder.parse(input);

        // 获取根元素
        Element root = document.getDocumentElement();

        // 调用递归方法解析根元素
        return parseXmlNodeToMap(root);
    }
}

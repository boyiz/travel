package com.xunye.core.tools;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class XMLUtil {
    private static Logger logger = LoggerFactory.getLogger(XMLUtil.class);

    private XMLUtil() {
    }

    /**
     * 获取根节点下指定的节点 返回节点元素
     */
    @SuppressWarnings("unchecked")
    public static Element getEntitySetElement(Element node,String entitySetName){
        if(node.getName().equals(entitySetName)){
            return node;
        }else{
            //使用递归  同时迭代当前节点下面的所有子节点
            Iterator<Element> iterator = node.elementIterator();
            while(iterator.hasNext()){
                node = iterator.next();
                getEntitySetElement(node ,entitySetName);
            }
        }
        return node;
    }

    /**
     * 将xml文件转换为实体类集合
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> transferXmlToEntityList(Element rootElemnt,String entitySetName, Class<T> clazz) {
        Element entityElement = getEntitySetElement(rootElemnt,entitySetName);
        List<T> list = new ArrayList<T>();
        try {
            Iterator<Element> it = entityElement.elementIterator();// 获取根节点下所有实体类
            while (it.hasNext()) {
                Element itemEle = (Element) it.next();
                T baseBean = (T) XMLUtil.fromXmlToBean(itemEle,clazz);
                list.add(baseBean);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.info("数据解析错误");
        }
        return list;
    }
    /**
     * 将指定的xml 转换为单个实体类
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Object fromXmlToBean(Element entityElement, Class pojo) throws Exception {
        Field[] field = pojo.getDeclaredFields();
        Object obj = pojo.newInstance();
        Iterator<Element> it = entityElement.elementIterator();// 获取实体类节点下所有属性 （如：class下的所有属性元素）
        while (it.hasNext()) {
            Element itemEle = (Element) it.next();// 属性元素
            String propName = itemEle.getName();//属性名称
            String value = itemEle.getText();//属性值
            propName = propName.substring(0, 1).toUpperCase()+ propName.substring(1);//首字母大写
            try {//反射获取所有方法
                Method m = pojo.getMethod("get" + propName);//实体类属性get方法
                Method setMethod = pojo.getDeclaredMethod("set" + propName,m.getReturnType());//实体类属性set方法
                if(m.getReturnType().toString().contains("java.lang")){//基本数据类型，直接set值
                    setMethod.invoke(obj, value);
                }else if(List.class.equals(m.getReturnType())){//集合属性，递归遍历组装数据
                    Iterator<Element> listit = itemEle.elementIterator();//xml集合属性元素（如：班级中的学生集合）
                    List<Object> propLists = new ArrayList<Object>();
                    while (listit.hasNext()) {
                        Element itemele = (Element) listit.next();//集合属性中的实体类xml元素（如单个学生）
                        for (Field field2 : field) {
                            String name = field2.getName(); // 获取集合属性的名字（：：：如学生集合的名称）
                            System.out.println(name+"--"+itemele.getName());
                            if (name.equals(itemEle.getName())) {//如果班级的学生集合属性名称和xml中的学生集合名称一样则获取学生的Class类型
                                //获取List<T> T的类型Start
                                Class fieldClazz = field2.getType();
                                if (fieldClazz.isAssignableFrom(List.class)) {//如果是List类型，
                                    Type fc = field2.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
                                    if (fc instanceof ParameterizedType){ // 如果是泛型参数的类型
                                        ParameterizedType pt = (ParameterizedType) fc;
                                        fieldClazz = (Class) pt.getActualTypeArguments()[0]; //得到泛型里的class类型对象。
                                    }else{
                                        logger.info("参数类型不是泛型参数。。。");
                                    }
                                }
                                //获取List<T> T的类型END
                                Object t = fromXmlToBean(itemele,fieldClazz);//将xml中的单个学生信息转换为学生实体类
                                propLists.add(t);//添加至集合
                            }
                        }
                    }
                    setMethod.invoke(obj, propLists);//将属性集合set到实体类中
                }else{//如果返回为其他类型（实体类）
                    setMethod.invoke(obj, fromXmlToBean(itemEle, m.getReturnType()));
                }
            } catch (Exception e) {//异常，xml中的属性名称和实体类属性名称不一致
// TODO Auto-generated catch block
                e.printStackTrace();
                continue;
            }
        }
        return obj;
    }

    /**
     * 首字母大写
     */
    @SuppressWarnings("unused")
    private static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 首字母小写
     */
    @SuppressWarnings({"rawtypes" })
    private static String lowerCase(Class clazz){
        String objName = clazz.toString();
        objName = objName.substring(objName.lastIndexOf(".")+1);
        objName = objName.substring(0, 1).toLowerCase()+ objName.substring(1);
        return objName;
    }

    /**
     * 将实体类转换为XML
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void entityTransferToXml(Object object, Element root, Class clazz) {
        try {
            // 创建根节点元素
            Element entity = root.addElement(lowerCase(clazz));
            Field[] field = object.getClass().getDeclaredFields(); // 获取实体类b的所有属性，返回Field数组
            for (int j = 0; j < field.length; j++) { // 遍历所有有属性
                String name = field[j].getName(); // 获取属属性的名字
                if (!name.equals("serialVersionUID")) {//去除串行化序列属性
                    name = name.substring(0, 1).toUpperCase()+ name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                    Method m = object.getClass().getMethod("get" + name);

                    Object propertievalue = m.invoke(object);// 获取属性值
                    if(null != propertievalue){
                        name = name.substring(0, 1).toLowerCase()+ name.substring(1);
                        if(m.getReturnType().toString().contains("java.lang")){
                            Element propertie = entity.addElement(name);
                            propertie.setText(propertievalue.toString());
                        }else if(List.class.equals(m.getReturnType())){
                            entityTransferToXml((List<Object>)propertievalue, entity, name);
                        }else{
                            Element propertie = entity.addElement(name);
                            entityTransferToXml(propertievalue, propertie,m.getReturnType());
                        }
                    }
                }
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
    }
    /**
     * 集合转换为XML文件
     */
    private static <T> void entityTransferToXml(List<T> objects,Element root,String name){
        Element entitySet = root.addElement(name);
        for (Object object : objects) {
            entityTransferToXml(object, entitySet,object.getClass());
        }
    }


    public static Document transferToXml(Object obj,  String elementName ){
        Document newDocument = DocumentHelper.createDocument();
        Element root = newDocument.addElement(elementName);
//
//        Element companyID = root.addElement("CompanyID");
//        companyID.setText("12345678");
//        Element msgID = root.addElement("MsgID");
//        msgID.setText("12345678");

        entityTransferToXml(obj, root, obj.getClass());
        return newDocument;
//    return "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+root.asXML();
    }

    /**
     *
     */
    public static <T> Document transferToXml(List<T> objects, String name){
        Document newDocument = DocumentHelper.createDocument();
        Element root = newDocument.addElement("ClientAPI");

//        Element companyID = root.addElement("CompanyID");
//        companyID.setText("12345678");
//        Element msgID = root.addElement("MsgID");
//        msgID.setText("12345678");

        entityTransferToXml(objects, root, name);
        return newDocument;
//    return "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+root.asXML();
    }

    public static String xml2Str(Document newDocument) {
        XMLWriter writer = null;
        String requestXML = null;
        try {
            StringWriter stringWriter = new StringWriter();
            OutputFormat format = new OutputFormat(" ", true);
            writer = new XMLWriter(stringWriter, format);
            writer.write(newDocument);
            writer.flush();
            requestXML = stringWriter.getBuffer().toString();
            return requestXML;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
        return requestXML;
    }
}

package utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class XMLUtil {

	/**
	 * Json to xml string.
	 * 
	 * @param json
	 *            the json
	 * @return the string
	 */
	public static String jsonToXml(JSONObject jObj) {
		try {
			StringBuffer buffer = new StringBuffer();
			//buffer.append("<xml version=\"1.0\" encoding=\"utf-8\">");
			buffer.append("<xml>");
			jsonToXmlstr(jObj, buffer);
			buffer.append("</xml>");
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Json to xmlstr string.
	 * 
	 * @param jObj
	 *            the j obj
	 * @param buffer
	 *            the buffer
	 * @return the string
	 */
	public static String jsonToXmlstr(JSONObject jObj, StringBuffer buffer) {
		Set<Map.Entry<String, Object>> se = jObj.entrySet();
		for (Iterator<Map.Entry<String, Object>> it = se.iterator(); it
				.hasNext();) {
			Map.Entry<String, Object> en = it.next();
			if (en.getValue().getClass().getName()
					.equals("net.sf.json.JSONObject")) {
				buffer.append("<" + en.getKey() + ">");
				JSONObject jo = jObj.getJSONObject(en.getKey());
				jsonToXmlstr(jo, buffer);
				buffer.append("</" + en.getKey() + ">");
			} else if (en.getValue().getClass().getName()
					.equals("net.sf.json.JSONArray")) {
				JSONArray jarray = jObj.getJSONArray(en.getKey());
				for (int i = 0; i < jarray.size(); i++) {
					buffer.append("<" + en.getKey() + ">");
					JSONObject jsonobject = jarray.getJSONObject(i);
					jsonToXmlstr(jsonobject, buffer);
					buffer.append("</" + en.getKey() + ">");
				}
			} else if (en.getValue().getClass().getName()
					.equals("java.lang.String")) {
				buffer.append("<" + en.getKey() + ">" + en.getValue());
				buffer.append("</" + en.getKey() + ">");
			} else {
				buffer.append("<" + en.getKey() + ">" + en.getValue());
				buffer.append("</" + en.getKey() + ">");
			}
		}
		return buffer.toString();
	}
	
	 /**
     * xml转json
     * @param xmlStr
     * @return
     * @throws DocumentException
     */
    public static JSONObject xmlToJson(String xmlStr) throws DocumentException{
        Document doc= DocumentHelper.parseText(xmlStr);
        JSONObject json=new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;
    }

    /**
     * xml转json
     * @param element
     * @param json
     */
    public static void dom4j2Json(Element element,JSONObject json){
        //如果是属性
        for(Object o:element.attributes()){
            Attribute attr=(Attribute)o;
            if(!isEmpty(attr.getValue())){
                json.put("@"+attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl=element.elements();
        if(chdEl.isEmpty()&&!isEmpty(element.getText())){//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for(Element e:chdEl){//有子元素
            if(!e.elements().isEmpty()){//子元素也有子元素
                JSONObject chdjson=new JSONObject();
                dom4j2Json(e,chdjson);
                Object o=json.get(e.getName());
                if(o!=null){
                    JSONArray jsona=null;
                    if(o instanceof JSONObject){//如果此元素已存在,则转为jsonArray
                        JSONObject jsono=(JSONObject)o;
                        json.remove(e.getName());
                        jsona=new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if(o instanceof JSONArray){
                        jsona=(JSONArray)o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                }else{
                    if(!chdjson.isEmpty()){
                        json.put(e.getName(), chdjson);
                    }
                }


            }else{//子元素没有子元素
                for(Object o:element.attributes()){
                    Attribute attr=(Attribute)o;
                    if(!isEmpty(attr.getValue())){
                        json.put("@"+attr.getName(), attr.getValue());
                    }
                }
                if(!e.getText().isEmpty()){
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }

    public static boolean isEmpty(String str) {

        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }
    
	/**
	 * The entry point of application.
	 * 
	 * @param args
	 *            the input arguments
	 */
	public static void main(String[] args) {
		
	}

}

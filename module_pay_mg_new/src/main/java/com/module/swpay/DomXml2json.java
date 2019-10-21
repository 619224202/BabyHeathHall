package com.module.swpay;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DomXml2json {
    public Document document;
    public JSONObject jsonObject;

    public JSONObject xml2Json(String xml){
        try {
            document = DocumentHelper.parseText(xml);
            jsonObject = new JSONObject();
            dome4j2Json(document.getRootElement(),jsonObject);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void dome4j2Json(Element e,JSONObject jobj){
        try {
            //对象属性
            for(Object o:e.attributes()){
                Attribute attribute = (Attribute) o;
                if(isEmpty(attribute.getValue())){
                    jobj.put(attribute.getName(),"");
                }else{
                    jobj.put(attribute.getName(),attribute.getValue());
                }
            }
            //如果没有子元素
            List<Element> childEles = e.elements();
            if(childEles.isEmpty()){
                if(isEmpty(e.getText())){
                    jobj.put(e.getName(),"");
                }else{
                    jobj.put(e.getName(),e.getText());
                }
            }
            for(Element childE:childEles){
                if (!e.elements().isEmpty()) {// 子元素也有子元素
                    JSONObject chdjson = new JSONObject();
                    dome4j2Json(e, chdjson);
                    Object o = jobj.get(e.getName());
                    if (o != null) {
                        JSONArray jsona = null;
                        if (o instanceof JSONObject) {// 如果此元素已存在,则转为jsonArray
                            JSONObject jsono = (JSONObject) o;
                            jobj.remove(e.getName());
                            jsona = new JSONArray();
                            jsona.put(jsono);
                            jsona.put(chdjson);
                        }
                        if (o instanceof JSONArray) {
                            jsona = (JSONArray) o;
                            jsona.put(chdjson);
                        }
                        jobj.put(e.getName(), jsona);
                    } else {
                        jobj.put(e.getName(), chdjson);
                    }

                } else {// 子元素没有子元素
                    for (Object o : e.attributes()) {
                        Attribute attr = (Attribute) o;
                        if (!isEmpty(attr.getValue())) {
                            jobj.put( attr.getName(), attr.getValue());
                        }
                    }
                    if (!e.getText().isEmpty()) {
                        jobj.put(e.getName(), e.getText());
                    }
                }
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    public boolean isEmpty(String str) {

        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }
}

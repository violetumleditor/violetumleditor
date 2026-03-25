package com.horstmann.violet.framework.file.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

@ManagedBean(registeredManually = true)
public class JSONPersistenceService implements IFilePersistenceService {

    static final String FORMAT_VERSION = "violet-json-v1";

    private static final String FIELD_FORMAT = "format";

    private static final String FIELD_ROOT = "root";

    private static final String FIELD_NAME = "name";

    private static final String FIELD_ATTRIBUTES = "attributes";

    private static final String FIELD_CHILDREN = "children";

    private static final String FIELD_TEXT = "text";

    private final IFilePersistenceService xmlPersistenceService;

    public JSONPersistenceService() {
        this(new XMLPersistenceService());
    }

    JSONPersistenceService(IFilePersistenceService xmlPersistenceService) {
        if (xmlPersistenceService == null) {
            throw new IllegalArgumentException("XML persistence service must not be null");
        }
        this.xmlPersistenceService = xmlPersistenceService;
    }

    @Override
    public void write(IGraph graph, OutputStream out) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must not be null");
        }

        try {
            ByteArrayOutputStream xmlOut = new ByteArrayOutputStream();
            this.xmlPersistenceService.write(graph, xmlOut);
            String xml = xmlOut.toString(StandardCharsets.UTF_8);
            Document xmlDocument = parseXml(xml);
            Map<String, Object> jsonDocument = new LinkedHashMap<String, Object>();
            jsonDocument.put(FIELD_FORMAT, FORMAT_VERSION);
            jsonDocument.put(FIELD_ROOT, xmlElementToJson(xmlDocument.getDocumentElement()));
            String json = toJson(jsonDocument);
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("Unable to write JSON graph", e);
        }
    }

    @Override
    public IGraph read(InputStream in) throws IOException {
        String json = readAll(in);
        if (json == null || json.trim().isEmpty()) {
            throw new IOException("Empty JSON content");
        }

        try {
            Object parsed = new JsonParser(json).parse();
            if (!(parsed instanceof Map<?, ?>)) {
                throw new IOException("JSON root must be an object");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> jsonDocument = (Map<String, Object>) parsed;

            Object formatValue = jsonDocument.get(FIELD_FORMAT);
            if (formatValue != null && !FORMAT_VERSION.equals(String.valueOf(formatValue))) {
                throw new IOException("Unsupported JSON persistence format: " + formatValue);
            }

            Object rootValue = jsonDocument.get(FIELD_ROOT);
            if (!(rootValue instanceof Map<?, ?>)) {
                throw new IOException("Missing required JSON object field: root");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> rootObject = (Map<String, Object>) rootValue;
            Document xmlDocument = buildXmlDocument(rootObject);
            String xml = toXml(xmlDocument);
            ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            return this.xmlPersistenceService.read(xmlStream);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Unable to parse JSON content", e);
        }
    }

    private static Map<String, Object> xmlElementToJson(Element element) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put(FIELD_NAME, element.getTagName());

        NamedNodeMap attrs = element.getAttributes();
        if (attrs != null && attrs.getLength() > 0) {
            Map<String, Object> attributes = new LinkedHashMap<String, Object>();
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                attributes.put(attr.getNodeName(), attr.getNodeValue());
            }
            result.put(FIELD_ATTRIBUTES, attributes);
        }

        List<Object> children = new ArrayList<Object>();
        StringBuilder textBuffer = new StringBuilder();

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                children.add(xmlElementToJson((Element) child));
                continue;
            }
            if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
                textBuffer.append(child.getNodeValue());
            }
        }

        if (!children.isEmpty()) {
            result.put(FIELD_CHILDREN, children);
        }

        String text = textBuffer.toString();
        if (!text.isEmpty() && (children.isEmpty() || !text.trim().isEmpty())) {
            result.put(FIELD_TEXT, text);
        }

        return result;
    }

    private static Document buildXmlDocument(Map<String, Object> rootObject) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element rootElement = jsonElementToXml(document, rootObject);
        document.appendChild(rootElement);
        return document;
    }

    private static Element jsonElementToXml(Document document, Map<String, Object> jsonElement) throws IOException {
        Object nameValue = jsonElement.get(FIELD_NAME);
        if (!(nameValue instanceof String) || ((String) nameValue).isEmpty()) {
            throw new IOException("Each JSON element must define a non-empty 'name'");
        }

        Element element = document.createElement((String) nameValue);

        Object attributesValue = jsonElement.get(FIELD_ATTRIBUTES);
        if (attributesValue != null) {
            if (!(attributesValue instanceof Map<?, ?>)) {
                throw new IOException("Element 'attributes' must be a JSON object");
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> attributes = (Map<String, Object>) attributesValue;
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                element.setAttribute(entry.getKey(), entry.getValue() == null ? "" : String.valueOf(entry.getValue()));
            }
        }

        Object childrenValue = jsonElement.get(FIELD_CHILDREN);
        if (childrenValue != null) {
            if (!(childrenValue instanceof List<?>)) {
                throw new IOException("Element 'children' must be a JSON array");
            }
            for (Object child : (List<?>) childrenValue) {
                if (!(child instanceof Map<?, ?>)) {
                    throw new IOException("Each child element must be a JSON object");
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> childObject = (Map<String, Object>) child;
                element.appendChild(jsonElementToXml(document, childObject));
            }
        }

        Object textValue = jsonElement.get(FIELD_TEXT);
        if (textValue != null) {
            element.appendChild(document.createTextNode(String.valueOf(textValue)));
        }

        return element;
    }

    private static String toXml(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }

    private static Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    private static String toJson(Object value) {
        StringBuilder json = new StringBuilder(4096);
        appendJsonValue(json, value, 0);
        json.append('\n');
        return json.toString();
    }

    private static void appendJsonValue(StringBuilder json, Object value, int indentLevel) {
        if (value == null) {
            json.append("null");
            return;
        }
        if (value instanceof String) {
            appendJsonString(json, (String) value);
            return;
        }
        if (value instanceof Number || value instanceof Boolean) {
            json.append(value);
            return;
        }
        if (value instanceof Map<?, ?>) {
            appendJsonObject(json, (Map<?, ?>) value, indentLevel);
            return;
        }
        if (value instanceof List<?>) {
            appendJsonArray(json, (List<?>) value, indentLevel);
            return;
        }
        appendJsonString(json, String.valueOf(value));
    }

    private static void appendJsonObject(StringBuilder json, Map<?, ?> map, int indentLevel) {
        json.append('{');
        if (map.isEmpty()) {
            json.append('}');
            return;
        }
        json.append('\n');
        int index = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            indent(json, indentLevel + 1);
            appendJsonString(json, String.valueOf(entry.getKey()));
            json.append(':').append(' ');
            appendJsonValue(json, entry.getValue(), indentLevel + 1);
            if (index < map.size() - 1) {
                json.append(',');
            }
            json.append('\n');
            index++;
        }
        indent(json, indentLevel);
        json.append('}');
    }

    private static void appendJsonArray(StringBuilder json, List<?> list, int indentLevel) {
        json.append('[');
        if (list.isEmpty()) {
            json.append(']');
            return;
        }
        json.append('\n');
        for (int i = 0; i < list.size(); i++) {
            indent(json, indentLevel + 1);
            appendJsonValue(json, list.get(i), indentLevel + 1);
            if (i < list.size() - 1) {
                json.append(',');
            }
            json.append('\n');
        }
        indent(json, indentLevel);
        json.append(']');
    }

    private static void appendJsonString(StringBuilder json, String value) {
        json.append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"':
                    json.append("\\\"");
                    break;
                case '\\':
                    json.append("\\\\");
                    break;
                case '\b':
                    json.append("\\b");
                    break;
                case '\f':
                    json.append("\\f");
                    break;
                case '\n':
                    json.append("\\n");
                    break;
                case '\r':
                    json.append("\\r");
                    break;
                case '\t':
                    json.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        json.append(String.format("\\u%04x", (int) c));
                    } else {
                        json.append(c);
                    }
                    break;
            }
        }
        json.append('"');
    }

    private static void indent(StringBuilder text, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            text.append("  ");
        }
    }

    private static final class JsonParser {

        private final String text;

        private int index;

        private JsonParser(String text) {
            this.text = text;
            this.index = 0;
        }

        private Object parse() throws IOException {
            skipWhitespace();
            Object value = parseValue();
            skipWhitespace();
            if (!isEnd()) {
                throw new IOException("Unexpected trailing JSON characters");
            }
            return value;
        }

        private Object parseValue() throws IOException {
            skipWhitespace();
            if (isEnd()) {
                throw new IOException("Unexpected end of JSON");
            }

            char c = this.text.charAt(this.index);
            if (c == '{') {
                return parseObject();
            }
            if (c == '[') {
                return parseArray();
            }
            if (c == '"') {
                return parseString();
            }
            if (startsWith("true")) {
                this.index += 4;
                return Boolean.TRUE;
            }
            if (startsWith("false")) {
                this.index += 5;
                return Boolean.FALSE;
            }
            if (startsWith("null")) {
                this.index += 4;
                return null;
            }
            return parseNumber();
        }

        private Map<String, Object> parseObject() throws IOException {
            expect('{');
            Map<String, Object> object = new LinkedHashMap<String, Object>();
            skipWhitespace();
            if (peek('}')) {
                expect('}');
                return object;
            }

            while (true) {
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                object.put(key, value);
                skipWhitespace();
                if (peek('}')) {
                    expect('}');
                    return object;
                }
                expect(',');
                skipWhitespace();
            }
        }

        private List<Object> parseArray() throws IOException {
            expect('[');
            List<Object> array = new ArrayList<Object>();
            skipWhitespace();
            if (peek(']')) {
                expect(']');
                return array;
            }

            while (true) {
                array.add(parseValue());
                skipWhitespace();
                if (peek(']')) {
                    expect(']');
                    return array;
                }
                expect(',');
                skipWhitespace();
            }
        }

        private String parseString() throws IOException {
            expect('"');
            StringBuilder result = new StringBuilder();
            while (!isEnd()) {
                char c = this.text.charAt(this.index++);
                if (c == '"') {
                    return result.toString();
                }
                if (c != '\\') {
                    result.append(c);
                    continue;
                }
                if (isEnd()) {
                    throw new IOException("Invalid JSON escape sequence");
                }
                char escaped = this.text.charAt(this.index++);
                switch (escaped) {
                    case '"':
                        result.append('"');
                        break;
                    case '\\':
                        result.append('\\');
                        break;
                    case '/':
                        result.append('/');
                        break;
                    case 'b':
                        result.append('\b');
                        break;
                    case 'f':
                        result.append('\f');
                        break;
                    case 'n':
                        result.append('\n');
                        break;
                    case 'r':
                        result.append('\r');
                        break;
                    case 't':
                        result.append('\t');
                        break;
                    case 'u':
                        if (this.index + 4 > this.text.length()) {
                            throw new IOException("Invalid JSON unicode escape sequence");
                        }
                        String hex = this.text.substring(this.index, this.index + 4);
                        try {
                            result.append((char) Integer.parseInt(hex, 16));
                        } catch (NumberFormatException e) {
                            throw new IOException("Invalid JSON unicode escape sequence", e);
                        }
                        this.index += 4;
                        break;
                    default:
                        throw new IOException("Invalid JSON escape sequence: \\" + escaped);
                }
            }
            throw new IOException("Unterminated JSON string");
        }

        private Number parseNumber() throws IOException {
            int start = this.index;
            if (peek('-')) {
                this.index++;
            }
            while (!isEnd() && Character.isDigit(this.text.charAt(this.index))) {
                this.index++;
            }
            if (!isEnd() && this.text.charAt(this.index) == '.') {
                this.index++;
                while (!isEnd() && Character.isDigit(this.text.charAt(this.index))) {
                    this.index++;
                }
            }
            if (!isEnd() && (this.text.charAt(this.index) == 'e' || this.text.charAt(this.index) == 'E')) {
                this.index++;
                if (!isEnd() && (this.text.charAt(this.index) == '+' || this.text.charAt(this.index) == '-')) {
                    this.index++;
                }
                while (!isEnd() && Character.isDigit(this.text.charAt(this.index))) {
                    this.index++;
                }
            }

            String number = this.text.substring(start, this.index);
            if (number.isEmpty() || "-".equals(number)) {
                throw new IOException("Invalid JSON number");
            }
            try {
                if (number.contains(".") || number.contains("e") || number.contains("E")) {
                    return Double.valueOf(number);
                }
                return Long.valueOf(number);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid JSON number", e);
            }
        }

        private void skipWhitespace() {
            while (!isEnd()) {
                char c = this.text.charAt(this.index);
                if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                    this.index++;
                    continue;
                }
                return;
            }
        }

        private void expect(char expected) throws IOException {
            if (isEnd() || this.text.charAt(this.index) != expected) {
                throw new IOException("Expected '" + expected + "' at JSON index " + this.index);
            }
            this.index++;
        }

        private boolean startsWith(String expected) {
            return this.text.regionMatches(this.index, expected, 0, expected.length());
        }

        private boolean peek(char expected) {
            return !isEnd() && this.text.charAt(this.index) == expected;
        }

        private boolean isEnd() {
            return this.index >= this.text.length();
        }
    }

    private static String readAll(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int read;
        while ((read = in.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}

package com.horstmann.violet.framework.file.persistence;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.PluginRegistry;
import com.horstmann.violet.framework.util.SerializableEnumeration;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.edge.EdgeTransitionPoint;
import com.horstmann.violet.product.diagram.abstracts.edge.ITransitionPoint;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

@ManagedBean(registeredManually=true)
public class LegacyVioletXmlPersistenceService implements IFilePersistenceService
{

    @InjectedBean
    private PluginRegistry pluginRegistry;

    private final Map<String, Class<?>> classAliases = new HashMap<String, Class<?>>();

    public LegacyVioletXmlPersistenceService()
    {
        BeanInjector.getInjector().inject(this);
        initializeAliases();
    }

    @Override
    public void write(IGraph graph, java.io.OutputStream out)
    {
        throw new UnsupportedOperationException("LegacyVioletXmlPersistenceService supports read only");
    }

    @Override
    public IGraph read(InputStream in) throws IOException
    {
        String xml = readAll(in);
        if (xml == null || xml.trim().isEmpty())
        {
            throw new IOException("Empty XML content");
        }

        Object rootObject = parseXml(xml);
        if (!(rootObject instanceof IGraph))
        {
            throw new IOException("Unsupported legacy XML content");
        }

        IGraph graph = (IGraph) rootObject;
        Collection<INode> allNodes = graph.getAllNodes();
        for (INode aNode : allNodes)
        {
            aNode.setGraph(graph);
        }
        return graph;
    }

    private void initializeAliases()
    {
        List<IDiagramPlugin> plugins = new ArrayList<IDiagramPlugin>();
        if (this.pluginRegistry != null)
        {
            plugins.addAll(this.pluginRegistry.getDiagramPlugins());
        }
        if (plugins.isEmpty())
        {
            ServiceLoader<IDiagramPlugin> discoveredPlugins = ServiceLoader.load(IDiagramPlugin.class, this.getClass().getClassLoader());
            for (IDiagramPlugin discoveredPlugin : discoveredPlugins)
            {
                plugins.add(discoveredPlugin);
                if (this.pluginRegistry != null)
                {
                    this.pluginRegistry.register(discoveredPlugin);
                }
            }
        }

        for (IDiagramPlugin aPlugin : plugins)
        {
            Class<? extends IGraph> graphClass = aPlugin.getGraphClass();
            this.classAliases.put(graphClass.getSimpleName(), graphClass);
            try
            {
                IGraph graph = graphClass.getDeclaredConstructor().newInstance();
                for (INode nodePrototype : graph.getNodePrototypes())
                {
                    this.classAliases.put(nodePrototype.getClass().getSimpleName(), nodePrototype.getClass());
                }
                for (IEdge edgePrototype : graph.getEdgePrototypes())
                {
                    this.classAliases.put(edgePrototype.getClass().getSimpleName(), edgePrototype.getClass());
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private Object parseXml(String xml) throws IOException
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new java.io.StringReader(xml)));
            Element root = document.getDocumentElement();
            LegacyContext context = new LegacyContext();
            return readElement(root, null, context);
        }
        catch (Exception e)
        {
            throw new IOException("Unable to parse legacy XML", e);
        }
    }

    private Object readElement(Element element, Type expectedType, LegacyContext context) throws Exception
    {
        String reference = element.getAttribute("reference");
        if (reference != null && !reference.isEmpty())
        {
            return context.idMap.get(reference);
        }

        Class<?> expectedClass = getRawClass(expectedType);
        Class<?> valueClass = resolveValueClass(element, expectedClass);

        if (isSimpleType(valueClass))
        {
            return parseSimpleValue(element, valueClass);
        }

        if (Collection.class.isAssignableFrom(valueClass))
        {
            Collection<Object> collection = new ArrayList<Object>();
            registerIdIfNeeded(element, collection, context);
            Class<?> childClass = getCollectionType(expectedType);
            for (Element child : getChildElements(element))
            {
                Object childValue = readElement(child, childClass, context);
                collection.add(childValue);
            }
            return collection;
        }

        if (valueClass.isArray())
        {
            Class<?> componentType = valueClass.getComponentType();
            List<Element> children = getChildElements(element);
            Object array = java.lang.reflect.Array.newInstance(componentType, children.size());
            registerIdIfNeeded(element, array, context);
            for (int i = 0; i < children.size(); i++)
            {
                Object childValue = readElement(children.get(i), componentType, context);
                if (ITransitionPoint.class.isAssignableFrom(componentType) && childValue instanceof Point2D)
                {
                    childValue = EdgeTransitionPoint.fromPoint2D((Point2D) childValue);
                }
                java.lang.reflect.Array.set(array, i, childValue);
            }
            return array;
        }

        if (valueClass.equals(Point2D.Double.class))
        {
            Point2D.Double point = new Point2D.Double(parseDoubleAttribute(element, "x"), parseDoubleAttribute(element, "y"));
            registerIdIfNeeded(element, point, context);
            return point;
        }

        if (valueClass.equals(Rectangle2D.Double.class))
        {
            Rectangle2D.Double rectangle = new Rectangle2D.Double(
                    parseDoubleAttribute(element, "x"),
                    parseDoubleAttribute(element, "y"),
                    parseDoubleAttribute(element, "width"),
                    parseDoubleAttribute(element, "height"));
            registerIdIfNeeded(element, rectangle, context);
            return rectangle;
        }

        if (valueClass.equals(RoundRectangle2D.Double.class))
        {
            RoundRectangle2D.Double rectangle = new RoundRectangle2D.Double(
                    parseDoubleAttribute(element, "x"),
                    parseDoubleAttribute(element, "y"),
                    parseDoubleAttribute(element, "width"),
                    parseDoubleAttribute(element, "height"),
                    parseDoubleAttribute(element, "arcwidth"),
                    parseDoubleAttribute(element, "archeight"));
            registerIdIfNeeded(element, rectangle, context);
            return rectangle;
        }

        if (valueClass.equals(Color.class))
        {
            Color color = readColor(element);
            registerIdIfNeeded(element, color, context);
            return color;
        }

        Object instance = newInstance(valueClass);
        registerIdIfNeeded(element, instance, context);
        applyAttributes(instance, element, expectedClass);

        for (Element child : getChildElements(element))
        {
            Field field = findField(valueClass, child.getTagName());
            if (field == null) continue;
            field.setAccessible(true);
            Type fieldType = field.getGenericType();
            Object fieldValue = readElement(child, fieldType, context);
            if (fieldValue != null || !field.getType().isPrimitive())
            {
                field.set(instance, fieldValue);
            }
        }

        return instance;
    }

    private void applyAttributes(Object instance, Element element, Class<?> expectedClass) throws Exception
    {
        for (int i = 0; i < element.getAttributes().getLength(); i++)
        {
            Node attribute = element.getAttributes().item(i);
            String name = attribute.getNodeName();
            if ("id".equals(name) || "reference".equals(name) || "class".equals(name)) continue;
            String value = attribute.getNodeValue();

            Field field = findField(instance.getClass(), name);
            if (field != null)
            {
                field.setAccessible(true);
                field.set(instance, parseValueFromString(field.getType(), value));
                continue;
            }

            if ("name".equals(name) && expectedClass != null)
            {
                Object enumValue = parseEnumerationFromName(expectedClass, value);
                if (enumValue != null)
                {
                    // handled at object creation level when needed
                }
            }
        }
    }

    private Object parseSimpleValue(Element element, Class<?> valueClass) throws Exception
    {
        String nameAttribute = element.getAttribute("name");
        if (nameAttribute != null && !nameAttribute.isEmpty())
        {
            Object enumValue = parseEnumerationFromName(valueClass, nameAttribute);
            if (enumValue != null) return enumValue;
        }
        return parseValueFromString(valueClass, element.getTextContent());
    }

    private Object parseEnumerationFromName(Class<?> valueClass, String value) throws Exception
    {
        if (valueClass.isEnum())
        {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Object enumValue = Enum.valueOf((Class<? extends Enum>) valueClass, value);
            return enumValue;
        }
        if (SerializableEnumeration.class.isAssignableFrom(valueClass))
        {
            Field field = valueClass.getField(value);
            if (Modifier.isStatic(field.getModifiers()))
            {
                return field.get(null);
            }
        }
        return null;
    }

    private Object parseValueFromString(Class<?> type, String value)
    {
        if (value == null) value = "";
        if (type.equals(String.class)) return value;
        if (type.equals(int.class) || type.equals(Integer.class)) return Integer.parseInt(value);
        if (type.equals(long.class) || type.equals(Long.class)) return Long.parseLong(value);
        if (type.equals(double.class) || type.equals(Double.class)) return Double.parseDouble(value);
        if (type.equals(float.class) || type.equals(Float.class)) return Float.parseFloat(value);
        if (type.equals(boolean.class) || type.equals(Boolean.class)) return Boolean.parseBoolean(value);
        if (type.equals(short.class) || type.equals(Short.class)) return Short.parseShort(value);
        if (type.equals(byte.class) || type.equals(Byte.class)) return Byte.parseByte(value);
        if (type.equals(char.class) || type.equals(Character.class)) return value.isEmpty() ? '\0' : value.charAt(0);
        return value;
    }

    private Object newInstance(Class<?> valueClass) throws Exception
    {
        Constructor<?> constructor = valueClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private Class<?> resolveValueClass(Element element, Class<?> expectedClass)
    {
        String classAttribute = element.getAttribute("class");
        if (classAttribute != null && !classAttribute.isEmpty())
        {
            Class<?> fromAttribute = resolveClassAlias(classAttribute);
            if (fromAttribute != null) return fromAttribute;
        }

        String tagName = element.getTagName();
        Class<?> fromTagName = resolveClassAlias(tagName);
        if (fromTagName != null) return fromTagName;

        if (expectedClass != null) return expectedClass;
        return String.class;
    }

    private Class<?> resolveClassAlias(String classAlias)
    {
        if ("Point2D.Double".equals(classAlias)) return Point2D.Double.class;
        if ("Rectangle2D.Double".equals(classAlias)) return Rectangle2D.Double.class;
        if ("RoundRectangle2D.Double".equals(classAlias)) return RoundRectangle2D.Double.class;

        Class<?> resolved = this.classAliases.get(classAlias);
        if (resolved != null) return resolved;

        try
        {
            return Class.forName(classAlias);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private static List<Element> getChildElements(Element element)
    {
        List<Element> children = new ArrayList<Element>();
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node instanceof Element)
            {
                children.add((Element) node);
            }
        }
        return children;
    }

    private static Field findField(Class<?> type, String name)
    {
        Class<?> current = type;
        while (current != null)
        {
            try
            {
                return current.getDeclaredField(name);
            }
            catch (NoSuchFieldException e)
            {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private static Class<?> getRawClass(Type type)
    {
        if (type instanceof Class<?>) return (Class<?>) type;
        if (type instanceof ParameterizedType)
        {
            Type raw = ((ParameterizedType) type).getRawType();
            if (raw instanceof Class<?>) return (Class<?>) raw;
        }
        return null;
    }

    private static Class<?> getCollectionType(Type type)
    {
        if (type instanceof ParameterizedType)
        {
            Type[] args = ((ParameterizedType) type).getActualTypeArguments();
            if (args.length > 0)
            {
                Class<?> rawClass = getRawClass(args[0]);
                if (rawClass != null) return rawClass;
            }
        }
        return Object.class;
    }

    private static boolean isSimpleType(Class<?> valueClass)
    {
        if (valueClass == null) return false;
        return valueClass.equals(String.class)
                || valueClass.equals(Integer.class) || valueClass.equals(int.class)
                || valueClass.equals(Long.class) || valueClass.equals(long.class)
                || valueClass.equals(Double.class) || valueClass.equals(double.class)
                || valueClass.equals(Float.class) || valueClass.equals(float.class)
                || valueClass.equals(Boolean.class) || valueClass.equals(boolean.class)
                || valueClass.equals(Short.class) || valueClass.equals(short.class)
                || valueClass.equals(Byte.class) || valueClass.equals(byte.class)
                || valueClass.equals(Character.class) || valueClass.equals(char.class)
                || valueClass.isEnum()
                || SerializableEnumeration.class.isAssignableFrom(valueClass);
    }

    private static void registerIdIfNeeded(Element element, Object value, LegacyContext context)
    {
        String id = element.getAttribute("id");
        if (id != null && !id.isEmpty())
        {
            context.idMap.put(id, value);
        }
    }

    private static double parseDoubleAttribute(Element element, String attribute)
    {
        String value = element.getAttribute(attribute);
        if (value == null || value.isEmpty()) return 0d;
        return Double.parseDouble(value);
    }

    private static Color readColor(Element element)
    {
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 255;

        for (Element child : getChildElements(element))
        {
            String text = child.getTextContent();
            if (text == null || text.isEmpty()) continue;
            if ("red".equals(child.getTagName())) red = Integer.parseInt(text);
            else if ("green".equals(child.getTagName())) green = Integer.parseInt(text);
            else if ("blue".equals(child.getTagName())) blue = Integer.parseInt(text);
            else if ("alpha".equals(child.getTagName())) alpha = Integer.parseInt(text);
        }

        return new Color(red, green, blue, alpha);
    }

    private static String readAll(InputStream in) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int read;
        while ((read = in.read(chunk)) != -1)
        {
            buffer.write(chunk, 0, read);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static class LegacyContext
    {
        private final Map<String, Object> idMap = new HashMap<String, Object>();
    }
}

package org.webswing.server.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.shiro.subject.Subject;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.FrameworkConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.model.MsgOut;
import org.webswing.model.c2s.ConnectionHandshakeMsgIn;
import org.webswing.model.c2s.InputEventsFrameMsgIn;
import org.webswing.model.s2c.AppFrameMsgOut;
import org.webswing.model.s2c.ApplicationInfoMsg;
import org.webswing.model.server.SwingAppletDescriptor;
import org.webswing.model.server.SwingApplicationDescriptor;
import org.webswing.model.server.SwingDescriptor;
import org.webswing.server.ConfigurationManager;
import org.webswing.server.handler.LoginServlet;
import org.webswing.server.model.EncodedMessage;

import main.Main;

public class ServerUtil {

	private static final String DEFAULT = "default";
	private static final Logger log = LoggerFactory.getLogger(ServerUtil.class);
	private static final Map<String, byte[]> iconMap = new HashMap<String, byte[]>();
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final ProtoMapper protoMapper = new ProtoMapper();

	static {
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
	}

	public static String encode2Json(MsgOut m) {
		try {
			return mapper.writeValueAsString(m);
		} catch (IOException e) {
			log.error("Json encoding object failed: " + m, e);
			return null;
		}
	}

	public static byte[] encode2Proto(MsgOut m) {
		try {
			return protoMapper.encodeProto(m);
		} catch (IOException e) {
			log.error("Proto encoding object failed: " + m, e);
			return null;
		}
	}

	public static Object decodeJson(String s) {
		Object o = null;
		Class<?>[] classes = new Class<?>[] { InputEventsFrameMsgIn.class };
		for (Class<?> c : classes) {
			try {
				o = mapper.readValue(s, c);
				break;
			} catch (IOException e) {
				log.error("Failed to decode json message:", e);
			}
		}
		return o;
	}

	public static Object decodeProto(byte[] message) {
		Object o = null;
		Class<?>[] classes = new Class<?>[] { InputEventsFrameMsgIn.class };
		for (Class<?> c : classes) {
			try {
				o = protoMapper.decodeProto(message, c);
				break;
			} catch (IOException e) {
				log.error("Failed to decode proto message:", e);
			}
		}
		return o;
	}

	public static AppFrameMsgOut decodePlaybackProto(byte[] message) {
		try {
			return protoMapper.decodeProto(message, AppFrameMsgOut.class);
		} catch (IOException e) {
			log.error("Failed to decode proto message:", e);
			return null;
		}
	}

	public static List<ApplicationInfoMsg> createApplicationInfoMsg(AtmosphereResource r, boolean includeAdminApp) {
		Map<String, SwingApplicationDescriptor> applications = ConfigurationManager.getInstance().getApplications();
		Map<String, SwingAppletDescriptor> applets = ConfigurationManager.getInstance().getApplets();

		List<ApplicationInfoMsg> apps = new ArrayList<ApplicationInfoMsg>();
		StrSubstitutor subs = getConfigSubstitutor(getUserName(r), null, ServerUtil.getClientIp(r.getRequest()), null, null);
		if (applications.size() == 0) {
			return null;
		} else {
			for (String name : applications.keySet()) {
				SwingApplicationDescriptor descriptor = applications.get(name);
				if (isUserAuthorizedForApplication(r, descriptor)) {
					ApplicationInfoMsg app = toApplicationInfoMsg(descriptor, subs);
					app.setApplet(false);
					apps.add(app);
				}
			}
			for (String name : applets.keySet()) {
				SwingAppletDescriptor descriptor = applets.get(name);
				if (isUserAuthorizedForApplication(r, descriptor)) {
					ApplicationInfoMsg app = toApplicationInfoMsg(descriptor, subs);
					app.setApplet(true);
					apps.add(app);
				}
			}
			Collections.sort(apps);

			if (includeAdminApp) {
				ApplicationInfoMsg adminConsole = new ApplicationInfoMsg();
				adminConsole.setName(Constants.ADMIN_CONSOLE_APP_NAME);
				apps.add(adminConsole);
			}
		}
		return apps;
	}

	public static ApplicationInfoMsg toApplicationInfoMsg(SwingDescriptor swingDesc, StrSubstitutor subs) {
		ApplicationInfoMsg app = new ApplicationInfoMsg();
		app.setName(swingDesc.getName());
		app.setAlwaysRestart(swingDesc.getSwingSessionTimeout() == 0);
		String icon = subs != null ? subs.replace(swingDesc.getIcon()) : swingDesc.getIcon();
		String homeDir = subs != null ? subs.replace(swingDesc.getHomeDir()) : swingDesc.getHomeDir();
		if (icon == null) {
			app.setBase64Icon(loadImage(null));
		} else {
			if (new File(icon).isFile()) {
				app.setBase64Icon(loadImage(icon));
			} else {
				if (new File(homeDir + File.separator + icon).isFile()) {
					app.setBase64Icon(loadImage(homeDir + File.separator + icon));
				} else if (new File(Main.getRootDir(), homeDir + File.separator + icon).isFile()) {
					app.setBase64Icon(loadImage(new File(Main.getRootDir(), homeDir + File.separator + icon).getAbsolutePath()));
				} else {
					log.error("Icon loading failed. File " + icon + " or " + homeDir + File.separator + icon + " does not exist.");
					app.setBase64Icon(loadImage(null));
				}
			}
		}
		return app;
	}

	public static boolean isUserAuthorized(AtmosphereResource r, SwingDescriptor app, ConnectionHandshakeMsgIn h) {

		// mirror view
		if (h.isMirrored()) {
			if (isUserinRole(r, Constants.ADMIN_ROLE)) {
				return true;
			}
			return false;
		}
		// regular
		return isUserAuthorizedForApplication(r, app);
	}

	public static boolean isUserAuthorizedForApplication(AtmosphereResource r, SwingDescriptor app) {
		if ((app.isAuthentication() || app.isAuthorization()) && isUserAnonymous(r)) {
			return false;
		}
		if (app.isAuthorization()) {
			if (isUserinRole(r, app.getName()) || isUserinRole(r, Constants.ADMIN_ROLE)) {
				return true;
			}
			return false;
		} else {
			return true;
		}
	}

	private static byte[] loadImage(String icon) {
		try {
			if (icon == null) {
				if (iconMap.containsKey(DEFAULT)) {
					return iconMap.get(DEFAULT);
				} else {
					BufferedImage defaultIcon = ImageIO.read(ServerUtil.class.getClassLoader().getResourceAsStream("images/java.png"));
					byte[] b64icon = getPngImage(defaultIcon);
					iconMap.put(DEFAULT, b64icon);
					return b64icon;
				}
			} else {
				if (iconMap.containsKey(icon)) {
					return iconMap.get(icon);
				} else {
					BufferedImage defaultIcon = ImageIO.read(new File(icon));
					byte[] b64icon = getPngImage(defaultIcon);
					iconMap.put(icon, b64icon);
					return b64icon;
				}
			}
		} catch (IOException e) {
			log.error("Failed to load image " + icon, e);
			return null;
		}
	}

	private static byte[] getPngImage(BufferedImage imageContent) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
			ImageIO.write(imageContent, "png", ios);
			byte[] result = baos.toByteArray();
			baos.close();
			return result;
		} catch (IOException e) {
			log.error("Writing image interupted:" + e.getMessage(), e);
		}
		return null;
	}

	public static String getUserPropsFileName() {
		String userFile = System.getProperty(Constants.USER_FILE_PATH);
		if (userFile == null) {
			String war = ServerUtil.getWarFileLocation();
			userFile = war.substring(0, war.lastIndexOf("/") + 1) + Constants.DEFAULT_USER_FILE_NAME;
			System.setProperty(userFile, Constants.USER_FILE_PATH);
		}
		return userFile;
	}

	public static String getWarFileLocation() {
		String warFile = System.getProperty(Constants.WAR_FILE_LOCATION);
		if (warFile == null) {
			ProtectionDomain domain = Main.class.getProtectionDomain();
			URL location = domain.getCodeSource().getLocation();
			String locationString = location.toExternalForm();
			if (locationString.endsWith("/WEB-INF/classes/")) {
				locationString = locationString.substring(0, locationString.length() - "/WEB-INF/classes/".length());
			}
			System.setProperty(Constants.WAR_FILE_LOCATION, locationString);
			return locationString;
		}
		return warFile;
	}

	public static String getUserName(AtmosphereResource resource) {
		Subject sub = (Subject) resource.getRequest().getAttribute(FrameworkConfig.SECURITY_SUBJECT);
		if (sub != null) {
			return sub.getPrincipal() + "";
		}
		return null;
	}

	public static boolean isUserinRole(AtmosphereResource resource, String role) {
		Subject sub = (Subject) resource.getRequest().getAttribute(FrameworkConfig.SECURITY_SUBJECT);
		if (sub != null) {
			return sub.hasRole(role);
		}
		return false;
	}

	public static boolean isUserAnonymous(AtmosphereResource resource) {
		if (LoginServlet.anonymUserName.equals(getUserName(resource))) {
			return true;
		}
		return false;
	}

	public static boolean isRecording(HttpServletRequest r) {
		String recording = (String) r.getHeader(Constants.HTTP_ATTR_RECORDING_FLAG);
		return Boolean.parseBoolean(recording);
	}

	public static String getCustomArgs(HttpServletRequest r) {
		String args = (String) r.getHeader(Constants.HTTP_ATTR_ARGS);
		return args != null ? args : "";
	}

	public static int getDebugPort(HttpServletRequest r) {
		String recording = (String) r.getHeader(Constants.HTTP_ATTR_DEBUG_PORT);
		try {
			return Integer.parseInt(recording);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static String getClientIp(AtmosphereRequest request) {
		return request.getRemoteAddr();
	}

	public static Map<String, String> getConfigSubstitutorMap(String user, String sessionId, String clientIp, String locale, String customArgs) {

		Map<String, String> result = new HashMap<String, String>();
		result.putAll(System.getenv());
		for (final String name : System.getProperties().stringPropertyNames()) {
			result.put(name, System.getProperties().getProperty(name));
		}
		if (user != null) {
			result.put(Constants.USER_NAME_SUBSTITUTE, user);
		}
		if (sessionId != null) {
			result.put(Constants.SESSION_ID_SUBSTITUTE, sessionId);
		}
		if (clientIp != null) {
			result.put(Constants.SESSION_IP_SUBSTITUTE, clientIp);
		}
		if (locale != null) {
			result.put(Constants.SESSION_LOCALE_SUBSTITUTE, locale);
		}
		if (customArgs != null) {
			result.put(Constants.SESSION_CUSTOMARGS_SUBSTITUTE, customArgs);
		}
		return result;
	}

	public static StrSubstitutor getConfigSubstitutor(String user, String sessionId, String clientIp, String locale, String customArgs) {
		return new StrSubstitutor(getConfigSubstitutorMap(user, sessionId, clientIp, locale, customArgs));
	}

	public static void broadcastMessage(AtmosphereResource r, EncodedMessage o) {
		for (AtmosphereResource resource : r.getBroadcaster().getAtmosphereResources()) {
			if (resource.uuid().equals(r.uuid())) {
				resource.getBroadcaster().broadcast(resource.forceBinaryWrite() ? o.getProtoMessage() : o.getJsonMessage(), resource);
			}
		}
	}

	public static void broadcastMessage(AtmosphereResource r, MsgOut o) {
		broadcastMessage(r, new EncodedMessage(o));
	}

	public static boolean isFileLocked(File file) {
		if (file.exists()) {
			try {
				Path source = file.toPath();
				Path dest = file.toPath().resolveSibling(file.getName() + ".wstest");
				Files.move(source, dest);
				Files.move(dest, source);
				return false;
			} catch (IOException e) {
				return true;
			}
		}
		return false;
	}

	private static List<String> logicalNames = Arrays.asList("monospaced", "serif", "sansserif", "dialoginput", "dialog");
	private static List<String> styles = Arrays.asList("bolditalic", "italic", "bold", "plain");
	private static String defaultChargroup = SystemUtils.IS_OS_WINDOWS ? "alpbabet" : "latin-1";

	public static String createFontConfiguration(SwingDescriptor appConfig, StrSubstitutor subs) throws IOException {
		if (appConfig.getFontConfig() != null && appConfig.getFontConfig().size() > 0) {
			StringBuilder fontConfig = new StringBuilder("version=1\n");
			StringBuilder metadata = new StringBuilder();
			fontConfig.append("sequence.allfonts=").append(defaultChargroup).append("\n");
			Map<String, File> fonts = buildFontMap(appConfig.getFontConfig(), subs);
			Map<File, String> fontNames = resolveFontNames(new HashSet<File>(fonts.values()));
			String defaultFont = findDefaultFontKey(fonts, false);
			String defaultMonospace = findDefaultFontKey(fonts, true);
			for (String logicalFont : logicalNames) {
				for (String style : styles) {
					String key = findFont(logicalFont, style, defaultFont, defaultMonospace, fonts);
					File file = fonts.get(key);
					String fullName = fontNames.get(file);
					fontConfig.append(logicalFont).append(".").append(style).append(".").append(defaultChargroup).append("=").append(fullName).append("\n");
					//directDraw font to file mapping:
					metadata.append("#@@").append(logicalFont).append(".").append(style).append("=").append(file.getAbsolutePath()).append("\n");
				}
			}
			for (File fontFile : fontNames.keySet()) {
				String fontName = fontNames.get(fontFile);
				String fontName_ = fontName.replace(' ', '_');
				fontConfig.append("filename.").append(fontName_).append("=").append(StringEscapeUtils.escapeJava(fontFile.getCanonicalPath())).append("\n");
				metadata.append("#@@").append(fontName).append("=").append(fontFile.getAbsolutePath()).append("\n");
			}
			fontConfig.append("\n").append(metadata);

			String tempDir = System.getProperty(Constants.TEMP_DIR_PATH);
			File configfile = new File(URI.create(tempDir + URLEncoder.encode(subs.replace("fontconfig-${clientId}.properties"), "UTF-8")));
			FileUtils.writeStringToFile(configfile, fontConfig.toString());

			return configfile.getAbsolutePath();
		} else {
			return null;
		}
	}

	private static String findFont(String logicalFont, String style, String defaultFont, String defaultMonospace, Map<String, File> fonts) {
		if (fonts.containsKey(logicalFont + " " + style)) {//check if exact font defined
			return logicalFont + " " + style;
		} else if (fonts.containsKey(logicalFont + " plain")) {//check if plain font exist
			return logicalFont + " plain";
		} else if (isMonospaceFont(logicalFont)) {
			return defaultMonospace;
		}
		return defaultFont;
	}

	private static Map<String, File> buildFontMap(Map<String, String> fontConfig, StrSubstitutor subs) {
		Map<String, File> result = new HashMap<String, File>();
		for (String key : fontConfig.keySet()) {
			String keyValue = subs.replace(key).toLowerCase().trim();
			if (isLogicalFont(keyValue) && logicalNames.contains(keyValue)) {//if no style is specified use plain
				keyValue = keyValue + " plain";
			}
			File fontFile = new File(subs.replace(fontConfig.get(key)).trim());
			if (!fontFile.exists()) {
				throw new RuntimeException("Loading font " + keyValue + " failed . Font file " + fontFile.getAbsolutePath() + " not found.");
			}
			if (!fontFile.isFile()) {
				throw new RuntimeException("Loading font " + keyValue + " failed . Font file " + fontFile.getAbsolutePath() + " is not a file.");
			}
			result.put(keyValue, fontFile);
		}
		return result;
	}

	@SuppressWarnings("restriction")
	private static Map<File, String> resolveFontNames(Set<File> fontFiles) {
		Map<File, String> result = new HashMap<File, String>();
		for (File file : fontFiles) {
			try {
				sun.font.TrueTypeFont ttfFile = new sun.font.TrueTypeFont(file.getAbsolutePath(), null, 0, false);
				String name = ttfFile.getFullName();
				result.put(file, name);
			} catch (Exception e) {
				throw new RuntimeException("Loading TTF font " + file + " failed .", e);
			}
		}
		return result;
	}

	private static String findDefaultFontKey(Map<String, File> fontConfig, boolean preferMonospace) {
		String result = "";
		for (String key : fontConfig.keySet()) {
			if (isLogicalFont(key)) {
				String[] logicalFont = key.split(" ");
				String fontName = logicalFont[0];
				String fontStyle = logicalFont.length > 1 ? logicalFont[1] : "plain";
				String resultName = result.split(" ")[0];
				String resultStyle = result.split(" ").length > 1 ? result.split(" ")[1] : "plain";
				int score = logicalNames.indexOf(fontName) * 10 + styles.indexOf(fontStyle);
				int resultScore = logicalNames.indexOf(resultName) * 10 + styles.indexOf(resultStyle);
				if (preferMonospace) {
					score = isMonospaceFont(key) ? score * 10 : score;
					resultScore = isMonospaceFont(result) ? resultScore * 10 : resultScore;
				}
				if (score > resultScore) {
					result = key;
				}
			} else if (result.isEmpty()) {
				result = key;
			}
		}
		return result.isEmpty() ? null : result;
	}

	private static boolean isLogicalFont(String keyValue) {
		for (String logicalName : logicalNames) {
			if (keyValue.startsWith(logicalName)) {
				String remainder = keyValue.substring(logicalName.length()).trim();
				if (remainder.isEmpty() || styles.contains(remainder)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isMonospaceFont(String keyValue) {
		if (isLogicalFont(keyValue) && (keyValue.startsWith("monospaced") || keyValue.startsWith("dialoginput"))) {
			return true;
		}
		return false;
	}

}

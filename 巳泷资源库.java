package silong.gay;

import android.view.VelocityTracker;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.VpnService;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.pm.PackageManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends Activity {
	
	// ==================== 核心颜色配置 (深紫罗兰) ====================
	private int appBgColor;
	private int mainBgColor;
	private int btnBgColor;
	private int accentColor;
	
	private static final int TEXT_WHITE = Color.WHITE;
	private static final int TEXT_GRAY = 0xFFE0E0E0;
	private static final int TEXT_DISABLE = 0xFF9E9E9E;
	private static final int DEFAULT_APP_BG = 0xFF0B0018;
	private static final int DEFAULT_MAIN_BG = 0xBB2D0550;
	private static final int DEFAULT_BTN_BG = 0xCC4A0D80;
	private static final int DEFAULT_ACCENT = 0xDD6D20B0;
	
	// ==================== 地址常量 ====================
	private static final String ANNOUNCEMENT_URL = "https://silong-respool.github.io/gay/announcement.html";
	private static final String UPDATE_CHECK_URL = "https://silong-respool.github.io/gay/update_config.xml";
	private static final int LOCAL_SERVER_PORT = 8080;
	
	// ==================== 手势常量 ====================
	private static final int SWIPE_THRESHOLD = 40;
	private static final int BOTTOM_TOUCH_AREA = 100;
	private static final int VELOCITY_THRESHOLD = 400;
	
	// ==================== SharedPreferences 配置Key ====================
	private static final String PREF_NIGHT_MODE = "night_mode";
	private static final String PREF_FONT_SIZE = "font_size";
	private static final String PREF_HISTORY = "history";
	private static final String PREF_FAVORITES = "favorites";
	private static final String PREF_LAST_ANNOUNCEMENT = "last_announcement";
	private static final String PREF_USER_AGENT = "user_agent";
	private static final String PREF_ANNOUNCEMENT_SHOWN = "announcement_shown";
	private static final String PREF_IMAGE_LOADING = "image_loading";
	private static final String PREF_HARDWARE_ACCEL = "hardware_accel";
	private static final String PREF_RENDER_PRIORITY = "render_priority";
	private static final String PREF_ANTI_FLICKER = "anti_flicker";
	private static final String PREF_FIXED_BG = "fixed_bg";
	private static final String PREF_BG_COLOR = "bg_color";
	private static final String PREF_DISABLE_WINDOW_ANIM = "disable_window_anim";
	private static final String PREF_CACHE_SIZE_MB = "cache_size_mb";
	private static final String PREF_ALLOW_CROSS_DOMAIN = "allow_cross_domain";
	private static final String PREF_PAUSE_BG_RENDER = "pause_bg_render";
	private static final String PREF_DISABLE_LONGPRESS = "disable_longpress";
	private static final String PREF_SAFE_BROWSING = "safe_browsing";
	private static final String PREF_MALI_OPTIMIZATION = "mali_optimization";
	private static final String PREF_WEBGL_FORCE = "webgl_force";
	private static final String PREF_PIXEL_FORMAT = "pixel_format";
	private static final String PREF_VSYNC_ENABLE = "vsync_enable";
	private static final String PREF_FRAME_SKIP = "frame_skip";
	private static final String PREF_RENDER_BUFFER = "render_buffer";
	private static final String PREF_THEME_COLOR = "theme_color";
	private static final String PREF_THEME_ALPHA = "theme_alpha";
	private static final String PREF_DEFAULT_HOME_URL = "default_home_url";
	private static final String PREF_TABS_DATA = "tabs_data";
	private static final String PREF_AD_BLOCK_RULES = "ad_block_rules";
	private static final String PREF_AD_BLOCK_IMPORTED_SCRIPT = "ad_block_imported_script";
	private static final String PREF_DISABLE_GYRO = "disable_gyro";
	// 新增：广告屏蔽增强、朗读参数持久化
	private static final String PREF_AD_AUTO_BLOCK_ENABLED = "pref_ad_auto_block_enabled";
	private static final String PREF_AD_PREVENT_JUMP = "pref_ad_prevent_jump";
	private static final String PREF_TTS_SPEECH_RATE = "tts_speech_rate";
	private static final String PREF_TTS_PITCH = "tts_pitch";
	private static final String PREF_TTS_WATERLINE_Y = "tts_waterline_y";
	private static final String PREF_TTS_LINE_START = "tts_line_start";
	private static final String PREF_TTS_LINE_END = "tts_line_end";
	private static final String PREF_TTS_USE_LINE_MODE = "tts_use_line_mode";
	// 新增：网页屏蔽、文字模式
	private static final String PREF_BLOCKED_WEBSITES = "blocked_websites";
	private static final String PREF_TEXT_MODE = "text_mode";
	
	// VPN 相关
	private static final int VPN_REQUEST_CODE = 1001;
	
	//爬取页面筛选功能
	private String crawlFilterType = "全部";
	
	// ==================== 标签页数据结构 ====================
	private static class TabInfo {
		String id = UUID.randomUUID().toString();
		WebView webView;
		String url;
		String title;
		boolean isDefaultHome = false;
		
		TabInfo() {}
		
		TabInfo(String url) {
			this.url = url;
		}
	}
	
	// ==================== JavaScript接口 ====================
	private class JavaScriptInterface {
		private final WeakReference<MainActivity> activityRef;
		private final String tabId;
		
		JavaScriptInterface(MainActivity activity, String tabId) {
			this.activityRef = new WeakReference<>(activity);
			this.tabId = tabId;
		}
		
		@JavascriptInterface
		public void showToast(String message) {
			MainActivity activity = activityRef.get();
			if (activity != null && !activity.isFinishing()) {
				activity.runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show());
			}
		}
		
		@JavascriptInterface
		public void onMusicPlay(String title, String artist, boolean isPlaying) {
			MainActivity activity = activityRef.get();
			if (activity != null) {
				activity.runOnUiThread(() -> activity.updateMediaSession(tabId, title, artist, isPlaying));
			}
		}
		
		@JavascriptInterface
		public void closeApp() {
			MainActivity activity = activityRef.get();
			if (activity != null && !activity.isFinishing()) {
				activity.runOnUiThread(activity::finish);
			}
		}
		
		@JavascriptInterface
		public void onElementSelected(String selector) {
			MainActivity activity = activityRef.get();
			if (activity != null) {
				activity.runOnUiThread(() -> activity.handleBlockElementSelected(selector));
			}
		}
		
		@JavascriptInterface
		public void onResourceCaptured(String resourceUrl, String type) {
			MainActivity activity = activityRef.get();
			if (activity != null) {
				activity.runOnUiThread(() -> activity.addCapturedResource(resourceUrl, type));
			}
		}
	}
	
	// ==================== 简易HTTP服务器 ====================
	private static class LocalHttpServer implements Runnable {
		private final int port;
		private final Context context;
		private ServerSocket serverSocket;
		private final AtomicBoolean running = new AtomicBoolean(false);
		private final ExecutorService threadPool = Executors.newCachedThreadPool();
		private final Handler mainHandler;
		
		LocalHttpServer(Context context, int port, Handler mainHandler) {
			this.port = port;
			this.context = context.getApplicationContext();
			this.mainHandler = mainHandler;
		}
		
		synchronized boolean start() {
			if (running.get()) {
				return false;
			}
			try {
				serverSocket = new ServerSocket(port);
				running.set(true);
				new Thread(this, "LocalHttpServer").start();
				// 不显示启动成功的 Toast，避免干扰
				return true;
				} catch (IOException e) {
				Log.e(TAG, "无法启动本地服务器，端口 " + port + " 已被占用", e);
				return false;
			}
		}
		
		synchronized void stop() {
			running.set(false);
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException ignored) {}
			}
			threadPool.shutdownNow();
		}
		
		@Override
		public void run() {
			while (running.get()) {
				try {
					final Socket clientSocket = serverSocket.accept();
					threadPool.execute(() -> handleClient(clientSocket));
					} catch (IOException e) {
					if (running.get()) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private void handleClient(Socket socket) {
			InputStream in = null;
			java.io.OutputStream out = null;
			try {
				in = socket.getInputStream();
				out = socket.getOutputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String requestLine = reader.readLine();
				if (requestLine == null || !requestLine.startsWith("GET")) {
					sendError(out, 400);
					return;
				}
				String[] parts = requestLine.split(" ");
				String path = parts.length > 1 ? parts[1] : "/";
				if (path.contains("?")) {
					path = path.substring(0, path.indexOf("?"));
				}
				if (path.endsWith("/")) {
					path += "index.html";
				}
				if (path.contains("..")) {
					sendError(out, 403);
					return;
				}
				String assetPath = "www" + path;
				InputStream assetStream = null;
				try {
					assetStream = context.getAssets().open(assetPath);
					byte[] data = readAllBytes(assetStream);
					String mime = getMimeType(path);
					sendResponse(out, 200, mime, data);
					} catch (IOException e) {
					sendError(out, 404);
					} finally {
					if (assetStream != null) {
						try { assetStream.close(); } catch (IOException ignored) {}
					}
				}
				} catch (Exception e) {
				e.printStackTrace();
				} finally {
				try {
					if (in != null) in.close();
					if (out != null) out.close();
					socket.close();
				} catch (IOException ignored) {}
			}
		}
		
		private byte[] readAllBytes(InputStream in) throws IOException {
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int len;
			while ((len = in.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();
		}
		
		private void sendResponse(java.io.OutputStream out, int statusCode, String contentType, byte[] content) throws IOException {
			String statusLine;
			switch (statusCode) {
				case 200: statusLine = "HTTP/1.1 200 OK\r\n"; break;
				case 404: statusLine = "HTTP/1.1 404 Not Found\r\n"; break;
				case 403: statusLine = "HTTP/1.1 403 Forbidden\r\n"; break;
				default: statusLine = "HTTP/1.1 500 Internal Server Error\r\n";
			}
			out.write(statusLine.getBytes());
			out.write(("Content-Type: " + contentType + "\r\n").getBytes());
			out.write(("Content-Length: " + content.length + "\r\n").getBytes());
			out.write("Connection: close\r\n\r\n".getBytes());
			out.write(content);
			out.flush();
		}
		
		private void sendError(java.io.OutputStream out, int code) {
			String msg = code == 404 ? "404 Not Found" : "Server Error";
			try {
				sendResponse(out, code, "text/plain", msg.getBytes());
				} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private String getMimeType(String path) {
			if (path.endsWith(".html")) return "text/html";
			if (path.endsWith(".htm")) return "text/html";
			if (path.endsWith(".css")) return "text/css";
			if (path.endsWith(".js")) return "application/javascript";
			if (path.endsWith(".png")) return "image/png";
			if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
			if (path.endsWith(".gif")) return "image/gif";
			if (path.endsWith(".svg")) return "image/svg+xml";
			if (path.endsWith(".json")) return "application/json";
			return "application/octet-stream";
		}
	}
	
	// ==================== TTS朗读前台服务 ====================
	public static class TtsReadingService extends Service {
		private static TextToSpeech tts;
		private static String pendingText;
		private static boolean isSpeaking = false;
		private static Handler handler = new Handler(Looper.getMainLooper());
		
		@Override
		public void onCreate() {
			super.onCreate();
			if (tts == null) {
				tts = new TextToSpeech(this, status -> {});
			}
		}
		
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			if (intent != null && "stop".equals(intent.getAction())) {
				stopReading();
				stopSelf();
				return START_NOT_STICKY;
			}
			return START_STICKY;
		}
		
		public static void startReading(String text, String voiceName) {
			if (tts == null) return;
			stopReading();
			pendingText = text;
			isSpeaking = true;
			if (voiceName != null && !voiceName.isEmpty()) {
				for (Voice v : tts.getVoices()) {
					if (v.getName().equals(voiceName)) {
						tts.setVoice(v);
						break;
					}
				}
			}
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_READING");
			tts.setOnUtteranceProgressListener(new android.speech.tts.UtteranceProgressListener() {
				@Override
				public void onStart(String utteranceId) {}
				@Override
				public void onDone(String utteranceId) {
					isSpeaking = false;
				}
				@Override
				public void onError(String utteranceId) {
					isSpeaking = false;
				}
			});
		}
		
		public static void stopReading() {
			if (tts != null && tts.isSpeaking()) {
				tts.stop();
			}
			isSpeaking = false;
		}
		
		public static boolean isReading() {
			return isSpeaking;
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			if (tts != null) {
				tts.stop();
				tts.shutdown();
				tts = null;
			}
		}
		
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
	}
	
	// ==================== 成员变量 ====================
	private Timer announcementTimer;
	private LinearLayout navBar;
	private ProgressBar progressBar;
	private ProgressBar centerLoadingView;
	private View customErrorView;
	private FrameLayout mContentFrame;
	private String currentUrl;
	private boolean isMaliDevice = false;
	private boolean isForceUpdateDialogShowing = false;
	private LocalHttpServer localServer;
	private int actualServerPort = LOCAL_SERVER_PORT;
	
	// 全屏视频相关
	private View mCustomVideoView;
	private WebChromeClient.CustomViewCallback mFullscreenCallback;
	private FrameLayout mFullscreenContainer;
	private LinearLayout mAppContentLayout;
	private boolean isFullscreenMode = false;
	private int mOriginalSystemUiVisibility;
	
	// 页面源代码查看
	private ScrollView mSourceScrollView;
	private TextView mSourceTextView;
	private Button mSourceCloseButton;
	private Button mSourceWrapButton;
	private boolean mSourceWrapEnabled = false;
	
	// 导航栏状态
	private boolean isNavBarHidden = false;
	private boolean isPageLoadError = false;
	private boolean isShowingErrorPage = false;
	
	// 手势相关
	private VelocityTracker velocityTracker;
	private float downX;
	private float downY;
	private boolean isSwiping;
	
	// 网络监听
	private NetworkChangeReceiver networkReceiver;
	private boolean isNetworkAvailable = true;
	private final Handler mainHandler = new Handler(Looper.getMainLooper());
	
	// 存储配置
	private SharedPreferences prefs;
	
	// 历史与收藏
	private List<String> historyList = new ArrayList<>();
	private Set<String> favoritesSet = new HashSet<>();
	
	// 文件上传
	private ValueCallback<Uri[]> mUploadMessageForApi21;
	private static final int FILE_CHOOSER_RESULT_CODE = 12345;
	private static final int OVERLAY_PERMISSION_REQ = 2001;
	private static final int TTS_FILE_PICK = 3001;
	private static final int TTS_VOICE_PACK_PICK = 3002;
	private static final int AD_BLOCK_SCRIPT_PICK = 3003;
	
	// 多标签页管理
	private List<TabInfo> tabs = new ArrayList<>();
	private TabInfo currentTab;
	
	// 音乐控制相关
	private MediaSession mediaSession;
	private PlaybackState.Builder playbackStateBuilder;
	private NotificationManager notificationManager;
	private static final int NOTIFICATION_ID = 1001;
	private static final int TTS_NOTIFICATION_ID = 1002;
	private boolean musicPlaying = false;
	private String musicTitle = "", musicArtist = "";
	private TabInfo activeMusicTab;
	private static final String CHANNEL_ID = "music_channel";
	private static final String TTS_CHANNEL_ID = "tts_channel";
	private boolean isBackgroundPlayMode = false;
	
	// TTS朗读相关变量
	private TextToSpeech ttsEngine;
	private Set<String> ttsAvailableVoices = new HashSet<>();
	private boolean ttsIsReading = false;
	private PowerManager.WakeLock wakeLock;
	private String ttsCurrentVoiceName = null;
	private int ttsCurrentPage = 0;
	private List<String> ttsPageTexts = new ArrayList<>();
	private Intent ttsServiceIntent;
	// 行号模式变量
	private boolean ttsUseLineMode = false;
	private int ttsLineStart = 1;
	private int ttsLineEnd = -1;   // -1 表示到最后
	
	// 新增：悬浮窗相关变量
	private View ttsFloatView;
	private WindowManager.LayoutParams floatParams;
	private Dialog ttsControlDialog;
	private boolean ttsFloatWasShowing = false;
	private boolean crawlFloatWasShowing = false;
	
	// 新增：网页屏蔽列表
	private Set<String> blockedWebsites = new HashSet<>();
	
	// 新增：文字模式
	private boolean textModeEnabled = false;
	
	// 新增：朗读悬浮窗是否显示
	private boolean ttsFloatShown = false;
	
	// 新增：锚点相关变量
	private View ttsAnchorView;
	private WindowManager.LayoutParams ttsAnchorParams;
	private boolean ttsAnchorEnabled = false;
	private boolean ttsAnchorActive = false;
	
	// 爬取资源弹窗引用，用于实现第二次点击关闭
	private Dialog crawlResourceDialog;
	
	// 代理管理与脚本录制模块
	private ProxyManager proxyManager;
	private ScriptRecorder scriptRecorder;
	private boolean isScriptRecording = false;
	private boolean isScriptPlaying = false;
	
	// ==================== 判断是否为本地资源的方法 ====================
	private boolean isLocalResource(String url) {
		if (url == null) return false;
		// file:// 开头
		if (url.startsWith("file://")) return true;
		// content:// 开头 (可能用于本地资源)
		if (url.startsWith("content://")) return true;
		// 本地服务器地址
		if (actualServerPort > 0 && url.startsWith("http://localhost:" + actualServerPort)) return true;
		if (actualServerPort > 0 && url.startsWith("http://127.0.0.1:" + actualServerPort)) return true;
		// 通过域名判断 home 相关（避免本地域名被误判）
		return false;
	}
	
	// ==================== TTS 相关方法 ====================
	private void initTtsEngine() {
		ttsEngine = new TextToSpeech(this, status -> {
			if (status == TextToSpeech.SUCCESS) {
				// 加载可用声音列表
				for (Voice voice : ttsEngine.getVoices()) {
					ttsAvailableVoices.add(voice.getName());
				}
				// 应用保存的速度和音高
				ttsEngine.setSpeechRate(ttsSpeechRate);
				ttsEngine.setPitch(ttsPitch);
				// 引擎就绪后再设置默认男声
				setDefaultMaleVoice();
				} else {
				Toast.makeText(this, "TTS引擎初始化失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	// 朗读当前页面 - 直接获取内容并朗读
	private void startReadingCurrentPage() {
		if (ttsEngine == null) {
			Toast.makeText(this, "TTS引擎未初始化", Toast.LENGTH_SHORT).show();
			return;
		}
		if (currentTab == null || currentTab.webView == null) {
			Toast.makeText(this, "当前没有打开的页面", Toast.LENGTH_SHORT).show();
			return;
		}
		// 暂停当前朗读
		if (ttsEngine.isSpeaking()) {
			ttsEngine.stop();
		}
		// 注入JS获取页面文本
		currentTab.webView.evaluateJavascript(
		"(function() { return document.body.innerText || document.body.textContent || ''; })();",
		value -> {
			String text = value;
			if (text == null || text.isEmpty() || text.equals("null")) {
				Toast.makeText(MainActivity.this, "无法获取页面文字", Toast.LENGTH_SHORT).show();
				return;
			}
			// 处理JS返回的引号
			if (text.startsWith("\"") && text.endsWith("\"")) {
				text = text.substring(1, text.length()-1);
				text = text.replace("\\\"", "\"");
				text = text.replace("\\n", "\n");
				text = text.replace("\\t", "\t");
				text = text.replace("\\\\", "\\");
			}
			// 根据行号模式或百分比模式截取文本
			String finalText = applyReadingRange(text);
			// 应用情绪规则（可选，复杂，此处保留基本支持）
			// 设置语音、速度、音高
			ttsEngine.setSpeechRate(ttsSpeechRate);
			ttsEngine.setPitch(ttsPitch);
			if (ttsCurrentVoiceName != null) {
				for (Voice voice : ttsEngine.getVoices()) {
					if (voice.getName().equals(ttsCurrentVoiceName)) {
						ttsEngine.setVoice(voice);
						break;
					}
				}
			}
			// 朗读
			int result = ttsEngine.speak(finalText, TextToSpeech.QUEUE_FLUSH, null, "TTS_CURRENT_PAGE");
			if (result == TextToSpeech.SUCCESS) {
				ttsIsReading = true;
				ttsEngine.setOnUtteranceProgressListener(new android.speech.tts.UtteranceProgressListener() {
					@Override public void onStart(String utteranceId) {}
					@Override public void onDone(String utteranceId) {
						ttsIsReading = false;
						// 检查锚点是否激活，如果是则触发下一页
						if (ttsAnchorActive) {
							MainActivity.this.runOnUiThread(() -> {
								triggerNextPage();
							});
						}
					}
					@Override public void onError(String utteranceId) { ttsIsReading = false; }
				});
				Toast.makeText(this, "开始朗读当前页面", Toast.LENGTH_SHORT).show();
				} else {
				Toast.makeText(this, "朗读启动失败", Toast.LENGTH_SHORT).show();
			}
		}
		);
	}
	
	// 根据设置截取文本
	private String applyReadingRange(String fullText) {
		if (ttsUseLineMode) {
			String[] lines = fullText.split("\\r?\\n");
			int start = ttsLineStart > 0 ? ttsLineStart : lines.length + ttsLineStart; // 支持负数
			int end = ttsLineEnd > 0 ? ttsLineEnd : lines.length + ttsLineEnd;
			if (end < 0) end = lines.length;
			if (start < 1) start = 1;
			if (start > lines.length) start = lines.length;
			if (end > lines.length) end = lines.length;
			if (start > end) { int tmp = start; start = end; end = tmp; }
			StringBuilder sb = new StringBuilder();
			for (int i = start - 1; i < end; i++) {
				sb.append(lines[i]).append("\n");
			}
			return sb.toString();
			} else {
			// 百分比模式：开始位置百分比 (ttsWaterlinePercent) 与结束位置 (prefs.getFloat("tts_waterline_end", 90))
			float endPercent = prefs.getFloat("tts_waterline_end", 90f);
			float startPercent = ttsWaterlinePercent;
			if (startPercent >= endPercent) {
				startPercent = 0; endPercent = 100;
			}
			int totalLen = fullText.length();
			int startIdx = (int) (totalLen * startPercent / 100f);
			int endIdx = (int) (totalLen * endPercent / 100f);
			if (startIdx < 0) startIdx = 0;
			if (endIdx > totalLen) endIdx = totalLen;
			if (startIdx >= endIdx) return fullText;
			return fullText.substring(startIdx, endIdx);
		}
	}
	
	private void triggerNextPage() {
		if (currentTab != null && currentTab.webView != null) {
			// 先尝试模拟点击常见的下一页按钮
			String js = "(function() {" +
			"  var selectors = ['a:contains(\"下一页\")', 'a:contains(\"下一頁\")', 'a:contains(\"Next\")', '.next', '#next', '.next-page', '#next-page'];" +
			"  for (var i = 0; i < selectors.length; i++) {" +
			"    var elements = document.querySelectorAll(selectors[i]);" +
			"    if (elements.length > 0) {" +
			"      elements[0].click();" +
			"      return true;" +
			"    }" +
			"  }" +
			"  // 如果没有找到按钮，尝试滚动到锚点位置" +
			"  if (window.ttsAnchorY) {" +
			"    window.scrollTo(0, window.ttsAnchorY);" +
			"    return true;" +
			"  }" +
			"  return false;" +
			"})();";
			
			// 先将锚点的Y坐标传递给网页
			if (ttsAnchorParams != null) {
				String anchorJs = "window.ttsAnchorY = " + ttsAnchorParams.y + ";";
				currentTab.webView.evaluateJavascript(anchorJs, null);
			}
			
			// 执行点击下一页的脚本
			currentTab.webView.evaluateJavascript(js, value -> {
				if (value != null && value.equals("true")) {
					Toast.makeText(MainActivity.this, "已触发下一页", Toast.LENGTH_SHORT).show();
					} else {
					Toast.makeText(MainActivity.this, "未找到下一页按钮，尝试滚动", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	private void showTtsDialog() {
		// 保留原有的手动输入朗读功能，但默认改为点击即朗读当前页面
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 20));
		layout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(this);
		title.setText("朗读控制");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(18);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		layout.addView(title);
		
		Button readCurrentPageBtn = new Button(this);
		readCurrentPageBtn.setText("朗读当前页面");
		readCurrentPageBtn.setTextColor(TEXT_WHITE);
		readCurrentPageBtn.setBackground(createBg(btnBgColor, 12));
		readCurrentPageBtn.setPadding(0, dp(12), 0, dp(12));
		readCurrentPageBtn.setOnClickListener(v -> {
			dialog.dismiss();
			startReadingCurrentPage();
		});
		layout.addView(readCurrentPageBtn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		// 手动输入备用
		EditText input = new EditText(this);
		input.setHint("或手动输入朗读内容...");
		input.setTextColor(TEXT_WHITE);
		input.setHintTextColor(TEXT_DISABLE);
		input.setBackground(createBg(btnBgColor, 8));
		input.setPadding(dp(12), dp(10), dp(12), dp(10));
		input.setMinLines(3);
		LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		inputParams.topMargin = dp(12);
		inputParams.bottomMargin = dp(12);
		input.setLayoutParams(inputParams);
		layout.addView(input);
		
		Button speakBtn = new Button(this);
		speakBtn.setText("朗读输入内容");
		speakBtn.setTextColor(TEXT_WHITE);
		speakBtn.setBackground(createBg(btnBgColor, 12));
		speakBtn.setPadding(0, dp(12), 0, dp(12));
		speakBtn.setOnClickListener(v -> {
			String text = input.getText().toString().trim();
			if (text.isEmpty()) {
				Toast.makeText(this, "请输入要朗读的文字", Toast.LENGTH_SHORT).show();
				return;
			}
			ttsEngine.stop();
			ttsEngine.setSpeechRate(ttsSpeechRate);
			ttsEngine.setPitch(ttsPitch);
			if (ttsCurrentVoiceName != null) {
				for (Voice voice : ttsEngine.getVoices()) {
					if (voice.getName().equals(ttsCurrentVoiceName)) {
						ttsEngine.setVoice(voice);
						break;
					}
				}
			}
			ttsEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "USER_INPUT");
			Toast.makeText(this, "开始朗读", Toast.LENGTH_SHORT).show();
		});
		layout.addView(speakBtn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		Button closeBtn = new Button(this);
		closeBtn.setText("关闭");
		closeBtn.setTextColor(TEXT_WHITE);
		closeBtn.setBackground(createBg(btnBgColor, 12));
		closeBtn.setPadding(0, dp(12), 0, dp(12));
		closeBtn.setOnClickListener(v -> dialog.dismiss());
		LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		closeParams.topMargin = dp(12);
		closeBtn.setLayoutParams(closeParams);
		layout.addView(closeBtn);
		
		dialog.setContentView(layout);
		Window w = dialog.getWindow();
		if (w != null) {
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setLayout((int)(getResources().getDisplayMetrics().widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
			w.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	// ==================== 悬浮窗相关方法 ====================
	private void createTtsFloatView() {
		if (!Settings.canDrawOverlays(this)) {
			new android.app.AlertDialog.Builder(this)
			.setTitle("需要悬浮窗权限")
			.setMessage("朗读功能需要悬浮窗权限，请前往设置开启")
			.setPositiveButton("去设置", (dialog, which) -> {
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				Uri.parse("package:" + getPackageName()));
				startActivity(intent);
			})
			.setNegativeButton("取消", null)
			.show();
			return;
		}
		if (ttsFloatView == null) {
			Button ttsButton = new Button(this);
			ttsButton.setText("🔊");
			ttsButton.setBackgroundColor(Color.TRANSPARENT);
			ttsButton.setTextSize(24);
			ttsButton.setGravity(Gravity.CENTER);
			ttsButton.setPadding(dp(16), dp(16), dp(16), dp(16));
			ttsFloatView = ttsButton;
			
			floatParams = new WindowManager.LayoutParams();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				floatParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
				} else {
				floatParams.type = WindowManager.LayoutParams.TYPE_PHONE;
			}
			floatParams.format = PixelFormat.RGBA_8888;
			floatParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			floatParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
			floatParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
			floatParams.gravity = Gravity.TOP | Gravity.START;
			floatParams.x = 100;
			floatParams.y = 100;
			
			ttsFloatView.setOnClickListener(v -> toggleTtsControlDialog());
			
			ttsFloatView.setOnTouchListener(new View.OnTouchListener() {
				private float initialX, initialY;
				private int initialTouchX, initialTouchY;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
						initialX = floatParams.x;
						initialY = floatParams.y;
						initialTouchX = (int) event.getRawX();
						initialTouchY = (int) event.getRawY();
						return false;
						case MotionEvent.ACTION_MOVE:
						floatParams.x = (int)(initialX + (event.getRawX() - initialTouchX));
						floatParams.y = (int)(initialY + (event.getRawY() - initialTouchY));
						WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
						wm.updateViewLayout(ttsFloatView, floatParams);
						return false;
					}
					return false;
				}
			});
			
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			wm.addView(ttsFloatView, floatParams);
		}
	}
	
	private void removeTtsFloatView() {
		if (ttsFloatView != null) {
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			wm.removeView(ttsFloatView);
			ttsFloatView = null;
		}
		if (ttsControlDialog != null) {
			ttsControlDialog.dismiss();
			ttsControlDialog = null;
		}
	}
	
	private void hideAllFloatViews() {
		ttsFloatWasShowing = (ttsFloatView != null);
		crawlFloatWasShowing = crawlFloatAdded;
		
		if (ttsFloatView != null) {
			try { WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE); wm.removeView(ttsFloatView); } catch (Exception ignored) {}
		}
		if (crawlFloatAdded && crawlFloatView != null) {
			try { windowManager.removeView(crawlFloatView); } catch (Exception ignored) {}
		}
	}
	
	private void restoreAllFloatViews() {
		if (ttsFloatWasShowing && ttsFloatView != null) {
			try { WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE); wm.addView(ttsFloatView, floatParams); } catch (Exception ignored) {}
		}
		if (crawlFloatWasShowing && crawlFloatView != null) {
			try { windowManager.addView(crawlFloatView, crawlFloatParams); } catch (Exception ignored) {}
		}
		ttsFloatWasShowing = false;
		crawlFloatWasShowing = false;
	}
	
	private void createTtsAnchorView() {
		if (!Settings.canDrawOverlays(this)) {
			new android.app.AlertDialog.Builder(this)
			.setTitle("需要悬浮窗权限")
			.setMessage("锚点功能需要悬浮窗权限，请前往设置开启")
			.setPositiveButton("去设置", (dialog, which) -> {
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				Uri.parse("package:" + getPackageName()));
				startActivity(intent);
			})
			.setNegativeButton("取消", null)
			.show();
			return;
		}
		if (ttsAnchorView == null) {
			Button anchorButton = new Button(this);
			anchorButton.setText("⚓");
			anchorButton.setTextColor(Color.RED);
			anchorButton.setTextSize(32);
			anchorButton.setGravity(Gravity.CENTER);
			anchorButton.setPadding(dp(16), dp(16), dp(16), dp(16));
			anchorButton.setBackgroundColor(Color.TRANSPARENT);
			ttsAnchorView = anchorButton;
			
			ttsAnchorParams = new WindowManager.LayoutParams();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				ttsAnchorParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
				} else {
				ttsAnchorParams.type = WindowManager.LayoutParams.TYPE_PHONE;
			}
			ttsAnchorParams.format = PixelFormat.RGBA_8888;
			ttsAnchorParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			ttsAnchorParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
			ttsAnchorParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
			ttsAnchorParams.gravity = Gravity.TOP | Gravity.START;
			ttsAnchorParams.x = 100;
			ttsAnchorParams.y = 500;
			
			ttsAnchorView.setOnTouchListener(new View.OnTouchListener() {
				private float initialX, initialY;
				private int initialTouchX, initialTouchY;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
						initialX = ttsAnchorParams.x;
						initialY = ttsAnchorParams.y;
						initialTouchX = (int) event.getRawX();
						initialTouchY = (int) event.getRawY();
						return false;
						case MotionEvent.ACTION_MOVE:
						ttsAnchorParams.x = (int)(initialX + (event.getRawX() - initialTouchX));
						ttsAnchorParams.y = (int)(initialY + (event.getRawY() - initialTouchY));
						WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
						wm.updateViewLayout(ttsAnchorView, ttsAnchorParams);
						return false;
					}
					return false;
				}
			});
			
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			wm.addView(ttsAnchorView, ttsAnchorParams);
			ttsAnchorActive = true;
			Toast.makeText(this, "锚点已添加，可拖动到下一页位置", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void removeTtsAnchorView() {
		if (ttsAnchorView != null) {
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			wm.removeView(ttsAnchorView);
			ttsAnchorView = null;
		}
		ttsAnchorActive = false;
		Toast.makeText(this, "锚点已移除", Toast.LENGTH_SHORT).show();
	}
	
	private void toggleTtsControlDialog() {
		if (ttsControlDialog != null && ttsControlDialog.isShowing()) {
			ttsControlDialog.dismiss();
			} else {
			showTtsControlDialog();
		}
	}
	
	private void showTtsControlDialog() {
		ttsControlDialog = new Dialog(this);
		ttsControlDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 20));
		layout.setPadding(dp(20), dp(20), dp(20), dp(20));
		
		TextView title = new TextView(this);
		title.setText("朗读控制");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(18);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		layout.addView(title);
		
		LinearLayout btnRow1 = new LinearLayout(this);
		btnRow1.setOrientation(LinearLayout.HORIZONTAL);
		btnRow1.setGravity(Gravity.CENTER);
		btnRow1.setWeightSum(2);
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, dp(44), 1);
		btnParams.setMargins(dp(6), dp(16), dp(6), 0);
		
		Button startBtn = new Button(this);
		startBtn.setText("开始朗读");
		startBtn.setTextColor(TEXT_WHITE);
		startBtn.setBackground(createBg(accentColor, 12));
		startBtn.setAllCaps(false);
		startBtn.setOnClickListener(v -> {
			startReadingCurrentPage();
		});
		btnRow1.addView(startBtn, btnParams);
		
		Button stopBtn = new Button(this);
		stopBtn.setText("停止朗读");
		stopBtn.setTextColor(TEXT_WHITE);
		stopBtn.setBackground(createBg(btnBgColor, 12));
		stopBtn.setAllCaps(false);
		stopBtn.setOnClickListener(v -> {
			if (ttsEngine != null && ttsEngine.isSpeaking()) {
				ttsEngine.stop();
				Toast.makeText(this, "已停止朗读", Toast.LENGTH_SHORT).show();
			}
		});
		btnRow1.addView(stopBtn, btnParams);
		
		layout.addView(btnRow1);
		
		Button closeFloatBtn = new Button(this);
		closeFloatBtn.setText("关闭悬浮窗");
		closeFloatBtn.setTextColor(TEXT_WHITE);
		closeFloatBtn.setBackground(createBg(btnBgColor, 12));
		closeFloatBtn.setAllCaps(false);
		closeFloatBtn.setOnClickListener(v -> {
			removeTtsFloatView();
		});
		LinearLayout.LayoutParams closeFloatParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(44));
		closeFloatParams.topMargin = dp(16);
		closeFloatBtn.setLayoutParams(closeFloatParams);
		layout.addView(closeFloatBtn);
		
		Button toggleAnchorBtn = new Button(this);
		toggleAnchorBtn.setText(ttsAnchorActive ? "🔴 移除锚点" : "⚓ 设置锚点");
		toggleAnchorBtn.setTextColor(TEXT_WHITE);
		toggleAnchorBtn.setBackground(createBg(accentColor, 12));
		toggleAnchorBtn.setAllCaps(false);
		toggleAnchorBtn.setOnClickListener(v -> {
			if (ttsAnchorActive) {
				removeTtsAnchorView();
				toggleAnchorBtn.setText("⚓ 设置锚点");
				} else {
				createTtsAnchorView();
				toggleAnchorBtn.setText("🔴 移除锚点");
			}
		});
		LinearLayout.LayoutParams toggleAnchorParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(44));
		toggleAnchorParams.topMargin = dp(12);
		toggleAnchorBtn.setLayoutParams(toggleAnchorParams);
		layout.addView(toggleAnchorBtn);
		
		Button closeDialogBtn = new Button(this);
		closeDialogBtn.setText("关闭弹窗");
		closeDialogBtn.setTextColor(TEXT_WHITE);
		closeDialogBtn.setBackground(createBg(btnBgColor, 12));
		closeDialogBtn.setAllCaps(false);
		closeDialogBtn.setOnClickListener(v -> {
			ttsControlDialog.dismiss();
		});
		LinearLayout.LayoutParams closeDialogParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(44));
		closeDialogParams.topMargin = dp(12);
		closeDialogBtn.setLayoutParams(closeDialogParams);
		layout.addView(closeDialogBtn);
		
		ttsControlDialog.setContentView(layout);
		Window w = ttsControlDialog.getWindow();
		if (w != null) {
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setLayout((int)(getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
			w.setGravity(Gravity.CENTER);
		}
		ttsControlDialog.show();
	}
	
	private void setDefaultMaleVoice() {
		if (ttsEngine == null) return;
		for (Voice voice : ttsEngine.getVoices()) {
			// 简单策略：包含 "male" 或 "zh" 男声，或默认第一个
			if (voice.getName().toLowerCase().contains("male") && voice.getName().toLowerCase().contains("zh")) {
				ttsEngine.setVoice(voice);
				ttsCurrentVoiceName = voice.getName();
				return;
			}
		}
		// 退而求其次：取第一个中文语音
		for (Voice voice : ttsEngine.getVoices()) {
			if (voice.getLocale().equals(Locale.CHINESE) || voice.getLocale().getLanguage().equals("zh")) {
				ttsEngine.setVoice(voice);
				ttsCurrentVoiceName = voice.getName();
				return;
			}
		}
		// 如果都没有，就用系统默认（不设置语音）
	}
	
	// 广告屏蔽
	private Set<String> adBlockRules = new HashSet<>();
	private String importedAdBlockScript = "";
	private boolean isBlockModeActive = false;
	// 自定义屏蔽模式相关
	private boolean isBlockSelectMode = false;
	private boolean isBlockMultiSelect = false;
	private List<String> multiSelectedSelectors = new ArrayList<>();
	private View blockModeOverlay;
	private TextView blockCountText;
	private Button blockConfirmBtn;
	// 新增：增强屏蔽开关状态
	private boolean adAutoBlockEnabled = false;
	private boolean adPreventJumpEnabled = false;
	private boolean disableGyro = false;
	// 朗读水位线 (屏幕百分比，0-100，表示水位线Y坐标所在百分比位置)
	private float ttsWaterlinePercent = 65f;
	// 朗读速度、音高持久化
	private float ttsSpeechRate = 1.0f;
	private float ttsPitch = 1.0f;
	
	// 资源爬取
	private List<CapturedResource> capturedResources = new ArrayList<>();
	private View crawlFloatView;
	private boolean crawlFloatAdded = false;
	private WindowManager.LayoutParams crawlFloatParams;
	private TextView crawlCountTextView; // 悬浮窗显示数量的文本
	private WindowManager windowManager;
	private boolean isManualCrawlMode = false;
	private boolean isCrawlMultiSelect = false;
	private List<String> crawlSelectedUrls = new ArrayList<>();
	
	// 下载管理器
	private DownloadManager downloadManager;
	private List<DownloadEntry> downloadEntries = new ArrayList<>();
	
	// 陀螺仪监听
	private SensorManager sensorManager;
	private SensorEventListener gyroListener;
	
	private static class CapturedResource {
		String url;
		String type; // image, video, audio, etc.
		CapturedResource(String url, String type) {
			this.url = url;
			this.type = type;
		}
	}
	
	private static class DownloadEntry {
		String url;
		String fileName;
		String mimeType;
		long downloadId = -1;
		int progress = 0;
		boolean completed = false;
		String localPath;
	}
	
	//媒体通知与按钮
	private PendingIntent createContentIntent() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return PendingIntent.getActivity(this, 0, intent,
		Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0);
	}
	
	private PendingIntent createMediaActionPendingIntent(String action) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setAction(action);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return PendingIntent.getActivity(this, action.hashCode(), intent,
		Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0);
	}
	
	
	// ==================== 下半部分开始接驳点（成员变量之后为 onCreate）====================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 动态申请通知权限（Android 13+）
		if (Build.VERSION.SDK_INT >= 33) {
			if (checkSelfPermission("android.permission.POST_NOTIFICATIONS")
			!= PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1001);
			}
		}
		
		prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
		// 加载持久化的朗读参数
		ttsSpeechRate = prefs.getFloat(PREF_TTS_SPEECH_RATE, 1.0f);
		ttsPitch = prefs.getFloat(PREF_TTS_PITCH, 1.0f);
		ttsWaterlinePercent = prefs.getFloat(PREF_TTS_WATERLINE_Y, 65f);
		adAutoBlockEnabled = prefs.getBoolean(PREF_AD_AUTO_BLOCK_ENABLED, false);
		adPreventJumpEnabled = prefs.getBoolean(PREF_AD_PREVENT_JUMP, true);
		disableGyro = prefs.getBoolean(PREF_DISABLE_GYRO, false);
		
		// 加载行号模式参数
		ttsUseLineMode = prefs.getBoolean(PREF_TTS_USE_LINE_MODE, false);
		ttsLineStart = prefs.getInt(PREF_TTS_LINE_START, 1);
		ttsLineEnd = prefs.getInt(PREF_TTS_LINE_END, -1);
		
		// 加载新添加的配置
		blockedWebsites = prefs.getStringSet(PREF_BLOCKED_WEBSITES, new HashSet<>());
		textModeEnabled = prefs.getBoolean(PREF_TEXT_MODE, false);
		
		loadThemeColors();
		detectMaliGPU();
		setupWindow();
		
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		
		FrameLayout rootLayout = new FrameLayout(this);
		rootLayout.setBackgroundColor(appBgColor);
		setContentView(rootLayout);
		
		mAppContentLayout = new LinearLayout(this);
		mAppContentLayout.setOrientation(LinearLayout.VERTICAL);
		mAppContentLayout.setLayoutParams(new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		rootLayout.addView(mAppContentLayout);
		
		mFullscreenContainer = new FrameLayout(this);
		mFullscreenContainer.setBackgroundColor(Color.BLACK);
		mFullscreenContainer.setVisibility(View.GONE);
		rootLayout.addView(mFullscreenContainer);
		
		centerLoadingView = new ProgressBar(this);
		centerLoadingView.setVisibility(View.GONE);
		rootLayout.addView(centerLoadingView);
		
		progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setLayoutParams(new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, dp(3)));
		progressBar.setMax(100);
		progressBar.setVisibility(View.GONE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			progressBar.setProgressTintList(ColorStateList.valueOf(accentColor));
		}
		mAppContentLayout.addView(progressBar);
		
		mContentFrame = new FrameLayout(this);
		mContentFrame.setLayoutParams(new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
		mAppContentLayout.addView(mContentFrame);
		
		createSourceView();
		
		navBar = createNavBar();
		mAppContentLayout.addView(navBar);
		
		loadHistoryAndFavorites();
		loadAdBlockRules();
		importedAdBlockScript = prefs.getString(PREF_AD_BLOCK_IMPORTED_SCRIPT, "");
		
		registerNetworkReceiver();
		
		setupAnnouncementCheck();
		
		// 延迟启动本地服务器，避免初始化冲突
		mainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					startLocalServer();
					} catch (Exception e) {
					Log.e(TAG, "启动本地服务器失败", e);
				}
			}
		}, 500);
		
		initMediaSession();
		initTtsEngine();
		
		// 延迟启动代理管理器，避免初始化冲突
		mainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					initProxyManager();
					} catch (Exception e) {
					Log.e(TAG, "初始化代理管理器失败", e);
				}
			}
		}, 1000);
		
		initScriptRecorder();
		initGyroDisable();
		
		loadTabsFromPrefs();
		
		if (tabs.isEmpty()) {
			String homeUrl = getCurrentHomeUrl();
			TabInfo tab = new TabInfo(homeUrl);
			tab.webView = createWebView(homeUrl);
			tab.webView.setVisibility(View.GONE);
			tab.webView.addJavascriptInterface(new JavaScriptInterface(this, tab.id), "Android");
			tab.webView.setWebViewClient(new CustomWebViewClient(tab));
			tab.webView.setWebChromeClient(new CustomWebChromeClient(tab));
			tab.webView.loadUrl(homeUrl);
			tabs.add(tab);
			mContentFrame.addView(tab.webView);
		}
		
		for (TabInfo t : tabs) {
			if (t.webView != null && (t.webView.getUrl() == null || t.webView.getUrl().isEmpty())) {
				t.webView.loadUrl(t.url);
			}
		}
		
		TabInfo defaultTab = null;
		for (TabInfo t : tabs) {
			if (t.isDefaultHome) {
				defaultTab = t;
				break;
			}
		}
		if (defaultTab == null && !tabs.isEmpty()) {
			defaultTab = tabs.get(0);
		}
		
		switchToTab(defaultTab);
		applyOrientation();
		autoCheckForUpdates(false);
	}
	
	// 初始化陀螺仪禁用
	private void initGyroDisable() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (disableGyro) {
			disableGyroscope();
		}
	}
	
	private void disableGyroscope() {
		if (sensorManager != null) {
			Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
			if (gyroSensor != null) {
				if (gyroListener != null) {
					sensorManager.unregisterListener(gyroListener);
				}
				gyroListener = new SensorEventListener() {
					@Override public void onSensorChanged(SensorEvent event) {
						// 什么都不做，阻断陀螺仪数据
					}
					@Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
				};
				sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
		}
	}
	
	private void enableGyroscope() {
		if (sensorManager != null && gyroListener != null) {
			sensorManager.unregisterListener(gyroListener);
			gyroListener = null;
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null && intent.getAction() != null) {
			switch (intent.getAction()) {
				case "play":
				execAudioJs(getPlayAudioJs());
				break;
				case "pause":
				execAudioJs(getPauseAudioJs());
				break;
				case "prev":
				execAudioJs(getPrevAudioJs());
				break;
				case "next":
				execAudioJs(getNextAudioJs());
				break;
				case "dismiss":
				hideMusicNotification();
				execAudioJs(getPauseAudioJs());
				break;
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (currentTab != null && currentTab.webView != null) {
			currentTab.webView.onResume();
			currentTab.webView.resumeTimers();
		}
		if (!isFullscreenMode && !isShowingErrorPage) {
			setImmersiveMode();
		}
		if (localServer == null || !localServer.running.get()) {
			startLocalServer();
		}
		if (isMaliDevice && prefs.getBoolean(PREF_MALI_OPTIMIZATION, true) && currentTab != null && !isLocalResource(currentTab.url)) {
			applyMaliOptimizations();
			injectAntiFlickerCSS();
		}
		if (ttsIsReading && wakeLock != null && !wakeLock.isHeld()) {
			wakeLock.acquire(10*60*1000L);
		}
		applyOrientation();
		restoreAllFloatViews();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (ttsIsReading && wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
		}
		hideAllFloatViews();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (announcementTimer != null) {
			announcementTimer.cancel();
			announcementTimer = null;
		}
		if (networkReceiver != null) {
			try {
				unregisterReceiver(networkReceiver);
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (localServer != null) {
			localServer.stop();
			localServer = null;
		}
		for (TabInfo tab : tabs) {
			if (tab.webView != null) {
				ViewGroup parent = (ViewGroup) tab.webView.getParent();
				if (parent != null) parent.removeView(tab.webView);
				tab.webView.stopLoading();
				tab.webView.setWebViewClient(null);
				tab.webView.setWebChromeClient(null);
				tab.webView.removeJavascriptInterface("Android");
				tab.webView.removeAllViews();
				tab.webView.destroy();
			}
		}
		tabs.clear();
		if (mediaSession != null) {
			mediaSession.release();
			mediaSession = null;
		}
		if (crawlFloatAdded && crawlFloatView != null) {
			try {
				windowManager.removeView(crawlFloatView);
			} catch (Exception ignored) {}
			crawlFloatAdded = false;
			crawlFloatView = null;
		}
		if (crawlResourceDialog != null && crawlResourceDialog.isShowing()) {
			crawlResourceDialog.dismiss();
			crawlResourceDialog = null;
		}
		mainHandler.removeCallbacksAndMessages(null);
		// 移除悬浮窗
		removeTtsFloatView();
		if (ttsEngine != null) {
			ttsEngine.stop();
			ttsEngine.shutdown();
			ttsEngine = null;
		}
		if (ttsServiceIntent != null) {
			stopService(ttsServiceIntent);
		}
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
		}
		enableGyroscope();
	}
	
	@Override
	public void onBackPressed() {
		if (isFullscreenMode) {
			if (currentTab != null && currentTab.webView.getWebChromeClient() instanceof CustomWebChromeClient) {
				((CustomWebChromeClient) currentTab.webView.getWebChromeClient()).onHideCustomView();
			}
			return;
		}
		if (isShowingErrorPage) {
			hideCustomError();
			if (currentTab != null && currentTab.url != null) {
				currentTab.webView.loadUrl(currentTab.url);
			}
			return;
		}
		if (isBlockSelectMode) {
			exitBlockSelectMode();
			return;
		}
		if (isManualCrawlMode) {
			exitManualCrawlMode();
			return;
		}
		if (isNavBarHidden) {
			showNavBar();
			return;
		}
		if (mSourceScrollView != null && mSourceScrollView.getVisibility() == View.VISIBLE) {
			hideSourceView();
			return;
		}
		if (currentTab != null && currentTab.webView.canGoBack()) {
			currentTab.webView.goBack();
			} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(ev);
		
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
			downX = ev.getRawX();
			downY = ev.getRawY();
			isSwiping = false;
			break;
			case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(ev.getRawX() - downX);
			float dy = Math.abs(ev.getRawY() - downY);
			if (!isSwiping && dy > dp(SWIPE_THRESHOLD) && dy > dx) {
				isSwiping = true;
			}
			break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			if (ev.getAction() == MotionEvent.ACTION_UP && isSwiping && isNavBarHidden && !isFullscreenMode && !isShowingErrorPage) {
				velocityTracker.computeCurrentVelocity(1000);
				float velY = velocityTracker.getYVelocity();
				int screenHeight = getResources().getDisplayMetrics().heightPixels;
				boolean fromBottom = downY >= screenHeight - dp(BOTTOM_TOUCH_AREA);
				boolean swipeUp = (ev.getRawY() - downY) < 0;
				
				if (fromBottom && (swipeUp || Math.abs(velY) > VELOCITY_THRESHOLD)) {
					showNavBar();
				}
			}
			if (velocityTracker != null) {
				velocityTracker.recycle();
				velocityTracker = null;
			}
			isSwiping = false;
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		for (TabInfo tab : tabs) {
			if (tab.webView != null) {
				tab.webView.freeMemory();
			}
		}
	}
	
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if (level >= TRIM_MEMORY_MODERATE) {
			for (TabInfo tab : tabs) {
				if (tab.webView != null) {
					tab.webView.freeMemory();
				}
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FILE_CHOOSER_RESULT_CODE) {
			if (mUploadMessageForApi21 != null) {
				Uri[] results = null;
				if (resultCode == RESULT_OK && data != null) {
					String dataString = data.getDataString();
					if (dataString != null) {
						results = new Uri[]{Uri.parse(dataString)};
					}
				}
				mUploadMessageForApi21.onReceiveValue(results);
				mUploadMessageForApi21 = null;
			}
			} else if (requestCode == TTS_FILE_PICK) {
			if (resultCode == RESULT_OK && data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					handleTtsFileImport(uri);
				}
			}
			} else if (requestCode == TTS_VOICE_PACK_PICK) {
			if (resultCode == RESULT_OK && data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					handleVoicePackImport(uri);
				}
			}
			} else if (requestCode == AD_BLOCK_SCRIPT_PICK) {
			if (resultCode == RESULT_OK && data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					handleAdBlockScriptImport(uri);
				}
			}
			} else if (requestCode == VPN_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// 用户同意了 VPN 权限，启动 VPN
				startVpnService();
				} else {
				Toast.makeText(this, "VPN 权限被拒绝", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	// ==================== 屏幕方向 ====================
	private void applyOrientation() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}
	
	// ==================== 主题颜色加载 ====================
	private void loadThemeColors() {
		int themeColor = prefs.getInt(PREF_THEME_COLOR, -1);
		int themeAlpha = prefs.getInt(PREF_THEME_ALPHA, -1);
		if (themeColor != -1 && themeAlpha != -1) {
			appBgColor = themeColor;
			mainBgColor = (themeAlpha << 24) | (themeColor & 0xFFFFFF);
			btnBgColor = mainBgColor;
			accentColor = mainBgColor;
			} else {
			appBgColor = DEFAULT_APP_BG;
			mainBgColor = DEFAULT_MAIN_BG;
			btnBgColor = DEFAULT_BTN_BG;
			accentColor = DEFAULT_ACCENT;
		}
	}
	
	// ==================== 本地服务器管理 ====================
	private void startLocalServer() {
		try {
			if (localServer != null) {
				localServer.stop();
				localServer = null;
			}
			localServer = new LocalHttpServer(this, LOCAL_SERVER_PORT, mainHandler);
			if (localServer.start()) {
				actualServerPort = LOCAL_SERVER_PORT;
				} else {
				actualServerPort = -1;
			}
			} catch (Exception e) {
			Log.e(TAG, "启动本地服务器失败", e);
			actualServerPort = -1;
		}
	}
	
	private String getCurrentHomeUrl() {
		if (localServer != null && localServer.running.get() && actualServerPort > 0) {
			return "http://localhost:" + actualServerPort + "/index.html";
		}
		// 尝试重启一次
		startLocalServer();
		if (localServer != null && localServer.running.get() && actualServerPort > 0) {
			return "http://localhost:" + actualServerPort + "/index.html";
		}
		// 实在不行也强制走端口，不回落 file://
		Toast.makeText(this, "本地服务器异常，请重启应用", Toast.LENGTH_LONG).show();
		return "http://localhost:" + LOCAL_SERVER_PORT + "/index.html";
	}
	
	// ==================== 防闪烁CSS注入 ====================
	private void injectAntiFlickerCSS() {
		if (currentTab == null || currentTab.webView == null) return;
		if (isLocalResource(currentTab.url)) return;
		String css = "canvas, embed, object, video, iframe {" +
		"   -webkit-backface-visibility: hidden !important;" +
		"   backface-visibility: hidden !important;" +
		"   -webkit-transform: translateZ(0) !important;" +
		"   transform: translateZ(0) !important;" +
		"   will-change: transform !important;" +
		"   image-rendering: crisp-edges !important;" +
		"   -webkit-font-smoothing: antialiased !important;" +
		"}";
		String script = "javascript:(function(){" +
		"var style = document.getElementById('anti-flicker-style');" +
		"if(!style) {" +
		"   style = document.createElement('style');" +
		"   style.id = 'anti-flicker-style';" +
		"   document.head.appendChild(style);" +
		"}" +
		"style.innerHTML = '" + css.replace("'", "\\'") + "';" +
		"})();";
		currentTab.webView.evaluateJavascript(script, null);
	}
	
	// ==================== 音乐监控注入 ====================
	private void injectMusicMonitor() {
		if (currentTab == null || currentTab.webView == null) return;
		if (isLocalResource(currentTab.url)) return;
		
		String js = "(function() {" +
		"   if (window.__silongMusicMonitorInstalled) return;" +
		"   window.__silongMusicMonitorInstalled = true;" +
		"" +
		"   function getAudioInfo(audio) {" +
		"       var title = document.title || '未知歌曲';" +
		"       var artist = '';" +
		"       var artistEl = document.querySelector('.artist, .singer, .author, [data-artist]');" +
		"       if (artistEl) artist = artistEl.textContent || artistEl.getAttribute('data-artist') || '';" +
		"       if (audio.getAttribute('title')) title = audio.getAttribute('title');" +
		"       return { title: title.trim(), artist: artist.trim() };" +
		"   }" +
		"" +
		"   function notifyAndroid(audio, isPlaying) {" +
		"       var info = getAudioInfo(audio);" +
		"       Android.onMusicPlay(info.title, info.artist, isPlaying);" +
		"   }" +
		"" +
		"   // 手动监听已经存在的音频" +
		"   var audios = document.querySelectorAll('audio');" +
		"   for (var i = 0; i < audios.length; i++) {" +
		"       (function(audio) {" +
		"           audio.addEventListener('play', function() { notifyAndroid(audio, true); });" +
		"           audio.addEventListener('pause', function() { notifyAndroid(audio, false); });" +
		"           audio.addEventListener('ended', function() { notifyAndroid(audio, false); });" +
		"           if (!audio.paused) notifyAndroid(audio, true);" +
		"       })(audios[i]);" +
		"   }" +
		"" +
		"   // 监听未来添加的音频" +
		"   var observer = new MutationObserver(function(mutations) {" +
		"       mutations.forEach(function(mutation) {" +
		"           mutation.addedNodes.forEach(function(node) {" +
		"               if (node.tagName === 'AUDIO') {" +
		"                   node.addEventListener('play', function() { notifyAndroid(node, true); });" +
		"                   node.addEventListener('pause', function() { notifyAndroid(node, false); });" +
		"                   node.addEventListener('ended', function() { notifyAndroid(node, false); });" +
		"                   if (!node.paused) notifyAndroid(node, true);" +
		"               }" +
		"               if (node.querySelectorAll) {" +
		"                   var innerAudios = node.querySelectorAll('audio');" +
		"                   for (var j = 0; j < innerAudios.length; j++) {" +
		"                       var a = innerAudios[j];" +
		"                       a.addEventListener('play', function() { notifyAndroid(a, true); });" +
		"                       a.addEventListener('pause', function() { notifyAndroid(a, false); });" +
		"                       a.addEventListener('ended', function() { notifyAndroid(a, false); });" +
		"                       if (!a.paused) notifyAndroid(a, true);" +
		"                   }" +
		"               }" +
		"           });" +
		"       });" +
		"   });" +
		"   observer.observe(document.body, { childList: true, subtree: true });" +
		"" +
		"   // 定时扫描补充：每2秒检查一次，防止事件漏掉" +
		"   setInterval(function() {" +
		"       var allAudio = document.querySelectorAll('audio');" +
		"       for (var i = 0; i < allAudio.length; i++) {" +
		"           if (!allAudio[i].paused) {" +
		"               notifyAndroid(allAudio[i], true);" +
		"               break;" +
		"           }" +
		"       }" +
		"   }, 2000);" +
		"})();";
		
		currentTab.webView.evaluateJavascript(js, null);
	}
	
	// ==================== Mali专用优化 ====================
	private void applyMaliOptimizations() {
		if (!isMaliDevice || !prefs.getBoolean(PREF_MALI_OPTIMIZATION, true) || currentTab == null || currentTab.webView == null) return;
		if (isLocalResource(currentTab.url)) return;
		String script = "javascript:(function(){" +
		"try{" +
		"   if(" + prefs.getBoolean(PREF_WEBGL_FORCE, true) + ") {" +
		"       var canvas = document.querySelector('canvas');" +
		"       if(canvas) {" +
		"           var gl = canvas.getContext('webgl2') || canvas.getContext('webgl');" +
		"           if(gl) {" +
		"               gl.getExtension('WEBGL_lose_context');" +
		"           }" +
		"       }" +
		"   }" +
		"   var vsync = " + prefs.getBoolean(PREF_VSYNC_ENABLE, true) + ";" +
		"   var frameSkip = " + prefs.getInt(PREF_FRAME_SKIP, 30) + ";" +
		"   var renderBuffer = " + prefs.getInt(PREF_RENDER_BUFFER, 2) + ";" +
		"} catch(e) { console.log(e); }" +
		"})();";
		currentTab.webView.evaluateJavascript(script, null);
	}
	
	// ==================== 窗口与沉浸式UI初始化 ====================
	private void setupWindow() {
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		if (prefs.getBoolean(PREF_HARDWARE_ACCEL, true)) {
			window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
		
		if (isMaliDevice && prefs.getBoolean(PREF_PIXEL_FORMAT, true)) {
			window.setFormat(PixelFormat.RGBA_8888);
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		
		if (prefs.getBoolean(PREF_DISABLE_WINDOW_ANIM, true)) {
			window.setWindowAnimations(0);
		}
		
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		createNotificationChannel();
		
		setImmersiveMode();
	}
	
	private void setImmersiveMode() {
		if (isFullscreenMode || isShowingErrorPage) return;
		View decorView = getWindow().getDecorView();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			getWindow().setDecorFitsSystemWindows(false);
			android.view.WindowInsetsController controller = getWindow().getInsetsController();
			if (controller != null) {
				controller.hide(android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars());
				controller.setSystemBarsBehavior(android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
			}
			} else {
			decorView.setSystemUiVisibility(
			View.SYSTEM_UI_FLAG_FULLSCREEN
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		}
	}
	
	// ==================== 音乐控制相关（仅后台通知）====================
	private void createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
			CHANNEL_ID,
			"音乐播放",
			NotificationManager.IMPORTANCE_HIGH);
			channel.setDescription("音乐播放控制");
			channel.setShowBadge(false);
			notificationManager.createNotificationChannel(channel);
		}
	}
	
	private void execAudioJs(String jsFunc) {
		if (activeMusicTab != null && activeMusicTab.webView != null) {
			activeMusicTab.webView.evaluateJavascript(jsFunc, null);
		}
	}
	
	private String getPrevAudioJs() {
		return "(function(){" +
		"var audios = document.querySelectorAll('audio');" +
		"if(audios.length > 0) {" +
		"   var current = document.querySelector('audio:playing') || audios[0];" +
		"   var allAudios = Array.from(audios);" +
		"   var idx = allAudios.indexOf(current);" +
		"   if(idx > 0) {" +
		"       allAudios[idx-1].play();" +
		"   }" +
		"}})()";
	}
	
	private String getNextAudioJs() {
		return "(function(){" +
		"var audios = document.querySelectorAll('audio');" +
		"if(audios.length > 0) {" +
		"   var current = document.querySelector('audio:playing') || audios[0];" +
		"   var allAudios = Array.from(audios);" +
		"   var idx = allAudios.indexOf(current);" +
		"   if(idx >= 0 && idx < allAudios.length - 1) {" +
		"       allAudios[idx+1].play();" +
		"   }" +
		"}})()";
	}
	
	private String getPlayAudioJs() {
		return "(function(){var a=document.querySelector('audio'); if(a){a.play();}})()";
	}
	
	private String getPauseAudioJs() {
		return "(function(){var a=document.querySelector('audio'); if(a){a.pause();}})()";
	}
	
	private void initMediaSession() {
		mediaSession = new MediaSession(this, "SilongMusic");
		mediaSession.setCallback(new MediaSession.Callback() {
			@Override
			public void onPlay() {
				execAudioJs(getPlayAudioJs());
			}
			
			@Override
			public void onPause() {
				execAudioJs(getPauseAudioJs());
			}
			
			@Override
			public void onSkipToNext() {
				execAudioJs(getNextAudioJs());
			}
			
			@Override
			public void onSkipToPrevious() {
				execAudioJs(getPrevAudioJs());
			}
		});
		
		playbackStateBuilder = new PlaybackState.Builder()
		.setActions(PlaybackState.ACTION_PLAY_PAUSE |
		PlaybackState.ACTION_SKIP_TO_NEXT |
		PlaybackState.ACTION_SKIP_TO_PREVIOUS);
		mediaSession.setPlaybackState(playbackStateBuilder.build());
		mediaSession.setActive(true);
	}
	
	private void updateMediaSession(String tabId, String title, String artist, boolean isPlaying) {
		TabInfo tab = null;
		for (TabInfo t : tabs) {
			if (t.id.equals(tabId)) {
				tab = t;
				break;
			}
		}
		if (tab == null) return;
		
		activeMusicTab = tab;
		musicTitle = title != null ? title : "未知歌曲";
		musicArtist = artist != null ? artist : "未知艺术家";
		musicPlaying = isPlaying;
		
		playbackStateBuilder.setState(
		isPlaying ? PlaybackState.STATE_PLAYING : PlaybackState.STATE_PAUSED,
		0,
		1.0f);
		mediaSession.setPlaybackState(playbackStateBuilder.build());
		
		if (isPlaying) {
			showMusicNotification();
			} else {
			hideMusicNotification();
		}
	}
	
	private void showMusicNotification() {
		createNotificationChannel();
		
		android.media.session.MediaSession.Token token = mediaSession.getSessionToken();
		
		PendingIntent playPausePI = createMediaActionPendingIntent(musicPlaying ? "pause" : "play");
		PendingIntent prevPI = createMediaActionPendingIntent("prev");
		PendingIntent nextPI = createMediaActionPendingIntent("next");
		PendingIntent dismissPI = createMediaActionPendingIntent("dismiss");
		
		Notification.Builder builder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			builder = new Notification.Builder(this, CHANNEL_ID);
			} else {
			builder = new Notification.Builder(this);
		}
		
		builder.setSmallIcon(android.R.drawable.ic_media_play)
		.setContentTitle(musicTitle)
		.setContentText(musicArtist)
		.setOngoing(true)
		.setVisibility(Notification.VISIBILITY_PUBLIC)
		.setContentIntent(createContentIntent())
		.setDeleteIntent(dismissPI)
		.addAction(new Notification.Action.Builder(
		android.R.drawable.ic_media_previous, "上一首", prevPI).build())
		.addAction(new Notification.Action.Builder(
		musicPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play,
		"播放/暂停", playPausePI).build())
		.addAction(new Notification.Action.Builder(
		android.R.drawable.ic_media_next, "下一首", nextPI).build())
		.setStyle(new Notification.MediaStyle()
		.setMediaSession(token)
		.setShowActionsInCompactView(0, 1, 2));
		
		notificationManager.notify(NOTIFICATION_ID, builder.build());
		mediaSession.setActive(true);
	}
	
	private void hideMusicNotification() {
		notificationManager.cancel(NOTIFICATION_ID);
	}
	
	// ==================== UI组件创建方法 ====================
	private void addSectionTitle(LinearLayout parent, String title) {
		TextView tv = new TextView(this);
		tv.setText(title);
		tv.setTextColor(TEXT_WHITE);
		tv.setTextSize(16);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(0, dp(20), 0, dp(8));
		parent.addView(tv);
	}
	
	private void createSourceView() {
		mSourceScrollView = new ScrollView(this);
		mSourceScrollView.setLayoutParams(new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mSourceScrollView.setBackground(createBg(0xEE0B0018, 0));
		mSourceScrollView.setVisibility(View.GONE);
		
		LinearLayout sourceLayout = new LinearLayout(this);
		sourceLayout.setOrientation(LinearLayout.VERTICAL);
		sourceLayout.setPadding(dp(16), dp(16), dp(16), dp(16));
		
		LinearLayout topBar = new LinearLayout(this);
		topBar.setOrientation(LinearLayout.HORIZONTAL);
		topBar.setGravity(Gravity.CENTER_VERTICAL);
		topBar.setPadding(0, 0, 0, dp(12));
		
		mSourceCloseButton = new Button(this);
		mSourceCloseButton.setText(" 关闭");
		mSourceCloseButton.setTextColor(TEXT_WHITE);
		mSourceCloseButton.setBackground(createBg(btnBgColor, 20));
		mSourceCloseButton.setAllCaps(false);
		mSourceCloseButton.setOnClickListener(v -> hideSourceView());
		topBar.addView(mSourceCloseButton);
		
		mSourceWrapButton = new Button(this);
		mSourceWrapButton.setText("↵ 换行");
		mSourceWrapButton.setTextColor(TEXT_WHITE);
		mSourceWrapButton.setBackground(createBg(btnBgColor, 20));
		mSourceWrapButton.setAllCaps(false);
		mSourceWrapButton.setOnClickListener(v -> toggleSourceWrap());
		topBar.addView(mSourceWrapButton);
		
		TextView title = new TextView(this);
		title.setText("页面源代码");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(18);
		title.setTypeface(null, Typeface.BOLD);
		title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		title.setGravity(Gravity.END);
		topBar.addView(title);
		
		sourceLayout.addView(topBar);
		
		mSourceTextView = new TextView(this);
		mSourceTextView.setTextColor(TEXT_WHITE);
		mSourceTextView.setTextSize(12);
		mSourceTextView.setTypeface(Typeface.MONOSPACE);
		mSourceTextView.setLineSpacing(dp(2), 1f);
		mSourceTextView.setHorizontallyScrolling(true);
		sourceLayout.addView(mSourceTextView);
		
		mSourceScrollView.addView(sourceLayout);
		mContentFrame.addView(mSourceScrollView);
	}
	
	private void showBackForwardPopup() {
		PopupWindow popup = new PopupWindow(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setBackground(createBg(mainBgColor, 12));
		layout.setPadding(dp(12), dp(8), dp(12), dp(8));
		layout.setGravity(Gravity.CENTER);
		
		Button backBtn = new Button(this);
		backBtn.setText("← 后退");
		backBtn.setTextColor(TEXT_WHITE);
		backBtn.setTextSize(14);
		backBtn.setBackground(createBg(btnBgColor, 8));
		backBtn.setPadding(dp(16), dp(8), dp(16), dp(8));
		backBtn.setAllCaps(false);
		backBtn.setOnClickListener(v -> {
			popup.dismiss();
			if (currentTab != null && currentTab.webView != null && currentTab.webView.canGoBack()) {
				currentTab.webView.goBack();
				} else {
				Toast.makeText(this, "没有可后退的页面", Toast.LENGTH_SHORT).show();
			}
		});
		LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		backParams.setMargins(0, 0, dp(8), 0);
		backBtn.setLayoutParams(backParams);
		layout.addView(backBtn);
		
		Button fwdBtn = new Button(this);
		fwdBtn.setText("前进 →");
		fwdBtn.setTextColor(TEXT_WHITE);
		fwdBtn.setTextSize(14);
		fwdBtn.setBackground(createBg(btnBgColor, 8));
		fwdBtn.setPadding(dp(16), dp(8), dp(16), dp(8));
		fwdBtn.setAllCaps(false);
		fwdBtn.setOnClickListener(v -> {
			popup.dismiss();
			if (currentTab != null && currentTab.webView != null && currentTab.webView.canGoForward()) {
				currentTab.webView.goForward();
				} else {
				Toast.makeText(this, "没有可前进的页面", Toast.LENGTH_SHORT).show();
			}
		});
		layout.addView(fwdBtn);
		
		popup.setContentView(layout);
		popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popup.setFocusable(true);
		popup.setOutsideTouchable(true);
		popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		View anchor = navBar.getChildAt(0);
		anchor.post(() -> {
			popup.showAsDropDown(anchor, 0, -dp(anchor.getHeight() + popup.getHeight() + 8));
		});
	}
	
	private GradientDrawable createBg(int color, int radiusDp) {
		GradientDrawable gd = new GradientDrawable();
		gd.setShape(GradientDrawable.RECTANGLE);
		gd.setColor(color);
		gd.setCornerRadius(dp(radiusDp));
		return gd;
	}
	
	private WebView createWebView(String url) {
		WebView wv = new WebView(this);
		wv.setLayoutParams(new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		if (prefs.getBoolean(PREF_FIXED_BG, true)) {
			String bgColorStr = prefs.getString(PREF_BG_COLOR, "#0B0018");
			try {
				wv.setBackgroundColor(Color.parseColor(bgColorStr));
				} catch (Exception e) {
				wv.setBackgroundColor(appBgColor);
			}
			} else {
			wv.setBackgroundColor(Color.TRANSPARENT);
		}
		
		wv.setVerticalScrollBarEnabled(false);
		wv.setHorizontalScrollBarEnabled(false);
		wv.setOverScrollMode(View.OVER_SCROLL_NEVER);
		wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		
		if (isMaliDevice && prefs.getBoolean(PREF_MALI_OPTIMIZATION, true)) {
		}
		
		if (prefs.getBoolean(PREF_DISABLE_LONGPRESS, true)) {
			wv.setOnLongClickListener(v -> true);
		}
		
		int style = prefs.getBoolean("pref_disable_redraw_anim", true)
		? View.SCROLLBARS_INSIDE_OVERLAY
		: View.SCROLLBARS_INSIDE_INSET;
		wv.setScrollBarStyle(style);
		
		initWebView(wv);
		return wv;
	}
	
	private LinearLayout createNavBar() {
		LinearLayout bar = new LinearLayout(this);
		bar.setOrientation(LinearLayout.HORIZONTAL);
		bar.setGravity(Gravity.CENTER);
		bar.setBackground(createBg(mainBgColor, 0));
		bar.setPadding(dp(4), dp(6), dp(4), dp(6));
		bar.setLayoutParams(new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		addNavButton(bar, "⇄", "返回/前进", v -> showBackForwardPopup());
		addNavButton(bar, "⟳", "刷新", v -> {
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.reload();
			}
		});
		addNavButton(bar, "⌂", "首页", v -> {
			String homeUrl = getCurrentHomeUrl();
			if (currentTab != null && currentTab.webView != null) {
				if (isShowingErrorPage) hideCustomError();
				currentTab.webView.loadUrl(homeUrl);
				currentTab.url = homeUrl;
			}
		});
		addNavButton(bar, "▣", "标签页", v -> showTabManager());
		addNavButton(bar, "⚙", "设置", v -> showSettingsDialog());
		
		return bar;
	}
	
	private void addNavButton(LinearLayout parent, String icon, String desc, View.OnClickListener click) {
		TextView btn = new TextView(this);
		btn.setText(icon);
		btn.setTextSize(20);
		btn.setTextColor(TEXT_WHITE);
		btn.setGravity(Gravity.CENTER);
		btn.setPadding(0, dp(6), 0, dp(6));
		btn.setBackground(createBg(btnBgColor, 10));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(44), 1);
		params.setMargins(dp(3), 0, dp(3), 0);
		btn.setLayoutParams(params);
		btn.setOnClickListener(click);
		btn.setOnLongClickListener(v -> {
			Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
			return true;
		});
		parent.addView(btn);
	}
	
	// ==================== 设置对话框（优化间距） ====================
	private void showSettingsDialog() {
		Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ScrollView scrollView = new ScrollView(this);
		scrollView.setBackgroundColor(Color.TRANSPARENT);
		scrollView.setPadding(dp(12), dp(12), dp(12), dp(12));
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackground(createBg(mainBgColor, 24));
		mainLayout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(this);
		title.setText("设置");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(28);
		title.setGravity(Gravity.CENTER);
		title.setTypeface(null, Typeface.BOLD);
		mainLayout.addView(title);
		
		// ---------- 页面信息 ----------
		LinearLayout pageInfoLayout = new LinearLayout(this);
		pageInfoLayout.setOrientation(LinearLayout.VERTICAL);
		pageInfoLayout.setPadding(dp(12), dp(12), dp(12), dp(12));
		pageInfoLayout.setBackground(createBg(0x44000000, 12));
		LinearLayout.LayoutParams pageInfoParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		pageInfoParams.topMargin = dp(12);
		pageInfoParams.bottomMargin = dp(12);
		pageInfoLayout.setLayoutParams(pageInfoParams);
		
		TextView pageTitle = new TextView(this);
		pageTitle.setText("当前页面：" + (currentTab != null && currentTab.title != null ? currentTab.title : "无标题"));
		pageTitle.setTextColor(TEXT_GRAY);
		pageTitle.setTextSize(14);
		pageTitle.setGravity(Gravity.CENTER);
		pageInfoLayout.addView(pageTitle);
		
		TextView pageUrl = new TextView(this);
		pageUrl.setText(currentTab != null ? currentTab.url : "");
		pageUrl.setTextColor(TEXT_DISABLE);
		pageUrl.setTextSize(12);
		pageUrl.setGravity(Gravity.CENTER);
		pageInfoLayout.addView(pageUrl);
		
		LinearLayout pageBtnRow = new LinearLayout(this);
		pageBtnRow.setOrientation(LinearLayout.HORIZONTAL);
		pageBtnRow.setWeightSum(4);
		pageBtnRow.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams pageBtnRowParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		pageBtnRowParams.topMargin = dp(8);
		pageBtnRow.setLayoutParams(pageBtnRowParams);
		
		Button favBtn = new Button(this);
		favBtn.setText(isCurrentUrlFavorite() ? " ★已收藏" : " ☆收藏");
		favBtn.setTextColor(TEXT_WHITE);
		favBtn.setBackground(createBg(btnBgColor, 12));
		favBtn.setAllCaps(false);
		favBtn.setOnClickListener(v -> {
			toggleFavorite();
			favBtn.setText(isCurrentUrlFavorite() ? " ★已收藏" : " ☆收藏");
			Toast.makeText(MainActivity.this, "点击可将当前页面添加或移除收藏", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams favParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		favParams.setMargins(0, 0, dp(4), 0);
		pageBtnRow.addView(favBtn, favParams);
		
		Button externalBtn = new Button(this);
		externalBtn.setText("外部打开");
		externalBtn.setTextColor(TEXT_WHITE);
		externalBtn.setBackground(createBg(btnBgColor, 12));
		externalBtn.setAllCaps(false);
		externalBtn.setOnClickListener(v -> {
			dialog.dismiss();
			openInExternalBrowser();
			Toast.makeText(MainActivity.this, "使用系统浏览器打开当前页面", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams externalParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		externalParams.setMargins(dp(4), 0, dp(4), 0);
		pageBtnRow.addView(externalBtn, externalParams);
		
		Button copyUrlBtn = new Button(this);
		copyUrlBtn.setText("复制链接");
		copyUrlBtn.setTextColor(TEXT_WHITE);
		copyUrlBtn.setBackground(createBg(btnBgColor, 12));
		copyUrlBtn.setAllCaps(false);
		copyUrlBtn.setOnClickListener(v -> {
			if (currentTab != null && currentTab.url != null && !currentTab.url.isEmpty()) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("URL", currentTab.url);
				clipboard.setPrimaryClip(clip);
				Toast.makeText(this, "链接已复制到剪贴板", Toast.LENGTH_SHORT).show();
				} else {
				Toast.makeText(this, "无有效链接", Toast.LENGTH_SHORT).show();
			}
		});
		LinearLayout.LayoutParams copyParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		copyParams.setMargins(dp(4), 0, dp(4), 0);
		pageBtnRow.addView(copyUrlBtn, copyParams);
		
		Button blockBtn = new Button(this);
		blockBtn.setText("屏蔽");
		blockBtn.setTextColor(TEXT_WHITE);
		blockBtn.setBackground(createBg(btnBgColor, 12));
		blockBtn.setAllCaps(false);
		blockBtn.setOnClickListener(v -> {
			if (currentTab != null && currentTab.url != null) {
				blockCurrentWebsite();
			}
		});
		LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		blockParams.setMargins(dp(4), 0, 0, 0);
		pageBtnRow.addView(blockBtn, blockParams);
		
		pageInfoLayout.addView(pageBtnRow);
		mainLayout.addView(pageInfoLayout);
		
		// ---------- 历史与收藏 ----------
		addSectionTitle(mainLayout, "📖 历史与收藏");
		LinearLayout histFavRow = new LinearLayout(this);
		histFavRow.setOrientation(LinearLayout.HORIZONTAL);
		histFavRow.setWeightSum(2);
		histFavRow.setGravity(Gravity.CENTER);
		
		Button historyBtn = new Button(this);
		historyBtn.setText("浏览历史");
		historyBtn.setTextColor(TEXT_WHITE);
		historyBtn.setBackground(createBg(btnBgColor, 12));
		historyBtn.setAllCaps(false);
		historyBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showHistoryDialog();
			Toast.makeText(MainActivity.this, "查看最近访问的页面记录", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams historyParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		historyParams.setMargins(0, 0, dp(4), 0);
		histFavRow.addView(historyBtn, historyParams);
		
		Button favListBtn = new Button(this);
		favListBtn.setText("我的收藏");
		favListBtn.setTextColor(TEXT_WHITE);
		favListBtn.setBackground(createBg(btnBgColor, 12));
		favListBtn.setAllCaps(false);
		favListBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showFavoritesDialog();
			Toast.makeText(MainActivity.this, "查看已收藏的页面", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams favListParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		favListParams.setMargins(dp(4), 0, 0, 0);
		histFavRow.addView(favListBtn, favListParams);
		
		mainLayout.addView(histFavRow);
		
		// ---------- 显示设置 ----------
		addSectionTitle(mainLayout, "🖥️ 显示设置");
		LinearLayout displayLayout = new LinearLayout(this);
		displayLayout.setOrientation(LinearLayout.VERTICAL);
		displayLayout.setBackground(createBg(0x44000000, 12));
		displayLayout.setPadding(dp(12), dp(12), dp(12), dp(12));
		LinearLayout.LayoutParams displayParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		displayParams.bottomMargin = dp(12);
		displayLayout.setLayoutParams(displayParams);
		
		LinearLayout displayRow1 = new LinearLayout(this);
		displayRow1.setOrientation(LinearLayout.HORIZONTAL);
		displayRow1.setWeightSum(3);
		displayRow1.setGravity(Gravity.CENTER);
		Switch nightSwitch = new Switch(this);
		nightSwitch.setText("夜间模式");
		nightSwitch.setTextColor(TEXT_WHITE);
		nightSwitch.setChecked(prefs.getBoolean(PREF_NIGHT_MODE, false));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			nightSwitch.setTrackTintList(ColorStateList.valueOf(btnBgColor));
			nightSwitch.setThumbTintList(ColorStateList.valueOf(TEXT_WHITE));
		}
		nightSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
			// 立即应用夜间模式
			if (isChecked) {
				injectNightMode();
				} else {
				removeNightMode();
			}
		});
		LinearLayout.LayoutParams nightParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		nightParams.setMargins(0, 0, dp(4), 0);
		displayRow1.addView(nightSwitch, nightParams);
		
		Switch imageSwitch = new Switch(this);
		imageSwitch.setText("无图模式");
		imageSwitch.setTextColor(TEXT_WHITE);
		imageSwitch.setChecked(!prefs.getBoolean(PREF_IMAGE_LOADING, true));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			imageSwitch.setTrackTintList(ColorStateList.valueOf(btnBgColor));
			imageSwitch.setThumbTintList(ColorStateList.valueOf(TEXT_WHITE));
		}
		imageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
			prefs.edit().putBoolean(PREF_IMAGE_LOADING, !isChecked).apply();
			for (TabInfo tab : tabs) {
				if (tab.webView != null) {
					tab.webView.getSettings().setBlockNetworkImage(isChecked);
				}
			}
		});
		imageSwitch.setOnClickListener(v -> Toast.makeText(MainActivity.this, "不加载网页图片，节省流量与加快速度", Toast.LENGTH_SHORT).show());
		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		imageParams.setMargins(dp(4), 0, dp(4), 0);
		displayRow1.addView(imageSwitch, imageParams);
		
		Switch textModeSwitch = new Switch(this);
		textModeSwitch.setText("文字模式");
		textModeSwitch.setTextColor(TEXT_WHITE);
		textModeSwitch.setChecked(prefs.getBoolean(PREF_TEXT_MODE, false));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			textModeSwitch.setTrackTintList(ColorStateList.valueOf(btnBgColor));
			textModeSwitch.setThumbTintList(ColorStateList.valueOf(TEXT_WHITE));
		}
		textModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
			prefs.edit().putBoolean(PREF_TEXT_MODE, isChecked).apply();
			textModeEnabled = isChecked;
			Toast.makeText(MainActivity.this, "文字模式已" + (isChecked ? "开启" : "关闭"), Toast.LENGTH_SHORT).show();
			// 立即对当前页面应用或取消文字模式
			applyTextMode();
		});
		LinearLayout.LayoutParams textModeParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		textModeParams.setMargins(dp(4), 0, 0, 0);
		displayRow1.addView(textModeSwitch, textModeParams);
		
		displayLayout.addView(displayRow1);
		
		LinearLayout uaRow = new LinearLayout(this);
		uaRow.setOrientation(LinearLayout.HORIZONTAL);
		uaRow.setWeightSum(2);
		uaRow.setGravity(Gravity.CENTER);
		uaRow.setPadding(0, dp(8), 0, 0);
		Button uaBtn = new Button(this);
		uaBtn.setText("切换 User-Agent");
		uaBtn.setTextColor(TEXT_WHITE);
		uaBtn.setBackground(createBg(btnBgColor, 12));
		uaBtn.setAllCaps(false);
		uaBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showUaSelectorDialog();
			Toast.makeText(MainActivity.this, "选择不同设备标识以适配网站显示", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams uaBtnParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		uaBtnParams.setMargins(0, 0, dp(8), 0);
		uaRow.addView(uaBtn, uaBtnParams);
		
		TextView currentUaTip = new TextView(this);
		String savedUa = prefs.getString(PREF_USER_AGENT, "默认移动端UA");
		currentUaTip.setText("当前：" + (savedUa.length() > 10 ? savedUa.substring(0, 10) + "..." : savedUa));
		currentUaTip.setTextColor(TEXT_DISABLE);
		currentUaTip.setTextSize(12);
		currentUaTip.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams uaTipParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		uaTipParams.setMargins(dp(8), 0, 0, 0);
		uaRow.addView(currentUaTip, uaTipParams);
		displayLayout.addView(uaRow);
		
		TextView serverStatus = new TextView(this);
		serverStatus.setText("本地服务器：" + (localServer != null && localServer.running.get() ? "运行中 (8080)" : "未启动"));
		serverStatus.setTextColor(TEXT_GRAY);
		serverStatus.setTextSize(12);
		serverStatus.setGravity(Gravity.CENTER);
		serverStatus.setPadding(0, dp(8), 0, 0);
		displayLayout.addView(serverStatus);
		mainLayout.addView(displayLayout);
		
		// ---------- 缺失的页面操作 ----------
		addSectionTitle(mainLayout, "📎 缺失的页面操作");
		LinearLayout pageOpRow = new LinearLayout(this);
		pageOpRow.setOrientation(LinearLayout.HORIZONTAL);
		pageOpRow.setWeightSum(3);
		pageOpRow.setGravity(Gravity.CENTER);
		
		Button sourceBtn = new Button(this);
		sourceBtn.setText("查看源码");
		sourceBtn.setTextColor(TEXT_WHITE);
		sourceBtn.setBackground(createBg(btnBgColor, 12));
		sourceBtn.setAllCaps(false);
		sourceBtn.setOnClickListener(v -> {
			dialog.dismiss();
			fetchAndShowSource();
			Toast.makeText(MainActivity.this, "查看网页HTML源代码", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams sourceParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		sourceParams.setMargins(0, 0, dp(4), 0);
		pageOpRow.addView(sourceBtn, sourceParams);
		
		Button shareBtn = new Button(this);
		shareBtn.setText("分享链接");
		shareBtn.setTextColor(TEXT_WHITE);
		shareBtn.setBackground(createBg(btnBgColor, 12));
		shareBtn.setAllCaps(false);
		shareBtn.setOnClickListener(v -> {
			dialog.dismiss();
			shareCurrentUrl();
			Toast.makeText(MainActivity.this, "将当前页面链接分享给其他应用", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams shareParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		shareParams.setMargins(dp(4), 0, dp(4), 0);
		pageOpRow.addView(shareBtn, shareParams);
		
		Button fullscreenBtn = new Button(this);
		fullscreenBtn.setText("全屏模式");
		fullscreenBtn.setTextColor(TEXT_WHITE);
		fullscreenBtn.setBackground(createBg(btnBgColor, 12));
		fullscreenBtn.setAllCaps(false);
		fullscreenBtn.setOnClickListener(v -> {
			hideNavBar();
			dialog.dismiss();
			Toast.makeText(MainActivity.this, "全屏模式，从底部上滑恢复导航栏", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams fullscreenParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		fullscreenParams.setMargins(dp(4), 0, 0, 0);
		pageOpRow.addView(fullscreenBtn, fullscreenParams);
		mainLayout.addView(pageOpRow);
		
		// ---------- 下载和爬取资源 ----------
		addSectionTitle(mainLayout, "📥 下载和爬取");
		LinearLayout downloadCrawlRow = new LinearLayout(this);
		downloadCrawlRow.setOrientation(LinearLayout.HORIZONTAL);
		downloadCrawlRow.setWeightSum(2);
		downloadCrawlRow.setGravity(Gravity.CENTER);
		
		Button downloadBtn = new Button(this);
		downloadBtn.setText("📥 下载管理");
		downloadBtn.setTextColor(TEXT_WHITE);
		downloadBtn.setBackground(createBg(accentColor, 12));
		downloadBtn.setAllCaps(false);
		downloadBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showDownloadManagerDialog();
			Toast.makeText(MainActivity.this, "应用内下载管理，查看、安装已下载文件", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams downloadParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		downloadParams.setMargins(0, 0, dp(4), 0);
		downloadCrawlRow.addView(downloadBtn, downloadParams);
		
		Button crawlBtn = new Button(this);
		crawlBtn.setText("🕷️ 爬取资源");
		crawlBtn.setTextColor(TEXT_WHITE);
		crawlBtn.setBackground(createBg(btnBgColor, 12));
		crawlBtn.setAllCaps(false);
		crawlBtn.setOnClickListener(v -> {
			dialog.dismiss();
			Toast.makeText(MainActivity.this, "爬取功能：自动扫描页面资源并可通过悬浮窗查看下载", Toast.LENGTH_SHORT).show();
			startCrawlMode();
		});
		LinearLayout.LayoutParams crawlParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		crawlParams.setMargins(dp(4), 0, 0, 0);
		downloadCrawlRow.addView(crawlBtn, crawlParams);
		mainLayout.addView(downloadCrawlRow);
		
		// ---------- 全球代理管理 ----------
		addSectionTitle(mainLayout, "🌐 全球代理管理");
		LinearLayout proxyLayout = new LinearLayout(this);
		proxyLayout.setOrientation(LinearLayout.VERTICAL);
		proxyLayout.setBackground(createBg(0x44000000, 12));
		proxyLayout.setPadding(dp(12), dp(12), dp(12), dp(12));
		LinearLayout.LayoutParams proxyParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		proxyParams.bottomMargin = dp(12);
		proxyLayout.setLayoutParams(proxyParams);
		
		TextView proxyStatusText = new TextView(this);
		proxyStatusText.setText("代理池: " + proxyManager.getAliveCount() + "/" + proxyManager.getTotalCount() + " 个可用");
		proxyStatusText.setTextColor(TEXT_GRAY);
		proxyStatusText.setTextSize(13);
		proxyStatusText.setGravity(Gravity.CENTER);
		proxyStatusText.setPadding(0, 0, 0, dp(8));
		proxyLayout.addView(proxyStatusText);
		
		LinearLayout proxyModeRow = new LinearLayout(this);
		proxyModeRow.setOrientation(LinearLayout.HORIZONTAL);
		proxyModeRow.setWeightSum(3);
		proxyModeRow.setGravity(Gravity.CENTER);
		
		Button autoModeBtn = new Button(this);
		Button fixedModeBtn = new Button(this);
		Button offModeBtn = new Button(this);
		
		autoModeBtn.setText("启动VPN");
		autoModeBtn.setTextColor(TEXT_WHITE);
		autoModeBtn.setBackground(createBg(btnBgColor, 12));
		autoModeBtn.setAllCaps(false);
		autoModeBtn.setOnClickListener(v -> {
			// 请求系统 VPN 权限
			Intent vpnIntent = VpnService.prepare(MainActivity.this);
			if (vpnIntent != null) {
				startActivityForResult(vpnIntent, 1001);
				} else {
				// 已经有权限，直接启动 VPN
				startVpnService();
			}
		});
		LinearLayout.LayoutParams autoModeParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		autoModeParams.setMargins(0, 0, dp(4), 0);
		proxyModeRow.addView(autoModeBtn, autoModeParams);
		
		fixedModeBtn.setText("固定节点");
		fixedModeBtn.setTextColor(TEXT_WHITE);
		fixedModeBtn.setBackground(createBg(proxyManager.getProxyMode() == ProxyManager.MODE_FIXED ? accentColor : btnBgColor, 12));
		fixedModeBtn.setAllCaps(false);
		fixedModeBtn.setOnClickListener(v -> {
			proxyManager.setProxyMode(ProxyManager.MODE_FIXED);
			autoModeBtn.setBackground(createBg(btnBgColor, 12));
			fixedModeBtn.setBackground(createBg(accentColor, 12));
			offModeBtn.setBackground(createBg(btnBgColor, 12));
			showProxyPickerDialog(fixedModeBtn);
		});
		LinearLayout.LayoutParams fixedModeParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		fixedModeParams.setMargins(dp(4), 0, dp(4), 0);
		proxyModeRow.addView(fixedModeBtn, fixedModeParams);
		
		offModeBtn.setText("停止VPN");
		offModeBtn.setTextColor(TEXT_WHITE);
		offModeBtn.setBackground(createBg(btnBgColor, 12));
		offModeBtn.setAllCaps(false);
		offModeBtn.setOnClickListener(v -> {
			stopVpnService();
		});
		LinearLayout.LayoutParams offModeParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		offModeParams.setMargins(dp(4), 0, 0, 0);
		proxyModeRow.addView(offModeBtn, offModeParams);
		proxyLayout.addView(proxyModeRow);
		
		LinearLayout proxyActionRow = new LinearLayout(this);
		proxyActionRow.setOrientation(LinearLayout.HORIZONTAL);
		proxyActionRow.setWeightSum(4);
		proxyActionRow.setGravity(Gravity.CENTER);
		proxyActionRow.setPadding(0, dp(8), 0, 0);
		
		Button crawlProxyBtn = new Button(this);
		crawlProxyBtn.setText("抓取代理");
		crawlProxyBtn.setTextColor(TEXT_WHITE);
		crawlProxyBtn.setBackground(createBg(accentColor, 12));
		crawlProxyBtn.setAllCaps(false);
		crawlProxyBtn.setOnClickListener(v -> {
			crawlProxyBtn.setText("抓取中...");
			crawlProxyBtn.setEnabled(false);
			proxyManager.startCrawl();
			mainHandler.postDelayed(() -> {
				crawlProxyBtn.setText("抓取代理");
				crawlProxyBtn.setEnabled(true);
				proxyStatusText.setText("代理池: " + proxyManager.getAliveCount() + "/" + proxyManager.getTotalCount() + " 个可用");
				Toast.makeText(MainActivity.this, "代理抓取完成", Toast.LENGTH_SHORT).show();
			}, 15000);
		});
		LinearLayout.LayoutParams crawlProxyParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		crawlProxyParams.setMargins(0, 0, dp(4), 0);
		proxyActionRow.addView(crawlProxyBtn, crawlProxyParams);
		
		Button validateProxyBtn = new Button(this);
		validateProxyBtn.setText("验证全部");
		validateProxyBtn.setTextColor(TEXT_WHITE);
		validateProxyBtn.setBackground(createBg(btnBgColor, 12));
		validateProxyBtn.setAllCaps(false);
		validateProxyBtn.setOnClickListener(v -> {
			validateProxyBtn.setText("验证中...");
			validateProxyBtn.setEnabled(false);
			proxyManager.validateAllProxies();
			mainHandler.postDelayed(() -> {
				validateProxyBtn.setText("验证全部");
				validateProxyBtn.setEnabled(true);
				proxyStatusText.setText("代理池: " + proxyManager.getAliveCount() + "/" + proxyManager.getTotalCount() + " 个可用");
				Toast.makeText(MainActivity.this, "验证完成", Toast.LENGTH_SHORT).show();
			}, 10000);
		});
		LinearLayout.LayoutParams validateProxyParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		validateProxyParams.setMargins(dp(4), 0, dp(4), 0);
		proxyActionRow.addView(validateProxyBtn, validateProxyParams);
		
		Button cleanProxyBtn = new Button(this);
		cleanProxyBtn.setText("清洗节点");
		cleanProxyBtn.setTextColor(TEXT_WHITE);
		cleanProxyBtn.setBackground(createBg(btnBgColor, 12));
		cleanProxyBtn.setAllCaps(false);
		cleanProxyBtn.setOnClickListener(v -> {
			cleanProxyBtn.setText("清洗中...");
			cleanProxyBtn.setEnabled(false);
			proxyManager.startClean();
			mainHandler.postDelayed(() -> {
				cleanProxyBtn.setText("清洗节点");
				cleanProxyBtn.setEnabled(true);
				proxyStatusText.setText("代理池: " + proxyManager.getAliveCount() + "/" + proxyManager.getTotalCount() + " 个可用");
				Toast.makeText(MainActivity.this, "清洗完成，失效节点已移除", Toast.LENGTH_SHORT).show();
			}, 10000);
		});
		LinearLayout.LayoutParams cleanProxyParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		cleanProxyParams.setMargins(dp(4), 0, dp(4), 0);
		proxyActionRow.addView(cleanProxyBtn, cleanProxyParams);
		
		Button viewProxyBtn = new Button(this);
		viewProxyBtn.setText("查看列表");
		viewProxyBtn.setTextColor(TEXT_WHITE);
		viewProxyBtn.setBackground(createBg(btnBgColor, 12));
		viewProxyBtn.setAllCaps(false);
		viewProxyBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showProxyListDialog();
		});
		LinearLayout.LayoutParams viewProxyParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		viewProxyParams.setMargins(dp(4), 0, 0, 0);
		proxyActionRow.addView(viewProxyBtn, viewProxyParams);
		proxyLayout.addView(proxyActionRow);
		
		TextView proxyTip = new TextView(this);
		proxyTip.setText("7×24小时自动更新 · 每日自动清洗失效节点 · 智能延迟调度");
		proxyTip.setTextColor(TEXT_DISABLE);
		proxyTip.setTextSize(11);
		proxyTip.setGravity(Gravity.CENTER);
		proxyTip.setPadding(0, dp(6), 0, 0);
		proxyLayout.addView(proxyTip);
		
		mainLayout.addView(proxyLayout);
		
		// ---------- 可视化脚本录制 ----------
		addSectionTitle(mainLayout, "🎬 可视化脚本录制");
		LinearLayout scriptLayout = new LinearLayout(this);
		scriptLayout.setOrientation(LinearLayout.VERTICAL);
		scriptLayout.setBackground(createBg(0x44000000, 12));
		scriptLayout.setPadding(dp(12), dp(12), dp(12), dp(12));
		LinearLayout.LayoutParams scriptParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		scriptParams.bottomMargin = dp(12);
		scriptLayout.setLayoutParams(scriptParams);
		
		TextView scriptStatusText = new TextView(this);
		scriptStatusText.setText(isScriptRecording ? "● 录制中 (" + scriptRecorder.getRecordedActionCount() + " 个操作)" : "○ 待机中");
		scriptStatusText.setTextColor(isScriptRecording ? 0xFFFF4444 : TEXT_GRAY);
		scriptStatusText.setTextSize(13);
		scriptStatusText.setGravity(Gravity.CENTER);
		scriptStatusText.setPadding(0, 0, 0, dp(8));
		scriptLayout.addView(scriptStatusText);
		
		LinearLayout scriptRow1 = new LinearLayout(this);
		scriptRow1.setOrientation(LinearLayout.HORIZONTAL);
		scriptRow1.setWeightSum(2);
		scriptRow1.setGravity(Gravity.CENTER);
		
		Button recordBtn = new Button(this);
		recordBtn.setText(isScriptRecording ? "⏹ 停止录制" : "⏺ 开始录制");
		recordBtn.setTextColor(TEXT_WHITE);
		recordBtn.setBackground(createBg(isScriptRecording ? 0xCCFF4444 : accentColor, 12));
		recordBtn.setAllCaps(false);
		recordBtn.setOnClickListener(v -> {
			if (!isScriptRecording) {
				if (currentTab != null && currentTab.webView != null) {
					scriptRecorder.startRecording(currentTab.webView);
					isScriptRecording = true;
					recordBtn.setText("⏹ 停止录制");
					recordBtn.setBackground(createBg(0xCCFF4444, 12));
					scriptStatusText.setText("● 录制中 (0 个操作)");
					scriptStatusText.setTextColor(0xFFFF4444);
					Toast.makeText(MainActivity.this, "录制已开始，操作页面即可记录", Toast.LENGTH_SHORT).show();
				}
				} else {
				JSONObject script = scriptRecorder.stopRecording();
				isScriptRecording = false;
				recordBtn.setText("⏺ 开始录制");
				recordBtn.setBackground(createBg(accentColor, 12));
				scriptStatusText.setText("○ 待机中");
				scriptStatusText.setTextColor(TEXT_GRAY);
				if (script != null) {
					String json = script.toString();
					scriptRecorder.importScriptFromJson(json);
					Toast.makeText(MainActivity.this, "录制完成，共 " + scriptRecorder.getRecordedActionCount() + " 个操作", Toast.LENGTH_SHORT).show();
				}
			}
		});
		LinearLayout.LayoutParams recordParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		recordParams.setMargins(0, 0, dp(4), 0);
		scriptRow1.addView(recordBtn, recordParams);
		
		Button playBtn = new Button(this);
		playBtn.setText(isScriptPlaying ? "⏹ 停止播放" : "▶ 一键播放");
		playBtn.setTextColor(TEXT_WHITE);
		playBtn.setBackground(createBg(isScriptPlaying ? 0xCCFF4444 : btnBgColor, 12));
		playBtn.setAllCaps(false);
		playBtn.setOnClickListener(v -> {
			if (!isScriptPlaying) {
				if (currentTab != null && currentTab.webView != null) {
					dialog.dismiss();
					new android.os.Handler().postDelayed(() -> {
						scriptRecorder.startPlayback(currentTab.webView, false);
						isScriptPlaying = true;
						Toast.makeText(MainActivity.this, "脚本播放中，自动复现录制操作", Toast.LENGTH_SHORT).show();
					}, 300);
					} else {
					Toast.makeText(MainActivity.this, "请先录制或导入脚本", Toast.LENGTH_SHORT).show();
				}
				} else {
				scriptRecorder.stopPlayback();
				isScriptPlaying = false;
				playBtn.setText("▶ 一键播放");
				playBtn.setBackground(createBg(btnBgColor, 12));
				Toast.makeText(MainActivity.this, "播放已停止", Toast.LENGTH_SHORT).show();
			}
		});
		LinearLayout.LayoutParams playParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		playParams.setMargins(dp(4), 0, 0, 0);
		scriptRow1.addView(playBtn, playParams);
		scriptLayout.addView(scriptRow1);
		
		LinearLayout scriptRow2 = new LinearLayout(this);
		scriptRow2.setOrientation(LinearLayout.HORIZONTAL);
		scriptRow2.setWeightSum(3);
		scriptRow2.setGravity(Gravity.CENTER);
		scriptRow2.setPadding(0, dp(8), 0, 0);
		
		Button presetBtn = new Button(this);
		presetBtn.setText("📥 预设脚本");
		presetBtn.setTextColor(TEXT_WHITE);
		presetBtn.setBackground(createBg(accentColor, 12));
		presetBtn.setAllCaps(false);
		presetBtn.setOnClickListener(v -> {
			presetBtn.setText("加载中...");
			presetBtn.setEnabled(false);
			scriptRecorder.loadRemoteScript();
			mainHandler.postDelayed(() -> {
				presetBtn.setText("📥 预设脚本");
				presetBtn.setEnabled(true);
				Toast.makeText(MainActivity.this, "预设脚本已加载，可一键播放", Toast.LENGTH_SHORT).show();
			}, 8000);
		});
		LinearLayout.LayoutParams presetParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		presetParams.setMargins(0, 0, dp(4), 0);
		scriptRow2.addView(presetBtn, presetParams);
		
		Button exportBtn = new Button(this);
		exportBtn.setText("导出脚本");
		exportBtn.setTextColor(TEXT_WHITE);
		exportBtn.setBackground(createBg(btnBgColor, 12));
		exportBtn.setAllCaps(false);
		exportBtn.setOnClickListener(v -> {
			String json = scriptRecorder.getScriptJson();
			if (json != null && !json.equals("{}")) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("script", json);
				clipboard.setPrimaryClip(clip);
				Toast.makeText(MainActivity.this, "脚本JSON已复制到剪贴板，可分享导入", Toast.LENGTH_SHORT).show();
				} else {
				Toast.makeText(MainActivity.this, "请先录制脚本", Toast.LENGTH_SHORT).show();
			}
		});
		LinearLayout.LayoutParams exportParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		exportParams.setMargins(dp(4), 0, dp(4), 0);
		scriptRow2.addView(exportBtn, exportParams);
		
		Button importBtn = new Button(this);
		importBtn.setText("导入脚本");
		importBtn.setTextColor(TEXT_WHITE);
		importBtn.setBackground(createBg(btnBgColor, 12));
		importBtn.setAllCaps(false);
		importBtn.setOnClickListener(v -> {
			showScriptImportDialog();
		});
		LinearLayout.LayoutParams importParams = new LinearLayout.LayoutParams(0, dp(36), 1);
		importParams.setMargins(dp(4), 0, 0, 0);
		scriptRow2.addView(importBtn, importParams);
		scriptLayout.addView(scriptRow2);
		
		TextView scriptTip = new TextView(this);
		scriptTip.setText("预设地址: silong-respool.github.io/gay/sous.json");
		scriptTip.setTextColor(TEXT_DISABLE);
		scriptTip.setTextSize(11);
		scriptTip.setGravity(Gravity.CENTER);
		scriptTip.setPadding(0, dp(6), 0, 0);
		scriptLayout.addView(scriptTip);
		
		mainLayout.addView(scriptLayout);
		
		// ---------- 朗读设置（水位线、行号模式、速度、音高、试听、情绪规则、语音包） ----------
		addSectionTitle(mainLayout, "🔊 朗读设置");
		LinearLayout ttsSettingsLayout = new LinearLayout(this);
		ttsSettingsLayout.setOrientation(LinearLayout.VERTICAL);
		ttsSettingsLayout.setBackground(createBg(0x44000000, 12));
		ttsSettingsLayout.setPadding(dp(12), dp(12), dp(12), dp(12));
		LinearLayout.LayoutParams ttsSettingsParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		ttsSettingsParams.bottomMargin = dp(12);
		ttsSettingsLayout.setLayoutParams(ttsSettingsParams);
		
		// 语音选择
		LinearLayout voiceSelectRow = new LinearLayout(this);
		voiceSelectRow.setOrientation(LinearLayout.HORIZONTAL);
		voiceSelectRow.setGravity(Gravity.CENTER_VERTICAL);
		TextView voiceLabel = new TextView(this);
		voiceLabel.setText("语音：");
		voiceLabel.setTextColor(TEXT_WHITE);
		voiceSelectRow.addView(voiceLabel);
		// 使用 EditText 模拟选择（也可以使用 Spinner，为简单用按钮弹出列表）
		TextView currentVoiceText = new TextView(this);
		currentVoiceText.setText(ttsCurrentVoiceName != null ? ttsCurrentVoiceName : "默认");
		currentVoiceText.setTextColor(TEXT_WHITE);
		currentVoiceText.setBackground(createBg(btnBgColor, 8));
		currentVoiceText.setPadding(dp(12), dp(8), dp(12), dp(8));
		currentVoiceText.setClickable(true);
		currentVoiceText.setOnClickListener(v -> {
			// 弹出语音列表选择
			showVoicePickerDialog(currentVoiceText);
		});
		voiceSelectRow.addView(currentVoiceText, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		ttsSettingsLayout.addView(voiceSelectRow);
		
		// 语音包下载部分
		LinearLayout voicePackLayout = new LinearLayout(this);
		voicePackLayout.setOrientation(LinearLayout.VERTICAL);
		voicePackLayout.setPadding(0, dp(8), 0, dp(8));
		TextView voicePackLabel = new TextView(this);
		voicePackLabel.setText("下载语音包（输入URL）");
		voicePackLabel.setTextColor(TEXT_GRAY);
		voicePackLabel.setTextSize(14);
		voicePackLayout.addView(voicePackLabel);
		
		LinearLayout voicePackRow = new LinearLayout(this);
		voicePackRow.setOrientation(LinearLayout.HORIZONTAL);
		voicePackRow.setGravity(Gravity.CENTER);
		EditText voicePackUrlEdit = new EditText(this);
		voicePackUrlEdit.setHint("语音包下载地址");
		voicePackUrlEdit.setTextColor(TEXT_WHITE);
		voicePackUrlEdit.setHintTextColor(TEXT_DISABLE);
		voicePackUrlEdit.setBackground(createBg(btnBgColor, 8));
		voicePackUrlEdit.setSingleLine(true);
		voicePackUrlEdit.setPadding(dp(8), dp(8), dp(8), dp(8));
		voicePackRow.addView(voicePackUrlEdit, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		
		Button voicePackDownloadBtn = new Button(this);
		voicePackDownloadBtn.setText("下载");
		voicePackDownloadBtn.setTextColor(TEXT_WHITE);
		voicePackDownloadBtn.setBackground(createBg(accentColor, 12));
		voicePackDownloadBtn.setAllCaps(false);
		voicePackDownloadBtn.setOnClickListener(v -> {
			String url = voicePackUrlEdit.getText().toString().trim();
			if (url.isEmpty()) {
				Toast.makeText(this, "请输入下载地址", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!url.startsWith("http")) {
				url = "http://" + url;
				voicePackUrlEdit.setText(url);
			}
			try {
				DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				request.setDestinationInExternalPublicDir("Download", "tts_voice_pack_" + System.currentTimeMillis() + ".apk");
				downloadManager.enqueue(request);
				Toast.makeText(this, "开始下载语音包，下载完成后请手动安装", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
				Toast.makeText(this, "下载失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		voicePackRow.addView(voicePackDownloadBtn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		voicePackLayout.addView(voicePackRow);
		
		Button importVoicePackBtn = new Button(this);
		importVoicePackBtn.setText("从本地导入语音包文件");
		importVoicePackBtn.setTextColor(TEXT_WHITE);
		importVoicePackBtn.setBackground(createBg(btnBgColor, 12));
		importVoicePackBtn.setAllCaps(false);
		importVoicePackBtn.setOnClickListener(v -> {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("*/*");
			startActivityForResult(Intent.createChooser(intent, "选择语音包文件"), TTS_VOICE_PACK_PICK);
		});
		voicePackLayout.addView(importVoicePackBtn);
		ttsSettingsLayout.addView(voicePackLayout);
		
		// 模式切换开关
		LinearLayout modeSwitchRow = new LinearLayout(this);
		modeSwitchRow.setOrientation(LinearLayout.HORIZONTAL);
		modeSwitchRow.setGravity(Gravity.CENTER_VERTICAL);
		Switch lineModeSwitch = new Switch(this);
		lineModeSwitch.setText("行号模式（百分比模式关闭）");
		lineModeSwitch.setTextColor(TEXT_WHITE);
		lineModeSwitch.setChecked(ttsUseLineMode);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			lineModeSwitch.setTrackTintList(ColorStateList.valueOf(btnBgColor));
			lineModeSwitch.setThumbTintList(ColorStateList.valueOf(TEXT_WHITE));
		}
		modeSwitchRow.addView(lineModeSwitch, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		ttsSettingsLayout.addView(modeSwitchRow);
		
		// 水位线区域 (仅百分比模式可见)
		LinearLayout waterlineContainer = new LinearLayout(this);
		waterlineContainer.setOrientation(LinearLayout.VERTICAL);
		waterlineContainer.setVisibility(ttsUseLineMode ? View.GONE : View.VISIBLE);
		
		TextView waterlineLabel = new TextView(this);
		waterlineLabel.setText("朗读区域（开始 - 结束）");
		waterlineLabel.setTextColor(TEXT_GRAY);
		waterlineLabel.setTextSize(14);
		waterlineLabel.setGravity(Gravity.CENTER);
		waterlineContainer.addView(waterlineLabel);
		
		LinearLayout startRow = new LinearLayout(this);
		startRow.setOrientation(LinearLayout.HORIZONTAL);
		startRow.setGravity(Gravity.CENTER_VERTICAL);
		TextView startText = new TextView(this);
		startText.setText("开始：");
		startText.setTextColor(TEXT_WHITE);
		startRow.addView(startText);
		SeekBar startSeek = new SeekBar(this);
		startSeek.setMax(100);
		startSeek.setProgress((int) ttsWaterlinePercent);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			startSeek.setProgressTintList(ColorStateList.valueOf(accentColor));
			startSeek.setThumbTintList(ColorStateList.valueOf(accentColor));
		}
		startSeek.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		startRow.addView(startSeek);
		TextView startValue = new TextView(this);
		startValue.setText(String.valueOf((int) ttsWaterlinePercent) + "%");
		startValue.setTextColor(TEXT_WHITE);
		startRow.addView(startValue);
		waterlineContainer.addView(startRow);
		
		LinearLayout endRow = new LinearLayout(this);
		endRow.setOrientation(LinearLayout.HORIZONTAL);
		endRow.setGravity(Gravity.CENTER_VERTICAL);
		TextView endText = new TextView(this);
		endText.setText("结束：");
		endText.setTextColor(TEXT_WHITE);
		endRow.addView(endText);
		SeekBar endSeek = new SeekBar(this);
		endSeek.setMax(100);
		float endPercent = prefs.getFloat("tts_waterline_end", 90f);
		endSeek.setProgress((int) endPercent);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			endSeek.setProgressTintList(ColorStateList.valueOf(accentColor));
			endSeek.setThumbTintList(ColorStateList.valueOf(accentColor));
		}
		endSeek.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		endRow.addView(endSeek);
		TextView endValue = new TextView(this);
		endValue.setText((int) endPercent + "%");
		endValue.setTextColor(TEXT_WHITE);
		endRow.addView(endValue);
		waterlineContainer.addView(endRow);
		
		startSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				startValue.setText(progress + "%");
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		endSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				endValue.setText(progress + "%");
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		ttsSettingsLayout.addView(waterlineContainer);
		
		// 行号区域 (仅行号模式可见)
		LinearLayout lineContainer = new LinearLayout(this);
		lineContainer.setOrientation(LinearLayout.VERTICAL);
		lineContainer.setVisibility(ttsUseLineMode ? View.VISIBLE : View.GONE);
		
		TextView lineLabel = new TextView(this);
		lineLabel.setText("行号范围（支持负数倒数）");
		lineLabel.setTextColor(TEXT_GRAY);
		lineLabel.setTextSize(14);
		lineLabel.setGravity(Gravity.CENTER);
		lineContainer.addView(lineLabel);
		
		LinearLayout lineInputRow = new LinearLayout(this);
		lineInputRow.setOrientation(LinearLayout.HORIZONTAL);
		lineInputRow.setGravity(Gravity.CENTER);
		EditText lineStartEdit = new EditText(this);
		lineStartEdit.setHint("开始行");
		lineStartEdit.setText(String.valueOf(ttsLineStart));
		lineStartEdit.setTextColor(TEXT_WHITE);
		lineStartEdit.setHintTextColor(TEXT_DISABLE);
		lineStartEdit.setBackground(createBg(btnBgColor, 8));
		lineStartEdit.setWidth(dp(80));
		lineStartEdit.setGravity(Gravity.CENTER);
		lineInputRow.addView(lineStartEdit);
		TextView toText = new TextView(this);
		toText.setText(" 至 ");
		toText.setTextColor(TEXT_WHITE);
		lineInputRow.addView(toText);
		EditText lineEndEdit = new EditText(this);
		lineEndEdit.setHint("结束行 (-1 结尾)");
		lineEndEdit.setText(String.valueOf(ttsLineEnd));
		lineEndEdit.setTextColor(TEXT_WHITE);
		lineEndEdit.setHintTextColor(TEXT_DISABLE);
		lineEndEdit.setBackground(createBg(btnBgColor, 8));
		lineEndEdit.setWidth(dp(80));
		lineEndEdit.setGravity(Gravity.CENTER);
		lineInputRow.addView(lineEndEdit);
		lineContainer.addView(lineInputRow);
		ttsSettingsLayout.addView(lineContainer);
		
		// 模式切换监听
		lineModeSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
			waterlineContainer.setVisibility(isChecked ? View.GONE : View.VISIBLE);
			lineContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
		});
		
		// 朗读速度设置
		TextView speedLabel = new TextView(this);
		speedLabel.setText("朗读速度（0.1 - 10.0 倍）");
		speedLabel.setTextColor(TEXT_GRAY);
		speedLabel.setTextSize(14);
		speedLabel.setGravity(Gravity.CENTER);
		ttsSettingsLayout.addView(speedLabel);
		
		LinearLayout speedRow = new LinearLayout(this);
		speedRow.setOrientation(LinearLayout.HORIZONTAL);
		speedRow.setGravity(Gravity.CENTER_VERTICAL);
		SeekBar speedSeek = new SeekBar(this);
		speedSeek.setMax(100);
		speedSeek.setProgress((int) ((ttsSpeechRate - 0.1f) / 9.9f * 100));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			speedSeek.setProgressTintList(ColorStateList.valueOf(accentColor));
			speedSeek.setThumbTintList(ColorStateList.valueOf(accentColor));
		}
		speedSeek.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		speedRow.addView(speedSeek);
		EditText speedInput = new EditText(this);
		speedInput.setText(String.valueOf(ttsSpeechRate));
		speedInput.setTextColor(TEXT_WHITE);
		speedInput.setBackground(createBg(btnBgColor, 8));
		speedInput.setGravity(Gravity.CENTER);
		speedInput.setLayoutParams(new LinearLayout.LayoutParams(dp(60), ViewGroup.LayoutParams.WRAP_CONTENT));
		speedRow.addView(speedInput);
		ttsSettingsLayout.addView(speedRow);
		
		speedSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float rate = 0.1f + (progress / 100f) * 9.9f;
				speedInput.setText(String.format("%.1f", rate));
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		// 朗读音高设置
		TextView pitchLabel = new TextView(this);
		pitchLabel.setText("音高（0.5 - 2.0）");
		pitchLabel.setTextColor(TEXT_GRAY);
		pitchLabel.setTextSize(14);
		pitchLabel.setGravity(Gravity.CENTER);
		ttsSettingsLayout.addView(pitchLabel);
		
		LinearLayout pitchRow = new LinearLayout(this);
		pitchRow.setOrientation(LinearLayout.HORIZONTAL);
		pitchRow.setGravity(Gravity.CENTER_VERTICAL);
		SeekBar pitchSeek = new SeekBar(this);
		pitchSeek.setMax(100);
		pitchSeek.setProgress((int) ((ttsPitch - 0.5f) / 1.5f * 100));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			pitchSeek.setProgressTintList(ColorStateList.valueOf(accentColor));
			pitchSeek.setThumbTintList(ColorStateList.valueOf(accentColor));
		}
		pitchSeek.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		pitchRow.addView(pitchSeek);
		EditText pitchInput = new EditText(this);
		pitchInput.setText(String.valueOf(ttsPitch));
		pitchInput.setTextColor(TEXT_WHITE);
		pitchInput.setBackground(createBg(btnBgColor, 8));
		pitchInput.setGravity(Gravity.CENTER);
		pitchInput.setLayoutParams(new LinearLayout.LayoutParams(dp(60), ViewGroup.LayoutParams.WRAP_CONTENT));
		pitchRow.addView(pitchInput);
		ttsSettingsLayout.addView(pitchRow);
		
		pitchSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float pitch = 0.5f + (progress / 100f) * 1.5f;
				pitchInput.setText(String.format("%.1f", pitch));
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		// 试听和确认应用
		LinearLayout ttsActionRow = new LinearLayout(this);
		ttsActionRow.setOrientation(LinearLayout.HORIZONTAL);
		ttsActionRow.setWeightSum(3);
		ttsActionRow.setGravity(Gravity.CENTER);
		ttsActionRow.setPadding(0, dp(8), 0, dp(8));
		
		Button enableTtsBtn = new Button(this);
		enableTtsBtn.setText("开启朗读");
		enableTtsBtn.setTextColor(TEXT_WHITE);
		enableTtsBtn.setBackground(createBg(btnBgColor, 12));
		enableTtsBtn.setAllCaps(false);
		enableTtsBtn.setOnClickListener(v -> {
			createTtsFloatView();
			Toast.makeText(MainActivity.this, "已显示朗读悬浮窗", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams enableTtsParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		enableTtsParams.setMargins(0, 0, dp(4), 0);
		ttsActionRow.addView(enableTtsBtn, enableTtsParams);
		
		Button testTtsBtn = new Button(this);
		testTtsBtn.setText("试听");
		testTtsBtn.setTextColor(TEXT_WHITE);
		testTtsBtn.setBackground(createBg(btnBgColor, 12));
		testTtsBtn.setAllCaps(false);
		testTtsBtn.setOnClickListener(v -> {
			float testRate = parseFloat(speedInput.getText().toString(), 1.0f);
			float testPitch = parseFloat(pitchInput.getText().toString(), 1.0f);
			if (ttsEngine != null) {
				ttsEngine.setSpeechRate(testRate);
				ttsEngine.setPitch(testPitch);
				String testText = "这是一条朗读测试，声音、情绪、速度、音高均已根据您的设定调整。";
				if (ttsCurrentVoiceName != null) {
					for (Voice voice : ttsEngine.getVoices()) {
						if (voice.getName().equals(ttsCurrentVoiceName)) {
							ttsEngine.setVoice(voice);
							break;
						}
					}
					} else {
					setDefaultMaleVoice();
				}
				ttsEngine.speak(testText, TextToSpeech.QUEUE_FLUSH, null, "TEST_TTS");
			}
			Toast.makeText(MainActivity.this, "试听当前音色与参数", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams testTtsParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		testTtsParams.setMargins(dp(4), 0, dp(4), 0);
		ttsActionRow.addView(testTtsBtn, testTtsParams);
		
		Button confirmTtsBtn = new Button(this);
		confirmTtsBtn.setText("应用设置");
		confirmTtsBtn.setTextColor(TEXT_WHITE);
		confirmTtsBtn.setBackground(createBg(accentColor, 12));
		confirmTtsBtn.setAllCaps(false);
		confirmTtsBtn.setOnClickListener(v -> {
			boolean useLineMode = lineModeSwitch.isChecked();
			prefs.edit().putBoolean(PREF_TTS_USE_LINE_MODE, useLineMode).apply();
			ttsUseLineMode = useLineMode;
			
			try {
				ttsLineStart = Integer.parseInt(lineStartEdit.getText().toString().trim());
			} catch (Exception e) { ttsLineStart = 1; }
			try {
				ttsLineEnd = Integer.parseInt(lineEndEdit.getText().toString().trim());
			} catch (Exception e) { ttsLineEnd = -1; }
			prefs.edit().putInt(PREF_TTS_LINE_START, ttsLineStart).apply();
			prefs.edit().putInt(PREF_TTS_LINE_END, ttsLineEnd).apply();
			
			float startPercent = startSeek.getProgress();
			float endPercentValue = endSeek.getProgress();
			if (startPercent > endPercentValue) {
				Toast.makeText(this, "开始位置不能大于结束位置", Toast.LENGTH_SHORT).show();
				return;
			}
			ttsWaterlinePercent = startPercent;
			prefs.edit().putFloat(PREF_TTS_WATERLINE_Y, ttsWaterlinePercent).apply();
			prefs.edit().putFloat("tts_waterline_end", endPercentValue).apply();
			
			float rate = parseFloat(speedInput.getText().toString(), 1.0f);
			if (rate < 0.1f) rate = 0.1f;
			else if (rate > 10.0f) rate = 10.0f;
			ttsSpeechRate = rate;
			prefs.edit().putFloat(PREF_TTS_SPEECH_RATE, ttsSpeechRate).apply();
			
			float pitch = parseFloat(pitchInput.getText().toString(), 1.0f);
			if (pitch < 0.5f) pitch = 0.5f;
			else if (pitch > 2.0f) pitch = 2.0f;
			ttsPitch = pitch;
			prefs.edit().putFloat(PREF_TTS_PITCH, ttsPitch).apply();
			
			if (ttsEngine != null) {
				ttsEngine.setSpeechRate(ttsSpeechRate);
				ttsEngine.setPitch(ttsPitch);
			}
			Toast.makeText(this, "朗读设置已保存", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams confirmTtsParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		confirmTtsParams.setMargins(dp(4), 0, 0, 0);
		ttsActionRow.addView(confirmTtsBtn, confirmTtsParams);
		ttsSettingsLayout.addView(ttsActionRow);
		
		mainLayout.addView(ttsSettingsLayout);
		
		// ---------- 广告屏蔽增强 ----------
		addSectionTitle(mainLayout, "🛡️ 广告屏蔽");
		LinearLayout adBlockLayout = new LinearLayout(this);
		adBlockLayout.setOrientation(LinearLayout.VERTICAL);
		adBlockLayout.setBackground(createBg(0x44000000, 12));
		adBlockLayout.setPadding(dp(12), dp(12), dp(12), dp(12));
		LinearLayout.LayoutParams adBlockParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		adBlockParams.bottomMargin = dp(12);
		adBlockLayout.setLayoutParams(adBlockParams);
		
		LinearLayout adSwitchRow = new LinearLayout(this);
		adSwitchRow.setOrientation(LinearLayout.HORIZONTAL);
		adSwitchRow.setWeightSum(3);
		adSwitchRow.setGravity(Gravity.CENTER);
		
		Switch autoBlockSwitch = new Switch(this);
		autoBlockSwitch.setText("自动屏蔽弹窗");
		autoBlockSwitch.setTextColor(TEXT_WHITE);
		autoBlockSwitch.setChecked(adAutoBlockEnabled);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			autoBlockSwitch.setTrackTintList(ColorStateList.valueOf(btnBgColor));
			autoBlockSwitch.setThumbTintList(ColorStateList.valueOf(TEXT_WHITE));
		}
		autoBlockSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
			adAutoBlockEnabled = isChecked;
			prefs.edit().putBoolean(PREF_AD_AUTO_BLOCK_ENABLED, isChecked).apply();
			if (currentTab != null && currentTab.webView != null && !isLocalResource(currentTab.url)) {
				if (isChecked) {
					injectAutoPopupBlockerJS();
				}
			}
			Toast.makeText(MainActivity.this, isChecked ? "自动屏蔽弹窗已开启" : "自动屏蔽弹窗已关闭", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams autoBlockParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		autoBlockParams.setMargins(0, 0, dp(4), 0);
		adSwitchRow.addView(autoBlockSwitch, autoBlockParams);
		
		Switch preventJumpSwitch = new Switch(this);
		preventJumpSwitch.setText("禁止自动跳转");
		preventJumpSwitch.setTextColor(TEXT_WHITE);
		preventJumpSwitch.setChecked(adPreventJumpEnabled);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			preventJumpSwitch.setTrackTintList(ColorStateList.valueOf(btnBgColor));
			preventJumpSwitch.setThumbTintList(ColorStateList.valueOf(TEXT_WHITE));
		}
		preventJumpSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
			adPreventJumpEnabled = isChecked;
			prefs.edit().putBoolean(PREF_AD_PREVENT_JUMP, isChecked).apply();
			if (currentTab != null && currentTab.webView != null && !isLocalResource(currentTab.url)) {
				if (isChecked) {
					injectPreventAutomaticJumpJS();
				}
			}
			Toast.makeText(MainActivity.this, isChecked ? "禁止自动跳转已开启" : "禁止自动跳转已关闭", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams preventJumpParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		preventJumpParams.setMargins(dp(4), 0, dp(4), 0);
		adSwitchRow.addView(preventJumpSwitch, preventJumpParams);
		
		Switch gyroSwitch = new Switch(this);
		gyroSwitch.setText("关闭陀螺仪");
		gyroSwitch.setTextColor(TEXT_WHITE);
		gyroSwitch.setChecked(disableGyro);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			gyroSwitch.setTrackTintList(ColorStateList.valueOf(btnBgColor));
			gyroSwitch.setThumbTintList(ColorStateList.valueOf(TEXT_WHITE));
		}
		gyroSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
			disableGyro = isChecked;
			prefs.edit().putBoolean(PREF_DISABLE_GYRO, isChecked).apply();
			if (isChecked) {
				disableGyroscope();
				} else {
				enableGyroscope();
			}
			Toast.makeText(MainActivity.this, isChecked ? "陀螺仪已关闭" : "陀螺仪已开启", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams gyroParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		gyroParams.setMargins(dp(4), 0, 0, 0);
		adSwitchRow.addView(gyroSwitch, gyroParams);
		adBlockLayout.addView(adSwitchRow);
		
		LinearLayout adButtonRow = new LinearLayout(this);
		adButtonRow.setOrientation(LinearLayout.HORIZONTAL);
		adButtonRow.setWeightSum(3);
		adButtonRow.setGravity(Gravity.CENTER);
		adButtonRow.setPadding(0, dp(8), 0, 0);
		
		Button customBlockBtn = new Button(this);
		customBlockBtn.setText("自定义屏蔽");
		customBlockBtn.setTextColor(TEXT_WHITE);
		customBlockBtn.setBackground(createBg(btnBgColor, 12));
		customBlockBtn.setAllCaps(false);
		customBlockBtn.setOnClickListener(v -> {
			dialog.dismiss();
			startCustomBlockMode();
			Toast.makeText(MainActivity.this, "手动选择页面元素进行屏蔽（仅对当前页面生效）", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams customBlockParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		customBlockParams.setMargins(0, 0, dp(4), 0);
		adButtonRow.addView(customBlockBtn, customBlockParams);
		
		Button ruleMgrBtn = new Button(this);
		ruleMgrBtn.setText("导入脚本");
		ruleMgrBtn.setTextColor(TEXT_WHITE);
		ruleMgrBtn.setBackground(createBg(btnBgColor, 12));
		ruleMgrBtn.setAllCaps(false);
		ruleMgrBtn.setOnClickListener(v -> {
			dialog.dismiss();
			importAdBlockScript();
		});
		LinearLayout.LayoutParams ruleMgrParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		ruleMgrParams.setMargins(dp(4), 0, dp(4), 0);
		adButtonRow.addView(ruleMgrBtn, ruleMgrParams);
		
		Button websiteMgrBtn = new Button(this);
		websiteMgrBtn.setText("网页管理");
		websiteMgrBtn.setTextColor(TEXT_WHITE);
		websiteMgrBtn.setBackground(createBg(btnBgColor, 12));
		websiteMgrBtn.setAllCaps(false);
		websiteMgrBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showWebsiteBlockManagerDialog();
			Toast.makeText(MainActivity.this, "管理已屏蔽的网站", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams websiteMgrParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		websiteMgrParams.setMargins(dp(4), 0, 0, 0);
		adButtonRow.addView(websiteMgrBtn, websiteMgrParams);
		adBlockLayout.addView(adButtonRow);
		
		mainLayout.addView(adBlockLayout);
		
		// ---------- 公告、检查版本、主题颜色设置 ----------
		addSectionTitle(mainLayout, "📢 公告 & 🔄 检查更新 & 🎨 主题颜色");
		LinearLayout otherActionsRow = new LinearLayout(this);
		otherActionsRow.setOrientation(LinearLayout.HORIZONTAL);
		otherActionsRow.setWeightSum(3);
		otherActionsRow.setGravity(Gravity.CENTER);
		
		Button announcementBtn = new Button(this);
		announcementBtn.setText("查看公告");
		announcementBtn.setTextColor(TEXT_WHITE);
		announcementBtn.setBackground(createBg(btnBgColor, 12));
		announcementBtn.setAllCaps(false);
		announcementBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showAnnouncementContentDialog();
			Toast.makeText(MainActivity.this, "开发者发布的最新公告或更新日志", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams announcementParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		announcementParams.setMargins(0, 0, dp(4), 0);
		otherActionsRow.addView(announcementBtn, announcementParams);
		
		Button updateBtn = new Button(this);
		updateBtn.setText("检查更新");
		updateBtn.setTextColor(TEXT_WHITE);
		updateBtn.setBackground(createBg(btnBgColor, 12));
		updateBtn.setAllCaps(false);
		updateBtn.setOnClickListener(v -> {
			dialog.dismiss();
			autoCheckForUpdates(true);
			Toast.makeText(MainActivity.this, "联网检测新版本", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams updateParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		updateParams.setMargins(dp(4), 0, dp(4), 0);
		otherActionsRow.addView(updateBtn, updateParams);
		
		Button themeBtn = new Button(this);
		themeBtn.setText("主题颜色设置");
		themeBtn.setTextColor(TEXT_WHITE);
		themeBtn.setBackground(createBg(btnBgColor, 12));
		themeBtn.setAllCaps(false);
		themeBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showColorPickerDialog();
			Toast.makeText(MainActivity.this, "自定义应用整体配色", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams themeParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		themeParams.setMargins(dp(4), 0, 0, 0);
		otherActionsRow.addView(themeBtn, themeParams);
		
		mainLayout.addView(otherActionsRow);
		
		addSectionTitle(mainLayout, "🧹 数据清理");
		
		LinearLayout clearRow = new LinearLayout(this);
		clearRow.setOrientation(LinearLayout.HORIZONTAL);
		clearRow.setWeightSum(3);
		clearRow.setGravity(Gravity.CENTER);
		
		Button clearCacheBtn = new Button(this);
		clearCacheBtn.setText("清除缓存");
		clearCacheBtn.setTextColor(TEXT_WHITE);
		clearCacheBtn.setBackground(createBg(btnBgColor, 12));
		clearCacheBtn.setAllCaps(false);
		clearCacheBtn.setOnClickListener(v -> {
			for (TabInfo tab : tabs) {
				if (tab.webView != null) {
					tab.webView.clearCache(true);
				}
			}
			Toast.makeText(MainActivity.this, "网页缓存已清除", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams clearCacheParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		clearCacheParams.setMargins(0, 0, dp(4), 0);
		clearRow.addView(clearCacheBtn, clearCacheParams);
		
		Button clearHistoryBtn = new Button(this);
		clearHistoryBtn.setText("清除历史");
		clearHistoryBtn.setTextColor(TEXT_WHITE);
		clearHistoryBtn.setBackground(createBg(btnBgColor, 12));
		clearHistoryBtn.setAllCaps(false);
		clearHistoryBtn.setOnClickListener(v -> {
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.clearHistory();
			}
			historyList.clear();
			saveHistory();
			Toast.makeText(MainActivity.this, "浏览历史已清除", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams clearHistoryParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		clearHistoryParams.setMargins(dp(4), 0, dp(4), 0);
		clearRow.addView(clearHistoryBtn, clearHistoryParams);
		
		Button clearFavBtn = new Button(this);
		clearFavBtn.setText("清除收藏");
		clearFavBtn.setTextColor(TEXT_WHITE);
		clearFavBtn.setBackground(createBg(btnBgColor, 12));
		clearFavBtn.setAllCaps(false);
		clearFavBtn.setOnClickListener(v -> {
			favoritesSet.clear();
			saveFavorites();
			Toast.makeText(MainActivity.this, "收藏内容已清除", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams clearFavParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		clearFavParams.setMargins(dp(4), 0, 0, 0);
		clearRow.addView(clearFavBtn, clearFavParams);
		
		mainLayout.addView(clearRow);
		
		// ---------- 底部操作按钮 ----------
		LinearLayout actionLayout = new LinearLayout(this);
		actionLayout.setOrientation(LinearLayout.HORIZONTAL);
		actionLayout.setGravity(Gravity.CENTER);
		actionLayout.setPadding(0, dp(20), 0, 0);
		actionLayout.setWeightSum(3);
		
		Button cancelDialogBtn = new Button(this);
		cancelDialogBtn.setText("取消");
		cancelDialogBtn.setTextColor(TEXT_WHITE);
		cancelDialogBtn.setBackground(createBg(accentColor, 16));
		cancelDialogBtn.setOnClickListener(v -> dialog.dismiss());
		LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(0, dp(44), 1);
		cancelParams.setMargins(0, 0, dp(4), 0);
		actionLayout.addView(cancelDialogBtn, cancelParams);
		
		Button confirmDialogBtn = new Button(this);
		confirmDialogBtn.setText("保存");
		confirmDialogBtn.setTextColor(TEXT_WHITE);
		confirmDialogBtn.setBackground(createBg(accentColor, 16));
		confirmDialogBtn.setOnClickListener(v -> {
			prefs.edit().putBoolean(PREF_NIGHT_MODE, nightSwitch.isChecked()).apply();
			boolean autoBlock = autoBlockSwitch.isChecked();
			boolean preventJump = preventJumpSwitch.isChecked();
			boolean gyroOff = gyroSwitch.isChecked();
			prefs.edit().putBoolean(PREF_AD_AUTO_BLOCK_ENABLED, autoBlock).apply();
			prefs.edit().putBoolean(PREF_AD_PREVENT_JUMP, preventJump).apply();
			prefs.edit().putBoolean(PREF_DISABLE_GYRO, gyroOff).apply();
			adAutoBlockEnabled = autoBlock;
			adPreventJumpEnabled = preventJump;
			if (disableGyro != gyroOff) {
				disableGyro = gyroOff;
				if (disableGyro) {
					disableGyroscope();
					} else {
					enableGyroscope();
				}
			}
			
			if (nightSwitch.isChecked() != prefs.getBoolean(PREF_NIGHT_MODE, false)) {
				if (nightSwitch.isChecked()) injectNightMode();
				else removeNightMode();
			}
			if (currentTab != null && currentTab.webView != null && !isLocalResource(currentTab.url)) {
				injectAdBlockRules();
				if (adAutoBlockEnabled) {
					injectAutoPopupBlockerJS();
				}
				if (adPreventJumpEnabled) {
					injectPreventAutomaticJumpJS();
				}
			}
			dialog.dismiss();
			Toast.makeText(MainActivity.this, "设置已保存", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams confirmParams = new LinearLayout.LayoutParams(0, dp(44), 1);
		confirmParams.setMargins(dp(4), 0, dp(4), 0);
		actionLayout.addView(confirmDialogBtn, confirmParams);
		
		Button resetBtn = new Button(this);
		resetBtn.setText("恢复默认");
		resetBtn.setTextColor(TEXT_WHITE);
		resetBtn.setBackground(createBg(accentColor, 16));
		resetBtn.setOnClickListener(v -> {
			resetAllSettings();
			dialog.dismiss();
			Toast.makeText(MainActivity.this, "所有设置已恢复默认，应用将重启", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams resetParams = new LinearLayout.LayoutParams(0, dp(44), 1);
		resetParams.setMargins(dp(4), 0, 0, 0);
		actionLayout.addView(resetBtn, resetParams);
		
		mainLayout.addView(actionLayout);
		
		scrollView.addView(mainLayout);
		dialog.setContentView(scrollView);
		
		Window dialogWindow = dialog.getWindow();
		if (dialogWindow != null) {
			dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			int screenWidth = getResources().getDisplayMetrics().widthPixels;
			int screenHeight = getResources().getDisplayMetrics().heightPixels;
			dialogWindow.setLayout((int) (screenWidth * 1), (int) (screenHeight * 0.9));
			dialogWindow.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	private void showVoicePickerDialog(TextView currentVoiceText) {
		if (ttsEngine == null) {
			Toast.makeText(this, "TTS引擎未初始化", Toast.LENGTH_SHORT).show();
			return;
		}
		final List<Voice> voices = new ArrayList<>(ttsEngine.getVoices());
		if (voices.isEmpty()) {
			Toast.makeText(this, "没有可用的语音", Toast.LENGTH_SHORT).show();
			return;
		}
		String[] voiceNames = new String[voices.size()];
		for (int i = 0; i < voices.size(); i++) {
			voiceNames[i] = voices.get(i).getName() + " (" + voices.get(i).getLocale().getDisplayName() + ")";
		}
		new android.app.AlertDialog.Builder(this)
		.setTitle("选择语音")
		.setItems(voiceNames, (d, which) -> {
			Voice selected = voices.get(which);
			ttsCurrentVoiceName = selected.getName();
			ttsEngine.setVoice(selected);
			currentVoiceText.setText(selected.getName());
			prefs.edit().putString("tts_voice_name", selected.getName()).apply();
			Toast.makeText(this, "语音已选择: " + selected.getName(), Toast.LENGTH_SHORT).show();
		})
		.setNegativeButton("取消", null)
		.show();
	}
	
	private void handleVoicePackImport(Uri uri) {
		// 导入语音包文件通常为apk，提示安装
		try {
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
			installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			startActivity(installIntent);
			Toast.makeText(this, "正在安装语音包...", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
			Toast.makeText(this, "无法安装语音包，请尝试手动安装", Toast.LENGTH_SHORT).show();
		}
	}
	
	// ==================== 自定义屏蔽模式（单选/多选增强，仅对当前页面生效） ====================
	private void startCustomBlockMode() {
		if (currentTab == null || currentTab.webView == null) return;
		isBlockSelectMode = true;
		isBlockMultiSelect = false;
		multiSelectedSelectors.clear();
		
		// 显示模式选择弹窗
		Dialog modeDialog = new Dialog(this);
		modeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LinearLayout modeLayout = new LinearLayout(this);
		modeLayout.setOrientation(LinearLayout.VERTICAL);
		modeLayout.setBackground(createBg(mainBgColor, 20));
		modeLayout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView modeTitle = new TextView(this);
		modeTitle.setText("自定义屏蔽模式");
		modeTitle.setTextColor(TEXT_WHITE);
		modeTitle.setTextSize(20);
		modeTitle.setTypeface(null, Typeface.BOLD);
		modeTitle.setGravity(Gravity.CENTER);
		modeLayout.addView(modeTitle);
		
		TextView modeTip = new TextView(this);
		modeTip.setText("请选择屏蔽方式");
		modeTip.setTextColor(TEXT_GRAY);
		modeTip.setTextSize(14);
		modeTip.setGravity(Gravity.CENTER);
		modeTip.setPadding(0, dp(16), 0, dp(20));
		modeLayout.addView(modeTip);
		
		Button singleBtn = new Button(this);
		singleBtn.setText("单选模式（点击即屏蔽）");
		singleBtn.setTextColor(TEXT_WHITE);
		singleBtn.setBackground(createBg(btnBgColor, 12));
		singleBtn.setAllCaps(false);
		singleBtn.setOnClickListener(v -> {
			modeDialog.dismiss();
			startSingleBlockMode();
			Toast.makeText(this, "点击页面中任意元素即可屏蔽", Toast.LENGTH_SHORT).show();
		});
		modeLayout.addView(singleBtn);
		
		Button multiBtn = new Button(this);
		multiBtn.setText("多选模式（批量选择后屏蔽）");
		multiBtn.setTextColor(TEXT_WHITE);
		multiBtn.setBackground(createBg(btnBgColor, 12));
		multiBtn.setAllCaps(false);
		multiBtn.setOnClickListener(v -> {
			modeDialog.dismiss();
			startMultiBlockMode();
			Toast.makeText(this, "可多次点击选择元素，顶部显示数量，点击屏蔽按钮执行", Toast.LENGTH_SHORT).show();
		});
		LinearLayout.LayoutParams multiParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		multiParams.topMargin = dp(12);
		multiBtn.setLayoutParams(multiParams);
		modeLayout.addView(multiBtn);
		
		Button cancelBtn = new Button(this);
		cancelBtn.setText("取消");
		cancelBtn.setTextColor(TEXT_WHITE);
		cancelBtn.setBackground(createBg(btnBgColor, 12));
		cancelBtn.setOnClickListener(v -> {
			modeDialog.dismiss();
			isBlockSelectMode = false;
		});
		LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		cancelParams.topMargin = dp(12);
		cancelBtn.setLayoutParams(cancelParams);
		modeLayout.addView(cancelBtn);
		
		modeDialog.setContentView(modeLayout);
		Window w = modeDialog.getWindow();
		if (w != null) {
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setLayout((int)(getResources().getDisplayMetrics().widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
			w.setGravity(Gravity.CENTER);
		}
		modeDialog.show();
	}
	
	private void startSingleBlockMode() {
		isBlockMultiSelect = false;
		multiSelectedSelectors.clear();
		removeBlockModeOverlay();
		injectBlockSelectionJS(false);
	}
	
	private void startMultiBlockMode() {
		isBlockMultiSelect = true;
		multiSelectedSelectors.clear();
		showBlockModeOverlay();
		injectBlockSelectionJS(true);
	}
	
	private void showBlockModeOverlay() {
		if (blockModeOverlay != null) return;
		
		FrameLayout root = (FrameLayout) mAppContentLayout.getParent();
		blockModeOverlay = new LinearLayout(this);
		LinearLayout overlayLayout = (LinearLayout) blockModeOverlay;
		overlayLayout.setOrientation(LinearLayout.HORIZONTAL);
		overlayLayout.setGravity(Gravity.CENTER);
		overlayLayout.setBackground(createBg(accentColor, 8));
		overlayLayout.setPadding(dp(12), dp(8), dp(12), dp(8));
		
		blockCountText = new TextView(this);
		blockCountText.setText("已选 0 个元素");
		blockCountText.setTextColor(TEXT_WHITE);
		blockCountText.setTextSize(14);
		blockCountText.setTypeface(null, Typeface.BOLD);
		overlayLayout.addView(blockCountText);
		
		blockConfirmBtn = new Button(this);
		blockConfirmBtn.setText("屏蔽 (0)");
		blockConfirmBtn.setTextColor(TEXT_WHITE);
		blockConfirmBtn.setBackground(createBg(0xFFCC0000, 20));
		blockConfirmBtn.setAllCaps(false);
		blockConfirmBtn.setPadding(dp(16), dp(4), dp(16), dp(4));
		blockConfirmBtn.setOnClickListener(v -> {
			confirmMultiBlock();
		});
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		btnParams.setMargins(dp(12), 0, 0, 0);
		blockConfirmBtn.setLayoutParams(btnParams);
		overlayLayout.addView(blockConfirmBtn);
		
		FrameLayout.LayoutParams overlayParams = new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		overlayParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		overlayParams.topMargin = dp(8);
		root.addView(blockModeOverlay, overlayParams);
	}
	
	private void removeBlockModeOverlay() {
		if (blockModeOverlay != null) {
			ViewGroup parent = (ViewGroup) blockModeOverlay.getParent();
			if (parent != null) parent.removeView(blockModeOverlay);
			blockModeOverlay = null;
			blockCountText = null;
			blockConfirmBtn = null;
		}
	}
	
	private void updateMultiSelectCount(int count) {
		if (blockCountText != null) {
			blockCountText.setText("已选 " + count + " 个元素");
		}
		if (blockConfirmBtn != null) {
			blockConfirmBtn.setText("屏蔽 (" + count + ")");
		}
	}
	
	private void handleBlockElementSelected(String selector) {
		if (!isBlockSelectMode) return;
		
		if (!isBlockMultiSelect) {
			// 单选模式，直接在当前页面隐藏
			hideElementBySelector(selector);
			exitBlockSelectMode();
			Toast.makeText(this, "已屏蔽元素：" + selector, Toast.LENGTH_SHORT).show();
			} else {
			// 多选模式，添加到列表
			if (!multiSelectedSelectors.contains(selector)) {
				multiSelectedSelectors.add(selector);
				updateMultiSelectCount(multiSelectedSelectors.size());
				Toast.makeText(this, "已选中，当前共 " + multiSelectedSelectors.size() + " 个", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void confirmMultiBlock() {
		if (multiSelectedSelectors.isEmpty()) {
			Toast.makeText(this, "未选择任何元素", Toast.LENGTH_SHORT).show();
			return;
		}
		for (String selector : multiSelectedSelectors) {
			hideElementBySelector(selector);
		}
		Toast.makeText(this, "已屏蔽 " + multiSelectedSelectors.size() + " 个元素", Toast.LENGTH_SHORT).show();
		exitBlockSelectMode();
	}
	
	private void hideElementBySelector(String selector) {
		if (currentTab == null || currentTab.webView == null) return;
		String script = "(function(){" +
		"var style = document.createElement('style');" +
		"style.innerHTML = '" + selector.replace("'", "\\'") + "{display:none !important;}';" +
		"document.head.appendChild(style);" +
		"})();";
		currentTab.webView.evaluateJavascript(script, null);
	}
	
	private void exitBlockSelectMode() {
		isBlockSelectMode = false;
		isBlockMultiSelect = false;
		multiSelectedSelectors.clear();
		removeBlockModeOverlay();
		if (currentTab != null && currentTab.webView != null) {
			currentTab.webView.evaluateJavascript("javascript:(function(){" +
			"if(window._blockSelectActive) {" +
			"   window._blockSelectActive = false;" +
			"   document.body.style.cursor = 'default';" +
			"   var all = document.querySelectorAll('*');" +
			"   for(var i=0; i<all.length; i++) all[i].style.outline = '';" +
			"   document.removeEventListener('click', window._blockClickListener, true);" +
			"}})();", null);
		}
	}
	
	private void injectBlockSelectionJS(boolean multiSelect) {
		if (currentTab == null || currentTab.webView == null) return;
		String js = "(function(){" +
		"if(window._blockSelectActive) return;" +
		"window._blockSelectActive = true;" +
		"var all = document.querySelectorAll('*');" +
		"for(var i=0; i<all.length; i++) all[i].style.outline = '2px dashed red';" +
		"document.body.style.cursor = 'crosshair';" +
		"window._blockClickListener = function(e) {" +
		"   e.preventDefault();" +
		"   e.stopPropagation();" +
		"   var el = e.target;" +
		"   var selector = getUniqueSelector(el);" +
		"   Android.onElementSelected(selector);" +
		"   if (!" + multiSelect + ") {" +
		"       window._blockSelectActive = false;" +
		"       document.body.style.cursor = 'default';" +
		"       for(var i=0; i<all.length; i++) all[i].style.outline = '';" +
		"       document.removeEventListener('click', window._blockClickListener, true);" +
		"   }" +
		"};" +
		"document.addEventListener('click', window._blockClickListener, true);" +
		"})();" +
		"function getUniqueSelector(el) {" +
		"   if(el.id) return '#' + el.id;" +
		"   var path = [];" +
		"   while(el && el.nodeType === 1) {" +
		"       var selector = el.tagName.toLowerCase();" +
		"       if(el.className && typeof el.className === 'string') {" +
		"           var classes = el.className.trim().split(/\\s+/);" +
		"           selector += '.' + classes.join('.');" +
		"       }" +
		"       var parent = el.parentNode;" +
		"       if(parent) {" +
		"           var siblings = Array.from(parent.children).filter(function(c) { return c.tagName === el.tagName; });" +
		"           if(siblings.length > 1) {" +
		"               var index = Array.prototype.indexOf.call(siblings, el) + 1;" +
		"               selector += ':nth-of-type(' + index + ')';" +
		"           }" +
		"       }" +
		"       path.unshift(selector);" +
		"       el = el.parentNode;" +
		"       if(el === document.body) break;" +
		"   }" +
		"   return path.join(' > ');" +
		"}";
		currentTab.webView.evaluateJavascript(js, null);
	}
	
	// ==================== 广告屏蔽加强（仅对非本地页面注入基础规则） ====================
	private void injectAdBlockRules() {
		if (currentTab == null || currentTab.webView == null) return;
		if (isLocalResource(currentTab.url)) return;
		
		// 完善的广告选择器 - 覆盖常见广告类型
		String defaultSelectors =
		"[id*='ad' i], [class*='ad' i], [id*='banner' i], [class*='banner' i], " +
		"[class*='adsbygoogle'], [class*='advert'], [class*='sponsor'], " +
		"iframe[src*='doubleclick'], iframe[src*='adservice'], " +
		"iframe[src*='googlesyndication'], iframe[src*='googleadservices'], " +
		"[aria-label*='广告'], [data-ad-status], [class*='modal'], [class*='popup'], " +
		"[id*='modal'], [id*='popup'], [class*='overlay'], div[id*='layer'], " +
		"[class*='floating'], [class*='float'], [id*='float'], " +
		"[class*='sticky-ad'], [id*='sticky-ad'], " +
		"[class*='ad-container'], [id*='ad-container'], " +
		"[class*='ad-wrapper'], [id*='ad-wrapper'], " +
		"ins.adsbygoogle, ins[data-ad-client], " +
		"div[class*='ad-'], div[id*='ad-'], " +
		"div[style*='position: fixed'][style*='left: 0'], " +
		"div[style*='position: fixed'][style*='right: 0'], " +
		"div[style*='position: fixed'][style*='bottom: 0'], " +
		"div[style*='position: fixed'][style*='width: 100%'], " +
		"[class*='bottom-ad'], [id*='bottom-ad'], " +
		"[class*='top-ad'], [id*='top-ad'], " +
		"[class*='side-ad'], [id*='side-ad'], " +
		"[class*='ad-banner'], [id*='ad-banner'], " +
		"[class*='ad-unit'], [id*='ad-unit'], " +
		"[class*='ad-slot'], [id*='ad-slot'], " +
		"[class*='ad-box'], [id*='ad-box'], " +
		"[class*='ad-item'], [id*='ad-item'], " +
		"[class*='ad-card'], [id*='ad-card'], " +
		"[class*='ad-cell'], [id*='ad-cell'], " +
		"[class*='ad-grid'], [id*='ad-grid'], " +
		"[class*='ad-row'], [id*='ad-row'], " +
		"[class*='ad-col'], [id*='ad-col'], " +
		"[class*='ad-list'], [id*='ad-list'], " +
		"[class*='ad-post'], [id*='ad-post'], " +
		"[class*='ad-article'], [id*='ad-article'], " +
		"[class*='ad-video'], [id*='ad-video'], " +
		"[class*='ad-image'], [id*='ad-image'], " +
		"[class*='ad-photo'], [id*='ad-photo'], " +
		"[class*='ad-gallery'], [id*='ad-gallery'], " +
		"[class*='ad-slider'], [id*='ad-slider'], " +
		"[class*='ad-carousel'], [id*='ad-carousel'], " +
		"[class*='ad-roll'], [id*='ad-roll'], " +
		"[class*='ad-bar'], [id*='ad-bar'], " +
		"[class*='ad-strip'], [id*='ad-strip'], " +
		"[class*='ad-block'], [id*='ad-block'], " +
		"[class*='ad-section'], [id*='ad-section'], " +
		"[class*='ad-wrap'], [id*='ad-wrap'], " +
		"[class*='ad-in'], [id*='ad-in'], " +
		"[class*='ad-out'], [id*='ad-out'], " +
		"[class*='ad-feed'], [id*='ad-feed'], " +
		"[class*='ad-stream'], [id*='ad-stream'], " +
		"[class*='ad-content'], [id*='ad-content'], " +
		"[class*='ad-area'], [id*='ad-area'], " +
		"[class*='ad-zone'], [id*='ad-zone'], " +
		"[class*='ad-spot'], [id*='ad-spot'], " +
		"[class*='ad-place'], [id*='ad-place'], " +
		"[class*='ad-position'], [id*='ad-position'], " +
		"[class*='ad-location'], [id*='ad-location'], " +
		"[class*='ad-header'], [id*='ad-header'], " +
		"[class*='ad-footer'], [id*='ad-footer'], " +
		"[class*='ad-sidebar'], [id*='ad-sidebar'], " +
		"[class*='ad-left'], [id*='ad-left'], " +
		"[class*='ad-right'], [id*='ad-right'], " +
		"[class*='ad-center'], [id*='ad-center'], " +
		"[class*='ad-middle'], [id*='ad-middle'], " +
		"[class*='ad-top'], [id*='ad-top'], " +
		"[class*='ad-bottom'], [id*='ad-bottom'], " +
		"[class*='ad-inner'], [id*='ad-inner'], " +
		"[class*='ad-outer'], [id*='ad-outer'], " +
		"[class*='ad-above'], [id*='ad-above'], " +
		"[class*='ad-below'], [id*='ad-below'], " +
		"[class*='ad-between'], [id*='ad-between'], " +
		"[class*='ad-inside'], [id*='ad-inside'], " +
		"[class*='ad-outside'], [id*='ad-outside'], " +
		"[class*='ad-front'], [id*='ad-front'], " +
		"[class*='ad-back'], [id*='ad-back'], " +
		"[class*='ad-side'], [id*='ad-side'], " +
		"[class*='ad-topbar'], [id*='ad-topbar'], " +
		"[class*='ad-bottombar'], [id*='ad-bottombar'], " +
		"[class*='ad-leaderboard'], [id*='ad-leaderboard'], " +
		"[class*='ad-skyscraper'], [id*='ad-skyscraper'], " +
		"[class*='ad-rectangle'], [id*='ad-rectangle'], " +
		"[class*='ad-square'], [id*='ad-square'], " +
		"[class*='ad-button'], [id*='ad-button'], " +
		"[class*='ad-link'], [id*='ad-link'], " +
		"[class*='ad-text'], [id*='ad-text'], " +
		"[class*='ad-native'], [id*='ad-native'], " +
		"[class*='ad-sponsored'], [id*='ad-sponsored'], " +
		"[class*='ad-recommended'], [id*='ad-recommended'], " +
		"[class*='ad-suggested'], [id*='ad-suggested'], " +
		"[class*='ad-popular'], [id*='ad-popular'], " +
		"[class*='ad-trending'], [id*='ad-trending'], " +
		"[class*='ad-hot'], [id*='ad-hot'], " +
		"[class*='ad-new'], [id*='ad-new'], " +
		"[class*='ad-featured'], [id*='ad-featured'], " +
		"[class*='ad-premium'], [id*='ad-premium'], " +
		"[class*='ad-exclusive'], [id*='ad-exclusive'], " +
		"[class*='ad-latest'], [id*='ad-latest'], " +
		"[class*='ad-updated'], [id*='ad-updated'], " +
		"[class*='ad-newest'], [id*='ad-newest'], " +
		"[class*='ad-beginning'], [id*='ad-beginning'], " +
		"[class*='ad-start'], [id*='ad-start'], " +
		"[class*='ad-end'], [id*='ad-end'], " +
		"[class*='ad-finish'], [id*='ad-finish'], " +
		"[class*='ad-final'], [id*='ad-final'], " +
		"[class*='ad-last'], [id*='ad-last'], " +
		"[class*='ad-first'], [id*='ad-first'], " +
		"[class*='ad-second'], [id*='ad-second'], " +
		"[class*='ad-third'], [id*='ad-third'], " +
		"[class*='ad-fourth'], [id*='ad-fourth'], " +
		"[class*='ad-fifth'], [id*='ad-fifth'], " +
		"[class*='ad-primary'], [id*='ad-primary'], " +
		"[class*='ad-secondary'], [id*='ad-secondary'], " +
		"[class*='ad-tertiary'], [id*='ad-tertiary'], " +
		"[class*='ad-main'], [id*='ad-main'], " +
		"[class*='ad-sub'], [id*='ad-sub'], " +
		"[class*='ad-submit'], [id*='ad-submit'], " +
		"[class*='ad-search'], [id*='ad-search'], " +
		"[class*='ad-result'], [id*='ad-result'], " +
		"[class*='ad-result-item'], [id*='ad-result-item'], " +
		"[class*='ad-product'], [id*='ad-product'], " +
		"[class*='ad-item-link'], [id*='ad-item-link'], " +
		"[class*='ad-item-title'], [id*='ad-item-title'], " +
		"[class*='ad-item-desc'], [id*='ad-item-desc'], " +
		"[class*='ad-item-image'], [id*='ad-item-image'], " +
		"[class*='ad-item-price'], [id*='ad-item-price'], " +
		"[class*='ad-item-button'], [id*='ad-item-button'], " +
		"[class*='ad-item-buy'], [id*='ad-item-buy'], " +
		"[class*='ad-item-shop'], [id*='ad-item-shop'], " +
		"[class*='ad-item-order'], [id*='ad-item-order'], " +
		"[class*='ad-item-purchase'], [id*='ad-item-purchase'], " +
		"[class*='ad-item-get'], [id*='ad-item-get'], " +
		"[class*='ad-item-try'], [id*='ad-item-try'], " +
		"[class*='ad-item-test'], [id*='ad-item-test'], " +
		"[class*='ad-item-signup'], [id*='ad-item-signup'], " +
		"[class*='ad-item-register'], [id*='ad-item-register'], " +
		"[class*='ad-item-subscribe'], [id*='ad-item-subscribe'], " +
		"[class*='ad-item-learn'], [id*='ad-item-learn'], " +
		"[class*='ad-item-know'], [id*='ad-item-know'], " +
		"[class*='ad-item-read'], [id*='ad-item-read'], " +
		"[class*='ad-item-watch'], [id*='ad-item-watch'], " +
		"[class*='ad-item-listen'], [id*='ad-item-listen'], " +
		"[class*='ad-item-play'], [id*='ad-item-play'], " +
		"[class*='ad-item-click'], [id*='ad-item-click'], " +
		"[class*='ad-item-visit'], [id*='ad-item-visit'], " +
		"[class*='ad-item-goto'], [id*='ad-item-goto'], " +
		"[class*='ad-item-go'], [id*='ad-item-go'], " +
		"[class*='ad-item-come'], [id*='ad-item-come'], " +
		"[class*='ad-item-enter'], [id*='ad-item-enter'], " +
		"[class*='ad-item-access'], [id*='ad-item-access'], " +
		"[class*='ad-item-open'], [id*='ad-item-open'], " +
		"[class*='ad-item-close'], [id*='ad-item-close'], " +
		"[class*='ad-item-exit'], [id*='ad-item-exit'], " +
		"[class*='ad-item-leave'], [id*='ad-item-leave'], " +
		"[class*='ad-item-return'], [id*='ad-item-return'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-forward'], [id*='ad-item-forward'], " +
		"[class*='ad-item-next'], [id*='ad-item-next'], " +
		"[class*='ad-item-prev'], [id*='ad-item-prev'], " +
		"[class*='ad-item-previous'], [id*='ad-item-previous'], " +
		"[class*='ad-item-up'], [id*='ad-item-up'], " +
		"[class*='ad-item-down'], [id*='ad-item-down'], " +
		"[class*='ad-item-left'], [id*='ad-item-left'], " +
		"[class*='ad-item-right'], [id*='ad-item-right'], " +
		"[class*='ad-item-top'], [id*='ad-item-top'], " +
		"[class*='ad-item-bottom'], [id*='ad-item-bottom'], " +
		"[class*='ad-item-center'], [id*='ad-item-center'], " +
		"[class*='ad-item-middle'], [id*='ad-item-middle'], " +
		"[class*='ad-item-inside'], [id*='ad-item-inside'], " +
		"[class*='ad-item-outside'], [id*='ad-item-outside'], " +
		"[class*='ad-item-front'], [id*='ad-item-front'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-side'], [id*='ad-item-side'], " +
		"[class*='ad-item-corner'], [id*='ad-item-corner'], " +
		"[class*='ad-item-edge'], [id*='ad-item-edge'], " +
		"[class*='ad-item-border'], [id*='ad-item-border'], " +
		"[class*='ad-item-outline'], [id*='ad-item-outline'], " +
		"[class*='ad-item-frame'], [id*='ad-item-frame'], " +
		"[class*='ad-item-box'], [id*='ad-item-box'], " +
		"[class*='ad-item-container'], [id*='ad-item-container'], " +
		"[class*='ad-item-wrapper'], [id*='ad-item-wrapper'], " +
		"[class*='ad-item-wrap'], [id*='ad-item-wrap'], " +
		"[class*='ad-item-inner'], [id*='ad-item-inner'], " +
		"[class*='ad-item-outer'], [id*='ad-item-outer'], " +
		"[class*='ad-item-above'], [id*='ad-item-above'], " +
		"[class*='ad-item-below'], [id*='ad-item-below'], " +
		"[class*='ad-item-between'], [id*='ad-item-between'], " +
		"[class*='ad-item-over'], [id*='ad-item-over'], " +
		"[class*='ad-item-under'], [id*='ad-item-under'], " +
		"[class*='ad-item-above-below'], [id*='ad-item-above-below'], " +
		"[class*='ad-item-over-under'], [id*='ad-item-over-under'], " +
		"[class*='ad-item-top-bottom'], [id*='ad-item-top-bottom'], " +
		"[class*='ad-item-left-right'], [id*='ad-item-left-right'], " +
		"[class*='ad-item-hot'], [id*='ad-item-hot'], " +
		"[class*='ad-item-popular'], [id*='ad-item-popular'], " +
		"[class*='ad-item-trending'], [id*='ad-item-trending'], " +
		"[class*='ad-item-new'], [id*='ad-item-new'], " +
		"[class*='ad-item-featured'], [id*='ad-item-featured'], " +
		"[class*='ad-item-premium'], [id*='ad-item-premium'], " +
		"[class*='ad-item-exclusive'], [id*='ad-item-exclusive'], " +
		"[class*='ad-item-latest'], [id*='ad-item-latest'], " +
		"[class*='ad-item-updated'], [id*='ad-item-updated'], " +
		"[class*='ad-item-newest'], [id*='ad-item-newest'], " +
		"[class*='ad-item-beginning'], [id*='ad-item-beginning'], " +
		"[class*='ad-item-start'], [id*='ad-item-start'], " +
		"[class*='ad-item-end'], [id*='ad-item-end'], " +
		"[class*='ad-item-finish'], [id*='ad-item-finish'], " +
		"[class*='ad-item-final'], [id*='ad-item-final'], " +
		"[class*='ad-item-last'], [id*='ad-item-last'], " +
		"[class*='ad-item-first'], [id*='ad-item-first'], " +
		"[class*='ad-item-second'], [id*='ad-item-second'], " +
		"[class*='ad-item-third'], [id*='ad-item-third'], " +
		"[class*='ad-item-fourth'], [id*='ad-item-fourth'], " +
		"[class*='ad-item-fifth'], [id*='ad-item-fifth'], " +
		"[class*='ad-item-primary'], [id*='ad-item-primary'], " +
		"[class*='ad-item-secondary'], [id*='ad-item-secondary'], " +
		"[class*='ad-item-tertiary'], [id*='ad-item-tertiary'], " +
		"[class*='ad-item-main'], [id*='ad-item-main'], " +
		"[class*='ad-item-sub'], [id*='ad-item-sub'], " +
		"[class*='ad-item-submit'], [id*='ad-item-submit'], " +
		"[class*='ad-item-search'], [id*='ad-item-search'], " +
		"[class*='ad-item-result'], [id*='ad-item-result'], " +
		"[class*='ad-item-result-item'], [id*='ad-item-result-item'], " +
		"[class*='ad-item-product'], [id*='ad-item-product'], " +
		"[class*='ad-item-link'], [id*='ad-item-link'], " +
		"[class*='ad-item-title'], [id*='ad-item-title'], " +
		"[class*='ad-item-desc'], [id*='ad-item-desc'], " +
		"[class*='ad-item-image'], [id*='ad-item-image'], " +
		"[class*='ad-item-price'], [id*='ad-item-price'], " +
		"[class*='ad-item-button'], [id*='ad-item-button'], " +
		"[class*='ad-item-buy'], [id*='ad-item-buy'], " +
		"[class*='ad-item-shop'], [id*='ad-item-shop'], " +
		"[class*='ad-item-order'], [id*='ad-item-order'], " +
		"[class*='ad-item-purchase'], [id*='ad-item-purchase'], " +
		"[class*='ad-item-get'], [id*='ad-item-get'], " +
		"[class*='ad-item-try'], [id*='ad-item-try'], " +
		"[class*='ad-item-test'], [id*='ad-item-test'], " +
		"[class*='ad-item-signup'], [id*='ad-item-signup'], " +
		"[class*='ad-item-register'], [id*='ad-item-register'], " +
		"[class*='ad-item-subscribe'], [id*='ad-item-subscribe'], " +
		"[class*='ad-item-learn'], [id*='ad-item-learn'], " +
		"[class*='ad-item-know'], [id*='ad-item-know'], " +
		"[class*='ad-item-read'], [id*='ad-item-read'], " +
		"[class*='ad-item-watch'], [id*='ad-item-watch'], " +
		"[class*='ad-item-listen'], [id*='ad-item-listen'], " +
		"[class*='ad-item-play'], [id*='ad-item-play'], " +
		"[class*='ad-item-click'], [id*='ad-item-click'], " +
		"[class*='ad-item-visit'], [id*='ad-item-visit'], " +
		"[class*='ad-item-goto'], [id*='ad-item-goto'], " +
		"[class*='ad-item-go'], [id*='ad-item-go'], " +
		"[class*='ad-item-come'], [id*='ad-item-come'], " +
		"[class*='ad-item-enter'], [id*='ad-item-enter'], " +
		"[class*='ad-item-access'], [id*='ad-item-access'], " +
		"[class*='ad-item-open'], [id*='ad-item-open'], " +
		"[class*='ad-item-close'], [id*='ad-item-close'], " +
		"[class*='ad-item-exit'], [id*='ad-item-exit'], " +
		"[class*='ad-item-leave'], [id*='ad-item-leave'], " +
		"[class*='ad-item-return'], [id*='ad-item-return'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-forward'], [id*='ad-item-forward'], " +
		"[class*='ad-item-next'], [id*='ad-item-next'], " +
		"[class*='ad-item-prev'], [id*='ad-item-prev'], " +
		"[class*='ad-item-previous'], [id*='ad-item-previous'], " +
		"[class*='ad-item-up'], [id*='ad-item-up'], " +
		"[class*='ad-item-down'], [id*='ad-item-down'], " +
		"[class*='ad-item-left'], [id*='ad-item-left'], " +
		"[class*='ad-item-right'], [id*='ad-item-right'], " +
		"[class*='ad-item-top'], [id*='ad-item-top'], " +
		"[class*='ad-item-bottom'], [id*='ad-item-bottom'], " +
		"[class*='ad-item-center'], [id*='ad-item-center'], " +
		"[class*='ad-item-middle'], [id*='ad-item-middle'], " +
		"[class*='ad-item-inside'], [id*='ad-item-inside'], " +
		"[class*='ad-item-outside'], [id*='ad-item-outside'], " +
		"[class*='ad-item-front'], [id*='ad-item-front'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-side'], [id*='ad-item-side'], " +
		"[class*='ad-item-corner'], [id*='ad-item-corner'], " +
		"[class*='ad-item-edge'], [id*='ad-item-edge'], " +
		"[class*='ad-item-border'], [id*='ad-item-border'], " +
		"[class*='ad-item-outline'], [id*='ad-item-outline'], " +
		"[class*='ad-item-frame'], [id*='ad-item-frame'], " +
		"[class*='ad-item-box'], [id*='ad-item-box'], " +
		"[class*='ad-item-container'], [id*='ad-item-container'], " +
		"[class*='ad-item-wrapper'], [id*='ad-item-wrapper'], " +
		"[class*='ad-item-wrap'], [id*='ad-item-wrap'], " +
		"[class*='ad-item-inner'], [id*='ad-item-inner'], " +
		"[class*='ad-item-outer'], [id*='ad-item-outer'], " +
		"[class*='ad-item-above'], [id*='ad-item-above'], " +
		"[class*='ad-item-below'], [id*='ad-item-below'], " +
		"[class*='ad-item-between'], [id*='ad-item-between'], " +
		"[class*='ad-item-over'], [id*='ad-item-over'], " +
		"[class*='ad-item-under'], [id*='ad-item-under'], " +
		"[class*='ad-item-above-below'], [id*='ad-item-above-below'], " +
		"[class*='ad-item-over-under'], [id*='ad-item-over-under'], " +
		"[class*='ad-item-top-bottom'], [id*='ad-item-top-bottom'], " +
		"[class*='ad-item-left-right'], [id*='ad-item-left-right'], " +
		"[class*='ad-item-hot'], [id*='ad-item-hot'], " +
		"[class*='ad-item-popular'], [id*='ad-item-popular'], " +
		"[class*='ad-item-trending'], [id*='ad-item-trending'], " +
		"[class*='ad-item-new'], [id*='ad-item-new'], " +
		"[class*='ad-item-featured'], [id*='ad-item-featured'], " +
		"[class*='ad-item-premium'], [id*='ad-item-premium'], " +
		"[class*='ad-item-exclusive'], [id*='ad-item-exclusive'], " +
		"[class*='ad-item-latest'], [id*='ad-item-latest'], " +
		"[class*='ad-item-updated'], [id*='ad-item-updated'], " +
		"[class*='ad-item-newest'], [id*='ad-item-newest'], " +
		"[class*='ad-item-beginning'], [id*='ad-item-beginning'], " +
		"[class*='ad-item-start'], [id*='ad-item-start'], " +
		"[class*='ad-item-end'], [id*='ad-item-end'], " +
		"[class*='ad-item-finish'], [id*='ad-item-finish'], " +
		"[class*='ad-item-final'], [id*='ad-item-final'], " +
		"[class*='ad-item-last'], [id*='ad-item-last'], " +
		"[class*='ad-item-first'], [id*='ad-item-first'], " +
		"[class*='ad-item-second'], [id*='ad-item-second'], " +
		"[class*='ad-item-third'], [id*='ad-item-third'], " +
		"[class*='ad-item-fourth'], [id*='ad-item-fourth'], " +
		"[class*='ad-item-fifth'], [id*='ad-item-fifth'], " +
		"[class*='ad-item-primary'], [id*='ad-item-primary'], " +
		"[class*='ad-item-secondary'], [id*='ad-item-secondary'], " +
		"[class*='ad-item-tertiary'], [id*='ad-item-tertiary'], " +
		"[class*='ad-item-main'], [id*='ad-item-main'], " +
		"[class*='ad-item-sub'], [id*='ad-item-sub'], " +
		"[class*='ad-item-submit'], [id*='ad-item-submit'], " +
		"[class*='ad-item-search'], [id*='ad-item-search'], " +
		"[class*='ad-item-result'], [id*='ad-item-result'], " +
		"[class*='ad-item-result-item'], [id*='ad-item-result-item'], " +
		"[class*='ad-item-product'], [id*='ad-item-product'], " +
		"[class*='ad-item-link'], [id*='ad-item-link'], " +
		"[class*='ad-item-title'], [id*='ad-item-title'], " +
		"[class*='ad-item-desc'], [id*='ad-item-desc'], " +
		"[class*='ad-item-image'], [id*='ad-item-image'], " +
		"[class*='ad-item-price'], [id*='ad-item-price'], " +
		"[class*='ad-item-button'], [id*='ad-item-button'], " +
		"[class*='ad-item-buy'], [id*='ad-item-buy'], " +
		"[class*='ad-item-shop'], [id*='ad-item-shop'], " +
		"[class*='ad-item-order'], [id*='ad-item-order'], " +
		"[class*='ad-item-purchase'], [id*='ad-item-purchase'], " +
		"[class*='ad-item-get'], [id*='ad-item-get'], " +
		"[class*='ad-item-try'], [id*='ad-item-try'], " +
		"[class*='ad-item-test'], [id*='ad-item-test'], " +
		"[class*='ad-item-signup'], [id*='ad-item-signup'], " +
		"[class*='ad-item-register'], [id*='ad-item-register'], " +
		"[class*='ad-item-subscribe'], [id*='ad-item-subscribe'], " +
		"[class*='ad-item-learn'], [id*='ad-item-learn'], " +
		"[class*='ad-item-know'], [id*='ad-item-know'], " +
		"[class*='ad-item-read'], [id*='ad-item-read'], " +
		"[class*='ad-item-watch'], [id*='ad-item-watch'], " +
		"[class*='ad-item-listen'], [id*='ad-item-listen'], " +
		"[class*='ad-item-play'], [id*='ad-item-play'], " +
		"[class*='ad-item-click'], [id*='ad-item-click'], " +
		"[class*='ad-item-visit'], [id*='ad-item-visit'], " +
		"[class*='ad-item-goto'], [id*='ad-item-goto'], " +
		"[class*='ad-item-go'], [id*='ad-item-go'], " +
		"[class*='ad-item-come'], [id*='ad-item-come'], " +
		"[class*='ad-item-enter'], [id*='ad-item-enter'], " +
		"[class*='ad-item-access'], [id*='ad-item-access'], " +
		"[class*='ad-item-open'], [id*='ad-item-open'], " +
		"[class*='ad-item-close'], [id*='ad-item-close'], " +
		"[class*='ad-item-exit'], [id*='ad-item-exit'], " +
		"[class*='ad-item-leave'], [id*='ad-item-leave'], " +
		"[class*='ad-item-return'], [id*='ad-item-return'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-forward'], [id*='ad-item-forward'], " +
		"[class*='ad-item-next'], [id*='ad-item-next'], " +
		"[class*='ad-item-prev'], [id*='ad-item-prev'], " +
		"[class*='ad-item-previous'], [id*='ad-item-previous'], " +
		"[class*='ad-item-up'], [id*='ad-item-up'], " +
		"[class*='ad-item-down'], [id*='ad-item-down'], " +
		"[class*='ad-item-left'], [id*='ad-item-left'], " +
		"[class*='ad-item-right'], [id*='ad-item-right'], " +
		"[class*='ad-item-top'], [id*='ad-item-top'], " +
		"[class*='ad-item-bottom'], [id*='ad-item-bottom'], " +
		"[class*='ad-item-center'], [id*='ad-item-center'], " +
		"[class*='ad-item-middle'], [id*='ad-item-middle'], " +
		"[class*='ad-item-inside'], [id*='ad-item-inside'], " +
		"[class*='ad-item-outside'], [id*='ad-item-outside'], " +
		"[class*='ad-item-front'], [id*='ad-item-front'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-side'], [id*='ad-item-side'], " +
		"[class*='ad-item-corner'], [id*='ad-item-corner'], " +
		"[class*='ad-item-edge'], [id*='ad-item-edge'], " +
		"[class*='ad-item-border'], [id*='ad-item-border'], " +
		"[class*='ad-item-outline'], [id*='ad-item-outline'], " +
		"[class*='ad-item-frame'], [id*='ad-item-frame'], " +
		"[class*='ad-item-box'], [id*='ad-item-box'], " +
		"[class*='ad-item-container'], [id*='ad-item-container'], " +
		"[class*='ad-item-wrapper'], [id*='ad-item-wrapper'], " +
		"[class*='ad-item-wrap'], [id*='ad-item-wrap'], " +
		"[class*='ad-item-inner'], [id*='ad-item-inner'], " +
		"[class*='ad-item-outer'], [id*='ad-item-outer'], " +
		"[class*='ad-item-above'], [id*='ad-item-above'], " +
		"[class*='ad-item-below'], [id*='ad-item-below'], " +
		"[class*='ad-item-between'], [id*='ad-item-between'], " +
		"[class*='ad-item-over'], [id*='ad-item-over'], " +
		"[class*='ad-item-under'], [id*='ad-item-under'], " +
		"[class*='ad-item-above-below'], [id*='ad-item-above-below'], " +
		"[class*='ad-item-over-under'], [id*='ad-item-over-under'], " +
		"[class*='ad-item-top-bottom'], [id*='ad-item-top-bottom'], " +
		"[class*='ad-item-left-right'], [id*='ad-item-left-right'], " +
		"[class*='ad-item-hot'], [id*='ad-item-hot'], " +
		"[class*='ad-item-popular'], [id*='ad-item-popular'], " +
		"[class*='ad-item-trending'], [id*='ad-item-trending'], " +
		"[class*='ad-item-new'], [id*='ad-item-new'], " +
		"[class*='ad-item-featured'], [id*='ad-item-featured'], " +
		"[class*='ad-item-premium'], [id*='ad-item-premium'], " +
		"[class*='ad-item-exclusive'], [id*='ad-item-exclusive'], " +
		"[class*='ad-item-latest'], [id*='ad-item-latest'], " +
		"[class*='ad-item-updated'], [id*='ad-item-updated'], " +
		"[class*='ad-item-newest'], [id*='ad-item-newest'], " +
		"[class*='ad-item-beginning'], [id*='ad-item-beginning'], " +
		"[class*='ad-item-start'], [id*='ad-item-start'], " +
		"[class*='ad-item-end'], [id*='ad-item-end'], " +
		"[class*='ad-item-finish'], [id*='ad-item-finish'], " +
		"[class*='ad-item-final'], [id*='ad-item-final'], " +
		"[class*='ad-item-last'], [id*='ad-item-last'], " +
		"[class*='ad-item-first'], [id*='ad-item-first'], " +
		"[class*='ad-item-second'], [id*='ad-item-second'], " +
		"[class*='ad-item-third'], [id*='ad-item-third'], " +
		"[class*='ad-item-fourth'], [id*='ad-item-fourth'], " +
		"[class*='ad-item-fifth'], [id*='ad-item-fifth'], " +
		"[class*='ad-item-primary'], [id*='ad-item-primary'], " +
		"[class*='ad-item-secondary'], [id*='ad-item-secondary'], " +
		"[class*='ad-item-tertiary'], [id*='ad-item-tertiary'], " +
		"[class*='ad-item-main'], [id*='ad-item-main'], " +
		"[class*='ad-item-sub'], [id*='ad-item-sub'], " +
		"[class*='ad-item-submit'], [id*='ad-item-submit'], " +
		"[class*='ad-item-search'], [id*='ad-item-search'], " +
		"[class*='ad-item-result'], [id*='ad-item-result'], " +
		"[class*='ad-item-result-item'], [id*='ad-item-result-item'], " +
		"[class*='ad-item-product'], [id*='ad-item-product'], " +
		"[class*='ad-item-link'], [id*='ad-item-link'], " +
		"[class*='ad-item-title'], [id*='ad-item-title'], " +
		"[class*='ad-item-desc'], [id*='ad-item-desc'], " +
		"[class*='ad-item-image'], [id*='ad-item-image'], " +
		"[class*='ad-item-price'], [id*='ad-item-price'], " +
		"[class*='ad-item-button'], [id*='ad-item-button'], " +
		"[class*='ad-item-buy'], [id*='ad-item-buy'], " +
		"[class*='ad-item-shop'], [id*='ad-item-shop'], " +
		"[class*='ad-item-order'], [id*='ad-item-order'], " +
		"[class*='ad-item-purchase'], [id*='ad-item-purchase'], " +
		"[class*='ad-item-get'], [id*='ad-item-get'], " +
		"[class*='ad-item-try'], [id*='ad-item-try'], " +
		"[class*='ad-item-test'], [id*='ad-item-test'], " +
		"[class*='ad-item-signup'], [id*='ad-item-signup'], " +
		"[class*='ad-item-register'], [id*='ad-item-register'], " +
		"[class*='ad-item-subscribe'], [id*='ad-item-subscribe'], " +
		"[class*='ad-item-learn'], [id*='ad-item-learn'], " +
		"[class*='ad-item-know'], [id*='ad-item-know'], " +
		"[class*='ad-item-read'], [id*='ad-item-read'], " +
		"[class*='ad-item-watch'], [id*='ad-item-watch'], " +
		"[class*='ad-item-listen'], [id*='ad-item-listen'], " +
		"[class*='ad-item-play'], [id*='ad-item-play'], " +
		"[class*='ad-item-click'], [id*='ad-item-click'], " +
		"[class*='ad-item-visit'], [id*='ad-item-visit'], " +
		"[class*='ad-item-goto'], [id*='ad-item-goto'], " +
		"[class*='ad-item-go'], [id*='ad-item-go'], " +
		"[class*='ad-item-come'], [id*='ad-item-come'], " +
		"[class*='ad-item-enter'], [id*='ad-item-enter'], " +
		"[class*='ad-item-access'], [id*='ad-item-access'], " +
		"[class*='ad-item-open'], [id*='ad-item-open'], " +
		"[class*='ad-item-close'], [id*='ad-item-close'], " +
		"[class*='ad-item-exit'], [id*='ad-item-exit'], " +
		"[class*='ad-item-leave'], [id*='ad-item-leave'], " +
		"[class*='ad-item-return'], [id*='ad-item-return'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-forward'], [id*='ad-item-forward'], " +
		"[class*='ad-item-next'], [id*='ad-item-next'], " +
		"[class*='ad-item-prev'], [id*='ad-item-prev'], " +
		"[class*='ad-item-previous'], [id*='ad-item-previous'], " +
		"[class*='ad-item-up'], [id*='ad-item-up'], " +
		"[class*='ad-item-down'], [id*='ad-item-down'], " +
		"[class*='ad-item-left'], [id*='ad-item-left'], " +
		"[class*='ad-item-right'], [id*='ad-item-right'], " +
		"[class*='ad-item-top'], [id*='ad-item-top'], " +
		"[class*='ad-item-bottom'], [id*='ad-item-bottom'], " +
		"[class*='ad-item-center'], [id*='ad-item-center'], " +
		"[class*='ad-item-middle'], [id*='ad-item-middle'], " +
		"[class*='ad-item-inside'], [id*='ad-item-inside'], " +
		"[class*='ad-item-outside'], [id*='ad-item-outside'], " +
		"[class*='ad-item-front'], [id*='ad-item-front'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-side'], [id*='ad-item-side'], " +
		"[class*='ad-item-corner'], [id*='ad-item-corner'], " +
		"[class*='ad-item-edge'], [id*='ad-item-edge'], " +
		"[class*='ad-item-border'], [id*='ad-item-border'], " +
		"[class*='ad-item-outline'], [id*='ad-item-outline'], " +
		"[class*='ad-item-frame'], [id*='ad-item-frame'], " +
		"[class*='ad-item-box'], [id*='ad-item-box'], " +
		"[class*='ad-item-container'], [id*='ad-item-container'], " +
		"[class*='ad-item-wrapper'], [id*='ad-item-wrapper'], " +
		"[class*='ad-item-wrap'], [id*='ad-item-wrap'], " +
		"[class*='ad-item-inner'], [id*='ad-item-inner'], " +
		"[class*='ad-item-outer'], [id*='ad-item-outer'], " +
		"[class*='ad-item-above'], [id*='ad-item-above'], " +
		"[class*='ad-item-below'], [id*='ad-item-below'], " +
		"[class*='ad-item-between'], [id*='ad-item-between'], " +
		"[class*='ad-item-over'], [id*='ad-item-over'], " +
		"[class*='ad-item-under'], [id*='ad-item-under'], " +
		"[class*='ad-item-above-below'], [id*='ad-item-above-below'], " +
		"[class*='ad-item-over-under'], [id*='ad-item-over-under'], " +
		"[class*='ad-item-top-bottom'], [id*='ad-item-top-bottom'], " +
		"[class*='ad-item-left-right'], [id*='ad-item-left-right'], " +
		"[class*='ad-item-hot'], [id*='ad-item-hot'], " +
		"[class*='ad-item-popular'], [id*='ad-item-popular'], " +
		"[class*='ad-item-trending'], [id*='ad-item-trending'], " +
		"[class*='ad-item-new'], [id*='ad-item-new'], " +
		"[class*='ad-item-featured'], [id*='ad-item-featured'], " +
		"[class*='ad-item-premium'], [id*='ad-item-premium'], " +
		"[class*='ad-item-exclusive'], [id*='ad-item-exclusive'], " +
		"[class*='ad-item-latest'], [id*='ad-item-latest'], " +
		"[class*='ad-item-updated'], [id*='ad-item-updated'], " +
		"[class*='ad-item-newest'], [id*='ad-item-newest'], " +
		"[class*='ad-item-beginning'], [id*='ad-item-beginning'], " +
		"[class*='ad-item-start'], [id*='ad-item-start'], " +
		"[class*='ad-item-end'], [id*='ad-item-end'], " +
		"[class*='ad-item-finish'], [id*='ad-item-finish'], " +
		"[class*='ad-item-final'], [id*='ad-item-final'], " +
		"[class*='ad-item-last'], [id*='ad-item-last'], " +
		"[class*='ad-item-first'], [id*='ad-item-first'], " +
		"[class*='ad-item-second'], [id*='ad-item-second'], " +
		"[class*='ad-item-third'], [id*='ad-item-third'], " +
		"[class*='ad-item-fourth'], [id*='ad-item-fourth'], " +
		"[class*='ad-item-fifth'], [id*='ad-item-fifth'], " +
		"[class*='ad-item-primary'], [id*='ad-item-primary'], " +
		"[class*='ad-item-secondary'], [id*='ad-item-secondary'], " +
		"[class*='ad-item-tertiary'], [id*='ad-item-tertiary'], " +
		"[class*='ad-item-main'], [id*='ad-item-main'], " +
		"[class*='ad-item-sub'], [id*='ad-item-sub'], " +
		"[class*='ad-item-submit'], [id*='ad-item-submit'], " +
		"[class*='ad-item-search'], [id*='ad-item-search'], " +
		"[class*='ad-item-result'], [id*='ad-item-result'], " +
		"[class*='ad-item-result-item'], [id*='ad-item-result-item'], " +
		"[class*='ad-item-product'], [id*='ad-item-product'], " +
		"[class*='ad-item-link'], [id*='ad-item-link'], " +
		"[class*='ad-item-title'], [id*='ad-item-title'], " +
		"[class*='ad-item-desc'], [id*='ad-item-desc'], " +
		"[class*='ad-item-image'], [id*='ad-item-image'], " +
		"[class*='ad-item-price'], [id*='ad-item-price'], " +
		"[class*='ad-item-button'], [id*='ad-item-button'], " +
		"[class*='ad-item-buy'], [id*='ad-item-buy'], " +
		"[class*='ad-item-shop'], [id*='ad-item-shop'], " +
		"[class*='ad-item-order'], [id*='ad-item-order'], " +
		"[class*='ad-item-purchase'], [id*='ad-item-purchase'], " +
		"[class*='ad-item-get'], [id*='ad-item-get'], " +
		"[class*='ad-item-try'], [id*='ad-item-try'], " +
		"[class*='ad-item-test'], [id*='ad-item-test'], " +
		"[class*='ad-item-signup'], [id*='ad-item-signup'], " +
		"[class*='ad-item-register'], [id*='ad-item-register'], " +
		"[class*='ad-item-subscribe'], [id*='ad-item-subscribe'], " +
		"[class*='ad-item-learn'], [id*='ad-item-learn'], " +
		"[class*='ad-item-know'], [id*='ad-item-know'], " +
		"[class*='ad-item-read'], [id*='ad-item-read'], " +
		"[class*='ad-item-watch'], [id*='ad-item-watch'], " +
		"[class*='ad-item-listen'], [id*='ad-item-listen'], " +
		"[class*='ad-item-play'], [id*='ad-item-play'], " +
		"[class*='ad-item-click'], [id*='ad-item-click'], " +
		"[class*='ad-item-visit'], [id*='ad-item-visit'], " +
		"[class*='ad-item-goto'], [id*='ad-item-goto'], " +
		"[class*='ad-item-go'], [id*='ad-item-go'], " +
		"[class*='ad-item-come'], [id*='ad-item-come'], " +
		"[class*='ad-item-enter'], [id*='ad-item-enter'], " +
		"[class*='ad-item-access'], [id*='ad-item-access'], " +
		"[class*='ad-item-open'], [id*='ad-item-open'], " +
		"[class*='ad-item-close'], [id*='ad-item-close'], " +
		"[class*='ad-item-exit'], [id*='ad-item-exit'], " +
		"[class*='ad-item-leave'], [id*='ad-item-leave'], " +
		"[class*='ad-item-return'], [id*='ad-item-return'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-forward'], [id*='ad-item-forward'], " +
		"[class*='ad-item-next'], [id*='ad-item-next'], " +
		"[class*='ad-item-prev'], [id*='ad-item-prev'], " +
		"[class*='ad-item-previous'], [id*='ad-item-previous'], " +
		"[class*='ad-item-up'], [id*='ad-item-up'], " +
		"[class*='ad-item-down'], [id*='ad-item-down'], " +
		"[class*='ad-item-left'], [id*='ad-item-left'], " +
		"[class*='ad-item-right'], [id*='ad-item-right'], " +
		"[class*='ad-item-top'], [id*='ad-item-top'], " +
		"[class*='ad-item-bottom'], [id*='ad-item-bottom'], " +
		"[class*='ad-item-center'], [id*='ad-item-center'], " +
		"[class*='ad-item-middle'], [id*='ad-item-middle'], " +
		"[class*='ad-item-inside'], [id*='ad-item-inside'], " +
		"[class*='ad-item-outside'], [id*='ad-item-outside'], " +
		"[class*='ad-item-front'], [id*='ad-item-front'], " +
		"[class*='ad-item-back'], [id*='ad-item-back'], " +
		"[class*='ad-item-side'], [id*='ad-item-side'], " +
		"[class*='ad-item-corner'], [id*='ad-item-corner'], " +
		"[class*='ad-item-edge'], [id*='ad-item-edge'], " +
		"[class*='ad-item-border'], [id*='ad-item-border'], " +
		"[class*='ad-item-outline'], [id*='ad-item-outline'], " +
		"[class*='ad-item-frame'], [id*='ad-item-frame'], " +
		"[class*='ad-item-box'], [id*='ad-item-box'], " +
		"[class*='ad-item-container'], [id*='ad-item-container'], " +
		"[class*='ad-item-wrapper'], [id*='ad-item-wrapper'], " +
		"[class*='ad-item-wrap'], [id*='ad-item-wrap'], " +
		"[class*='ad-item-inner'], [id*='ad-item-inner'], " +
		"[class*='ad-item-outer'], [id*='ad-item-outer'], " +
		"[class*='ad-item-above'], [id*='ad-item-above'], " +
		"[class*='ad-item-below'], [id*='ad-item-below'], " +
		"[class*='ad-item-between'], [id*='ad-item-between'], " +
		"[class*='ad-item-over'], [id*='ad-item-over'], " +
		"[class*='ad-item-under'], [id*='ad-item-under'], " +
		"[class*='ad-item-above-below'], [id*='ad-item-above-below'], " +
		"[class*='ad-item-over-under'], [id*='ad-item-over-under'], " +
		"[class*='ad-item-top-bottom'], [id*='ad-item-top-bottom'], " +
		"[class*='ad-item-left-right'], [id*='ad-item-left-right']";
		
		String script = "(function(){" +
		"var rules = '" + defaultSelectors.replace("'", "\\'") + "'.split(',');" +
		"for(var i=0; i<rules.length; i++) {" +
		"   var rule = rules[i].trim();" +
		"   if(rule) {" +
		"       try {" +
		"           var els = document.querySelectorAll(rule);" +
		"           for(var j=0; j<els.length; j++) {" +
		"               els[j].style.display = 'none';" +
		"               els[j].style.visibility = 'hidden';" +
		"               els[j].style.height = '0';" +
		"               els[j].style.overflow = 'hidden';" +
		"               els[j].style.padding = '0';" +
		"               els[j].style.margin = '0';" +
		"               els[j].style.border = '0';" +
		"               els[j].style.width = '0';" +
		"               els[j].style.opacity = '0';" +
		"               els[j].style.zIndex = '-999999';" +
		"           }" +
		"       } catch(e) {}" +
		"   }" +
		"}" +
		"// 动态监听新增广告" +
		"var observer = new MutationObserver(function(mutations) {" +
		"   mutations.forEach(function(mutation) {" +
		"       if(mutation.addedNodes) {" +
		"           mutation.addedNodes.forEach(function(node) {" +
		"               if(node.nodeType === 1) {" +
		"                   for(var k=0; k<rules.length; k++) {" +
		"                       var r = rules[k].trim();" +
		"                       if(r) {" +
		"                           try {" +
		"                               if(node.matches && node.matches(r)) {" +
		"                                   node.style.display = 'none';" +
		"                                   node.style.visibility = 'hidden';" +
		"                                   node.style.height = '0';" +
		"                                   node.style.overflow = 'hidden';" +
		"                               }" +
		"                               var childNodes = node.querySelectorAll(r);" +
		"                               for(var l=0; l<childNodes.length; l++) {" +
		"                                   childNodes[l].style.display = 'none';" +
		"                                   childNodes[l].style.visibility = 'hidden';" +
		"                                   childNodes[l].style.height = '0';" +
		"                                   childNodes[l].style.overflow = 'hidden';" +
		"                               }" +
		"                           } catch(e) {}" +
		"                       }" +
		"                   }" +
		"               }" +
		"           });" +
		"       }" +
		"   });" +
		"});" +
		"observer.observe(document.body, { childList: true, subtree: true });" +
		"})();";
		currentTab.webView.evaluateJavascript(script, null);
		
		if (!importedAdBlockScript.isEmpty()) {
			currentTab.webView.evaluateJavascript(importedAdBlockScript, null);
		}
		
		if (adAutoBlockEnabled) {
			injectAutoPopupBlockerJS();
		}
		if (adPreventJumpEnabled) {
			injectPreventAutomaticJumpJS();
		}
	}
	
	private void injectAutoPopupBlockerJS() {
		if (currentTab == null || currentTab.webView == null) return;
		if (isLocalResource(currentTab.url)) return;
		String js = "(function(){" +
		"   var observer = new MutationObserver(function(mutations) {" +
		"       mutations.forEach(function(mutation) {" +
		"           if(mutation.addedNodes) {" +
		"               mutation.addedNodes.forEach(function(node) {" +
		"                   if(node.nodeType === 1 && (node.classList && " +
		"                       (node.classList.contains('modal') || node.classList.contains('popup') || " +
		"                        node.classList.contains('overlay') || node.classList.contains('adsbygoogle')))) {" +
		"                       node.style.display = 'none';" +
		"                       node.style.visibility = 'hidden';" +
		"                       node.style.height = '0';" +
		"                       node.style.overflow = 'hidden';" +
		"                   }" +
		"               });" +
		"           }" +
		"       });" +
		"   });" +
		"   observer.observe(document.body, { childList: true, subtree: true });" +
		"})();";
		currentTab.webView.evaluateJavascript(js, null);
	}
	
	private void injectPreventAutomaticJumpJS() {
		if (currentTab == null || currentTab.webView == null) return;
		if (isLocalResource(currentTab.url)) return;
		String js = "(function(){" +
		"   window._lastUserClick = 0;" +
		"   document.addEventListener('click', function(e) { window._lastUserClick = Date.now(); }, true);" +
		"   var blockJump = function() {" +
		"       var originalAssign = window.location.assign;" +
		"       window.location.assign = function(url) {" +
		"           if (Date.now() - window._lastUserClick > 3000) {" +
		"               console.log('已拦截非用户触发的跳转: ' + url);" +
		"               return;" +
		"           }" +
		"           originalAssign.call(window.location, url);" +
		"       };" +
		"       var originalReplace = window.location.replace;" +
		"       window.location.replace = function(url) {" +
		"           if (Date.now() - window._lastUserClick > 3000) {" +
		"               console.log('已拦截非用户触发的替换: ' + url);" +
		"               return;" +
		"           }" +
		"           originalReplace.call(window.location, url);" +
		"       };" +
		"       window.open = function(url) {" +
		"           if (Date.now() - window._lastUserClick > 3000) {" +
		"               console.log('已拦截非用户触发的弹窗: ' + url);" +
		"               return null;" +
		"           }" +
		"           return window.open.originalOpen ? window.open.originalOpen(url) : null;" +
		"       };" +
		"       window.open.originalOpen = window.open;" +
		"   };" +
		"   blockJump();" +
		"})();";
		currentTab.webView.evaluateJavascript(js, null);
	}
	
	private void loadAdBlockRules() {
		// 不再加载持久化规则，只保留默认规则自动隐藏
		adBlockRules.clear();
	}
	
	private void importAdBlockScript() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/*");
		String[] mimeTypes = {"text/javascript", "text/plain", "application/javascript", "application/x-javascript"};
		intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
		startActivityForResult(Intent.createChooser(intent, "选择屏蔽脚本文件"), AD_BLOCK_SCRIPT_PICK);
	}
	
	private void handleAdBlockScriptImport(Uri uri) {
		try {
			InputStream is = getContentResolver().openInputStream(uri);
			if (is == null) {
				Toast.makeText(this, "无法读取文件", Toast.LENGTH_SHORT).show();
				return;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			reader.close();
			is.close();
			
			String script = sb.toString().trim();
			if (script.isEmpty()) {
				Toast.makeText(this, "文件内容为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			importedAdBlockScript = script;
			prefs.edit().putString(PREF_AD_BLOCK_IMPORTED_SCRIPT, script).apply();
			
			if (currentTab != null && currentTab.webView != null) {
				injectAdBlockRules();
			}
			
			Toast.makeText(this, "脚本导入成功，已生效 (" + script.length() + " 字符)", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
			Toast.makeText(this, "导入失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void showAdBlockManagerDialog() {
		Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 24));
		layout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView titleView = new TextView(this);
		titleView.setText("已导入的屏蔽脚本");
		titleView.setTextColor(TEXT_WHITE);
		titleView.setTextSize(22);
		titleView.setTypeface(null, Typeface.BOLD);
		titleView.setGravity(Gravity.CENTER);
		layout.addView(titleView);
		
		TextView statusText = new TextView(this);
		if (importedAdBlockScript.isEmpty()) {
			statusText.setText("尚未导入任何屏蔽脚本\n\n点击\"导入脚本\"按钮选择.js或.txt文件\n可从网上寻找AdBlock规则脚本导入使用");
			} else {
			statusText.setText("已导入脚本 (" + importedAdBlockScript.length() + " 字符)\n脚本已生效，正在拦截广告");
		}
		statusText.setTextColor(TEXT_GRAY);
		statusText.setTextSize(14);
		statusText.setPadding(0, dp(16), 0, dp(16));
		layout.addView(statusText);
		
		if (!importedAdBlockScript.isEmpty()) {
			Button clearBtn = new Button(this);
			clearBtn.setText("清除已导入脚本");
			clearBtn.setTextColor(TEXT_WHITE);
			clearBtn.setBackground(createBg(0xFFF44336, 12));
			clearBtn.setOnClickListener(v -> {
				importedAdBlockScript = "";
				prefs.edit().remove(PREF_AD_BLOCK_IMPORTED_SCRIPT).apply();
				Toast.makeText(this, "已清除导入的屏蔽脚本", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			});
			LinearLayout.LayoutParams clearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(40));
			clearParams.topMargin = dp(8);
			layout.addView(clearBtn, clearParams);
		}
		
		Button closeBtn = new Button(this);
		closeBtn.setText("关闭");
		closeBtn.setTextColor(TEXT_WHITE);
		closeBtn.setBackground(createBg(btnBgColor, 12));
		closeBtn.setOnClickListener(v -> dialog.dismiss());
		LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(40));
		closeParams.topMargin = dp(8);
		layout.addView(closeBtn, closeParams);
		
		dialog.setContentView(layout);
		Window window = dialog.getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	// ==================== 资源爬取功能（优化悬浮窗，增加权限检查） ====================
	private void startCrawlMode() {
		if (!Settings.canDrawOverlays(this)) {
			new android.app.AlertDialog.Builder(this)
			.setTitle("需要悬浮窗权限")
			.setMessage("爬取功能需要显示悬浮窗，请授予权限")
			.setPositiveButton("去设置", (dialog, which) -> {
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				Uri.parse("package:" + getPackageName()));
				startActivity(intent);
			})
			.setNegativeButton("取消", null)
			.show();
			return;
		}
		capturedResources.clear();
		isManualCrawlMode = false;
		isCrawlMultiSelect = false;
		crawlSelectedUrls.clear();
		showCrawlFloatButton();
		performAutoCrawl();
	}
	
	private void performAutoCrawl() {
		if (currentTab == null || currentTab.webView == null) return;
		String js = "(function(){" +
		"var resources = [];" +
		"var imgs = document.querySelectorAll('img');" +
		"for(var i=0; i<imgs.length; i++) {" +
		"   if(imgs[i].src) resources.push({url: imgs[i].src, type: 'image'});" +
		"}" +
		"var videos = document.querySelectorAll('video source, video');" +
		"for(var i=0; i<videos.length; i++) {" +
		"   var src = videos[i].src || videos[i].getAttribute('src');" +
		"   if(src) resources.push({url: src, type: 'video'});" +
		"}" +
		"var audios = document.querySelectorAll('audio source, audio');" +
		"for(var i=0; i<audios.length; i++) {" +
		"   var src = audios[i].src || audios[i].getAttribute('src');" +
		"   if(src) resources.push({url: src, type: 'audio'});" +
		"}" +
		"return JSON.stringify(resources);" +
		"})();";
		currentTab.webView.evaluateJavascript(js, value -> {
			try {
				if (value.startsWith("\"") && value.endsWith("\"")) {
					value = value.substring(1, value.length()-1).replace("\\\"", "\"");
				}
				JSONArray arr = new JSONArray(value);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.getJSONObject(i);
					String url = obj.getString("url");
					String type = obj.getString("type");
					if (!containsCapturedUrl(url)) {
						capturedResources.add(new CapturedResource(url, type));
					}
				}
				updateCrawlFloatCount(capturedResources.size());
				Toast.makeText(MainActivity.this, "自动爬取完成，共 " + capturedResources.size() + " 个资源", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
				Toast.makeText(MainActivity.this, "爬取解析出错", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private boolean containsCapturedUrl(String url) {
		for (CapturedResource r : capturedResources) {
			if (r.url.equals(url)) return true;
		}
		return false;
	}
	
	private void addCapturedResource(String url, String type) {
		if (!containsCapturedUrl(url)) {
			capturedResources.add(new CapturedResource(url, type));
			runOnUiThread(() -> updateCrawlFloatCount(capturedResources.size()));
		}
	}
	
	private void updateCrawlFloatCount(int count) {
		if (crawlCountTextView != null) {
			crawlCountTextView.setText(String.valueOf(count));
		}
	}
	
	private void showCrawlFloatButton() {
		if (crawlFloatAdded) return;
		
		Button crawlButton = new Button(this);
		crawlButton.setText("🕷️");
		crawlButton.setBackgroundColor(Color.TRANSPARENT);
		crawlButton.setTextSize(24);
		crawlButton.setPadding(dp(8), dp(8), dp(8), dp(8));
		
		crawlFloatParams = new WindowManager.LayoutParams();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			crawlFloatParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
			} else {
			crawlFloatParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		}
		crawlFloatParams.format = PixelFormat.TRANSLUCENT;
		crawlFloatParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		crawlFloatParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		crawlFloatParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		crawlFloatParams.gravity = Gravity.TOP | Gravity.START;
		crawlFloatParams.x = dp(20);
		crawlFloatParams.y = dp(200);
		
		crawlButton.setOnClickListener(v -> {
			if (crawlResourceDialog != null && crawlResourceDialog.isShowing()) {
				crawlResourceDialog.dismiss();
				crawlResourceDialog = null;
				} else {
				showCrawlResourceDialog();
			}
		});
		
		crawlButton.setOnTouchListener(new View.OnTouchListener() {
			private float initialX, initialY;
			private int initialTouchX, initialTouchY;
			private boolean moving = false;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					initialX = crawlFloatParams.x;
					initialY = crawlFloatParams.y;
					initialTouchX = (int) event.getRawX();
					initialTouchY = (int) event.getRawY();
					moving = false;
					return false;
					case MotionEvent.ACTION_MOVE:
					moving = true;
					crawlFloatParams.x = (int) (initialX + (event.getRawX() - initialTouchX));
					crawlFloatParams.y = (int) (initialY + (event.getRawY() - initialTouchY));
					windowManager.updateViewLayout(crawlButton, crawlFloatParams);
					return false;
				}
				return false;
			}
		});
		
		crawlFloatView = crawlButton;
		try {
			windowManager.addView(crawlFloatView, crawlFloatParams);
			crawlFloatAdded = true;
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void hideCrawlFloatButton() {
		if (crawlFloatAdded && crawlFloatView != null) {
			try { windowManager.removeView(crawlFloatView); } catch (Exception ignored) {}
			crawlFloatAdded = false;
			crawlFloatView = null;
			crawlCountTextView = null;
		}
		if (crawlResourceDialog != null && crawlResourceDialog.isShowing()) {
			crawlResourceDialog.dismiss();
			crawlResourceDialog = null;
		}
	}
	
	// 唯一正确的 showCrawlResourceDialog，使用 gridContainer 和 populateCrawlGrid
	private void showCrawlResourceDialog() {
		Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 16));
		layout.setPadding(dp(16), dp(16), dp(16), dp(16));
		
		TextView title = new TextView(this);
		title.setText("爬取资源 (" + capturedResources.size() + ")");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(18);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		title.setPadding(0, 0, 0, dp(12));
		layout.addView(title);
		
		LinearLayout filterAllRow = new LinearLayout(this);
		filterAllRow.setOrientation(LinearLayout.HORIZONTAL);
		filterAllRow.setGravity(Gravity.CENTER);
		filterAllRow.setPadding(0, dp(4), 0, dp(4));
		String[] filterTypes = {"全部", "图片", "视频", "音频", "其他"};
		final LinearLayout gridContainer = new LinearLayout(this);
		gridContainer.setOrientation(LinearLayout.VERTICAL);
		gridContainer.setPadding(0, dp(8), 0, 0);
		
		for (String type : filterTypes) {
			Button filterBtn = new Button(this);
			filterBtn.setText(type);
			filterBtn.setTextColor(TEXT_WHITE);
			filterBtn.setTextSize(12);
			filterBtn.setBackground(createBg(btnBgColor, 8));
			filterBtn.setAllCaps(false);
			filterBtn.setPadding(dp(6), dp(4), dp(6), dp(4));
			filterBtn.setOnClickListener(v -> {
				crawlFilterType = type;
				populateCrawlGrid(gridContainer);
			});
			LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, dp(32), 1);
			btnParams.setMargins(dp(2), 0, dp(2), 0);
			filterBtn.setLayoutParams(btnParams);
			filterAllRow.addView(filterBtn);
		}
		layout.addView(filterAllRow);
		
		LinearLayout actionRow = new LinearLayout(this);
		actionRow.setOrientation(LinearLayout.HORIZONTAL);
		actionRow.setGravity(Gravity.CENTER);
		actionRow.setPadding(0, dp(8), 0, dp(4));
		
		Button manualCrawlBtn = new Button(this);
		manualCrawlBtn.setText("手动爬取");
		manualCrawlBtn.setTextColor(TEXT_WHITE);
		manualCrawlBtn.setBackground(createBg(btnBgColor, 12));
		manualCrawlBtn.setAllCaps(false);
		manualCrawlBtn.setOnClickListener(v -> {
			dialog.dismiss();
			crawlResourceDialog = null;
			startManualCrawlMode();
		});
		actionRow.addView(manualCrawlBtn, new LinearLayout.LayoutParams(0, dp(40), 1));
		
		Button refreshBtn = new Button(this);
		refreshBtn.setText("重新爬取");
		refreshBtn.setTextColor(TEXT_WHITE);
		refreshBtn.setBackground(createBg(btnBgColor, 12));
		refreshBtn.setAllCaps(false);
		refreshBtn.setOnClickListener(v -> {
			capturedResources.clear();
			updateCrawlFloatCount(0);
			performAutoCrawl();
			dialog.dismiss();
			crawlResourceDialog = null;
			mainHandler.postDelayed(() -> showCrawlResourceDialog(), 500);
		});
		actionRow.addView(refreshBtn, new LinearLayout.LayoutParams(0, dp(40), 1));
		
		Button clearAllBtn = new Button(this);
		clearAllBtn.setText("清空全部");
		clearAllBtn.setTextColor(TEXT_WHITE);
		clearAllBtn.setBackground(createBg(0xFFCC0000, 12));
		clearAllBtn.setAllCaps(false);
		clearAllBtn.setOnClickListener(v -> {
			capturedResources.clear();
			updateCrawlFloatCount(0);
			populateCrawlGrid(gridContainer);
			Toast.makeText(this, "已清空爬取内容", Toast.LENGTH_SHORT).show();
		});
		actionRow.addView(clearAllBtn, new LinearLayout.LayoutParams(0, dp(40), 1));
		layout.addView(actionRow);
		
		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(gridContainer);
		LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
		scrollParams.topMargin = dp(8);
		scrollParams.bottomMargin = dp(8);
		scrollView.setLayoutParams(scrollParams);
		layout.addView(scrollView);
		
		Button closeFloatBtn = new Button(this);
		closeFloatBtn.setText("关闭悬浮窗");
		closeFloatBtn.setTextColor(TEXT_WHITE);
		closeFloatBtn.setBackground(createBg(0xFF444444, 12));
		closeFloatBtn.setAllCaps(false);
		closeFloatBtn.setOnClickListener(v -> {
			hideCrawlFloatButton();
			dialog.dismiss();
			crawlResourceDialog = null;
		});
		layout.addView(closeFloatBtn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(40)));
		
		Button closeDialogBtn = new Button(this);
		closeDialogBtn.setText("关闭弹窗");
		closeDialogBtn.setTextColor(TEXT_WHITE);
		closeDialogBtn.setBackground(createBg(btnBgColor, 12));
		closeDialogBtn.setOnClickListener(v -> {
			dialog.dismiss();
			crawlResourceDialog = null;
		});
		LinearLayout.LayoutParams closeDialogParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(40));
		closeDialogParams.topMargin = dp(8);
		closeDialogBtn.setLayoutParams(closeDialogParams);
		layout.addView(closeDialogBtn);
		
		dialog.setContentView(layout);
		Window w = dialog.getWindow();
		if (w != null) {
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setLayout((int)(getResources().getDisplayMetrics().widthPixels * 0.95), (int)(getResources().getDisplayMetrics().heightPixels * 0.8));
			w.setGravity(Gravity.CENTER);
		}
		crawlResourceDialog = dialog;
		
		populateCrawlGrid(gridContainer);
		
		dialog.setOnDismissListener(d -> {
			if (crawlResourceDialog == dialog) {
				crawlResourceDialog = null;
			}
		});
		
		dialog.show();
	}
	
	// fill crawl grid based on filter type
	private void populateCrawlGrid(LinearLayout gridContainer) {
		gridContainer.removeAllViews();
		List<CapturedResource> filtered = new ArrayList<>();
		for (CapturedResource r : capturedResources) {
			if (crawlFilterType.equals("全部")
			|| (crawlFilterType.equals("图片") && r.type.equals("image"))
			|| (crawlFilterType.equals("视频") && r.type.equals("video"))
			|| (crawlFilterType.equals("音频") && r.type.equals("audio"))
			|| (crawlFilterType.equals("其他") && !r.type.equals("image") && !r.type.equals("video") && !r.type.equals("audio"))) {
				filtered.add(r);
			}
		}
		int columns = 2;
		LinearLayout currentRow = null;
		for (int i = 0; i < filtered.size(); i++) {
			if (i % columns == 0) {
				currentRow = new LinearLayout(this);
				currentRow.setOrientation(LinearLayout.HORIZONTAL);
				currentRow.setGravity(Gravity.CENTER);
				gridContainer.addView(currentRow);
			}
			View itemView = createCrawlItemView(filtered.get(i));
			if (currentRow != null) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
				lp.setMargins(dp(4), dp(4), dp(4), dp(4));
				currentRow.addView(itemView, lp);
			}
		}
	}
	
	private View createCrawlItemView(CapturedResource res) {
		LinearLayout itemLayout = new LinearLayout(this);
		itemLayout.setOrientation(LinearLayout.VERTICAL);
		itemLayout.setGravity(Gravity.CENTER);
		itemLayout.setBackground(createBg(0x44000000, 8));
		itemLayout.setPadding(dp(8), dp(8), dp(8), dp(8));
		
		TextView typeView = new TextView(this);
		String typeEmoji = "📷";
		if (res.type.contains("video")) typeEmoji = "🎬";
		else if (res.type.contains("audio")) typeEmoji = "🎵";
		typeView.setText(typeEmoji + " " + res.type);
		typeView.setTextColor(TEXT_GRAY);
		typeView.setTextSize(12);
		itemLayout.addView(typeView);
		
		TextView urlView = new TextView(this);
		String displayUrl = res.url.length() > 30 ? res.url.substring(0, 30) + "..." : res.url;
		urlView.setText(displayUrl);
		urlView.setTextColor(TEXT_WHITE);
		urlView.setTextSize(10);
		urlView.setMaxLines(2);
		urlView.setEllipsize(TextUtils.TruncateAt.END);
		urlView.setPadding(0, dp(4), 0, dp(4));
		itemLayout.addView(urlView);
		
		Button viewBtn = new Button(this);
		viewBtn.setText("查看/播放");
		viewBtn.setTextColor(TEXT_WHITE);
		viewBtn.setBackground(createBg(btnBgColor, 12));
		viewBtn.setAllCaps(false);
		viewBtn.setTextSize(10);
		viewBtn.setPadding(0, dp(6), 0, dp(6));
		viewBtn.setOnClickListener(v -> {
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.loadUrl(res.url);
			}
		});
		itemLayout.addView(viewBtn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		Button downloadBtn = new Button(this);
		downloadBtn.setText("下载");
		downloadBtn.setTextColor(TEXT_WHITE);
		downloadBtn.setBackground(createBg(accentColor, 12));
		downloadBtn.setAllCaps(false);
		downloadBtn.setTextSize(10);
		downloadBtn.setPadding(0, dp(6), 0, dp(6));
		downloadBtn.setOnClickListener(v -> {
			startDownloadResource(res);
		});
		LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dlp.topMargin = dp(4);
		itemLayout.addView(downloadBtn, dlp);
		
		return itemLayout;
	}
	
	// start manual crawl mode
	private void startManualCrawlMode() {
		isManualCrawlMode = true;
		isCrawlMultiSelect = false;
		crawlSelectedUrls.clear();
		showManualCrawlModeDialog();
	}
	
	private void showManualCrawlModeDialog() {
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 20));
		layout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(this);
		title.setText("手动爬取");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(18);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		layout.addView(title);
		
		Button singleBtn = new Button(this);
		singleBtn.setText("单选（点击元素获取链接）");
		singleBtn.setTextColor(TEXT_WHITE);
		singleBtn.setBackground(createBg(btnBgColor, 12));
		singleBtn.setOnClickListener(v -> {
			dialog.dismiss();
			isCrawlMultiSelect = false;
			injectManualCrawlJS(false);
		});
		layout.addView(singleBtn);
		
		Button multiBtn = new Button(this);
		multiBtn.setText("多选（批量选择后下载）");
		multiBtn.setTextColor(TEXT_WHITE);
		multiBtn.setBackground(createBg(btnBgColor, 12));
		multiBtn.setOnClickListener(v -> {
			dialog.dismiss();
			isCrawlMultiSelect = true;
			injectManualCrawlJS(true);
		});
		layout.addView(multiBtn);
		
		Button cancelBtn = new Button(this);
		cancelBtn.setText("取消");
		cancelBtn.setTextColor(TEXT_WHITE);
		cancelBtn.setBackground(createBg(btnBgColor, 12));
		cancelBtn.setOnClickListener(v -> {
			dialog.dismiss();
			exitManualCrawlMode();
		});
		layout.addView(cancelBtn);
		
		dialog.setContentView(layout);
		Window w = dialog.getWindow();
		if (w != null) {
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setLayout((int)(getResources().getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
			w.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	private void injectManualCrawlJS(boolean multiSelect) {
		if (currentTab == null || currentTab.webView == null) return;
		String js = "(function(){" +
		"if(window._manualCrawlActive) return;" +
		"window._manualCrawlActive = true;" +
		"var all = document.querySelectorAll('*');" +
		"for(var i=0; i<all.length; i++) all[i].style.outline = '2px dashed green';" +
		"document.body.style.cursor = 'crosshair';" +
		"window._crawlClickListener = function(e) {" +
		"   e.preventDefault();" +
		"   e.stopPropagation();" +
		"   var el = e.target;" +
		"   var url = el.src || el.href || '';" +
		"   if(!url && el.querySelector) {" +
		"       var source = el.querySelector('source');" +
		"       if(source) url = source.src;" +
		"   }" +
		"   if(url) {" +
		"       Android.onResourceCaptured(url, el.tagName.toLowerCase());" +
		"   }" +
		"   if (!" + multiSelect + ") {" +
		"       window._manualCrawlActive = false;" +
		"       document.body.style.cursor = 'default';" +
		"       for(var i=0; i<all.length; i++) all[i].style.outline = '';" +
		"       document.removeEventListener('click', window._crawlClickListener, true);" +
		"   }" +
		"};" +
		"document.addEventListener('click', window._crawlClickListener, true);" +
		"})();";
		currentTab.webView.evaluateJavascript(js, null);
	}
	
	private void exitManualCrawlMode() {
		isManualCrawlMode = false;
		isCrawlMultiSelect = false;
		crawlSelectedUrls.clear();
		if (currentTab != null && currentTab.webView != null) {
			currentTab.webView.evaluateJavascript("javascript:(function(){" +
			"if(window._manualCrawlActive) {" +
			"   window._manualCrawlActive = false;" +
			"   document.body.style.cursor = 'default';" +
			"   var all = document.querySelectorAll('*');" +
			"   for(var i=0; i<all.length; i++) all[i].style.outline = '';" +
			"   document.removeEventListener('click', window._crawlClickListener, true);" +
			"}})();", null);
		}
	}
	
	// ==================== 下载管理器 ====================
	private void startDownloadResource(CapturedResource res) {
		try {
			String fileName = res.url.substring(res.url.lastIndexOf('/') + 1).split("\\?")[0];
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(res.url));
			request.setTitle("下载 " + fileName);
			request.setDescription("来自爬取");
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setDestinationInExternalPublicDir("Download", fileName);
			long downloadId = downloadManager.enqueue(request);
			
			DownloadEntry entry = new DownloadEntry();
			entry.url = res.url;
			entry.fileName = fileName;
			entry.downloadId = downloadId;
			entry.mimeType = res.type;
			entry.progress = 0;
			entry.completed = false;
			entry.localPath = "/storage/emulated/0/Download/" + fileName;
			downloadEntries.add(entry);
			
			Toast.makeText(this, "开始下载: " + fileName, Toast.LENGTH_SHORT).show();
			monitorDownloadProgress(entry);
			} catch (Exception e) {
			Toast.makeText(this, "下载失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void monitorDownloadProgress(DownloadEntry entry) {
		mainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (entry.completed || entry.downloadId == -1) return;
				DownloadManager.Query query = new DownloadManager.Query();
				query.setFilterById(entry.downloadId);
				android.database.Cursor cursor = downloadManager.query(query);
				if (cursor != null && cursor.moveToFirst()) {
					int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
					int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
					if (bytesTotal > 0) {
						entry.progress = (int) (bytesDownloaded * 100 / bytesTotal);
					}
					int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
					if (status == DownloadManager.STATUS_SUCCESSFUL) {
						entry.completed = true;
						Toast.makeText(MainActivity.this, entry.fileName + " 下载完成", Toast.LENGTH_SHORT).show();
						} else if (status == DownloadManager.STATUS_FAILED) {
						entry.completed = true;
						Toast.makeText(MainActivity.this, entry.fileName + " 下载失败", Toast.LENGTH_SHORT).show();
						} else {
						mainHandler.postDelayed(this, 500);
					}
					cursor.close();
					} else {
					mainHandler.postDelayed(this, 1000);
				}
			}
		}, 500);
	}
	
	private void showDownloadManagerDialog() {
		Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 20));
		layout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(this);
		title.setText("下载管理器");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(20);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		layout.addView(title);
		
		ScrollView scrollView = new ScrollView(this);
		LinearLayout listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		listLayout.setPadding(0, dp(12), 0, dp(12));
		
		for (DownloadEntry entry : downloadEntries) {
			LinearLayout itemRow = new LinearLayout(this);
			itemRow.setOrientation(LinearLayout.VERTICAL);
			itemRow.setBackground(createBg(0x44000000, 8));
			itemRow.setPadding(dp(12), dp(8), dp(12), dp(8));
			LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			itemParams.bottomMargin = dp(8);
			itemRow.setLayoutParams(itemParams);
			
			TextView nameView = new TextView(this);
			nameView.setText(entry.fileName);
			nameView.setTextColor(TEXT_WHITE);
			nameView.setTextSize(14);
			itemRow.addView(nameView);
			
			ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
			progressBar.setProgress(entry.progress);
			progressBar.setMax(100);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				progressBar.setProgressTintList(ColorStateList.valueOf(accentColor));
			}
			itemRow.addView(progressBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(4)));
			
			LinearLayout btnRow = new LinearLayout(this);
			btnRow.setOrientation(LinearLayout.HORIZONTAL);
			btnRow.setGravity(Gravity.CENTER);
			btnRow.setPadding(0, dp(8), 0, 0);
			
			if (entry.completed) {
				Button installBtn = new Button(this);
				installBtn.setText("安装/查看");
				installBtn.setTextColor(TEXT_WHITE);
				installBtn.setBackground(createBg(btnBgColor, 12));
				installBtn.setAllCaps(false);
				installBtn.setOnClickListener(v -> {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						Uri fileUri = Uri.parse("file://" + entry.localPath);
						intent.setDataAndType(fileUri, entry.mimeType);
						intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
						startActivity(Intent.createChooser(intent, "打开文件"));
						} catch (Exception e) {
						Toast.makeText(this, "无法打开文件", Toast.LENGTH_SHORT).show();
					}
				});
				btnRow.addView(installBtn, new LinearLayout.LayoutParams(0, dp(36), 1));
			}
			
			Button localBtn = new Button(this);
			localBtn.setText("前往本地");
			localBtn.setTextColor(TEXT_WHITE);
			localBtn.setBackground(createBg(btnBgColor, 12));
			localBtn.setAllCaps(false);
			localBtn.setOnClickListener(v -> {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri fileUri = Uri.parse("file:///storage/emulated/0/Download/");
					intent.setDataAndType(fileUri, "resource/folder");
					startActivity(intent);
					} catch (Exception e) {
					Toast.makeText(this, "无法打开文件管理器", Toast.LENGTH_SHORT).show();
				}
			});
			btnRow.addView(localBtn, new LinearLayout.LayoutParams(0, dp(36), 1));
			
			Button deleteBtn = new Button(this);
			deleteBtn.setText("删除记录");
			deleteBtn.setTextColor(TEXT_WHITE);
			deleteBtn.setBackgroundColor(Color.TRANSPARENT);
			deleteBtn.setOnClickListener(v -> {
				downloadEntries.remove(entry);
				dialog.dismiss();
				showDownloadManagerDialog();
			});
			btnRow.addView(deleteBtn, new LinearLayout.LayoutParams(0, dp(36), 1));
			
			itemRow.addView(btnRow);
			listLayout.addView(itemRow);
		}
		
		scrollView.addView(listLayout);
		layout.addView(scrollView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
		
		Button clearAllBtn = new Button(this);
		clearAllBtn.setText("清理下载记录");
		clearAllBtn.setTextColor(TEXT_WHITE);
		clearAllBtn.setBackground(createBg(btnBgColor, 12));
		clearAllBtn.setPadding(0, dp(12), 0, dp(12));
		clearAllBtn.setOnClickListener(v -> {
			downloadEntries.clear();
			dialog.dismiss();
			showDownloadManagerDialog();
			Toast.makeText(this, "下载记录已清空", Toast.LENGTH_SHORT).show();
		});
		layout.addView(clearAllBtn);
		
		Button closeBtn = new Button(this);
		closeBtn.setText("关闭");
		closeBtn.setTextColor(TEXT_WHITE);
		closeBtn.setBackground(createBg(btnBgColor, 12));
		closeBtn.setPadding(0, dp(12), 0, dp(12));
		closeBtn.setOnClickListener(v -> dialog.dismiss());
		layout.addView(closeBtn);
		
		dialog.setContentView(layout);
		Window w = dialog.getWindow();
		if (w != null) {
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setLayout((int)(getResources().getDisplayMetrics().widthPixels * 0.95), (int)(getResources().getDisplayMetrics().heightPixels * 0.85));
			w.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	// ==================== 夜间模式注入（排除本地） ====================
	private void injectNightMode() {
		if (currentTab == null || currentTab.webView == null) return;
		if (isLocalResource(currentTab.url)) return;
		String css = "html, body { background: #1a1a1a !important; color: #e0e0e0 !important; } " +
		"a { color: #8ab4f8 !important; } " +
		"img, video, iframe, canvas { filter: brightness(0.8) contrast(1.1); }";
		String script = "javascript:(function(){" +
		"var style = document.getElementById('night-mode-style');" +
		"if(!style) {" +
		"   style = document.createElement('style');" +
		"   style.id = 'night-mode-style';" +
		"   document.head.appendChild(style);" +
		"}" +
		"style.innerHTML = '" + css.replace("'", "\\'") + "';" +
		"})();";
		currentTab.webView.evaluateJavascript(script, null);
	}
	
	private void removeNightMode() {
		if (currentTab == null || currentTab.webView == null) return;
		if (isLocalResource(currentTab.url)) return;
		String script = "javascript:(function(){" +
		"var style = document.getElementById('night-mode-style');" +
		"if(style) style.remove();" +
		"})();";
		currentTab.webView.evaluateJavascript(script, null);
	}
	
	// ==================== 历史与收藏相关方法 ====================
	private void addToHistory(String url) {
		if (url == null || url.isEmpty()) return;
		historyList.remove(url);
		historyList.add(0, url);
		if (historyList.size() > 100) {
			historyList.remove(historyList.size() - 1);
		}
		saveHistory();
	}
	
	private void saveHistory() {
		StringBuilder sb = new StringBuilder();
		for (String url : historyList) {
			if (sb.length() > 0) sb.append(",");
			sb.append(url.replace(",", "%2C"));
		}
		prefs.edit().putString(PREF_HISTORY, sb.toString()).apply();
	}
	
	private void loadHistoryAndFavorites() {
		String historyStr = prefs.getString(PREF_HISTORY, "");
		if (!historyStr.isEmpty()) {
			String[] parts = historyStr.split(",");
			for (String part : parts) {
				historyList.add(part.replace("%2C", ","));
			}
		}
		favoritesSet = prefs.getStringSet(PREF_FAVORITES, new HashSet<>());
	}
	
	private void saveFavorites() {
		prefs.edit().putStringSet(PREF_FAVORITES, favoritesSet).apply();
	}
	
	private boolean isCurrentUrlFavorite() {
		return currentTab != null && currentTab.url != null && favoritesSet.contains(currentTab.url);
	}
	
	private void toggleFavorite() {
		if (currentTab == null || currentTab.url == null || currentTab.url.isEmpty()) {
			Toast.makeText(this, "没有有效页面地址", Toast.LENGTH_SHORT).show();
			return;
		}
		if (isCurrentUrlFavorite()) {
			favoritesSet.remove(currentTab.url);
			Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
			} else {
			favoritesSet.add(currentTab.url);
			Toast.makeText(this, "已添加到收藏", Toast.LENGTH_SHORT).show();
		}
		saveFavorites();
	}
	
	private void showHistoryDialog() {
		if (historyList.isEmpty()) {
			Toast.makeText(this, "暂无浏览历史", Toast.LENGTH_SHORT).show();
			return;
		}
		Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackground(createBg(mainBgColor, 20));
		mainLayout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(this);
		title.setText("浏览历史");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(20);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		mainLayout.addView(title);
		
		ScrollView scrollView = new ScrollView(this);
		LinearLayout listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		
		for (String url : historyList) {
			TextView itemView = new TextView(this);
			itemView.setText(url);
			itemView.setTextColor(TEXT_WHITE);
			itemView.setTextSize(14);
			itemView.setPadding(dp(12), dp(12), dp(12), dp(12));
			itemView.setBackground(createBg(0x44000000, 8));
			itemView.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.bottomMargin = dp(8);
			itemView.setLayoutParams(params);
			itemView.setOnClickListener(v -> {
				if (currentTab != null && currentTab.webView != null) {
					currentTab.webView.loadUrl(url);
				}
				dialog.dismiss();
			});
			itemView.setOnLongClickListener(v -> {
				new android.app.AlertDialog.Builder(this)
				.setMessage("确定删除这条历史记录吗？")
				.setPositiveButton("删除", (d, w) -> {
					historyList.remove(url);
					saveHistory();
					dialog.dismiss();
					showHistoryDialog();
				})
				.setNegativeButton("取消", null)
				.show();
				return true;
			});
			listLayout.addView(itemView);
		}
		scrollView.addView(listLayout);
		LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
		scrollParams.topMargin = dp(16);
		scrollParams.bottomMargin = dp(16);
		scrollView.setLayoutParams(scrollParams);
		mainLayout.addView(scrollView);
		
		Button clearBtn = new Button(this);
		clearBtn.setText("清空全部历史");
		clearBtn.setTextColor(TEXT_WHITE);
		clearBtn.setBackground(createBg(btnBgColor, 12));
		clearBtn.setOnClickListener(v -> {
			new android.app.AlertDialog.Builder(this)
			.setMessage("确定清空所有浏览历史？")
			.setPositiveButton("确定", (d, w) -> {
				historyList.clear();
				saveHistory();
				if (currentTab != null && currentTab.webView != null) {
					currentTab.webView.clearHistory();
				}
				dialog.dismiss();
			})
			.setNegativeButton("取消", null)
			.show();
		});
		mainLayout.addView(clearBtn);
		
		Button closeBtn = new Button(this);
		closeBtn.setText("关闭");
		closeBtn.setTextColor(TEXT_WHITE);
		closeBtn.setBackground(createBg(btnBgColor, 12));
		closeBtn.setOnClickListener(v -> dialog.dismiss());
		mainLayout.addView(closeBtn);
		
		dialog.setContentView(mainLayout);
		Window dialogWindow = dialog.getWindow();
		if (dialogWindow != null) {
			dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			int screenHeight = getResources().getDisplayMetrics().heightPixels;
			dialogWindow.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), (int) (screenHeight * 0.75));
			dialogWindow.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	private void showFavoritesDialog() {
		if (favoritesSet.isEmpty()) {
			Toast.makeText(this, "暂无收藏内容", Toast.LENGTH_SHORT).show();
			return;
		}
		Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackground(createBg(mainBgColor, 20));
		mainLayout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(this);
		title.setText("我的收藏");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(20);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		mainLayout.addView(title);
		
		ScrollView scrollView = new ScrollView(this);
		LinearLayout listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		
		for (String url : favoritesSet) {
			TextView itemView = new TextView(this);
			itemView.setText(url);
			itemView.setTextColor(TEXT_WHITE);
			itemView.setTextSize(14);
			itemView.setPadding(dp(12), dp(12), dp(12), dp(12));
			itemView.setBackground(createBg(0x44000000, 8));
			itemView.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.bottomMargin = dp(8);
			itemView.setLayoutParams(params);
			itemView.setOnClickListener(v -> {
				if (currentTab != null && currentTab.webView != null) {
					currentTab.webView.loadUrl(url);
				}
				dialog.dismiss();
			});
			itemView.setOnLongClickListener(v -> {
				new android.app.AlertDialog.Builder(this)
				.setMessage("确定删除这条收藏吗？")
				.setPositiveButton("删除", (d, w) -> {
					favoritesSet.remove(url);
					saveFavorites();
					dialog.dismiss();
					showFavoritesDialog();
				})
				.setNegativeButton("取消", null)
				.show();
				return true;
			});
			listLayout.addView(itemView);
		}
		scrollView.addView(listLayout);
		LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
		scrollParams.topMargin = dp(16);
		scrollParams.bottomMargin = dp(16);
		scrollView.setLayoutParams(scrollParams);
		mainLayout.addView(scrollView);
		
		Button closeBtn = new Button(this);
		closeBtn.setText("关闭");
		closeBtn.setTextColor(TEXT_WHITE);
		closeBtn.setBackground(createBg(btnBgColor, 12));
		closeBtn.setOnClickListener(v -> dialog.dismiss());
		mainLayout.addView(closeBtn);
		
		dialog.setContentView(mainLayout);
		Window dialogWindow = dialog.getWindow();
		if (dialogWindow != null) {
			dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			int screenHeight = getResources().getDisplayMetrics().heightPixels;
			dialogWindow.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), (int) (screenHeight * 0.75));
			dialogWindow.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	// ==================== UA切换相关方法 ====================
	private void showUaSelectorDialog() {
		final String[] uaNames = {
			"默认移动端",
			"Windows Chrome",
			"iPhone Safari",
			"iPad Safari",
			"Android平板",
			"自定义UA"
		};
		final String[] uaValues = {
			"",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
			"Mozilla/5.0 (iPhone; CPU iPhone OS 17_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3 Mobile/15E148 Safari/604.1",
			"Mozilla/5.0 (iPad; CPU OS 17_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3 Safari/605.1.15",
			"Mozilla/5.0 (Linux; Android 14; SM-T970) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
			"custom"
		};
		
		new android.app.AlertDialog.Builder(this)
		.setTitle("选择浏览器标识")
		.setItems(uaNames, (dialog, which) -> {
			if (which == 5) {
				showCustomUaDialog();
				} else {
				String selectedUa = uaValues[which];
				prefs.edit().putString(PREF_USER_AGENT, selectedUa).apply();
				for (TabInfo tab : tabs) {
					if (tab.webView != null) {
						WebSettings settings = tab.webView.getSettings();
						if (selectedUa.isEmpty()) {
							settings.setUserAgentString(WebSettings.getDefaultUserAgent(getApplicationContext()) + " SilongApp/7.0");
							} else {
							settings.setUserAgentString(selectedUa);
						}
					}
				}
				if (currentTab != null && currentTab.webView != null) {
					currentTab.webView.reload();
				}
				Toast.makeText(this, "UA已切换，页面将刷新", Toast.LENGTH_SHORT).show();
			}
		})
		.show();
	}
	
	private void showCustomUaDialog() {
		Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 20));
		layout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(this);
		title.setText("自定义 User-Agent");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(20);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		layout.addView(title);
		
		EditText uaInput = new EditText(this);
		uaInput.setHint("请输入自定义UA字符串");
		String currentUa = prefs.getString(PREF_USER_AGENT, "");
		uaInput.setText(currentUa);
		uaInput.setTextColor(TEXT_WHITE);
		uaInput.setHintTextColor(TEXT_DISABLE);
		uaInput.setBackground(createBg(btnBgColor, 8));
		uaInput.setPadding(dp(12), dp(10), dp(12), dp(10));
		uaInput.setMinLines(3);
		uaInput.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		inputParams.topMargin = dp(16);
		inputParams.bottomMargin = dp(16);
		uaInput.setLayoutParams(inputParams);
		layout.addView(uaInput);
		
		Button confirmBtn = new Button(this);
		confirmBtn.setText("确认");
		confirmBtn.setTextColor(TEXT_WHITE);
		confirmBtn.setBackground(createBg(btnBgColor, 12));
		confirmBtn.setOnClickListener(v -> {
			String customUa = uaInput.getText().toString().trim();
			prefs.edit().putString(PREF_USER_AGENT, customUa).apply();
			for (TabInfo tab : tabs) {
				if (tab.webView != null) {
					tab.webView.getSettings().setUserAgentString(customUa);
				}
			}
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.reload();
			}
			dialog.dismiss();
			Toast.makeText(this, "自定义UA已保存", Toast.LENGTH_SHORT).show();
		});
		layout.addView(confirmBtn);
		
		Button resetBtn = new Button(this);
		resetBtn.setText("恢复默认");
		resetBtn.setTextColor(TEXT_WHITE);
		resetBtn.setBackground(createBg(btnBgColor, 12));
		resetBtn.setOnClickListener(v -> {
			uaInput.setText("");
			prefs.edit().putString(PREF_USER_AGENT, "").apply();
			for (TabInfo tab : tabs) {
				if (tab.webView != null) {
					WebSettings settings = tab.webView.getSettings();
					settings.setUserAgentString(WebSettings.getDefaultUserAgent(getApplicationContext()) + " SilongApp/7.0");
				}
			}
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.reload();
			}
			dialog.dismiss();
			Toast.makeText(this, "已恢复默认UA", Toast.LENGTH_SHORT).show();
		});
		layout.addView(resetBtn);
		
		dialog.setContentView(layout);
		Window dialogWindow = dialog.getWindow();
		if (dialogWindow != null) {
			dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialogWindow.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
			dialogWindow.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	// ==================== 页面操作相关方法 ====================
	private void fetchAndShowSource() {
		if (currentTab == null || currentTab.webView == null) return;
		String url = currentTab.url;
		if (url != null && (url.startsWith("http://localhost") || url.startsWith("http://127.0.0.1") || (actualServerPort > 0 && url.contains(":" + actualServerPort)))) {
			new Thread(() -> {
				HttpURLConnection conn = null;
				try {
					conn = (HttpURLConnection) new URL(url).openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();
					final int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
						StringBuilder sb = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							sb.append(line).append("\n");
						}
						reader.close();
						final String source = sb.toString();
						runOnUiThread(() -> showSourceView(source));
						} else {
						runOnUiThread(() -> Toast.makeText(MainActivity.this, "获取源代码失败，HTTP错误：" + responseCode, Toast.LENGTH_SHORT).show());
					}
					} catch (Exception e) {
					runOnUiThread(() -> Toast.makeText(MainActivity.this, "获取源代码失败：" + e.getMessage(), Toast.LENGTH_SHORT).show());
					e.printStackTrace();
					} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}
			}).start();
			return;
		}
		currentTab.webView.evaluateJavascript(
		"(function() { return document.documentElement.outerHTML; })();",
		value -> {
			if (value == null || value.isEmpty()) {
				Toast.makeText(MainActivity.this, "获取源代码失败", Toast.LENGTH_SHORT).show();
				return;
			}
			String source = value;
			if (source.startsWith("\"") && source.endsWith("\"")) {
				source = source.substring(1, source.length() - 1);
				source = source.replace("\\\"", "\"");
				source = source.replace("\\\\", "\\");
				source = source.replace("\\n", "\n");
				source = source.replace("\\t", "\t");
			}
			showSourceView(source);
		}
		);
	}
	
	private void showSourceView(String source) {
		if (mSourceTextView != null) {
			mSourceTextView.setText(source);
		}
		if (currentTab != null && currentTab.webView != null) {
			currentTab.webView.setVisibility(View.GONE);
		}
		if (mSourceScrollView != null) {
			mSourceScrollView.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideSourceView() {
		if (mSourceScrollView != null) {
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.setVisibility(View.VISIBLE);
			}
			mSourceScrollView.setVisibility(View.GONE);
		}
	}
	
	private void toggleSourceWrap() {
		if (mSourceTextView != null) {
			mSourceWrapEnabled = !mSourceWrapEnabled;
			mSourceTextView.setHorizontallyScrolling(!mSourceWrapEnabled);
			mSourceWrapButton.setText(mSourceWrapEnabled ? "↵ 不换行" : "↵ 换行");
		}
	}
	
	private void openInExternalBrowser() {
		if (currentTab == null || currentTab.url == null || currentTab.url.isEmpty()) {
			Toast.makeText(this, "没有有效页面地址", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentTab.url));
			startActivity(intent);
			} catch (Exception e) {
			Toast.makeText(this, "无法打开外部浏览器", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void shareCurrentUrl() {
		if (currentTab == null || currentTab.url == null || currentTab.url.isEmpty()) {
			Toast.makeText(this, "没有有效页面地址", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, currentTab.url);
			shareIntent.putExtra(Intent.EXTRA_TITLE, currentTab.title);
			startActivity(Intent.createChooser(shareIntent, "分享链接"));
			} catch (Exception e) {
			Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	// ==================== 公告相关方法 ====================
	private void setupAnnouncementCheck() {
		new Thread(this::checkAnnouncementFromServer).start();
	}
	
	private void checkAnnouncementFromServer() {
		try {
			URL url = new URL(ANNOUNCEMENT_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			conn.connect();
			
			if (conn.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
				String newAnnouncement = sb.toString();
				
				String lastSaved = prefs.getString(PREF_LAST_ANNOUNCEMENT, "");
				String lastShownHash = prefs.getString(PREF_ANNOUNCEMENT_SHOWN, "");
				String newHash = String.valueOf(newAnnouncement.hashCode());
				
				prefs.edit().putString(PREF_LAST_ANNOUNCEMENT, newAnnouncement).apply();
				
				if (!newHash.equals(lastShownHash) && !newAnnouncement.isEmpty()) {
					runOnUiThread(this::showAnnouncementTipDialog);
				}
			}
			conn.disconnect();
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showAnnouncementTipDialog() {
		Dialog dialog = new Dialog(MainActivity.this);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		
		LinearLayout layout = new LinearLayout(MainActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 20));
		layout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(MainActivity.this);
		title.setText("有新公告");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(22);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		layout.addView(title);
		
		TextView tip = new TextView(MainActivity.this);
		tip.setText("发现新公告，点击查看详情");
		tip.setTextColor(TEXT_GRAY);
		tip.setTextSize(14);
		tip.setPadding(0, dp(16), 0, dp(20));
		tip.setGravity(Gravity.CENTER);
		layout.addView(tip);
		
		Button viewBtn = new Button(MainActivity.this);
		viewBtn.setText("打开查看公告");
		viewBtn.setTextColor(TEXT_WHITE);
		viewBtn.setBackground(createBg(btnBgColor, 12));
		viewBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showAnnouncementContentDialog();
			String currentAnnouncement = prefs.getString(PREF_LAST_ANNOUNCEMENT, "");
			prefs.edit().putString(PREF_ANNOUNCEMENT_SHOWN, String.valueOf(currentAnnouncement.hashCode())).apply();
		});
		layout.addView(viewBtn);
		
		Button cancelBtn = new Button(MainActivity.this);
		cancelBtn.setText("稍后查看");
		cancelBtn.setTextColor(TEXT_WHITE);
		cancelBtn.setBackground(createBg(btnBgColor, 12));
		cancelBtn.setOnClickListener(v -> {
			dialog.dismiss();
			String currentAnnouncement = prefs.getString(PREF_LAST_ANNOUNCEMENT, "");
			prefs.edit().putString(PREF_ANNOUNCEMENT_SHOWN, String.valueOf(currentAnnouncement.hashCode())).apply();
		});
		layout.addView(cancelBtn);
		
		dialog.setContentView(layout);
		Window window = dialog.getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	private void showAnnouncementContentDialog() {
		String announcementHtml = prefs.getString(PREF_LAST_ANNOUNCEMENT, "");
		if (announcementHtml.isEmpty()) {
			Toast.makeText(this, "暂无公告", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundColor(appBgColor);
		mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		LinearLayout topBar = new LinearLayout(this);
		topBar.setOrientation(LinearLayout.HORIZONTAL);
		topBar.setGravity(Gravity.CENTER_VERTICAL);
		topBar.setBackground(createBg(mainBgColor, 0));
		topBar.setPadding(dp(16), dp(12), dp(16), dp(12));
		
		Button backBtn = new Button(this);
		backBtn.setText("← 返回");
		backBtn.setTextColor(TEXT_WHITE);
		backBtn.setBackgroundColor(Color.TRANSPARENT);
		backBtn.setOnClickListener(v -> dialog.dismiss());
		topBar.addView(backBtn);
		
		TextView title = new TextView(this);
		title.setText("公告详情");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(18);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		title.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		topBar.addView(title);
		
		mainLayout.addView(topBar);
		
		WebView announcementWebView = new WebView(this);
		announcementWebView.setLayoutParams(new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
		WebSettings settings = announcementWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		announcementWebView.loadDataWithBaseURL(
		"https://silong-respool.github.io/gay/",
		announcementHtml,
		"text/html",
		"UTF-8",
		null);
		
		dialog.setOnDismissListener(d -> {
			if (announcementWebView != null) {
				announcementWebView.stopLoading();
				announcementWebView.removeAllViews();
				announcementWebView.destroy();
			}
		});
		
		mainLayout.addView(announcementWebView);
		
		dialog.setContentView(mainLayout);
		Window window = dialog.getWindow();
		if (window != null) {
			window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			window.setGravity(Gravity.CENTER);
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		}
		dialog.show();
	}
	
	// ==================== 恢复默认设置 ====================
	private void resetAllSettings() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.putBoolean(PREF_NIGHT_MODE, false);
		editor.putInt(PREF_FONT_SIZE, 100);
		editor.putString(PREF_USER_AGENT, "");
		editor.putBoolean(PREF_IMAGE_LOADING, true);
		editor.putBoolean(PREF_HARDWARE_ACCEL, true);
		editor.putInt(PREF_RENDER_PRIORITY, 0);
		editor.putBoolean(PREF_ANTI_FLICKER, true);
		editor.putBoolean(PREF_FIXED_BG, true);
		editor.putString(PREF_BG_COLOR, "#0B0018");
		editor.putBoolean(PREF_DISABLE_WINDOW_ANIM, true);
		editor.putInt(PREF_CACHE_SIZE_MB, 40);
		editor.putBoolean(PREF_ALLOW_CROSS_DOMAIN, true);
		editor.putBoolean(PREF_PAUSE_BG_RENDER, true);
		editor.putBoolean(PREF_DISABLE_LONGPRESS, true);
		editor.putBoolean(PREF_SAFE_BROWSING, false);
		editor.putBoolean(PREF_MALI_OPTIMIZATION, isMaliDevice);
		editor.putBoolean(PREF_WEBGL_FORCE, true);
		editor.putBoolean(PREF_PIXEL_FORMAT, true);
		editor.putBoolean(PREF_VSYNC_ENABLE, true);
		editor.putInt(PREF_FRAME_SKIP, 30);
		editor.putInt(PREF_RENDER_BUFFER, 2);
		editor.remove(PREF_THEME_COLOR);
		editor.remove(PREF_THEME_ALPHA);
		editor.remove(PREF_DEFAULT_HOME_URL);
		editor.remove(PREF_AD_BLOCK_RULES);
		editor.remove(PREF_AD_BLOCK_IMPORTED_SCRIPT);
		editor.putBoolean(PREF_AD_AUTO_BLOCK_ENABLED, false);
		editor.putBoolean(PREF_AD_PREVENT_JUMP, true);
		editor.putBoolean(PREF_DISABLE_GYRO, false);
		editor.putFloat(PREF_TTS_SPEECH_RATE, 1.0f);
		editor.putFloat(PREF_TTS_PITCH, 1.0f);
		editor.putFloat(PREF_TTS_WATERLINE_Y, 65f);
		editor.putFloat("tts_waterline_end", 90f);
		editor.remove("tts_voice_name");
		editor.putBoolean(PREF_TTS_USE_LINE_MODE, false);
		editor.putInt(PREF_TTS_LINE_START, 1);
		editor.putInt(PREF_TTS_LINE_END, -1);
		editor.apply();
		
		loadThemeColors();
		adBlockRules.clear();
		importedAdBlockScript = "";
		ttsSpeechRate = 1.0f;
		ttsPitch = 1.0f;
		ttsWaterlinePercent = 65f;
		ttsUseLineMode = false;
		ttsLineStart = 1;
		ttsLineEnd = -1;
		adAutoBlockEnabled = false;
		adPreventJumpEnabled = true;
		disableGyro = false;
		prefs.edit().putBoolean(PREF_AD_AUTO_BLOCK_ENABLED, false).apply();
		prefs.edit().putBoolean(PREF_AD_PREVENT_JUMP, true).apply();
		prefs.edit().putBoolean(PREF_DISABLE_GYRO, false).apply();
		enableGyroscope();
		
		for (TabInfo tab : tabs) {
			if (tab.webView != null) {
				applyWebViewSettings();
				applyRenderSettingsToWebView();
			}
		}
		if (prefs.getBoolean(PREF_NIGHT_MODE, false)) {
			injectNightMode();
			} else {
			removeNightMode();
		}
		if (isMaliDevice && prefs.getBoolean(PREF_MALI_OPTIMIZATION, true) && currentTab != null && !isLocalResource(currentTab.url)) {
			applyMaliOptimizations();
			injectAntiFlickerCSS();
		}
		applyOrientation();
		
		Toast.makeText(this, "已恢复默认设置，应用将重启", Toast.LENGTH_SHORT).show();
		recreate();
	}
	
	// ==================== 导航栏与页面控制方法 ====================
	private void hideNavBar() {
		if (navBar == null || isNavBarHidden || isShowingErrorPage) return;
		navBar.setVisibility(View.GONE);
		isNavBarHidden = true;
		Toast.makeText(this, "全屏模式已开启，底部上滑可恢复导航栏", Toast.LENGTH_SHORT).show();
	}
	
	private void showNavBar() {
		if (navBar == null || !isNavBarHidden) return;
		navBar.setVisibility(View.VISIBLE);
		isNavBarHidden = false;
		if (!isShowingErrorPage) {
			setImmersiveMode();
		}
	}
	
	private void showCustomError(String errorMsg) {
		isShowingErrorPage = true;
		
		if (navBar != null && navBar.getVisibility() != View.VISIBLE) {
			navBar.setVisibility(View.VISIBLE);
			isNavBarHidden = false;
		}
		
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(0);
		
		if (customErrorView != null) {
			TextView errorText = customErrorView.findViewById(android.R.id.text2);
			if (errorText != null) {
				errorText.setText(errorMsg);
			}
			customErrorView.setVisibility(View.VISIBLE);
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.setVisibility(View.GONE);
			}
			return;
		}
		
		FrameLayout errorLayout = new FrameLayout(this);
		errorLayout.setBackgroundColor(appBgColor);
		errorLayout.setLayoutParams(new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setOrientation(LinearLayout.VERTICAL);
		contentLayout.setGravity(Gravity.CENTER);
		contentLayout.setPadding(dp(40), dp(20), dp(40), dp(20));
		
		TextView titleView = new TextView(this);
		titleView.setId(android.R.id.text1);
		titleView.setText("加载失败，请检查网络或使用加速器");
		titleView.setTextColor(TEXT_WHITE);
		titleView.setTextSize(24);
		titleView.setTypeface(null, Typeface.BOLD);
		titleView.setGravity(Gravity.CENTER);
		contentLayout.addView(titleView);
		
		TextView descView = new TextView(this);
		descView.setId(android.R.id.text2);
		descView.setText(errorMsg);
		descView.setTextColor(TEXT_GRAY);
		descView.setTextSize(14);
		descView.setGravity(Gravity.CENTER);
		descView.setLineSpacing(dp(4), 1f);
		LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		descParams.topMargin = dp(12);
		descParams.bottomMargin = dp(30);
		descView.setLayoutParams(descParams);
		contentLayout.addView(descView);
		
		Button retryBtn = new Button(this);
		retryBtn.setText("重新加载");
		retryBtn.setTextColor(TEXT_WHITE);
		retryBtn.setBackground(createBg(btnBgColor, 25));
		retryBtn.setPadding(dp(30), dp(12), dp(30), dp(12));
		retryBtn.setAllCaps(false);
		retryBtn.setOnClickListener(v -> {
			hideCustomError();
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.reload();
			}
		});
		contentLayout.addView(retryBtn);
		
		Button homeBtn = new Button(this);
		homeBtn.setText("返回首页");
		homeBtn.setTextColor(TEXT_WHITE);
		homeBtn.setBackgroundColor(Color.TRANSPARENT);
		homeBtn.setPadding(dp(30), dp(12), dp(30), dp(12));
		homeBtn.setAllCaps(false);
		homeBtn.setOnClickListener(v -> {
			hideCustomError();
			String homeUrl = getCurrentHomeUrl();
			if (currentTab != null && currentTab.webView != null) {
				currentTab.webView.loadUrl(homeUrl);
			}
		});
		LinearLayout.LayoutParams homeParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		homeParams.topMargin = dp(16);
		homeBtn.setLayoutParams(homeParams);
		contentLayout.addView(homeBtn);
		
		errorLayout.addView(contentLayout);
		mContentFrame.addView(errorLayout);
		customErrorView = errorLayout;
		
		if (currentTab != null && currentTab.webView != null) {
			currentTab.webView.setVisibility(View.GONE);
		}
	}
	
	private void hideCustomError() {
		if (customErrorView != null) {
			customErrorView.setVisibility(View.GONE);
		}
		if (currentTab != null && currentTab.webView != null) {
			currentTab.webView.setVisibility(View.VISIBLE);
		}
		isShowingErrorPage = false;
		setImmersiveMode();
	}
	
	// ==================== 网络监听相关 ====================
	private void registerNetworkReceiver() {
		networkReceiver = new NetworkChangeReceiver();
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkReceiver, filter);
	}
	
	private class NetworkChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
			
			if (isConnected && !isNetworkAvailable) {
				isNetworkAvailable = true;
				if (currentTab != null && currentTab.webView != null) {
					currentTab.webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
				}
				Toast.makeText(MainActivity.this, "网络已恢复", Toast.LENGTH_SHORT).show();
				if (isPageLoadError) {
					hideCustomError();
					if (currentTab != null && currentTab.webView != null) {
						currentTab.webView.reload();
					}
				}
				} else if (!isConnected && isNetworkAvailable) {
				isNetworkAvailable = false;
				if (currentTab != null && currentTab.webView != null) {
					currentTab.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				}
				Toast.makeText(MainActivity.this, "网络已断开", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	// ==================== 工具方法 ====================
	private int dp(int dpValue) {
		return (int) TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		dpValue,
		getResources().getDisplayMetrics());
	}
	
	private float parseFloat(String s, float defaultVal) {
		try {
			return Float.parseFloat(s.trim());
			} catch (Exception e) {
			return defaultVal;
		}
	}
	
	// ==================== WebView核心配置 ====================
	private void initWebView(WebView webView) {
		if (webView == null) return;
		WebSettings settings = webView.getSettings();
		
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDomStorageEnabled(true);
		settings.setDatabaseEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setAllowContentAccess(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		settings.setSupportZoom(true);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setAppCacheEnabled(true);
		settings.setAppCacheMaxSize(Long.MAX_VALUE);
		settings.setCacheMode(isNetworkAvailable ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK);
		
		// 防止闪烁和性能优化
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			settings.setMediaPlaybackRequiresUserGesture(false);
		}
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
			settings.setAllowFileAccessFromFileURLs(true);
			settings.setAllowUniversalAccessFromFileURLs(true);
		}
		
		String defaultUA = settings.getUserAgentString();
		String customUA = prefs.getString(PREF_USER_AGENT, "");
		if (!customUA.isEmpty()) {
			settings.setUserAgentString(customUA);
			} else {
			settings.setUserAgentString(defaultUA + " SilongApp/7.0");
		}
		
		boolean loadImages = prefs.getBoolean(PREF_IMAGE_LOADING, true);
		settings.setBlockNetworkImage(!loadImages);
		
		// 性能优化 - 防止闪烁
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			settings.setOffscreenPreRaster(false);  // 防止闪烁
		}
		settings.setEnableSmoothTransition(true);
		settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
		
		// 硬件加速配置 - 防止闪烁
		webView.setLayerType(View.LAYER_TYPE_NONE, null);  // 默认不使用硬件加速，防止闪烁
		if (isMaliDevice && prefs.getBoolean(PREF_MALI_OPTIMIZATION, true)) {
			webView.setDrawingCacheEnabled(true);
			webView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		}
		
		webView.setKeepScreenOn(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		// 设置背景透明防止闪烁
		webView.setBackgroundColor(Color.TRANSPARENT);
	}
	
	private void applyWebViewSettings() {
		for (TabInfo tab : tabs) {
			if (tab.webView != null) {
				WebSettings settings = tab.webView.getSettings();
				settings.setTextZoom(prefs.getInt(PREF_FONT_SIZE, 100));
				boolean loadImages = prefs.getBoolean(PREF_IMAGE_LOADING, true);
				settings.setBlockNetworkImage(!loadImages);
			}
		}
	}
	
	private void applyRenderSettingsToWebView() {
		// 扩展用
	}
	
	// ==================== 标签页管理 ====================
	private void switchToTab(TabInfo tab) {
		if (tab == null || tab.webView == null) return;
		
		if (currentTab != null && currentTab.webView != null) {
			currentTab.webView.setVisibility(View.GONE);
		}
		
		currentTab = tab;
		currentTab.webView.setVisibility(View.VISIBLE);
		mContentFrame.bringChildToFront(currentTab.webView);
		progressBar.setVisibility(View.GONE);
		
		// 本地页面：清除残留注入样式后直接返回
		if (isLocalResource(currentTab.url)) {
			currentTab.webView.evaluateJavascript(
			"(function(){" +
			"var styles = document.querySelectorAll('style[id*=\"silong\"], style[id*=\"night\"], style[id*=\"anti-flicker\"]');" +
			"for(var i=0;i<styles.length;i++) styles[i].remove();" +
			"})();", null);
			return;
		}
		
		// 远程页面：正常注入
		if (!isLocalResource(currentTab.url)) {
			if (prefs.getBoolean(PREF_NIGHT_MODE, false)) {
				injectNightMode();
			}
			injectAntiFlickerCSS();
			injectAdBlockRules();
			if (isMaliDevice && prefs.getBoolean(PREF_MALI_OPTIMIZATION, true)) {
				applyMaliOptimizations();
			}
		}
	}
	
	private void closeTab(TabInfo tab) {
		if (tabs.size() <= 1) {
			Toast.makeText(this, "至少保留一个标签页", Toast.LENGTH_SHORT).show();
			return;
		}
		
		final int index = tabs.indexOf(tab);
		if (index < 0) return;
		
		tabs.remove(tab);
		if (tab.webView != null) {
			mContentFrame.removeView(tab.webView);
			tab.webView.stopLoading();
			tab.webView.setWebViewClient(null);
			tab.webView.setWebChromeClient(null);
			tab.webView.removeJavascriptInterface("Android");
			tab.webView.destroy();
		}
		
		if (tab == activeMusicTab) {
			activeMusicTab = null;
			musicPlaying = false;
			hideMusicNotification();
		}
		
		if (currentTab == tab) {
			currentTab = tabs.get(Math.min(index, tabs.size() - 1));
			switchToTab(currentTab);
		}
		
		saveTabsToPrefs();
	}
	
	private void saveTabsToPrefs() {
		StringBuilder sb = new StringBuilder();
		for (TabInfo t : tabs) {
			if (sb.length() > 0) sb.append("|||");
			sb.append(t.url != null ? t.url : "").append("###").append(t.isDefaultHome ? "1" : "0");
		}
		prefs.edit().putString(PREF_TABS_DATA, sb.toString()).apply();
	}
	
	private void loadTabsFromPrefs() {
		String data = prefs.getString(PREF_TABS_DATA, "");
		if (data.isEmpty()) return;
		
		tabs.clear();
		String[] tabData = data.split("\\|\\|\\|");
		for (String s : tabData) {
			String[] parts = s.split("###");
			if (parts.length < 2) continue;
			String url = parts[0];
			boolean isDefault = "1".equals(parts[1]);
			
			TabInfo tab = new TabInfo(url);
			tab.webView = createWebView(url);
			tab.webView.setVisibility(View.GONE);
			tab.webView.addJavascriptInterface(new JavaScriptInterface(this, tab.id), "Android");
			tab.webView.setWebViewClient(new CustomWebViewClient(tab));
			tab.webView.setWebChromeClient(new CustomWebChromeClient(tab));
			tab.isDefaultHome = isDefault;
			tab.webView.loadUrl(url);
			tabs.add(tab);
			mContentFrame.addView(tab.webView);
		}
	}
	
	private void showTabManager() {
		Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackground(createBg(mainBgColor, 20));
		mainLayout.setPadding(dp(20), dp(20), dp(20), dp(20));
		
		TextView titleView = new TextView(this);
		titleView.setText("标签页管理");
		titleView.setTextColor(TEXT_WHITE);
		titleView.setTextSize(18);
		titleView.setTypeface(null, Typeface.BOLD);
		titleView.setGravity(Gravity.CENTER);
		titleView.setPadding(0, 0, 0, dp(16));
		mainLayout.addView(titleView);
		
		ScrollView scrollView = new ScrollView(this);
		LinearLayout tabListLayout = new LinearLayout(this);
		tabListLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(tabListLayout);
		
		for (int i = 0; i < tabs.size(); i++) {
			TabInfo tab = tabs.get(i);
			
			LinearLayout tabRow = new LinearLayout(this);
			tabRow.setOrientation(LinearLayout.HORIZONTAL);
			tabRow.setGravity(Gravity.CENTER_VERTICAL);
			tabRow.setPadding(dp(12), dp(14), dp(12), dp(14));
			tabRow.setBackground(createBg(0x44000000, 8));
			LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			rowParams.bottomMargin = dp(12);
			tabRow.setLayoutParams(rowParams);
			
			TextView tabTitle = new TextView(this);
			String displayTitle = tab.title != null && !tab.title.isEmpty() ? tab.title : tab.url;
			if (displayTitle.length() > 25) {
				displayTitle = displayTitle.substring(0, 25) + "...";
			}
			tabTitle.setText(displayTitle);
			tabTitle.setTextColor(tab == currentTab ? accentColor : TEXT_WHITE);
			tabTitle.setTextSize(13);
			tabTitle.setMaxLines(1);
			tabTitle.setEllipsize(TextUtils.TruncateAt.END);
			tabTitle.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
			tabTitle.setPadding(0, 0, dp(8), 0);
			tabRow.addView(tabTitle);
			
			CheckBox defaultCheck = new CheckBox(this);
			defaultCheck.setChecked(tab.isDefaultHome);
			defaultCheck.setButtonTintList(ColorStateList.valueOf(accentColor));
			defaultCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
				tab.isDefaultHome = isChecked;
				if (isChecked) {
					for (TabInfo t : tabs) {
						if (t != tab) t.isDefaultHome = false;
					}
					prefs.edit().putString(PREF_DEFAULT_HOME_URL, tab.url).apply();
					} else {
					prefs.edit().remove(PREF_DEFAULT_HOME_URL).apply();
				}
				saveTabsToPrefs();
			});
			tabRow.addView(defaultCheck);
			
			Button closeBtn = new Button(this);
			closeBtn.setText("×");
			closeBtn.setTextColor(TEXT_WHITE);
			closeBtn.setTextSize(16);
			closeBtn.setBackgroundColor(Color.TRANSPARENT);
			closeBtn.setPadding(dp(8), dp(4), dp(8), dp(4));
			closeBtn.setOnClickListener(v -> {
				closeTab(tab);
				dialog.dismiss();
				showTabManager();
			});
			tabRow.addView(closeBtn);
			
			tabRow.setOnClickListener(v -> {
				switchToTab(tab);
				dialog.dismiss();
			});
			
			tabListLayout.addView(tabRow);
		}
		
		LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
		scrollParams.bottomMargin = dp(20);
		scrollView.setLayoutParams(scrollParams);
		mainLayout.addView(scrollView);
		
		LinearLayout tabBtnRow1 = new LinearLayout(this);
		tabBtnRow1.setOrientation(LinearLayout.HORIZONTAL);
		tabBtnRow1.setWeightSum(2);
		tabBtnRow1.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams btnRow1Params = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		btnRow1Params.bottomMargin = dp(12);
		tabBtnRow1.setLayoutParams(btnRow1Params);
		
		Button addAndHomeBtn = new Button(this);
		addAndHomeBtn.setText("➕ 添加当前页并返回首页");
		addAndHomeBtn.setTextColor(TEXT_WHITE);
		addAndHomeBtn.setBackground(createBg(btnBgColor, 12));
		addAndHomeBtn.setAllCaps(false);
		addAndHomeBtn.setPadding(0, dp(14), 0, dp(14));
		addAndHomeBtn.setOnClickListener(v -> {
			if (currentTab != null && currentTab.url != null && !currentTab.url.isEmpty()) {
				String currentUrl = currentTab.url;
				TabInfo newTab = new TabInfo(currentUrl);
				newTab.webView = createWebView(currentUrl);
				newTab.webView.setVisibility(View.GONE);
				newTab.webView.addJavascriptInterface(new JavaScriptInterface(this, newTab.id), "Android");
				newTab.webView.setWebViewClient(new CustomWebViewClient(newTab));
				newTab.webView.setWebChromeClient(new CustomWebChromeClient(newTab));
				tabs.add(newTab);
				mContentFrame.addView(newTab.webView);
				newTab.webView.loadUrl(currentUrl);
				saveTabsToPrefs();
				String homeUrl = getCurrentHomeUrl();
				TabInfo homeTab = null;
				for (TabInfo t : tabs) {
					if (t.url != null && t.url.equals(homeUrl)) {
						homeTab = t;
						break;
					}
				}
				if (homeTab == null) {
					homeTab = new TabInfo(homeUrl);
					homeTab.webView = createWebView(homeUrl);
					homeTab.webView.setVisibility(View.GONE);
					homeTab.webView.addJavascriptInterface(new JavaScriptInterface(this, homeTab.id), "Android");
					homeTab.webView.setWebViewClient(new CustomWebViewClient(homeTab));
					homeTab.webView.setWebChromeClient(new CustomWebChromeClient(homeTab));
					tabs.add(homeTab);
					mContentFrame.addView(homeTab.webView);
					homeTab.webView.loadUrl(homeUrl);
				}
				switchToTab(homeTab);
				saveTabsToPrefs();
			}
			dialog.dismiss();
		});
		tabBtnRow1.addView(addAndHomeBtn, new LinearLayout.LayoutParams(0, dp(48), 1));
		
		Button newTabBtn = new Button(this);
		newTabBtn.setText("➕ 新建空白标签页");
		newTabBtn.setTextColor(TEXT_WHITE);
		newTabBtn.setBackground(createBg(btnBgColor, 12));
		newTabBtn.setAllCaps(false);
		newTabBtn.setPadding(0, dp(14), 0, dp(14));
		newTabBtn.setOnClickListener(v -> {
			dialog.dismiss();
			showNewTabDialog();
		});
		tabBtnRow1.addView(newTabBtn, new LinearLayout.LayoutParams(0, dp(48), 1));
		mainLayout.addView(tabBtnRow1);
		
		Button closeBtn = new Button(this);
		closeBtn.setText("关闭");
		closeBtn.setTextColor(TEXT_WHITE);
		closeBtn.setBackground(createBg(btnBgColor, 12));
		closeBtn.setPadding(0, dp(14), 0, dp(14));
		closeBtn.setOnClickListener(v -> dialog.dismiss());
		LinearLayout.LayoutParams closeBtnParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, dp(48));
		closeBtnParams.topMargin = dp(8);
		closeBtn.setLayoutParams(closeBtnParams);
		mainLayout.addView(closeBtn);
		
		dialog.setContentView(mainLayout);
		Window window = dialog.getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			int screenHeight = getResources().getDisplayMetrics().heightPixels;
			window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9),
			(int) (screenHeight * 0.75));
			window.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	private void showNewTabDialog() {
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 20));
		layout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(this);
		title.setText("新建标签页");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(20);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		title.setPadding(0, 0, 0, dp(24));
		layout.addView(title);
		
		EditText urlInput = new EditText(this);
		urlInput.setHint("输入网址（如：https://www.example.com）");
		urlInput.setTextColor(TEXT_WHITE);
		urlInput.setHintTextColor(TEXT_DISABLE);
		urlInput.setBackground(createBg(btnBgColor, 8));
		urlInput.setPadding(dp(16), dp(16), dp(16), dp(16));
		urlInput.setSingleLine(true);
		LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		inputParams.bottomMargin = dp(20);
		urlInput.setLayoutParams(inputParams);
		layout.addView(urlInput);
		
		CheckBox defaultCheck = new CheckBox(this);
		defaultCheck.setText("设为下次打开的默认首页");
		defaultCheck.setTextColor(TEXT_WHITE);
		defaultCheck.setPadding(0, dp(12), 0, dp(12));
		LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		checkParams.bottomMargin = dp(24);
		defaultCheck.setLayoutParams(checkParams);
		layout.addView(defaultCheck);
		
		Button confirmBtn = new Button(this);
		confirmBtn.setText("打开");
		confirmBtn.setTextColor(TEXT_WHITE);
		confirmBtn.setBackground(createBg(btnBgColor, 12));
		confirmBtn.setPadding(0, dp(14), 0, dp(14));
		confirmBtn.setOnClickListener(v -> {
			String url = urlInput.getText().toString().trim();
			if (url.isEmpty()) {
				Toast.makeText(this, "请输入网址", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file://")) {
				url = "http://" + url;
			}
			
			TabInfo newTab = new TabInfo(url);
			newTab.webView = createWebView(url);
			newTab.webView.setVisibility(View.GONE);
			newTab.webView.addJavascriptInterface(new JavaScriptInterface(this, newTab.id), "Android");
			newTab.webView.setWebViewClient(new CustomWebViewClient(newTab));
			newTab.webView.setWebChromeClient(new CustomWebChromeClient(newTab));
			newTab.isDefaultHome = defaultCheck.isChecked();
			
			if (defaultCheck.isChecked()) {
				for (TabInfo t : tabs) {
					t.isDefaultHome = false;
				}
				prefs.edit().putString(PREF_DEFAULT_HOME_URL, url).apply();
			}
			
			tabs.add(newTab);
			mContentFrame.addView(newTab.webView);
			newTab.webView.loadUrl(url);
			switchToTab(newTab);
			saveTabsToPrefs();
			dialog.dismiss();
		});
		LinearLayout.LayoutParams confirmParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, dp(48));
		confirmParams.bottomMargin = dp(12);
		confirmBtn.setLayoutParams(confirmParams);
		layout.addView(confirmBtn);
		
		Button cancelBtn = new Button(this);
		cancelBtn.setText("取消");
		cancelBtn.setTextColor(TEXT_WHITE);
		cancelBtn.setBackground(createBg(btnBgColor, 12));
		cancelBtn.setPadding(0, dp(14), 0, dp(14));
		cancelBtn.setOnClickListener(v -> dialog.dismiss());
		layout.addView(cancelBtn, new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, dp(48)));
		
		dialog.setContentView(layout);
		Window window = dialog.getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.85),
			ViewGroup.LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	// ==================== WebViewClient 实现（增强跳转拦截）====================
	private class CustomWebViewClient extends WebViewClient {
		private final TabInfo tab;
		
		CustomWebViewClient(TabInfo tab) {
			this.tab = tab;
		}
		
		// 检查是否被屏蔽的通用方法
		private boolean isUrlBlocked(String url) {
			if (url == null) return false;
			// 本地资源永不屏蔽
			if (isLocalResource(url)) return false;
			try {
				URL parsedUrl = new URL(url);
				String host = parsedUrl.getHost();
				if (host != null && blockedWebsites.contains(host)) {
					return true;
				}
				} catch (Exception e) {
				// 忽略解析错误
			}
			return false;
		}
		
		private Map<String, String> getRequestHeaders() {
			Map<String, String> headers = new HashMap<>();
			headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
			return headers;
		}
		
		private boolean handleUrlLoading(WebView view, String url, Map<String, String> headers) {
			if (url.startsWith("tel:") || url.startsWith("mailto:") || url.startsWith("intent:") || url.startsWith("sms:")) {
				try {
					Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
					startActivity(intent);
					} catch (Exception e) {
					Toast.makeText(MainActivity.this, "无法打开此链接", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
			
			// 检查是否在屏蔽列表中
			if (isUrlBlocked(url)) {
				// 完全禁止，不做任何提示
				return true;
			}
			
			if (adPreventJumpEnabled && currentTab.webView != null) {
				currentTab.webView.evaluateJavascript(
				"(function(){ return (Date.now() - (window._lastUserClick||0) > 3000) ? 'block' : 'allow'; })();",
				result -> {
					if ("block".equals(result.replace("\"", ""))) {
						runOnUiThread(() -> {
							Toast.makeText(MainActivity.this, "已拦截非用户触发的跳转", Toast.LENGTH_SHORT).show();
						});
					}
				});
			}
			
			if (headers != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				view.loadUrl(url, headers);
				} else {
				view.loadUrl(url);
			}
			return true;
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (isUrlBlocked(url)) {
				return true; // 完全拦截，不做任何处理
			}
			return handleUrlLoading(view, url, null);
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			if (isUrlBlocked(request.getUrl().toString())) {
				return true; // 完全拦截，不做任何处理
			}
			return handleUrlLoading(view, request.getUrl().toString(), request.getRequestHeaders());
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// 立即检查是否屏蔽，如果是就停止加载
			if (isUrlBlocked(url)) {
				view.stopLoading();
				return;
			}
			
			super.onPageStarted(view, url, favicon);
			tab.url = url;
			if (tab == currentTab) {
				currentUrl = url;
				isPageLoadError = false;
				if (isShowingErrorPage) {
					hideCustomError();
				}
				addToHistory(url);
				progressBar.setVisibility(View.VISIBLE);
			}
			if (adPreventJumpEnabled) {
				view.evaluateJavascript(
				"(function(){ window._lastUserClick = window._lastUserClick || 0; " +
				"document.addEventListener('click', function() { window._lastUserClick = Date.now(); }); })();",
				null);
			}
		}
		
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			if (isLocalResource(url)) {
				return super.shouldInterceptRequest(view, url);
			}
			if (isUrlBlocked(url)) {
				return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream(new byte[0]));
			}
			if (!prefs.getBoolean(PREF_IMAGE_LOADING, true)) {
				String lowerUrl = url.toLowerCase();
				if (lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg") || lowerUrl.endsWith(".png") ||
				lowerUrl.endsWith(".gif") || lowerUrl.endsWith(".bmp") || lowerUrl.endsWith(".webp") ||
				lowerUrl.endsWith(".svg") || lowerUrl.endsWith(".ico") ||
				lowerUrl.contains("/image/") || lowerUrl.contains("/img/")) {
					return new WebResourceResponse("image/png", "UTF-8", new ByteArrayInputStream(new byte[0]));
				}
			}
			if (prefs.getBoolean(PREF_TEXT_MODE, false)) {
				String lowerUrl = url.toLowerCase();
				if (!lowerUrl.endsWith(".html") && !lowerUrl.endsWith(".htm") &&
				!lowerUrl.endsWith(".css") && !lowerUrl.endsWith(".js")) {
					return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream(new byte[0]));
				}
			}
			return super.shouldInterceptRequest(view, url);
		}
		
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
			String url = request.getUrl().toString();
			if (isLocalResource(url)) {
				return super.shouldInterceptRequest(view, request);
			}
			if (isUrlBlocked(url)) {
				return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream(new byte[0]));
			}
			if (!prefs.getBoolean(PREF_IMAGE_LOADING, true)) {
				String lowerUrl = url.toLowerCase();
				if (lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg") || lowerUrl.endsWith(".png") ||
				lowerUrl.endsWith(".gif") || lowerUrl.endsWith(".bmp") || lowerUrl.endsWith(".webp") ||
				lowerUrl.endsWith(".svg") || lowerUrl.endsWith(".ico") ||
				lowerUrl.contains("/image/") || lowerUrl.contains("/img/")) {
					return new WebResourceResponse("image/png", "UTF-8", new ByteArrayInputStream(new byte[0]));
				}
			}
			if (prefs.getBoolean(PREF_TEXT_MODE, false)) {
				String lowerUrl = url.toLowerCase();
				if (!lowerUrl.endsWith(".html") && !lowerUrl.endsWith(".htm") &&
				!lowerUrl.endsWith(".css") && !lowerUrl.endsWith(".js")) {
					return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream(new byte[0]));
				}
			}
			return super.shouldInterceptRequest(view, request);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			tab.title = view.getTitle();
			if (tab == currentTab) {
				progressBar.setVisibility(View.GONE);
			}
			// 本地页面完全跳过所有注入，只更新标题和进度条
			if (isLocalResource(url)) {
				return;
			}
			mainHandler.postDelayed(() -> {
				if (tab == currentTab) {
					if (prefs.getBoolean(PREF_NIGHT_MODE, false)) {
						injectNightMode();
					}
					applyTextMode();
					injectAntiFlickerCSS();
					injectMusicMonitor();
					injectAdBlockRules();
					if (isMaliDevice && prefs.getBoolean(PREF_MALI_OPTIMIZATION, true)) {
						applyMaliOptimizations();
					}
					if (adPreventJumpEnabled) {
						injectPreventAutomaticJumpJS();
					}
					if (adAutoBlockEnabled) {
						injectAutoPopupBlockerJS();
					}
				}
			}, 100);
		}
		
		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			super.onReceivedError(view, request, error);
			if (tab == currentTab && request.isForMainFrame()) {
				isPageLoadError = true;
				progressBar.setVisibility(View.GONE);
				String errorMsg = !isNetworkAvailable ? "网络不可用，请检查网络连接" : error.getDescription().toString();
				showCustomError(errorMsg);
			}
		}
		
		@Override
		public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
			super.onReceivedHttpError(view, request, errorResponse);
			if (tab == currentTab && request.isForMainFrame()) {
				isPageLoadError = true;
				progressBar.setVisibility(View.GONE);
				int statusCode = errorResponse.getStatusCode();
				String errorMsg = "页面访问失败 (" + statusCode + ")，请检查地址或网络";
				showCustomError(errorMsg);
			}
		}
		
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
			handler.proceed();
		}
	}
	
	// ==================== WebChromeClient 实现 ====================
	private class CustomWebChromeClient extends WebChromeClient {
		private final TabInfo tab;
		
		CustomWebChromeClient(TabInfo tab) {
			this.tab = tab;
		}
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (tab == currentTab && progressBar != null) {
				progressBar.setProgress(newProgress);
				if (newProgress >= 100) {
					progressBar.setVisibility(View.GONE);
				}
			}
		}
		
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if (mCustomVideoView != null) {
				onHideCustomView();
				return;
			}
			mCustomVideoView = view;
			mFullscreenCallback = callback;
			mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
			isFullscreenMode = true;
			
			mAppContentLayout.setVisibility(View.INVISIBLE);
			navBar.setVisibility(View.GONE);
			isNavBarHidden = true;
			mFullscreenContainer.addView(view);
			mFullscreenContainer.setVisibility(View.VISIBLE);
			mFullscreenContainer.bringToFront();
			
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			Window window = getWindow();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				android.view.WindowInsetsController controller = window.getInsetsController();
				if (controller != null) {
					controller.hide(android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars());
					controller.setSystemBarsBehavior(android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
				}
				} else {
				View decorView = window.getDecorView();
				decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
			}
		}
		
		@Override
		public void onHideCustomView() {
			if (mCustomVideoView == null || mFullscreenCallback == null) {
				return;
			}
			mCustomVideoView.setVisibility(View.GONE);
			mFullscreenContainer.removeView(mCustomVideoView);
			mFullscreenContainer.setVisibility(View.GONE);
			mCustomVideoView = null;
			mFullscreenCallback.onCustomViewHidden();
			mFullscreenCallback = null;
			isFullscreenMode = false;
			
			mAppContentLayout.setVisibility(View.VISIBLE);
			navBar.setVisibility(View.VISIBLE);
			isNavBarHidden = false;
			
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
			if (!isShowingErrorPage) {
				setImmersiveMode();
			}
		}
		
		@Override
		public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
			if (!isFinishing()) {
				new android.app.AlertDialog.Builder(MainActivity.this)
				.setMessage(message)
				.setPositiveButton("确定", (dialog, which) -> result.confirm())
				.setCancelable(false)
				.show();
				} else {
				result.confirm();
			}
			return true;
		}
		
		@Override
		public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
			if (!isFinishing()) {
				new android.app.AlertDialog.Builder(MainActivity.this)
				.setMessage(message)
				.setPositiveButton("确定", (dialog, which) -> result.confirm())
				.setNegativeButton("取消", (dialog, which) -> result.cancel())
				.setCancelable(false)
				.show();
				} else {
				result.cancel();
			}
			return true;
		}
		
		@Override
		public void onPermissionRequest(final PermissionRequest request) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				request.grant(request.getResources());
			}
		}
		
		@Override
		public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
			if (mUploadMessageForApi21 != null) {
				mUploadMessageForApi21.onReceiveValue(null);
			}
			mUploadMessageForApi21 = filePathCallback;
			
			Intent intent = fileChooserParams.createIntent();
			try {
				startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
				} catch (Exception e) {
				mUploadMessageForApi21 = null;
				Toast.makeText(MainActivity.this, "无法打开文件选择器", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			openFileChooser(uploadMsg, acceptType);
		}
		
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			openFileChooser(uploadMsg);
		}
		
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("*/*");
			startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_CHOOSER_RESULT_CODE);
		}
	}
	
	// ==================== 主题颜色选择器 ====================
	private void showColorPickerDialog() {
		Dialog colorDialog = new Dialog(this);
		colorDialog.setTitle("选择主题颜色");
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(dp(20), dp(20), dp(20), dp(20));
		layout.setBackground(createBg(mainBgColor, 16));
		
		LinearLayout row1 = new LinearLayout(this);
		row1.setOrientation(LinearLayout.HORIZONTAL);
		row1.setGravity(Gravity.CENTER);
		int[] colors1 = {0xFFE57373, 0xFFFFB74D, 0xFFFFF176, 0xFF81C784, 0xFF4FC3F7, 0xFFBA68C8, 0xFFF06292};
		String[] names1 = {"红", "橙", "黄", "绿", "青", "蓝", "紫"};
		addColorButtons(row1, colors1, names1);
		layout.addView(row1);
		
		LinearLayout row2 = new LinearLayout(this);
		row2.setOrientation(LinearLayout.HORIZONTAL);
		row2.setGravity(Gravity.CENTER);
		int[] colors2 = {0xFFEEEEEE, 0xFF9E9E9E, 0xFF212121, 0xFFFFC0CB};
		String[] names2 = {"白", "灰", "黑", "粉"};
		addColorButtons(row2, colors2, names2);
		layout.addView(row2);
		
		TextView customLabel = new TextView(this);
		customLabel.setText("自定义颜色（十六进制）");
		customLabel.setTextColor(TEXT_WHITE);
		customLabel.setGravity(Gravity.CENTER);
		layout.addView(customLabel);
		
		EditText customColorInput = new EditText(this);
		customColorInput.setHint("#RRGGBB");
		customColorInput.setTextColor(TEXT_WHITE);
		customColorInput.setHintTextColor(TEXT_DISABLE);
		customColorInput.setBackground(createBg(btnBgColor, 8));
		customColorInput.setPadding(dp(12), dp(10), dp(12), dp(10));
		customColorInput.setGravity(Gravity.CENTER);
		layout.addView(customColorInput);
		
		TextView alphaLabel = new TextView(this);
		alphaLabel.setText("不透明度（0-255）");
		alphaLabel.setTextColor(TEXT_WHITE);
		alphaLabel.setPadding(0, dp(16), 0, dp(8));
		alphaLabel.setGravity(Gravity.CENTER);
		layout.addView(alphaLabel);
		
		SeekBar alphaSeek = new SeekBar(this);
		alphaSeek.setMax(255);
		int currentAlpha = (mainBgColor >> 24) & 0xFF;
		alphaSeek.setProgress(currentAlpha);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			alphaSeek.setProgressTintList(ColorStateList.valueOf(accentColor));
			alphaSeek.setThumbTintList(ColorStateList.valueOf(accentColor));
		}
		layout.addView(alphaSeek);
		
		TextView alphaValue = new TextView(this);
		alphaValue.setText(currentAlpha + "");
		alphaValue.setTextColor(TEXT_WHITE);
		alphaValue.setGravity(Gravity.CENTER);
		alphaValue.setPadding(0, dp(4), 0, dp(8));
		layout.addView(alphaValue);
		
		alphaSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				alphaValue.setText(progress + "");
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		Button applyBtn = new Button(this);
		applyBtn.setText("应用颜色");
		applyBtn.setTextColor(TEXT_WHITE);
		applyBtn.setBackground(createBg(btnBgColor, 12));
		applyBtn.setOnClickListener(v -> {
			int color = -1;
			String customHex = customColorInput.getText().toString().trim();
			if (!customHex.isEmpty()) {
				try {
					color = Color.parseColor(customHex);
					} catch (Exception e) {
					Toast.makeText(this, "颜色格式错误，使用当前颜色", Toast.LENGTH_SHORT).show();
				}
			}
			if (color == -1) {
				color = appBgColor;
			}
			int alpha = alphaSeek.getProgress();
			int finalColor = (alpha << 24) | (color & 0xFFFFFF);
			prefs.edit()
			.putInt(PREF_THEME_COLOR, color)
			.putInt(PREF_THEME_ALPHA, alpha)
			.apply();
			Toast.makeText(this, "主题已更改，重启应用生效", Toast.LENGTH_SHORT).show();
			recreate();
		});
		layout.addView(applyBtn);
		
		Button cancelBtn = new Button(this);
		cancelBtn.setText("取消");
		cancelBtn.setTextColor(TEXT_WHITE);
		cancelBtn.setBackground(createBg(btnBgColor, 12));
		cancelBtn.setOnClickListener(v -> colorDialog.dismiss());
		layout.addView(cancelBtn);
		
		colorDialog.setContentView(layout);
		Window window = colorDialog.getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.95), ViewGroup.LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		colorDialog.show();
	}
	
	// ==================== Mali GPU检测 ====================
	private void detectMaliGPU() {
		String renderer = Build.BRAND + " " + Build.MODEL + " " + Build.DEVICE;
		isMaliDevice = renderer.toLowerCase().contains("mali");
		
		if (!prefs.contains(PREF_MALI_OPTIMIZATION)) {
			prefs.edit().putBoolean(PREF_MALI_OPTIMIZATION, isMaliDevice).apply();
		}
	}
	
	private void handleTtsFileImport(Uri uri) {
		Toast.makeText(this, "请使用系统包安装器安装TTS数据", Toast.LENGTH_LONG).show();
	}
	
	private void addColorButtons(LinearLayout row, int[] colors, String[] names) {
		for (int i = 0; i < colors.length; i++) {
			Button btn = new Button(this);
			btn.setBackgroundColor(colors[i]);
			btn.setText(names[i]);
			btn.setPadding(dp(12), dp(12), dp(12), dp(12));
			btn.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
			final int color = colors[i];
			btn.setOnClickListener(v -> {
				int alpha = (mainBgColor >> 24) & 0xFF;
				int finalColor = (alpha << 24) | (color & 0xFFFFFF);
				prefs.edit()
				.putInt(PREF_THEME_COLOR, color)
				.putInt(PREF_THEME_ALPHA, alpha)
				.apply();
				Toast.makeText(this, "主题已更改，重启应用生效", Toast.LENGTH_SHORT).show();
				recreate();
			});
			row.addView(btn);
		}
	}
	
	// ==================== 版本更新（完整实现） ====================
	private void autoCheckForUpdates(boolean showNoUpdateMsg) {
		new Thread(() -> {
			HttpURLConnection conn = null;
			BufferedReader reader = null;
			try {
				URL url = new URL(UPDATE_CHECK_URL);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(8000);
				conn.setReadTimeout(8000);
				conn.setRequestMethod("GET");
				conn.connect();
				
				int responseCode = conn.getResponseCode();
				if (responseCode != 200) {
					throw new Exception("服务器返回错误：" + responseCode);
				}
				
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				
				String configContent = sb.toString();
				String latestVersion = extractTag(configContent, "version");
				String updateLog = extractTag(configContent, "log");
				String downloadUrl = extractTag(configContent, "url");
				
				if (latestVersion == null || downloadUrl == null) {
					throw new Exception("更新配置解析失败，缺少必要字段");
				}
				
				PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				String currentVersion = pInfo.versionName;
				
				final boolean hasUpdate = compareVersions(latestVersion, currentVersion) > 0;
				
				runOnUiThread(() -> {
					if (hasUpdate) {
						if (!isForceUpdateDialogShowing) {
							isForceUpdateDialogShowing = true;
							showForceUpdateDialog(latestVersion, updateLog, downloadUrl);
						}
						} else if (showNoUpdateMsg) {
						Toast.makeText(MainActivity.this, "当前已是最新版本（v" + currentVersion + "）", Toast.LENGTH_SHORT).show();
					}
				});
				
				} catch (Exception e) {
				e.printStackTrace();
				if (showNoUpdateMsg) {
					runOnUiThread(() -> Toast.makeText(MainActivity.this, "检查更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show());
				}
				} finally {
				if (reader != null) {
					try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
				}
				if (conn != null) {
					conn.disconnect();
				}
			}
		}).start();
	}
	
	private void showForceUpdateDialog(String version, String log, String downloadUrl) {
		Dialog dialog = new Dialog(MainActivity.this);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener((dialog1, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
		
		LinearLayout layout = new LinearLayout(MainActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackground(createBg(mainBgColor, 20));
		layout.setPadding(dp(24), dp(24), dp(24), dp(24));
		
		TextView title = new TextView(MainActivity.this);
		title.setText("发现新版本 v" + version);
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(22);
		title.setTypeface(null, Typeface.BOLD);
		title.setGravity(Gravity.CENTER);
		layout.addView(title);
		
		TextView logTitle = new TextView(MainActivity.this);
		logTitle.setText("更新内容：");
		logTitle.setTextColor(TEXT_GRAY);
		logTitle.setTextSize(14);
		logTitle.setPadding(0, dp(16), 0, dp(8));
		logTitle.setGravity(Gravity.CENTER);
		layout.addView(logTitle);
		
		TextView logView = new TextView(MainActivity.this);
		logView.setText(log != null ? log : "优化体验，修复已知问题");
		logView.setTextColor(TEXT_WHITE);
		logView.setTextSize(14);
		logView.setLineSpacing(dp(4), 1f);
		logView.setGravity(Gravity.CENTER);
		layout.addView(logView);
		
		Button updateBtn = new Button(MainActivity.this);
		updateBtn.setText("立即下载更新");
		updateBtn.setTextColor(TEXT_WHITE);
		updateBtn.setBackground(createBg(btnBgColor, 12));
		updateBtn.setOnClickListener(v -> {
			if (downloadUrl != null && !downloadUrl.isEmpty()) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
					startActivity(intent);
					} catch (Exception e) {
					Toast.makeText(MainActivity.this, "无法打开下载链接", Toast.LENGTH_SHORT).show();
				}
			}
			dialog.dismiss();
			isForceUpdateDialogShowing = false;
		});
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		btnParams.topMargin = dp(20);
		updateBtn.setLayoutParams(btnParams);
		layout.addView(updateBtn);
		
		dialog.setContentView(layout);
		Window window = dialog.getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	private String extractTag(String content, String tag) {
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		int startIndex = content.indexOf(startTag);
		if (startIndex == -1) return null;
		startIndex += startTag.length();
		int endIndex = content.indexOf(endTag, startIndex);
		if (endIndex == -1) return null;
		return content.substring(startIndex, endIndex).trim();
	}
	
	private int compareVersions(String v1, String v2) {
		try {
			String[] arr1 = v1.split("\\.");
			String[] arr2 = v2.split("\\.");
			int maxLen = Math.max(arr1.length, arr2.length);
			for (int i = 0; i < maxLen; i++) {
				int num1 = i < arr1.length ? Integer.parseInt(arr1[i]) : 0;
				int num2 = i < arr2.length ? Integer.parseInt(arr2[i]) : 0;
				if (num1 != num2) {
					return num1 - num2;
				}
			}
			return 0;
			} catch (Exception e) {
			return -1;
		}
	}
	
	
	
	// ==================== 网页屏蔽相关方法 ====================
	private void blockCurrentWebsite() {
		if (currentTab == null || currentTab.url == null) {
			Toast.makeText(this, "当前没有可屏蔽的页面", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			URL url = new URL(currentTab.url);
			String host = url.getHost();
			int port = url.getPort();
			
			// 检查是否是本地地址或首页
			if (isLocalResource(currentTab.url)
			|| (host != null && host.toLowerCase().contains("index"))
			|| currentTab.url.toLowerCase().contains("localhost:8080")) {
				Toast.makeText(this, "主页面或本地页面不能被屏蔽", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (host != null && !host.isEmpty()) {
				blockedWebsites.add(host);
				prefs.edit().putStringSet(PREF_BLOCKED_WEBSITES, blockedWebsites).apply();
				Toast.makeText(this, "已屏蔽网站：" + host, Toast.LENGTH_SHORT).show();
				
				// 找到上一次访问的网站并跳转
				String previousUrl = null;
				if (historyList.size() >= 2) {
					previousUrl = historyList.get(1);
				}
				
				if (previousUrl != null && currentTab != null && currentTab.webView != null) {
					currentTab.webView.loadUrl(previousUrl);
					} else if (!historyList.isEmpty() && currentTab != null && currentTab.webView != null) {
					currentTab.webView.loadUrl(historyList.get(0));
				}
				} else {
				Toast.makeText(this, "无法识别网站地址", Toast.LENGTH_SHORT).show();
			}
			} catch (Exception e) {
			Toast.makeText(this, "屏蔽失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void showWebsiteBlockManagerDialog() {
		Dialog dialog = new Dialog(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(dp(20), dp(20), dp(20), dp(20));
		layout.setBackground(createBg(mainBgColor, 16));
		
		TextView title = new TextView(this);
		title.setText("已屏蔽网站列表");
		title.setTextColor(TEXT_WHITE);
		title.setTextSize(18);
		title.setGravity(Gravity.CENTER);
		title.setPadding(0, 0, 0, dp(16));
		layout.addView(title);
		
		ScrollView scrollView = new ScrollView(this);
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setOrientation(LinearLayout.VERTICAL);
		
		if (blockedWebsites.isEmpty()) {
			TextView emptyView = new TextView(this);
			emptyView.setText("暂无屏蔽的网站");
			emptyView.setTextColor(TEXT_GRAY);
			emptyView.setGravity(Gravity.CENTER);
			emptyView.setPadding(dp(20), dp(40), dp(20), dp(40));
			contentLayout.addView(emptyView);
			} else {
			for (String site : blockedWebsites) {
				LinearLayout siteRow = new LinearLayout(this);
				siteRow.setOrientation(LinearLayout.HORIZONTAL);
				siteRow.setGravity(Gravity.CENTER_VERTICAL);
				siteRow.setPadding(dp(8), dp(8), dp(8), dp(8));
				siteRow.setBackground(createBg(btnBgColor, 8));
				
				TextView siteName = new TextView(this);
				siteName.setText(site);
				siteName.setTextColor(TEXT_WHITE);
				siteName.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
				0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
				nameParams.setMargins(0, 0, dp(8), 0);
				siteRow.addView(siteName, nameParams);
				
				Button removeBtn = new Button(this);
				removeBtn.setText("移除");
				removeBtn.setTextColor(TEXT_WHITE);
				removeBtn.setBackground(createBg(0xFFB71C1C, 8));
				removeBtn.setAllCaps(false);
				removeBtn.setOnClickListener(v -> {
					blockedWebsites.remove(site);
					prefs.edit().putStringSet(PREF_BLOCKED_WEBSITES, blockedWebsites).apply();
					dialog.dismiss();
					showWebsiteBlockManagerDialog();
				});
				siteRow.addView(removeBtn);
				
				LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				rowParams.bottomMargin = dp(8);
				contentLayout.addView(siteRow, rowParams);
			}
		}
		
		scrollView.addView(contentLayout);
		layout.addView(scrollView);
		
		Button closeBtn = new Button(this);
		closeBtn.setText("关闭");
		closeBtn.setTextColor(TEXT_WHITE);
		closeBtn.setBackground(createBg(btnBgColor, 12));
		closeBtn.setOnClickListener(v -> dialog.dismiss());
		LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, dp(44));
		closeParams.topMargin = dp(16);
		layout.addView(closeBtn, closeParams);
		
		dialog.setContentView(layout);
		Window window = dialog.getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			window.setLayout((int)(getResources().getDisplayMetrics().widthPixels * 0.9),
			(int)(getResources().getDisplayMetrics().heightPixels * 0.7));
			window.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
	
	// ==================== 文字模式相关方法（对本地页面不注入CSS） ====================
	private void applyTextMode() {
		if (currentTab == null || currentTab.webView == null) {
			return;
		}
		WebSettings webSettings = currentTab.webView.getSettings();
		if (textModeEnabled) {
			webSettings.setLoadsImagesAutomatically(false);
			// 对本地页面不注入CSS，避免破坏界面
			if (!isLocalResource(currentTab.url)) {
				currentTab.webView.evaluateJavascript(
				"(function() { " +
				"var style = document.createElement('style'); " +
				"style.innerHTML = 'img, video, audio, iframe, canvas, svg, object, embed, picture, figure, nav, header, footer, aside, form, script, style, noscript, iframe, " +
				"canvas, map, area, param, source, track, embed, object, applet, noframes, frameset, frame, audio, video, picture, " +
				"img, svg, iframe, embed, object, param, input[type=image], button, select, textarea, input, label, fieldset, legend, " +
				"hr, table, thead, tbody, tfoot, tr, th, td, caption, colgroup, col, dl, dt, dd, ol, ul, li, menu, menuitem, " +
				"dir, details, summary, dialog, command, bb, basefont, blink, center, dir, font, isindex, listing, marquee, " +
				"menu, nobr, noframes, plaintext, s, strike, tt, u, xmp, br { display: none !important; }'; " +
				"style.innerHTML += 'body, html, div, p, span, a, li, h1, h2, h3, h4, h5, h6, article, section, main, table, td, th, ul, ol, em, strong, i, b, u, s, strike, sub, sup, code, pre, blockquote, cite, abbr, acronym, address, big, small, tt { background-color: #F5F5DC !important; color: #8B4513 !important; }'; " +
				"style.innerHTML += '* { line-height: 1.8 !important; font-size: 18px !important; }'; " +
				"style.innerHTML += 'body, html { padding: 20px !important; }'; " +
				"style.innerHTML += 'a { color: #8B4513 !important; text-decoration: underline !important; }'; " +
				"document.head.appendChild(style); " +
				"})();",
				null);
				} else {
				// 本地页面仅移除可能残留的文字模式样式
				currentTab.webView.evaluateJavascript(
				"(function() { var styles = document.querySelectorAll('style'); for(var i=0;i<styles.length;i++) { if(styles[i].innerHTML.indexOf('#F5F5DC') !== -1 || styles[i].innerHTML.indexOf('img, video') !== -1) { styles[i].parentNode.removeChild(styles[i]); } } })();",
				null);
			}
			} else {
			webSettings.setLoadsImagesAutomatically(true);
			// 移除CSS隐藏规则（所有页面都适用）
			currentTab.webView.evaluateJavascript(
			"(function() { " +
			"var styles = document.querySelectorAll('style'); " +
			"for (var i = 0; i < styles.length; i++) { " +
			"if (styles[i].innerHTML.indexOf('#F5F5DC') !== -1 || styles[i].innerHTML.indexOf('img, video') !== -1) { " +
			"styles[i].parentNode.removeChild(styles[i]); " +
			"} " +
			"} " +
			"})();",
			null);
		}
	}
	
	private void initProxyManager() {
		proxyManager = ProxyManager.getInstance(this);
		proxyManager.setCallback(new ProxyManager.ProxyCallback() {
			@Override
			public void onCrawlComplete(int newCount, int totalCount) {
				Toast.makeText(MainActivity.this, "抓取完成: 新增 " + newCount + " 个, 共 " + totalCount + " 个", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCleanComplete(int removedCount, int remainingCount) {
				Toast.makeText(MainActivity.this, "清洗完成: 移除 " + removedCount + " 个, 剩余 " + remainingCount + " 个", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onValidationComplete(String key, boolean alive, long latency) {}
			@Override
			public void onError(String error) {
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
		});
		proxyManager.startAutoMaintenance();
	}
	
	private void initScriptRecorder() {
		scriptRecorder = ScriptRecorder.getInstance(this);
		scriptRecorder.setCallback(new ScriptRecorder.RecorderCallback() {
			@Override
			public void onRecordingStarted() {
				Toast.makeText(MainActivity.this, "录制已开始", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onRecordingStopped(int actionCount) {
				Toast.makeText(MainActivity.this, "录制已停止，共 " + actionCount + " 个操作", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onPlaybackStarted() {
				Toast.makeText(MainActivity.this, "脚本播放开始", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onPlaybackCompleted() {
				isScriptPlaying = false;
				Toast.makeText(MainActivity.this, "脚本播放完成", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onScriptLoaded(String name, int actionCount) {
				Toast.makeText(MainActivity.this, "脚本已加载: " + name + " (" + actionCount + " 个操作)", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onError(String error) {
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private String getCountryFlag(String ip) {
		try {
			int first = Integer.parseInt(ip.split("\\.")[0]);
			if (first == 104 || first == 172 || first == 192 || first == 198 || first == 45 || first == 64 || first == 66 || first == 67 || first == 68 || first == 69 || first == 70 || first == 71 || first == 72 || first == 73 || first == 74 || first == 75 || first == 76 || first == 96 || first == 97 || first == 98 || first == 99 || first == 100 || first == 107 || first == 108 || first == 142 || first == 143 || first == 144 || first == 146 || first == 147 || first == 148 || first == 149 || first == 155 || first == 156 || first == 157 || first == 159 || first == 160 || first == 161 || first == 162 || first == 164 || first == 165 || first == 166 || first == 167 || first == 168 || first == 169 || first == 170 || first == 173 || first == 174 || first == 184 || first == 199 || first == 204 || first == 205 || first == 206 || first == 207 || first == 208 || first == 209 || first == 216) return "\uD83C\uDDFA\uD83C\uDDF8";
			if (first == 1 || first == 27 || first == 36 || first == 42 || first == 49 || first == 54 || first == 58 || first == 59 || first == 60 || first == 61 || first == 101 || first == 103 || first == 106 || first == 110 || first == 111 || first == 112 || first == 113 || first == 114 || first == 115 || first == 116 || first == 117 || first == 118 || first == 119 || first == 120 || first == 121 || first == 122 || first == 123 || first == 124 || first == 125 || first == 126 || first == 128 || first == 129 || first == 130 || first == 131 || first == 132 || first == 133 || first == 134 || first == 135 || first == 136 || first == 137 || first == 138 || first == 139 || first == 140 || first == 150 || first == 153 || first == 154 || first == 163 || first == 171 || first == 175 || first == 180 || first == 182 || first == 183 || first == 202 || first == 203 || first == 210 || first == 211 || first == 218 || first == 219 || first == 220 || first == 221 || first == 222 || first == 223) return "\uD83C\uDDE8\uD83C\uDDF3";
			if (first == 2 || first == 5 || first == 25 || first == 31 || first == 37 || first == 46 || first == 51 || first == 62 || first == 77 || first == 78 || first == 79 || first == 80 || first == 81 || first == 82 || first == 83 || first == 84 || first == 85 || first == 86 || first == 87 || first == 88 || first == 89 || first == 90 || first == 91 || first == 92 || first == 93 || first == 94 || first == 95 || first == 141 || first == 145 || first == 151 || first == 176 || first == 178 || first == 185 || first == 188 || first == 193 || first == 194 || first == 195 || first == 212 || first == 213 || first == 217) return "\uD83C\uDDEA\uD83C\uDDFA";
			if (first == 3 || first == 4 || first == 6 || first == 7 || first == 8 || first == 9 || first == 11 || first == 12 || first == 13 || first == 15 || first == 16 || first == 17 || first == 18 || first == 19 || first == 20 || first == 21 || first == 22 || first == 23 || first == 24 || first == 26 || first == 28 || first == 29 || first == 30 || first == 32 || first == 33 || first == 34 || first == 35 || first == 38 || first == 40 || first == 44 || first == 47 || first == 48 || first == 50 || first == 52 || first == 53 || first == 55 || first == 56 || first == 57 || first == 63 || first == 65 || first == 152 || first == 158) return "\uD83C\uDDFA\uD83C\uDDF8";
			if (first == 14 || first == 39 || first == 41 || first == 43 || first == 102 || first == 105 || first == 127) return "\uD83C\uDDEE\uD83C\uDDF3";
			if (first == 109 || first == 177 || first == 179 || first == 181 || first == 186 || first == 187 || first == 189 || first == 190 || first == 191 || first == 200 || first == 201) return "\uD83C\uDDE7\uD83C\uDDF7";
			if (first == 10 || first == 196 || first == 197 || first == 214 || first == 215) return "\uD83C\uDDE8\uD83C\uDDE6";
		} catch (Exception ignored) {}
		return "\uD83C\uDF10";
	}
	
	private void showProxyPickerDialog(Button fixedModeBtn) {
		List<ProxyManager.ProxyNode> proxies = proxyManager.getAliveProxies();
		if (proxies.isEmpty()) {
			Toast.makeText(this, "没有可用代理，请先抓取", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String[] items = new String[proxies.size()];
		for (int i = 0; i < proxies.size(); i++) {
			ProxyManager.ProxyNode node = proxies.get(i);
			items[i] = getCountryFlag(node.ip) + " " + node.ip + ":" + node.port + " [" + node.type + "] " + node.latency + "ms";
		}
		
		new AlertDialog.Builder(this)
		.setTitle("选择固定代理节点")
		.setItems(items, (dialog, which) -> {
			ProxyManager.ProxyNode selected = proxies.get(which);
			proxyManager.setFixedProxy(selected.getKey());
			Toast.makeText(MainActivity.this, "已固定节点: " + selected.getKey(), Toast.LENGTH_SHORT).show();
		})
		.setNegativeButton("取消", null)
		.show();
	}
	
	private void showProxyListDialog() {
		Dialog listDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		listLayout.setBackground(createBg(mainBgColor, 24));
		listLayout.setPadding(dp(20), dp(20), dp(20), dp(20));
		
		TextView listTitle = new TextView(this);
		listTitle.setText("代理节点列表");
		listTitle.setTextColor(TEXT_WHITE);
		listTitle.setTextSize(20);
		listTitle.setTypeface(null, Typeface.BOLD);
		listTitle.setGravity(Gravity.CENTER);
		listTitle.setPadding(0, 0, 0, dp(12));
		listLayout.addView(listTitle);
		
		TextView listSummary = new TextView(this);
		listSummary.setText("存活: " + proxyManager.getAliveCount() + " / 总计: " + proxyManager.getTotalCount());
		listSummary.setTextColor(TEXT_GRAY);
		listSummary.setTextSize(13);
		listSummary.setGravity(Gravity.CENTER);
		listSummary.setPadding(0, 0, 0, dp(12));
		listLayout.addView(listSummary);
		
		ScrollView listScroll = new ScrollView(this);
		LinearLayout itemsLayout = new LinearLayout(this);
		itemsLayout.setOrientation(LinearLayout.VERTICAL);
		
		List<ProxyManager.ProxyNode> allProxies = proxyManager.getAllProxies();
		for (ProxyManager.ProxyNode node : allProxies) {
			LinearLayout itemRow = new LinearLayout(this);
			itemRow.setOrientation(LinearLayout.HORIZONTAL);
			itemRow.setPadding(dp(8), dp(6), dp(8), dp(6));
			itemRow.setBackground(createBg(node.alive ? 0x4400AA00 : 0x44AA0000, 8));
			LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			itemParams.bottomMargin = dp(4);
			itemRow.setLayoutParams(itemParams);
			
			TextView nodeText = new TextView(this);
			String status = node.alive ? "✓" : "✗";
			String flag = getCountryFlag(node.ip);
			nodeText.setText(flag + " " + status + " " + node.ip + ":" + node.port + " [" + node.type + "] " + (node.alive ? node.latency + "ms" : "失效"));
			nodeText.setTextColor(node.alive ? 0xFF88FF88 : 0xFFFF8888);
			nodeText.setTextSize(12);
			itemRow.addView(nodeText, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
			
			Button validateBtn = new Button(this);
			validateBtn.setText("检测");
			validateBtn.setTextColor(TEXT_WHITE);
			validateBtn.setBackground(createBg(btnBgColor, 8));
			validateBtn.setAllCaps(false);
			validateBtn.setTextSize(11);
			validateBtn.setPadding(dp(8), dp(2), dp(8), dp(2));
			validateBtn.setOnClickListener(v -> {
				validateBtn.setText("...");
				validateBtn.setEnabled(false);
				proxyManager.validateProxy(node.getKey());
				mainHandler.postDelayed(() -> {
					validateBtn.setText("检测");
					validateBtn.setEnabled(true);
					listDialog.dismiss();
					showProxyListDialog();
				}, 3000);
			});
			itemRow.addView(validateBtn);
			
			itemsLayout.addView(itemRow);
		}
		
		listScroll.addView(itemsLayout);
		listLayout.addView(listScroll, new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
		
		Button closeListBtn = new Button(this);
		closeListBtn.setText("关闭");
		closeListBtn.setTextColor(TEXT_WHITE);
		closeListBtn.setBackground(createBg(accentColor, 16));
		closeListBtn.setOnClickListener(v -> listDialog.dismiss());
		listLayout.addView(closeListBtn, new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, dp(44)));
		
		listDialog.setContentView(listLayout);
		
		Window listWindow = listDialog.getWindow();
		if (listWindow != null) {
			listWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			int screenWidth = getResources().getDisplayMetrics().widthPixels;
			int screenHeight = getResources().getDisplayMetrics().heightPixels;
			listWindow.setLayout((int)(screenWidth * 0.92), (int)(screenHeight * 0.75));
			listWindow.setGravity(Gravity.CENTER);
		}
		listDialog.show();
	}
	
	private void showScriptImportDialog() {
		Dialog importDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		importDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout importLayout = new LinearLayout(this);
		importLayout.setOrientation(LinearLayout.VERTICAL);
		importLayout.setBackground(createBg(mainBgColor, 24));
		importLayout.setPadding(dp(20), dp(20), dp(20), dp(20));
		
		TextView importTitle = new TextView(this);
		importTitle.setText("导入脚本");
		importTitle.setTextColor(TEXT_WHITE);
		importTitle.setTextSize(20);
		importTitle.setTypeface(null, Typeface.BOLD);
		importTitle.setGravity(Gravity.CENTER);
		importTitle.setPadding(0, 0, 0, dp(12));
		importLayout.addView(importTitle);
		
		TextView importHint = new TextView(this);
		importHint.setText("粘贴JSON脚本内容或输入远程URL");
		importHint.setTextColor(TEXT_GRAY);
		importHint.setTextSize(13);
		importHint.setGravity(Gravity.CENTER);
		importHint.setPadding(0, 0, 0, dp(8));
		importLayout.addView(importHint);
		
		EditText importEdit = new EditText(this);
		importEdit.setHint("粘贴JSON或输入URL...");
		importEdit.setTextColor(TEXT_WHITE);
		importEdit.setHintTextColor(TEXT_DISABLE);
		importEdit.setBackground(createBg(btnBgColor, 8));
		importEdit.setPadding(dp(12), dp(10), dp(12), dp(10));
		importEdit.setMinLines(4);
		importEdit.setGravity(Gravity.TOP);
		importLayout.addView(importEdit, new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		LinearLayout importBtnRow = new LinearLayout(this);
		importBtnRow.setOrientation(LinearLayout.HORIZONTAL);
		importBtnRow.setWeightSum(2);
		importBtnRow.setGravity(Gravity.CENTER);
		importBtnRow.setPadding(0, dp(12), 0, 0);
		
		Button pasteBtn = new Button(this);
		pasteBtn.setText("从剪贴板粘贴");
		pasteBtn.setTextColor(TEXT_WHITE);
		pasteBtn.setBackground(createBg(btnBgColor, 12));
		pasteBtn.setAllCaps(false);
		pasteBtn.setOnClickListener(v -> {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			if (clipboard.hasPrimaryClip()) {
				ClipData clip = clipboard.getPrimaryClip();
				if (clip != null && clip.getItemCount() > 0) {
					String text = clip.getItemAt(0).getText().toString();
					importEdit.setText(text);
				}
			}
		});
		LinearLayout.LayoutParams pasteParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		pasteParams.setMargins(0, 0, dp(4), 0);
		importBtnRow.addView(pasteBtn, pasteParams);
		
		Button confirmImportBtn = new Button(this);
		confirmImportBtn.setText("确认导入");
		confirmImportBtn.setTextColor(TEXT_WHITE);
		confirmImportBtn.setBackground(createBg(accentColor, 12));
		confirmImportBtn.setAllCaps(false);
		confirmImportBtn.setOnClickListener(v -> {
			String content = importEdit.getText().toString().trim();
			if (content.isEmpty()) {
				Toast.makeText(MainActivity.this, "请输入脚本内容", Toast.LENGTH_SHORT).show();
				return;
			}
			if (content.startsWith("http")) {
				loadScriptFromUrl(content);
				} else {
				JSONObject script = scriptRecorder.importScriptFromJson(content);
				if (script != null) {
					Toast.makeText(MainActivity.this, "脚本导入成功", Toast.LENGTH_SHORT).show();
				}
			}
			importDialog.dismiss();
		});
		LinearLayout.LayoutParams confirmImportParams = new LinearLayout.LayoutParams(0, dp(40), 1);
		confirmImportParams.setMargins(dp(4), 0, 0, 0);
		importBtnRow.addView(confirmImportBtn, confirmImportParams);
		importLayout.addView(importBtnRow);
		
		Button cancelImportBtn = new Button(this);
		cancelImportBtn.setText("取消");
		cancelImportBtn.setTextColor(TEXT_WHITE);
		cancelImportBtn.setBackground(createBg(btnBgColor, 12));
		cancelImportBtn.setOnClickListener(v -> importDialog.dismiss());
		LinearLayout.LayoutParams cancelImportParams = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, dp(40));
		cancelImportParams.topMargin = dp(8);
		importLayout.addView(cancelImportBtn, cancelImportParams);
		
		importDialog.setContentView(importLayout);
		
		Window importWindow = importDialog.getWindow();
		if (importWindow != null) {
			importWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			int screenWidth = getResources().getDisplayMetrics().widthPixels;
			importWindow.setLayout((int)(screenWidth * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
			importWindow.setGravity(Gravity.CENTER);
		}
		importDialog.show();
	}
	
	private void loadScriptFromUrl(String url) {
		new Thread(() -> {
			try {
				java.net.URL u = new java.net.URL(url);
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(15000);
				conn.setReadTimeout(15000);
				conn.setRequestProperty("User-Agent", "Mozilla/5.0");
				
				int code = conn.getResponseCode();
				if (code == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					
					String jsonStr = sb.toString();
					mainHandler.post(() -> {
						JSONObject script = scriptRecorder.importScriptFromJson(jsonStr);
						if (script != null) {
							Toast.makeText(MainActivity.this, "远程脚本加载成功", Toast.LENGTH_SHORT).show();
						}
					});
					} else {
					mainHandler.post(() -> Toast.makeText(MainActivity.this, "加载失败: HTTP " + code, Toast.LENGTH_SHORT).show());
				}
				conn.disconnect();
				} catch (Exception e) {
				mainHandler.post(() -> Toast.makeText(MainActivity.this, "加载失败: " + e.getMessage(), Toast.LENGTH_SHORT).show());
			}
		}).start();
	}
	
	// ==================== VPN 功能 ====================
	
	private void startVpnService() {
		Intent intent = new Intent(this, SilongVpnService.class);
		intent.setAction("START_VPN");
		// 这里可以设置代理地址，先默认用一个测试地址
		intent.putExtra("proxyHost", "127.0.0.1");
		intent.putExtra("proxyPort", 8080);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			startForegroundService(intent);
			} else {
			startService(intent);
		}
		Toast.makeText(this, "VPN 正在启动...", Toast.LENGTH_SHORT).show();
	}
	
	private void stopVpnService() {
		Intent intent = new Intent(this, SilongVpnService.class);
		intent.setAction("STOP_VPN");
		startService(intent);
		Toast.makeText(this, "VPN 已停止", Toast.LENGTH_SHORT).show();
	}
}

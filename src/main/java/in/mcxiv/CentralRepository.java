package in.mcxiv;

import com.mcxiv.logger.decorations.Decorations;
import com.mcxiv.logger.decorations.Format;
import com.mcxiv.logger.formatted.FLog;
import com.mcxiv.logger.formatted.fixed.FileLog;
import com.mcxiv.logger.tools.LogLevel;
import com.mcxiv.logger.ultimate.ULog;
import in.mcxiv.utils.Strings;

import java.time.LocalDateTime;

public class CentralRepository {

    public static final String APP_NAME = "THATLANG_Compiler";
    public static final String LOG_HEAD = getLogHead();

    public static final String UNDER_STATIC_INITIALIZER = "Under static initializer :)";

    public static final int NUMBER_OF_CORES;
    public static String USER_NAME;
    public static String USER_DIR;
    public static String USER_HOME;

    public static String JDK_HOME;
    public static String JAVA_HOME;
    public static String JAVA_TEMP_DIR;

    public static final FLog log;

    static {
        LogLevel.NOTICE.activate();

        FLog file_logger = FileLog.getNew(APP_NAME + ".log");
        FLog strm_logger = FLog.getNew();
        file_logger.setDecorationType(Decorations.TAG);
        strm_logger.setDecorationType(Decorations.CONSOLE);
        log = ULog.forNew().add(file_logger).add(strm_logger).create();
        prt(LOG_HEAD, UNDER_STATIC_INITIALIZER);

        prt("DATE AND TIME", LocalDateTime.now());

        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        prt("CORES AVAILABLE", NUMBER_OF_CORES);

        USER_NAME = System.getProperty("user.name");
        USER_DIR = System.getProperty("user.dir");
        USER_HOME = System.getProperty("user.home");
        prt("User Name", USER_NAME);
        prt("User Directory", USER_DIR);
        prt("User Home", USER_HOME);
    }

    @Format({":: :@6829F6 #F %*40s: ::", ":: :@A8A9F6 #0 n %-100s: ::"})
    public static void prt(Object type, Object msg) {
        log.prt(type, msg);
    }

    @Format({":: :@F66829 #F %*40s: ::", ":: :@F6A9A9 #0 n %-100s: ::"})
    public static void err(Object type, Object msg) {
        log.prt(type, msg);
    }


    public static String getLogHead() {
        return Strings.camelToUpper(Strings.getLastWord(Thread.currentThread().getStackTrace()[2].getClassName(), '.'));
    }

    public static String getLogHead(Class<?> clazz) {
        return Strings.camelToUpper(clazz.getSimpleName());
    }

}

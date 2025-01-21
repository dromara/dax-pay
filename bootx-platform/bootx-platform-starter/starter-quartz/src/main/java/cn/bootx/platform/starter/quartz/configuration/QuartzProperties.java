package cn.bootx.platform.starter.quartz.configuration;

import lombok.Getter;
import lombok.Setter;
import org.quartz.Calendar;
import org.quartz.impl.jdbcjobstore.DriverDelegate;
import org.quartz.impl.jdbcjobstore.Semaphore;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerSignaler;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;

import static org.quartz.impl.jdbcjobstore.Constants.DEFAULT_TABLE_PREFIX;

/**
 * 支持配置文件自动提示
 *
 * @author xxm
 * @since 2021/12/2
 */
@Getter
@Setter
@ConfigurationProperties("spring.quartz.properties.org.quartz")
public class QuartzProperties {

    /** 调度器 */
    private Scheduler scheduler;

    /**  */
    private JobStore jobStore;

    /** 线程池 */
    private ThreadPool threadPool;

    @Setter
    @Getter
    static class Scheduler {

    }

    @Setter
    @Getter
    static class JobStore {

        protected String dsName;

        protected String tablePrefix = DEFAULT_TABLE_PREFIX;

        protected boolean useProperties = false;

        protected String instanceId;

        protected String instanceName;

        protected String delegateClassName;

        protected String delegateInitString;

        protected Class<? extends DriverDelegate> delegateClass = StdJDBCDelegate.class;

        protected HashMap<String, Calendar> calendarCache = new HashMap<>();

        private DriverDelegate delegate;

        private long misfireThreshold = 60000L; // one minute

        private boolean dontSetAutoCommitFalse = false;

        private boolean isClustered = false;

        private boolean useDBLocks = false;

        private boolean lockOnInsert = true;

        private Semaphore lockHandler = null; // set in initialize() method...

        private String selectWithLockSQL = null;

        private long clusterCheckinInterval = 7500L;

        private ClassLoadHelper classLoadHelper;

        private SchedulerSignaler schedSignaler;

        protected int maxToRecoverAtATime = 20;

        private boolean setTxIsolationLevelSequential = false;

        private boolean acquireTriggersWithinLock = false;

        private long dbRetryInterval = 15000L; // 15 secs

        private boolean makeThreadsDaemons = false;

        private boolean threadsInheritInitializersClassLoadContext = false;

        private ClassLoader initializersLoader = null;

        private boolean doubleCheckLockMisfireHandler = true;

    }

    @Setter
    @Getter
    static class ThreadPool {

        private int count = -1;

        private int prio = Thread.NORM_PRIORITY;

        private boolean isShutdown = false;

        private boolean handoffPending = false;

        private boolean inheritLoader = false;

        private boolean inheritGroup = true;

        private boolean makeThreadsDaemons = false;

        private boolean threadsInheritContextClassLoaderOfInitializingThread = false;

    }

}

package protect.videotranscoder;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.CoreConfiguration;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.sender.ReportSenderFactory;

import protect.videotranscoder.report.AcraReportSenderFactory;
import protect.videotranscoder.report.ErrorActivity;
import protect.videotranscoder.report.UserAction;

public class App extends Application
{
    private static final Class<? extends ReportSenderFactory>[] reportSenderFactoryClasses = new Class[]{AcraReportSenderFactory.class};

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        initACRA();
    }

    protected void initACRA() {
        if (ACRA.isACRASenderServiceProcess()) {
            return;
        }

        try {
            final CoreConfiguration acraConfig = new CoreConfigurationBuilder(this)
                    .setBuildConfigClass(BuildConfig.class)
                    .build();
            ACRA.init(this, acraConfig);

        } catch (final ACRAConfigurationException ace) {

            ace.printStackTrace();
            ErrorActivity.reportError(this,
                    ace,
                    null,
                    null,
                    ErrorActivity.ErrorInfo.make(UserAction.SOMETHING_ELSE, "none",
                            "Could not initialize ACRA crash report", R.string.app_ui_crash));
        }
    }
}

import com.calendarfx.view.popover.EntryHeaderView;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import com.calendarfx.view.popover.EntryDetailsView;

public class CalendarFxPatcher {
    public static void premain(String args, Instrumentation inst) {
        String detailsPath = EntryDetailsView.class.getName().replace('.', '/') + ".class";
        InputStream detailsStream = EntryDetailsView.class.getClassLoader().getResourceAsStream(detailsPath);
        String headerPath = EntryHeaderView.class.getName().replace('.', '/') + ".class";
        InputStream headerStream = EntryDetailsView.class.getClassLoader().getResourceAsStream(headerPath);
        try {
            final byte[] detailsPatched = IOUtils.toByteArray(detailsStream);
            final byte[] headerPatched = IOUtils.toByteArray(headerStream);
            inst.addTransformer(new ClassFileTransformer() {
                @Override
                public byte[] transform(ClassLoader loader,
                                        String className,
                                        Class<?> classBeingRedefined,
                                        ProtectionDomain protectionDomain,
                                        byte[] classfileBuffer) {
                    if (className.equals("com/calendarfx/view/popover/EntryDetailsView")) {
                        return detailsPatched;
                        // Consider removing the transformer for future class loading
                    } else if (className.equals("com/calendarfx/view/popover/EntryHeaderView")) {
                        return headerPatched;
                        // Consider removing the transformer for future class loading
                    } else {
                        return null; // skips instrumentation for other classes
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

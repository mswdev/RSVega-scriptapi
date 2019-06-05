package org.api.http.data_tracking.data_tracker_factory.user.system_info;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTracker;
import org.api.http.data_tracking.data_tracker_factory.RSVegaTrackerFactory;
import org.api.script.SPXScriptUtil;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class SystemInfoDataTracker extends RSVegaTrackerFactory {

    private static final int MB_TO_B = 1048576;
    private static String file_uuid;

    public static RequestBody getUserData(int userId) {
        final FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("user_id", String.valueOf(userId));
        try {
            formBuilder.add("uuid", getUUID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        formBuilder.add("os", System.getProperty("os.name"));
        formBuilder.add("os_version", System.getProperty("os.version"));
        formBuilder.add("user", System.getProperty("user.name"));
        formBuilder.add("java_version", System.getProperty("java.version"));
        formBuilder.add("processors", String.valueOf(Runtime.getRuntime().availableProcessors()));
        formBuilder.add("ram", String.valueOf(((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / MB_TO_B));
        return formBuilder.build();
    }

    private static String getUUID() throws IOException {
        if (file_uuid != null)
            return file_uuid;

        final Path uuidPath = Paths.get(SPXScriptUtil.getSPXDataPath() + File.separator + "uuid.dat");
        final File uuidFile = uuidPath.toFile();
        if (!uuidFile.exists()) {
            Files.createFile(uuidPath);
        }

        final String fileUuid = Files.lines(uuidPath).findFirst().orElse(null);
        if (fileUuid != null) {
            file_uuid = fileUuid;
            return fileUuid;
        }

        final UUID uuid = UUID.randomUUID();
        Files.write(uuidPath, uuid.toString().getBytes());
        file_uuid = uuid.toString();
        return uuid.toString();
    }

    @Override
    protected RSVegaTracker getDataTracker() {
        return new SystemInfoData();
    }
}

package Utility;

import Model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;

public class Log {

    private static final String logfile = "login.txt";

    public static void write(User user) throws IOException {
        String data = user.getUserName();
        OutputStream os = null;
        data = ZonedDateTime.now().toString() + " -> " + data + "\n";
        os = new FileOutputStream(new File(logfile), true);
        os.write(data.getBytes(), 0, data.length());
        os.close();
    }
}
